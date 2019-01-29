package com.amx.jax.radar.jobs.sample;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.MCQ;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class SampleTask2 {

	private static final Logger LOGGER = LoggerService.getLogger(SampleTask2.class);

	@Autowired
	private MCQ mcq;

	@Autowired
	AppConfig appConfig;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 5)
	public void doTask() throws InterruptedException {
		boolean x;
		if (x = mcq.claimLeaderShip(SampleTask2.class, AmxCurConstants.INTERVAL_SEC * 10)) {
			Thread.sleep(AmxCurConstants.INTERVAL_SEC * 3);
			LOGGER.info("======= I am doing my Task @ {}", appConfig.getSpringAppName());
			mcq.resignLeaderShip(SampleTask2.class);
		} else {
			LOGGER.info("======= I NOT doing my Task @ {}", appConfig.getSpringAppName());
		}
	}

}
