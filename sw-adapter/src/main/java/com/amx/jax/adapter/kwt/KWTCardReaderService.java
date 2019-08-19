package com.amx.jax.adapter.kwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import com.amx.jax.adapter.ACardReaderService;

import pacicardlibrary.PACICardAPI;
import pacicardlibrary.PaciException;

@Configuration
@EnableScheduling
@Component
@ConditionalOnProperty(value = ACardReaderService.PROP_TENANT_KEY, havingValue = "kwt")
public class KWTCardReaderService extends ACardReaderService {

	public static Logger LOGGER = LoggerFactory.getLogger(KWTCardReaderService.class);
	public static PACICardAPI API = null;
	public static KWTCardReaderListner LISTNER = null;

	@Override
	public boolean start() {
		LOGGER.debug("KWTCardReaderService:start");
		try {
			deviceStatus = DeviceStatus.CONNECTING;
			if (API != null && LISTNER != null) {
				LISTNER.setEnabled(false);
				API.RemoveEventListener(LISTNER);
			}
			API = new PACICardAPI();
			LISTNER = new KWTCardReaderListner();
			LISTNER.setEnabled(true);
			API.AddEventListener(LISTNER);
			return true;
		} catch (PaciException pe) {
			LOGGER.error("AMX --- PaciException", pe);
			return false;
		} catch (Exception e) {
			LOGGER.error("AMX --- Exception", e);
			return false;
		}
	}

	@Override
	public DeviceStatus ping(boolean checkCard) {
		LOGGER.debug("KWTCardReaderService:ping");
		if (API != null) {
			if (API.GetReaders() == null) {
				return DeviceStatus.DISCONNECTED;
			}
			if (API.GetReaders().length > 0 && API.GetNumberOfReaders() > 0) {
				if (checkCard) {
					if (LISTNER == null && LISTNER.ping()) {
						return DeviceStatus.CONNECTED;
					}
				} else {
					return DeviceStatus.CONNECTED;
				}
			}
		}
		return deviceStatus;
	}

}
