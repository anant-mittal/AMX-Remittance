package com.amx.jax.adapter;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;
import com.amx.utils.TimeUtils;

public abstract class ACardReaderService {

	public static String CARD_READER_KEY = CardData.class.getName();
	private static final Logger LOGGER = LoggerService.getLogger(ACardReaderService.class);
	public static ACardReaderService CONTEXT = null;
	protected static CardReader READER = new CardReader();
	protected static BlockingHashMap<String, CardData> MAP = new BlockingHashMap<String, CardData>();

	public static enum DeviceStatus {
		ERROR, TIMEOUT, DISCONNECTED, CONNECTING, CONNECTED;
	}

	public static enum CardStatus {
		ERROR, NOCARD, REMOVED, FOUND, READING, SCANNED;
	}

	public static enum DataStatus {
		READ_ERROR, SYNC_ERROR, EMPTY, VALID_DATA, SYNCING, SYNCED;
	}

	@Value("${device.server.url}")
	String serverUrl;
	@Value("${device.system.id}")
	String systemId;

	@Autowired
	RestService restService;

	boolean readerStarted = false;
	private long lastreadtime = 0L;
	protected DeviceStatus deviceStatus = DeviceStatus.DISCONNECTED;
	protected CardStatus cardStatusValue = CardStatus.NOCARD;
	protected DataStatus dataStatusValue = DataStatus.EMPTY;

	@Scheduled(fixedDelay = 1000)
	public void readTask() {
		LOGGER.debug("ACardReaderService:readTask");
		if (SWAdapterGUI.CONTEXT == null) {
			return;
		}
		try {
			CardReader reader = read();
			LOGGER.debug("ACardReaderService:readTask:GUI {} {}", reader.getCardActiveTime(), lastreadtime);
			if (reader.getCardActiveTime() > lastreadtime) {
				LOGGER.debug("ACardReaderService:readTask:TIME");
				lastreadtime = reader.getCardActiveTime();
				status(DataStatus.SYNCING);
				restService.ajax(serverUrl).path(DeviceConstants.DEVICE_INFO_URL)
						.pathParam(DeviceConstants.PARAM_SYSTEM_ID, systemId).post(reader).asObject();
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
