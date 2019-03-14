package com.amx.jax.adapter;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.scheduling.annotation.Scheduled;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceRestModels;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.DeviceRestModel;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.exception.AmxException;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.TenantProperties;
import com.amx.utils.ArgUtil;
import com.amx.utils.NetworkAdapter;
import com.amx.utils.NetworkAdapter.NetAddress;
import com.amx.utils.TimeUtils;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

public abstract class ACardReaderService {

	public static final String PROP_TENANT_KEY = "app.profile.tnt";

	public static final String CARD_READER_KEY = CardData.class.getName();
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
		ERROR, NOCARD, REMOVED, FOUND, READING, CARD_NOT_GENUINE, CARD_EXPIRED, SCANNED;
	}

	public static enum DataStatus {
		READ_ERROR, SYNC_ERROR, EMPTY, INVALID, VALID_DATA, SYNCING, SYNCED;
	}

	// @Value("${device.terminal.id}")
	String terminalId;

	@Value("${app.profile.version}")
	String version;

	@Value("${app.profile.tnt}")
	String tnt;

	@Value("${app.profile.env}")
	String env;

	@Autowired
	private ConfigurableEnvironment environment;

	@Autowired
	TenantProperties tenantProperties;

	Properties tntProp;

	@Autowired
	private DeviceConnectorClient adapterServiceClient;

	String serverUrl;

	boolean isLocal = false;

	String localIdentity;

	public String getServerUrl() {
		if (ArgUtil.isEmpty(serverUrl)) {
			synchronized (lock) {
				serverUrl = tntProp.getProperty("adapter." + tnt + "." + env + ".url");
				String serverDB = tntProp.getProperty("adapter." + tnt + "." + env + ".db");
				if (ArgUtil.isEmpty(serverUrl)) {
					serverUrl = environment.getProperty("adapter.local.url");
					this.isLocal = true;
					this.localIdentity = environment.getProperty("local.identity");
				}
				if (ArgUtil.isEmpty(serverDB)) {
					serverDB = environment.getProperty("adapter.local.db");
				}
				KeyUtil.setServiceName(serverDB);
				adapterServiceClient.setOffSiteUrl(serverUrl);
				String winTitle = tntProp.getProperty("adapter.title");
				SWAdapterGUI.updateTitle(String.format("%s - %s", winTitle, version));
			}
		}
		return serverUrl;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

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

		if (ArgUtil.isEmpty(getServerUrl())) {
			return null;
		}

		if (ArgUtil.isEmpty(getAddress() == null)) {
			return null;
		}

		if (!ArgUtil.isEmpty(devicePairingCreds)) {
			return devicePairingCreds;
		}

		synchronized (lock) {
			try {
				KeyUtil.getKeyRing();
			} catch (BackendNotSupportedException ex) {
				SWAdapterGUI.CONTEXT.log(ex.getMessage());
				LOGGER.error("pairing Exception", ex);
				status(DeviceStatus.KEYRING_EXCEPTION);
				return null;
			} catch (IOException ex) {
				status(DeviceStatus.KEYRING_FILE_EXCEPTION);
				SWAdapterGUI.CONTEXT.log(ex.getMessage());
				LOGGER.error("pairing Exception:IOException", ex);
				return null;
			} catch (KeyStoreException e) {
				LOGGER.error("KeyStoreException", e);
			} catch (CertificateException e) {
				LOGGER.error("CertificateException", e);
			} catch (NoSuchAlgorithmException e) {
				LOGGER.error("NoSuchAlgorithmException", e);
			}

			try {
				if (devicePairingCredsValid) {
					DevicePairingCreds dpr = KeyUtil.getDevicePairingCreds(address);
					if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegId())) {
						devicePairingCreds = dpr;
						terminalId = devicePairingCreds.getDeivceTerminalId();
						status(DeviceStatus.PAIRING_KEYS_FOUND);
					} else {
						devicePairingCredsValid = false;
					}
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

			if (ArgUtil.isEmpty(devicePairingCreds)) {
				if (ArgUtil.isEmpty(terminalId)) {
					return null;
				}
				DevicePairingRequest req = DeviceRestModels.get();
				req.setDeivceTerminalId(terminalId);
				req.setDeivceClientType(ClientType.BRANCH_ADAPTER);
				try {
					AmxApiResponse<DevicePairingCreds, Object> resp = adapterServiceClient.pairDevice(address, req);
					if (resp.getResults().size() > 0) {
						DevicePairingCreds dpr = resp.getResult();
						if (!ArgUtil.isEmpty(dpr) && !ArgUtil.isEmpty(dpr.getDeviceRegId())) {
							dpr.setDeivceTerminalId(terminalId);
							dpr.setDeivceClientType(ClientType.BRANCH_ADAPTER);
							devicePairingCreds = dpr;
							devicePairingCredsValid = true;

							try {
								KeyUtil.setDevicePairingCreds(dpr, address);
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
					SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getErrorKey(), e.getErrorMessage());
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
				sessionPairingCreds = adapterServiceClient.createSession(address, devicePairingCreds).getResult();
				status(DeviceStatus.SESSION_CREATED);
			} catch (AmxApiException e) {
				status(DeviceStatus.SESSION_ERROR);
				SWAdapterGUI.CONTEXT.log(e.getErrorKey() + " - REGID : " + devicePairingCreds.getDeviceRegId(),
						e.getErrorMessage());
				if ("CLIENT_INVALID_PAIR_TOKEN".equals(e.getErrorKey())) {
					devicePairingCredsValid = false;
				} else if ("CLIENT_NOT_FOUND".equals(e.getErrorKey())) {
					devicePairingCredsValid = false;
					terminalId = null;
				}
				LOGGER.error("getSessionPairingCreds" + e);
			} catch (AmxException e) {
				status(DeviceStatus.SESSION_ERROR);
				SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getMessage());
				LOGGER.error("getSessionPairingCreds" + e);
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

	public void resetTerminalPairing()
			throws Exception {
		if (!ArgUtil.isEmpty(this.getDevicePairingCreds())) {
			adapterServiceClient.deActivateDevice(this.getDevicePairingCreds());
		}
		KeyUtil.getKeyRing();
		KeyUtil.setDevicePairingCreds(new DeviceRestModel(), getAddress());
		devicePairingCreds = null;
		sessionPairingCreds = null;
		terminalId = null;
	}

	@Scheduled(fixedDelay = 1000, initialDelay = 4000)
	public void readTask() {

		AppContextUtil.init();
		getServerUrl();

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
				adapterServiceClient.saveCardDetailsByTerminal(terminalId, reader, address, devicePairingCreds,
						sessionPairingCreds);
				status(DataStatus.SYNCED);
			}
		} catch (AmxApiException e) {
			status(DataStatus.SYNC_ERROR);
			SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getErrorKey());
			LOGGER.error(getServerUrl(), e);
		} catch (AmxException e) {
			status(DataStatus.SYNC_ERROR);
			SWAdapterGUI.CONTEXT.log("SERVICE ERROR : " + e.getMessage());
			LOGGER.error(getServerUrl(), e);
		} catch (InterruptedException e) {
			status(DataStatus.SYNC_ERROR);
			LOGGER.error(getServerUrl(), e);
		} catch (Exception e) {
			status(DataStatus.SYNC_ERROR);
			LOGGER.error(getServerUrl(), e);
		}
	}

	@Scheduled(fixedDelay = 2000, initialDelay = 5000)
	public void pingTask() {

		AppContextUtil.init();

		LOGGER.debug("ACardReaderService:pingTask {} {}", tnt, env);
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
		SWAdapterGUI.CONTEXT.readerName(readerName);
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
			status(getDataStatus(cardData));
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
		DataStatus dataStatus = getDataStatus(data);
		status(dataStatus);
		if (DataStatus.VALID_DATA.equals(dataStatus)) {
			SWAdapterGUI.CONTEXT.log("ID : " + data.getIdentity());
		}
		if (!DataStatus.EMPTY.equals(dataStatus)) {
			push(data);
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
		if (this.isLocal) {
			try {
				CardReader reader = new CardReader();
				reader.setData(new CardData());
				reader.getData().setIdentity(localIdentity);
				adapterServiceClient.saveCardDetailsByTerminal(terminalId, reader, address, devicePairingCreds,
						sessionPairingCreds);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

	public DeviceStatus getDeviceStatus() {
		return deviceStatus;
	}

	public CardStatus getCardStatusValue() {
		return cardStatusValue;
	}

	public DataStatus getDataStatusValue() {
		return dataStatusValue;
	}

	// UTILS FUNXIONS
	private static DataStatus getDataStatus(CardData cardData) {
		if (ArgUtil.isEmpty(cardData)) {
			return DataStatus.EMPTY;
		} else if (!cardData.isValid()) {
			return DataStatus.INVALID;
		}
		return DataStatus.VALID_DATA;
	}

	@Autowired
	AppConfig appConfig;

	@PostConstruct
	public void init() {
		TenantContextHolder.setCurrent(tnt);
		if (TenantContextHolder.currentSite() != null) {
			Tenant.DEFAULT = TenantContextHolder.currentSite();
		}
		tntProp = tenantProperties.getProperties();
	}

	public String getVersion() {
		return version;
	}
}
