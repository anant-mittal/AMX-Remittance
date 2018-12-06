package com.amx.jax.rates;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rates.models.AmanKuwaitModels.Rates;
import com.amx.jax.rest.RestService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
public class CurScheduler {

	@Autowired
	RestService restService;

	Logger logger = LoggerService.getLogger(CurScheduler.class);
	public static final int DELETE_NOTIFICATION_FREQUENCY = 2 * 1000;

	@Scheduled(fixedDelay = DELETE_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {
		String response = restService.ajax("http://www.amankuwait.com/AmanWebsite/RateSheet/RateSheet.aspx")
				.get().asString();
		ObjectMapper objectMapper = new XmlMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		try {
			Rates rates = objectMapper.readValue(response, Rates.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
