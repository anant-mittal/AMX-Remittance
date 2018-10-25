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
import com.amx.jax.adapter.NetworkAdapter.NetAddress;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.DevicePairingResponse;
import com.amx.jax.device.DeviceRestModels.SessionPairingResponse;
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

	public static String CARD_READER_KEY = CardData.class.getName();
	private static final Logger LOGGER = LoggerService.getLogger(ACardReaderService.class);
	public static ACardReaderService CONTEXT = null;
	protected static CardReader READER = new CardReader();
	protected static BlockingHashMap<String, CardData> MAP = new BlockingHashMap<String, CardData>();

	public static enum DeviceStatus {
		ERROR, TIMEOUT, DISCONNECTED, NOT_PAIRED, PAIRED, CONNECTING, CONNECTED;
	}

	public static enum CardStatus {
		ERROR, NOCARD, REMOVED, FOUND, READING, SCANNED;
	}

	public static enum DataStatus {
		READ_ERROR, SYNC_ERROR, EMPTY, VALID_DATA, SYNCING, SYNCED;
	}

	@Value("${device.server.url}")
	String serverUrl;
	@Value("${device.terminal.id}")
	String terminalId;
	@Value("${device.reset.date}")
	String resetDate;

	@Autowired
	RestService restService;

	DevicePairingResponse devicePairingCreds;
	SessionPairingResponse sessionPairingCreds;

	boolean readerStarted = false;
	private long lastreadtime = 0L;
	protected DeviceStatus deviceStatus = DeviceStatus.DISCONNECTED;
	protected CardStatus cardStatusValue = CardStatus.NOCARD;
	protected DataStatus dataStatusValue = DataStatus.EMPTY;

	private NetAddress address = null;

	private NetAddress getAddress() {
		address = NetworkAdapter.getAddress();
		return address;
	}

	private DevicePairingResponse getDevicePairingCreds() {

		if (getAddress() == null) {
			return null;
		}

		if (devicePairingCreds != null) {
			return devicePairingCreds;
		}

		Keyring keyring;
		try {
			keyring = Keyring.create();
		} catch (BackendNotSupportedException ex) {
			SWAdapterGUI.CONTEXT.log(ex.getMessage());
			LOGGER.error("pairing Exception", ex);
			return null;
		}

		if (keyring.isKeyStorePathRequired()) {
			try {
				File keyStoreFile = File.createTempFile("keystore", ".keystore");
				keyring.setKeyStorePath(keyStoreFile.getPath());
			} catch (IOException ex) {
				SWAdapterGUI.CONTEXT.log(ex.getMessage());
				LOGGER.error("pairing Exception:IOException", ex);
			}
		}
		try {
			String passwordEncd = keyring.getPassword("amx-adapter", terminalId + "#" + resetDate);
			byte[] terminalCredsByts = Base64.getDecoder().decode(passwordEncd);
			String terminalCredsStrs = new String(terminalCredsByts);
			DevicePairingResponse dpr = JsonUtil.fromJson(terminalCredsStrs, DevicePairingResponse.class);
			if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegKey())) {
				devicePairingCreds = dpr;
			}
		} catch (LockException ex) {
			SWAdapterGUI.CONTEXT.log(ex.getMessage());
			LOGGER.error("pairing Exception:LockException", ex);
		} catch (PasswordRetrievalException ex) {
			SWAdapterGUI.CONTEXT.log("PAIRING_KEYS_NOT_FOUND");
		}

		if (ArgUtil.isEmpty(devicePairingCreds)) {
			DevicePairingRequest req = DeviceRestModels.get();
			req.setDeivceTerminalId(terminalId);
			req.setDeivceClientType(ClientType.BRANCH_ADAPTER);
			try {
				AmxApiResponse<DevicePairingResponse, Object> resp = restService.ajax(serverUrl)
						.path(DeviceConstants.Path.DEVICE_PAIR).header(AppConstants.DEVICE_ID_XKEY, address.getMac())
						.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp()).post(req)
						.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairingResponse, Object>>() {
						});
				if (resp.getResults().size() > 0) {
					DevicePairingResponse dpr = resp.getResult();
					if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegKey())) {
						devicePairingCreds = dpr;
						String terminalCredsStrs = JsonUtil.toJson(dpr);
						String passwordEncd = Base64.getEncoder().encodeToString(terminalCredsStrs.getBytes());

						try {
							keyring.setPassword("amx-adapter", terminalId + "#" + resetDate, passwordEncd);
							status(DeviceStatus.PAIRED);
						} catch (LockException ex) {
							SWAdapterGUI.CONTEXT.log(ex.getMessage());
							LOGGER.error("pairing Exception:LockException", ex);
						} catch (PasswordSaveException ex) {
							SWAdapterGUI.CONTEXT.log("PAIRING_KEYS_CANNOT_SAVED");
						}
					}
				}
			} catch (AmxApiException e) {
				SWAdapterGUI.CONTEXT.log(e.getErrorKey());
			} catch (AmxException e) {
				SWAdapterGUI.CONTEXT.log(e.getStatusKey());
			} catch (Exception e) {
				SWAdapterGUI.CONTEXT.log(e.getMessage());
			}
		}
		return devicePairingCreds;
	}

	private SessionPairingResponse getSessionPairingCreds() {
		if (sessionPairingCreds != null) {
			return sessionPairingCreds;
		}
		if (getDevicePairingCreds() == null) {
			return null;
		}

		try {
			sessionPairingCreds = restService.ajax(serverUrl).path(DeviceConstants.Path.SESSION_PAIR)
					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
					.header(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY, devicePairingCreds.getDeviceRegKey())
					.header(DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<SessionPairingResponse, Object>>() {
					}).getResult();
		} catch (AmxApiException e) {
			SWAdapterGUI.CONTEXT.log(e.getErrorKey());
		} catch (AmxException e) {
			SWAdapterGUI.CONTEXT.log(e.getStatusKey());
		} catch (Exception e) {
			SWAdapterGUI.CONTEXT.log(e.getMessage());
		}

		return sessionPairingCreds;
	}

	@Scheduled(fixedDelay = 1000)
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
				restService.ajax(serverUrl).path(DeviceConstants.Path.DEVICE_INFO_URL)
						.pathParam(DeviceConstants.Params.PARAM_SYSTEM_ID, terminalId)
						.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
						.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
						.header(DeviceConstants.Keys.DEVICE_REG_KEY_XKEY, devicePairingCreds.getDeviceRegKey())
						.header(DeviceConstants.Keys.DEVICE_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())
						.header(DeviceConstants.Keys.DEVICE_REQ_TOKEN_XKEY,
								DeviceConstants.generateDeviceReqToken(null, devicePairingCreds.getDeviceRegKey()))
						.post(reader).asObject();
				status(DataStatus.SYNCED);
			}
		} catch (InterruptedException e) {
			status(DataStatus.SYNC_ERROR);
			LOGGER.error(serverUrl, e);
		}
	}

	@Scheduled(fixedDelay = 2000, initialDelay = 1000)
	public void pingTask() {
		SWAdapterGUI.CONTEXT.updateDeviceHealthStatus(1);// PING COUNT
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

	private void reset() {
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
