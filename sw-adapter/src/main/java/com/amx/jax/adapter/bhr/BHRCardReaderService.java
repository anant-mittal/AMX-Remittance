package com.amx.jax.adapter.bhr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amx.jax.adapter.ACardReaderService;
import com.amx.jax.device.CardData;

@Configuration
@EnableScheduling
@Component
@ConditionalOnProperty(value = ACardReaderService.PROP_TENANT_KEY, havingValue = "bhr")
public class BHRCardReaderService extends ACardReaderService {

	public static Logger LOGGER = LoggerFactory.getLogger(BHRCardReaderService.class);

	@Override
	public boolean start() {
		LOGGER.debug("BHRCardReaderService");
		try {
			deviceStatus = DeviceStatus.CONNECTING;
			return true;
		} catch (Exception e) {
			LOGGER.error("AMX --- Exception", e);
			return false;
		}
	}

	@Override
	public DeviceStatus ping(boolean checkCard) {
		LOGGER.debug("BHRCardReaderService");
		return DeviceStatus.CONNECTED;
	}

	@Scheduled(fixedDelay = 60000, initialDelay = 4000)
	public void readCard() {
		if (CONTEXT == null) {
			return;
		}
		BHRCardReaderService.CONTEXT.status(CardStatus.FOUND);
		try {
			BHRCardReaderService.CONTEXT.status(CardStatus.READING);
			LOGGER.debug("KWTCardReaderServiceListner:CardConnectionEvent");
			CardData data = new CardData();

			data.setTitle("MR");
			data.setIdentity("");

			// Arabic Details
			data.setLocalFullName("Amar Akbar Anthony");
			data.setLocalGender("M");

			// English Details
			data.setFullName("Amar Akbar Anthony");

			data.setDob("15/11/1987");

			BHRCardReaderService.CONTEXT.status(CardStatus.SCANNED);

			try {
				data.setGenuine(true);
			} catch (Exception exp) {
				data.setGenuine(false);
				BHRCardReaderService.CONTEXT.status(CardStatus.CARD_NOT_GENUINE);
			}

			try {
				data.setValid(true);
			} catch (Exception exp) {
				data.setValid(false);
				BHRCardReaderService.CONTEXT.status(CardStatus.CARD_EXPIRED);
			}
			BHRCardReaderService.CONTEXT.push(data);

		} catch (Exception e2) {
			LOGGER.error("KwCardReaderListner:CardConnectionEvent:Exception {}", e2);
			BHRCardReaderService.CONTEXT.status(DataStatus.READ_ERROR);
		}

	}

}
