package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rates.AmxCurConstants.RSource;
import com.amx.jax.rates.AmxCurConstants.RType;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.jax.rest.RestService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class AmanKuwaitJob extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(AmanKuwaitJob.class);

	@Autowired
	private RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	XmlMapper xmlMapper = new XmlMapper();

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void doTask() {

		String response = restService.ajax("http://www.amankuwait.com/AmanWebsite/RateSheet/RateSheet.aspx")
				.get().asString();
		// xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		try {
			AmanKuwaitModels.Rates rates2 = xmlMapper.readValue(response, AmanKuwaitModels.Rates.class);
			for (AmanKuwaitModels.CurRates rates : rates2.getCurRates()) {
				AmxCurRate trnsfrRate = new AmxCurRate();
				trnsfrRate.setrSrc(RSource.AMANKUWAIT);
				trnsfrRate.setrDomCur(RCur.KWD);
				trnsfrRate.setrForCur(rates.getCode());

				trnsfrRate.setrType(RType.SELL_TRNSFR);
				trnsfrRate.setrRate(rates.getKdrate());
				curRateRepository.insertRate(trnsfrRate);

				AmxCurRate buyCash = trnsfrRate.clone(RType.BUY_CASH, rates.getBuyrate());
				curRateRepository.insertRate(buyCash);

				AmxCurRate sellCash = trnsfrRate.clone(RType.SELL_CASH, rates.getSellrate());
				curRateRepository.insertRate(sellCash);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
