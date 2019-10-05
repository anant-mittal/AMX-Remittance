package com.amx.jax.adapter.omn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.adapter.ACardReaderService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.JsonPath;
import com.amx.utils.TimeUtils;

@Configuration
@EnableScheduling
@Component
@ConditionalOnProperty(value = ACardReaderService.PROP_TENANT_KEY, havingValue = "omn")
public class OMNCardReaderService extends ACardReaderService {

	public static Logger LOGGER = LoggerFactory.getLogger(OMNCardReaderService.class);

	private static final JsonPath PATH_CARDSERIALNUMBER = new JsonPath("/CardSerialNumber");
	private static final JsonPath PATH_CPRNO = new JsonPath("/IdNumber");
	private static final JsonPath PATH_EMAIL = new JsonPath("/Email");
	private static final JsonPath PATH_ENGLISHFULLNAME = new JsonPath("/EnglishFullName");
	private static final JsonPath PATH_BIRTHDATE = new JsonPath("/BirthDate");

	@Override
	public boolean start() {
		LOGGER.debug("OMNCardReaderService");
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

	@Value("${rop.server.rest.url}")
	String ropserver;

	private String cprid;

	long touchtime = 0L;

	@Override
	public DeviceStatus ping(boolean checkCard) {
		LOGGER.debug("OMNCardReaderService");
		if (TimeUtils.isExpired(touchtime, 2000)) {
			return DeviceStatus.DISCONNECTED;
		}
		return DeviceStatus.CONNECTED;
	}

	@Scheduled(fixedDelay = 1000, initialDelay = 4000)
	public void readCard() {
		if (CONTEXT == null) {
			return;
		}
		String sessionId = this.getParams().getString(AppConstants.SESSION_ID_XKEY);
		if (ArgUtil.isEmpty(sessionId)) {
			return;
		}

		try {

			// HttpComponentsClientHttpRequestFactory x;
			LOGGER.info("Cookie==== {}", sessionId);
			AmxApiResponse<CardData, Object> resp = restService
					.ajax(ropserver + "/get-card-details")
					.header("Cookie", "JSESSIONID=" + sessionId)
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CardData, Object>>() {
					});

			CardData data = resp.getResult();
			data.setInfo(resp.getMessage());

			touchtime = System.currentTimeMillis();

			String cpridTemp = ArgUtil.parseAsString(data.getIdentity(), Constants.BLANK);

			if (cpridTemp.equals(cprid)) {
				return;
			}

			cprid = cpridTemp;

			if (ArgUtil.isEmpty(cprid)) {
				OMNCardReaderService.CONTEXT.status(CardStatus.REMOVED);
				return;
			}
			OMNCardReaderService.CONTEXT.status(CardStatus.FOUND);

			OMNCardReaderService.CONTEXT.status(CardStatus.READING);

			LOGGER.debug("OMNCardReaderServiceListner:CardConnectionEvent");

			// Arabic Details
			// data.setLocalFullName("Amar Akbar Anthony");
			// data.setLocalGender("M");

			// English Details

			OMNCardReaderService.CONTEXT.status(CardStatus.SCANNED);

			try {
				data.setGenuine(true);
			} catch (Exception exp) {
				data.setGenuine(false);
				OMNCardReaderService.CONTEXT.status(CardStatus.CARD_NOT_GENUINE);
			}

			try {
				data.setValid(true);
			} catch (Exception exp) {
				data.setValid(false);
				OMNCardReaderService.CONTEXT.status(CardStatus.CARD_EXPIRED);
			}
			OMNCardReaderService.CONTEXT.push(data);

		} catch (Exception e2) {
			// LOGGER.error("OMNCardReaderService:CardConnectionEvent:Exception {}", e2);
			OMNCardReaderService.CONTEXT.status(DataStatus.READ_ERROR);
		}

	}

}
