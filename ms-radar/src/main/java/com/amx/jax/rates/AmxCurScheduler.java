package com.amx.jax.rates;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rates.AmxConstants.RCur;
import com.amx.jax.rates.AmxConstants.RSource;
import com.amx.jax.rates.AmxConstants.RType;
import com.amx.jax.rates.models.AmanKuwaitModels;
import com.amx.jax.rest.RestService;
import com.amx.utils.CollectionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
public class AmxCurScheduler {

	@Autowired
	RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	Logger logger = LoggerService.getLogger(AmxCurScheduler.class);
	public static final int DELETE_NOTIFICATION_FREQUENCY = 2 * 1000;

	@Scheduled(fixedDelay = DELETE_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {
		try {
			AmanKuwaitModels.Rates rates = new AmanKuwaitModels.Rates();
			List<AmanKuwaitModels.CurRates> curRates = CollectionUtil.getList(new AmanKuwaitModels.CurRates(),
					new AmanKuwaitModels.CurRates());
			rates.setCurRates(curRates);
			String xml = xmlMapper.writeValueAsString(rates);
			System.out.println(xml);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String response = restService.ajax("http://www.amankuwait.com/AmanWebsite/RateSheet/RateSheet.aspx")
				.get().asString();
		// xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		try {
			AmanKuwaitModels.Rates rates2 = xmlMapper.readValue(response, AmanKuwaitModels.Rates.class);

			for (AmanKuwaitModels.CurRates rates : rates2.getCurRates()) {
				AmxCurRate trnsfrRate = new AmxCurRate();
				trnsfrRate.setrSrc(RSource.AMANKUWAIT);
				trnsfrRate.setrDomCur(RCur.KWD);
				trnsfrRate.setrType(RType.SELL_TRNSFR);
				trnsfrRate.setrForCur(rates.getCode());
				trnsfrRate.setrRate(rates.getKdrate());
				curRateRepository.insertRate(trnsfrRate);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
