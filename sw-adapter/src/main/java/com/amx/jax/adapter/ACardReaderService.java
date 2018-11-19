package com.amx.jax.adapter;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceMetaInfo;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.NetAddress;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.AmxException;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.TimeUtils;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.Keyring;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

public abstract class ACardReaderService {

	private static final String SERVICE_NAME = "amx-adapter";
	public static String CARD_READER_KEY = CardData.class.getName();
	private static final Logger LOGGER = LoggerService.getLogger(ACardReaderService.class);
	public static ACardReaderService CONTEXT = null;
	protected static CardReader READER = new CardReader();
	protected static BlockingHashMap<String, CardData> MAP = new BlockingHashMap<String, CardData>();

	public static enum DeviceStatus {
		ERROR, TIMEOUT,
		// KEYRINGExceptions
		KEYRING_EXCEPTION, KEYRING_FILE_EXCEPTION, PAIRING_KEYS_EXCEPTION, PAIRING_KEYS_FOUND_ERROR,
		PAIRING_KEYS_NOT_FOUND, DEVICE_PAIRING_ERROR, PAIRING_KEY_SAVE_ERROR, PAIRING_ERROR, NOT_PAIRED,
		// Session Exceptions
		SESSION_ERROR,
		// Default Errors
		DISCONNECTED,
		// Positive Cases
		SESSION_CREATED, PAIRED, CONNECTING, CONNECTED, PAIRING_KEYS_FOUND;
	}

	public static enum CardStatus {
		ERROR, NOCARD, REMOVED, FOUND, READING, SCANNED;
	}

	public static enum DataStatus {
		READ_ERROR, SYNC_ERROR, EMPTY, VALID_DATA, SYNCING, SYNCED;
	}

	@Value("${jax.offsite.url}")
	String serverUrl;

	// @Value("${device.terminal.id}")
	String terminalId;

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	@Autowired
	RestService restService;

	boolean devicePairingCredsValid = true;
	DevicePairingCreds devicePairingCreds;
	SessionPairingCreds sessionPairingCreds;

	boolean readerStarted = false;
	private long lastreadtime = 0L;
	protected DeviceStatus deviceStatus = DeviceStatus.DISCONNECTED;
	protected CardStatus cardStatusValue = CardStatus.NOCARD;
	protected DataStatus dataStatusValue = DataStatus.EMPTY;
	private Object lock = new Object();

	private NetAddress address = null;

	public NetAddress getAddress() {
		if (address != null) {
			return address;
		}
		synchronized (lock) {
			address = NetworkAdapter.getAddress();
		}
		return address;
	}

	public DevicePairingCreds getDevicePairingCreds() {

		if (getAddress() == null) {
			return null;
		}

		if (devicePairingCreds != null) {
			return devicePairingCreds;
		}

		synchronized (lock) {
			Keyring keyring;
			try {
				keyring = Keyring.create();
			} catch (BackendNotSupportedException ex) {
				SWAdapterGUI.CONTEXT.log(ex.getMessage());
				LOGGER.error("pairing Exception", ex);
				status(DeviceStatus.KEYRING_EXCEPTION);
				return null;
			}

			if (keyring.isKeyStorePathRequired()) {
				try {
					File keyStoreFile = File.createTempFile("keystore", ".keystore");
					keyring.setKeyStorePath(keyStoreFile.getPath());
				} catch (IOException ex) {
					status(DeviceStatus.KEYRING_FILE_EXCEPTION);
					SWAdapterGUI.CONTEXT.log(ex.getMessage());
					LOGGER.error("pairing Exception:IOException", ex);
				}
			}

			if (devicePairingCredsValid) {
				try {
					String passwordEncd = keyring.getPassword(SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString());
					byte[] terminalCredsByts = Base64.getDecoder().decode(passwordEncd);
					String terminalCredsStrs = new String(terminalCredsByts);
					DevicePairingCreds dpr = JsonUtil.fromJson(terminalCredsStrs, DevicePairingCreds.class);
					if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegId())) {
						devicePairingCreds = dpr;
						terminalId = devicePairingCreds.getDeivceTerminalId();
						status(DeviceStatus.PAIRING_KEYS_FOUND);
					} else {
						devicePairingCredsValid = false;
					}
				} catch (LockException ex) {
					SWAdapterGUI.CONTEXT.log(ex.getMessage());
					status(DeviceStatus.PAIRING_KEYS_FOUND_ERROR);
				} catch (PasswordRetrievalException ex) {
					status(DeviceStatus.PAIRING_KEYS_NOT_FOUND);
					SWAdapterGUI.CONTEXT.log(DeviceStatus.PAIRING_KEYS_NOT_FOUND.toString());
				} catch (Exception e) {
					status(DeviceStatus.PAIRING_KEYS_EXCEPTION);
					SWAdapterGUI.CONTEXT.log(DeviceStatus.PAIRING_KEYS_EXCEPTION.toString());
					LOGGER.error("pairing Exception", e);
					devicePairingCredsValid = false;
				}
			} else {
				// keyring.setPassword(SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString(),
				// null);
				// devicePairingCredsValid
			}

