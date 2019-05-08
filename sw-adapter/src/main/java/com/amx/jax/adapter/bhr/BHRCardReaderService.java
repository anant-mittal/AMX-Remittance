package com.amx.jax.adapter.bhr;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amx.jax.adapter.ACardReaderService;
import com.amx.jax.device.CardData;
import com.amx.jax.rest.RestService;
import com.amx.utils.Constants;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;

@Configuration
@EnableScheduling
@Component
@ConditionalOnProperty(value = ACardReaderService.PROP_TENANT_KEY, havingValue = "bhr")
public class BHRCardReaderService extends ACardReaderService {

	public static Logger LOGGER = LoggerFactory.getLogger(BHRCardReaderService.class);

	private static final JsonPath PATH_CARDSERIALNUMBER = new JsonPath("/CardData/CardSerialNumber");
	private static final JsonPath PATH_CPRNO = new JsonPath("/CardData/IdNumber");
	private static final JsonPath PATH_EMAIL = new JsonPath("/CardData/Email");
	private static final JsonPath PATH_ENGLISHFULLNAME = new JsonPath("/CardData/EnglishFullName");
	private static final JsonPath PATH_BIRTHDATE = new JsonPath("/CardData/BirthDate");

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

	@Autowired
	RestService restService;

	private String cprid;

	@Override
	public DeviceStatus ping(boolean checkCard) {
		LOGGER.debug("BHRCardReaderService");
		return DeviceStatus.CONNECTED;
	}

	@Scheduled(fixedDelay = 1000, initialDelay = 4000)
	public void readCard() {
		if (CONTEXT == null) {
			return;
		}
		BHRCardReaderService.CONTEXT.status(CardStatus.FOUND);
		try {

			Map<String, Object> options = new HashMap<String, Object>();

			options.put("ReadCardInfo", true);
			options.put("ReadPersonalInfo", true);
			options.put("ReadAddressDetails", true);
			options.put("ReadBiometrics", true);
			options.put("ReadEmploymentInfo", true);
			options.put("ReadImmigrationDetails", true);
			options.put("ReadTrafficDetails", true);
			options.put("SilentReading", true);
			options.put("ReaderIndex", -1);
			options.put("ReaderName", "");
			options.put("OutputFormat", "JSON");
			options.put("ValidateCard", true);

			Map<String, Object> resp = restService.ajax("http://localhost:5050/api/operation/ReadCard").post(options)
					.asMap();

			LOGGER.info("=========" + JsonUtil.toJson(resp));

			String cpridTemp = PATH_CPRNO.load(resp, Constants.BLANK);
			if (cpridTemp.equals(cprid)) {
				return;
			}

			cprid = cpridTemp;

			BHRCardReaderService.CONTEXT.status(CardStatus.READING);

			LOGGER.debug("BHRCardReaderServiceListner:CardConnectionEvent");
			CardData data = new CardData();

			// data.setTitle("MR");
			data.setIdentity(cprid);

			// Arabic Details
			// data.setLocalFullName("Amar Akbar Anthony");
			// data.setLocalGender("M");

			// English Details
			data.setFullName(PATH_ENGLISHFULLNAME.load(resp, Constants.BLANK));

			data.setDob(PATH_BIRTHDATE.load(resp, Constants.BLANK));

			data.setEmail(PATH_EMAIL.load(resp, Constants.BLANK));

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
			LOGGER.error("BHRCardReaderListner:CardConnectionEvent:Exception {}", e2);
			BHRCardReaderService.CONTEXT.status(DataStatus.READ_ERROR);
		}

	}

}
