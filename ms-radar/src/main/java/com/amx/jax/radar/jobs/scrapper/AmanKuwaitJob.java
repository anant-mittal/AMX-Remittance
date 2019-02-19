package com.amx.jax.radar.jobs.scrapper;

import java.io.IOException;

import org.redisson.api.RType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.client.snap.ISnapService.RateSource;
import com.amx.jax.client.snap.ISnapService.RateType;
import com.amx.jax.dict.Currency;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQLocker;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.rates.AmxCurConstants;
import com.amx.jax.rates.AmxCurRate;
import com.amx.jax.rates.AmxCurRateRepository;
import com.amx.jax.rest.RestService;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
@ConditionalOnProperty("jax.jobs.scrapper.rate")
public class AmanKuwaitJob extends ARadarTask {

	private static final Logger LOGGER = LoggerService.getLogger(AmanKuwaitJob.class);

	@Autowired
	private RestService restService;

	@Autowired
	private AmxCurRateRepository curRateRepository;

	private XmlMapper xmlMapper = new XmlMapper();

	private Candidate LOCK = new Candidate().fixedDelay(AmxCurConstants.INTERVAL_MIN_30)
			.maxAge(AmxCurConstants.INTERVAL_HRS).queue(AmanKuwaitJob.class);

	@Autowired
	private MCQLocker mcq;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_MIN_30)
	public void lockedTask() {
		if (mcq.lead(LOCK)) {
			doTask();
			mcq.resign(LOCK);
		}
	}

	public void doTask() {
		LOGGER.info("Scrapper Task");

		String response = restService.ajax("http://www.amankuwait.com/AmanWebsite/RateSheet/RateSheet.aspx")
				.get().asString();
		// xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
		try {
			AmanKuwaitModels.Rates rates2 = xmlMapper.readValue(response, AmanKuwaitModels.Rates.class);
			for (AmanKuwaitModels.CurRates rates : rates2.getCurRates()) {
				AmxCurRate trnsfrRate = new AmxCurRate();
				trnsfrRate.setrSrc(RateSource.AMANKUWAIT);
				trnsfrRate.setrDomCur(Currency.KWD);
				trnsfrRate.setrForCur(rates.getCode());

				trnsfrRate.setrType(RateType.SELL_TRNSFR);
				trnsfrRate.setrRate(rates.getKdrate());
				curRateRepository.insertRate(trnsfrRate);

				AmxCurRate buyCash = trnsfrRate.clone(RateType.BUY_CASH, rates.getBuyrate());
				curRateRepository.insertRate(buyCash);

				AmxCurRate sellCash = trnsfrRate.clone(RateType.SELL_CASH, rates.getSellrate());
				curRateRepository.insertRate(sellCash);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