			if (ArgUtil.isEmpty(devicePairingCreds)) {
				if (ArgUtil.isEmpty(terminalId)) {
					return null;
				}
				DevicePairingRequest req = DeviceRestModels.get();
				req.setDeivceTerminalId(terminalId);
				req.setDeivceClientType(ClientType.BRANCH_ADAPTER);
				try {
					AmxApiResponse<DevicePairingCreds, Object> resp = restService.ajax(serverUrl)
							.meta(new DeviceMetaInfo())
							.path(DeviceConstants.Path.DEVICE_PAIR)
							.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
							.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp()).post(req)
							.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairingCreds, Object>>() {
							});
					if (resp.getResults().size() > 0) {
						DevicePairingCreds dpr = resp.getResult();
						if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegId())) {
							dpr.setDeivceTerminalId(terminalId);
							dpr.setDeivceClientType(ClientType.BRANCH_ADAPTER);
							devicePairingCreds = dpr;
							devicePairingCredsValid = true;
							String terminalCredsStrs = JsonUtil.toJson(dpr);
							String passwordEncd = Base64.getEncoder().encodeToString(terminalCredsStrs.getBytes());

							try {
								keyring.setPassword(SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString(), passwordEncd);
								status(DeviceStatus.PAIRED);
							} catch (LockException ex) {
								status(DeviceStatus.PAIRING_KEY_SAVE_ERROR);
								SWAdapterGUI.CONTEXT.log(ex.getMessage());
								LOGGER.error("pairing Exception:LockException", ex);
							} catch (PasswordSaveException ex) {
								status(DeviceStatus.PAIRING_KEY_SAVE_ERROR);
								SWAdapterGUI.CONTEXT.log("PAIRING_KEYS_CANNOT_SAVED");
							} catch (Exception e) {
								status(DeviceStatus.PAIRING_KEY_SAVE_ERROR);
								SWAdapterGUI.CONTEXT.log(e.getMessage());
							}
						}
					} else {
						status(DeviceStatus.NOT_PAIRED);
						SWAdapterGUI.CONTEXT.log("NOT_ABLE_TO_PAIR");
					}
				} catch (AmxApiException e) {
					status(DeviceStatus.PAIRING_ERROR);
					SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getErrorKey());
				} catch (AmxException e) {
					status(DeviceStatus.PAIRING_ERROR);
					SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getMessage());
				} catch (Exception e) {
					status(DeviceStatus.PAIRING_ERROR);
					SWAdapterGUI.CONTEXT.log("CLIENT ERROR : " + e.getMessage());
				}
			}
		}
		return devicePairingCreds;
	}

	public SessionPairingCreds getSessionPairingCreds() {
		if (sessionPairingCreds != null) {
			return sessionPairingCreds;
		}
		if (getDevicePairingCreds() == null) {
			return null;
		}

		synchronized (lock) {
			try {
				sessionPairingCreds = restService.ajax(serverUrl)
						.meta(new DeviceMetaInfo())
						.path(DeviceConstants.Path.SESSION_CREATE)
						.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
						.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
						.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
						.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())
						.get().as(new ParameterizedTypeReference<AmxApiResponse<SessionPairingCreds, Object>>() {
						}).getResult();
				status(DeviceStatus.SESSION_CREATED);
			} catch (AmxApiException e) {
				status(DeviceStatus.SESSION_ERROR);
				SWAdapterGUI.CONTEXT.log(e.getErrorKey() + " - REGID : " + devicePairingCreds.getDeviceRegId());
				if ("CLIENT_INVALID_PAIR_TOKEN".equals(e.getErrorKey())) {
					devicePairingCredsValid = false;
				} else if ("CLIENT_NOT_FOUND".equals(e.getErrorKey())) {
					devicePairingCredsValid = false;
					terminalId = null;
				}
			} catch (AmxException e) {
				status(DeviceStatus.SESSION_ERROR);
				SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getMessage());
			} catch (Exception e) {
				status(DeviceStatus.SESSION_ERROR);
				SWAdapterGUI.CONTEXT.log("CLIENT ERROR : " + e.getMessage());
				LOGGER.error("getSessionPairingCreds", e);
			}

			if (!devicePairingCredsValid) {
				devicePairingCreds = null;
			}
		}

		return sessionPairingCreds;
	}

	@Scheduled(fixedDelay = 1000, initialDelay = 4000)
	public void readTask() {
		LOGGER.debug("ACardReaderService:readTask");
		if (SWAdapterGUI.CONTEXT == null) {
			return;
		}
		if (getSessionPairingCreds() == null) {
			return;
		}
		try {
			CardReader reader = read();
			LOGGER.debug("ACardReaderService:readTask:GUI {} {}", reader.getCardActiveTime(), lastreadtime);
			if (reader.getCardActiveTime() > lastreadtime) {
				LOGGER.debug("ACardReaderService:readTask:TIME");
				lastreadtime = reader.getCardActiveTime();
				status(DataStatus.SYNCING);
				restService.ajax(serverUrl)
						.meta(new DeviceMetaInfo())
						.path(DeviceConstants.Path.DEVICE_STATUS_CARD)
						.pathParam(DeviceConstants.Params.PARAM_SYSTEM_ID, terminalId)
						.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
						.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
						.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
						.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())
						.header(
								DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY,
								sessionPairingCreds.getDeviceSessionToken())
						.header(
								DeviceConstants.Keys.CLIENT_REQ_TOKEN_XKEY,
								DeviceConstants.generateDeviceReqToken(sessionPairingCreds, devicePairingCreds))
						.post(reader).asObject();
				status(DataStatus.SYNCED);
			}
		} catch (AmxApiException e) {
			status(DataStatus.SYNC_ERROR);
			SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getErrorKey());
		} catch (AmxException e) {
			status(DataStatus.SYNC_ERROR);
			SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getMessage());
		} catch (InterruptedException e) {
			status(DataStatus.SYNC_ERROR);
			LOGGER.error(serverUrl, e);
		}
	}

	@Scheduled(fixedDelay = 2000, initialDelay = 5000)
	public void pingTask() {
		LOGGER.debug("ACardReaderService:pingTask");
		if (SWAdapterGUI.CONTEXT == null || CONTEXT == null) {
			CONTEXT = this;
			status(DeviceStatus.DISCONNECTED);
			SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(0);// PING COUNT

		} else if (devicePairingCreds == null) {
			CONTEXT = this;
			status(DeviceStatus.NOT_PAIRED);
			SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(0);// PING COUNT
		} else {
			SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(2);
			if (TimeUtils.isDead(READER.getDeviceActiveTime(), 5000)) {
				LOGGER.debug("ACardReaderService:pingTask:TIME:5sec");
				DeviceStatus readerStatusNow = ping(false);
				if (TimeUtils.isDead(READER.getDeviceActiveTime(), 15000)) { // TIMEOUT 15 sec
					if (DeviceStatus.CONNECTING.equals(readerStatusNow)) {
						status(DeviceStatus.TIMEOUT);
						SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(0);// PING COUNT
						SWAdapterGUI.CONTEXT.log(deviceStatus.toString());
					} else if (TimeUtils.isDead(READER.getDeviceActiveTime(), 20000)
							&& DeviceStatus.TIMEOUT.equals(readerStatusNow)) { // DISCONNECTED 20 sec
						status(DeviceStatus.DISCONNECTED);
						SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(0);// PING COUNT
						SWAdapterGUI.CONTEXT.log(deviceStatus.toString());
					}
				}
			} else {
				SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(3); // PING COUNT
			}
		}
		progress();
	}

	public void pong(String readerName) {
		LOGGER.debug("ACardReaderService:pong");
		status(DeviceStatus.CONNECTED);
		READER.setReader(readerName);
		READER.setDeviceActiveTime(System.currentTimeMillis());
		SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(4); // PING COUNT
	}

	public abstract DeviceStatus ping(boolean checkCard);

	public abstract boolean start();

	public CardData poll() throws InterruptedException {
		LOGGER.debug("ACardReaderService:poll");
		return MAP.take(CARD_READER_KEY, 5, TimeUnit.SECONDS);
	}

	public void push(CardData cardData) {
		if (cardData != null && cardData.getTimestamp() >= READER.getCardActiveTime()) {
			LOGGER.debug("ACardReaderService:push");
			READER.setData(cardData.isValid() ? cardData : null);
			READER.setCardActiveTime(cardData.getTimestamp());
			READER.setDeviceActiveTime(Math.max(READER.getDeviceActiveTime(), cardData.getTimestamp()));
			status(DataStatus.VALID_DATA);
			MAP.put(CARD_READER_KEY, cardData);
		}
	}

	public CardReader read() throws InterruptedException {
		LOGGER.debug("ACardReaderService:read");
		SWAdapterGUI.CONTEXT.log("Waiting...");
		if (!readerStarted || DeviceStatus.DISCONNECTED.equals(deviceStatus)) {
			readerStarted = start();
		}
		CardData data = poll();
		if (data != null && data.isValid()) {
			SWAdapterGUI.CONTEXT.log("ID : " + data.getIdentity());
			push(data);
		} else {
			status(DataStatus.EMPTY);
		}
		return READER;
	}

	private void clear() {
		LOGGER.debug("KWTCardReader:clear:START");
		push(new CardData());
		LOGGER.debug("KWTCardReader:clear:END");
	}

	public void reset() {
		LOGGER.debug("KWTCardReader:reset");
		deviceStatus = DeviceStatus.DISCONNECTED;
	}

	private void progress() {
		SWAdapterGUI.CONTEXT.progress(deviceStatus, cardStatusValue, dataStatusValue);
	}

	public void status(DataStatus syncing) {
		this.dataStatusValue = syncing;
		SWAdapterGUI.CONTEXT.foundData(syncing);
		progress();
	}

	public void status(CardStatus cardStatusValue) {
		this.cardStatusValue = cardStatusValue;
		if (cardStatusValue.compareTo(CardStatus.ERROR) > 0) {
			this.deviceStatus = DeviceStatus.CONNECTED;
		}

		SWAdapterGUI.CONTEXT.foundDevice(this.deviceStatus);
		SWAdapterGUI.CONTEXT.foundCard(this.cardStatusValue);

		if (CardStatus.SCANNED.equals(cardStatusValue)) {
			SWAdapterGUI.CONTEXT.foundCard(cardStatusValue);
			SWAdapterGUI.CONTEXT.foundData(DataStatus.VALID_DATA);
		} else if (CardStatus.REMOVED.equals(this.cardStatusValue)) {
			SWAdapterGUI.CONTEXT.foundCard(cardStatusValue);
			dataStatusValue = DataStatus.EMPTY;
			status(DataStatus.EMPTY);
			clear();
		} else if (CardStatus.ERROR.equals(this.cardStatusValue)) {
			SWAdapterGUI.CONTEXT.foundCard(cardStatusValue);
			reset();
		}
		progress();
	}

	public void status(DeviceStatus deviceStatusvalue) {
		this.deviceStatus = deviceStatusvalue;
		SWAdapterGUI.CONTEXT.foundDevice(deviceStatusvalue);
		progress();
	}
}
