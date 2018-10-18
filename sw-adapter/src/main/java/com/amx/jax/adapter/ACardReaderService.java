package com.amx.jax.adapter;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import com.amx.jax.adapter.kwt.KWTCardReaderService;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;
import com.amx.utils.TimeUtils;

public abstract class ACardReaderService {

	private static final Logger LOGGER = LoggerService.getLogger(KWTCardReaderService.class);

	@Value("${device.server.url}")
	String serverUrl;
	@Value("${device.system.id}")
	String systemId;

	@Autowired
	RestService restService;

	boolean readerConnected = false;
	long lastPingTime = 0L;

	private long lastreadtime = 0L;

	@Scheduled(fixedDelay = 1000)
	public void readTask() {
		if (SWAdapterLauncher.GUI == null) {
			return;
		}
		try {
			CardReader reader = read();
			lastPingTime = Math.max(lastPingTime, reader.getReaderTime());
			lastPingTime = Math.max(lastPingTime, reader.getDataTime());
			if (reader.getDataTime() > lastreadtime) {
				lastreadtime = reader.getDataTime();
				SWAdapterLauncher.GUI.foundSync(true);
				restService.ajax(serverUrl).path(DeviceConstants.DEVICE_INFO_URL)
						.pathParam(DeviceConstants.PARAM_SYSTEM_ID, systemId).post(reader).asObject();
				SWAdapterLauncher.GUI.foundSync(false);
			}
		} catch (InterruptedException e) {
			SWAdapterLauncher.GUI.foundSync(false);
			LOGGER.error(serverUrl, e);
		}
	}

	@Scheduled(fixedDelay = 2000, initialDelay = 5000)
	public void pingTask() {
		if (SWAdapterLauncher.GUI != null && TimeUtils.isDead(lastPingTime, 2000)) {
			boolean readerDetected = ping();
			if (readerConnected != readerDetected) {
				SWAdapterLauncher.GUI.foundReader(readerDetected);
			}
			readerConnected = readerDetected;
			if (!readerConnected) {
				SWAdapterLauncher.GUI.foundReader(false);
				start();
			}
			lastPingTime = System.currentTimeMillis();
		}
	}

	public abstract boolean start();

	public abstract boolean ping();

	public abstract CardReader read() throws InterruptedException;
}
