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

public abstract class ACardReaderService {

	private static final Logger LOGGER = LoggerService.getLogger(KWTCardReaderService.class);

	@Value("${device.server.url}")
	String serverUrl;
	@Value("${device.system.id}")
	String systemId;

	@Autowired
	RestService restService;

	private long lastreadtime = 0L;

	@Scheduled(fixedDelay = 1000)
	public void readTask() {
		try {
			CardReader reader = read();
			if (reader.getDataTime() > lastreadtime) {
				lastreadtime = reader.getDataTime();
				LOGGER.info("Data Updated...  Sending....");
				restService.ajax(serverUrl).path(DeviceConstants.DEVICE_INFO_URL)
						.pathParam(DeviceConstants.PARAM_SYSTEM_ID, systemId).post(reader).asObject();
			}
		} catch (InterruptedException e) {
			LOGGER.error(serverUrl, e);
		}
	}

	public abstract CardReader read() throws InterruptedException;
}
