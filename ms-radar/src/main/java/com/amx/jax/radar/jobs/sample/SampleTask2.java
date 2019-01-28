package com.amx.jax.radar.jobs.sample;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.MCQ;
import com.amx.jax.rates.AmxCurConstants;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class SampleTask2 {

	private static final Logger LOGGER = LoggerService.getLogger(SampleTask2.class);

	@Autowired
	private MCQ mcq;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 5)
	public void doTask() throws InterruptedException {
		if (mcq.claimLeaderShip(this.getClass().getName(), AmxCurConstants.INTERVAL_SEC * 5)) {
			Thread.sleep(AmxCurConstants.INTERVAL_SEC * 3);
			LOGGER.info("======= I am doing my Task");
			mcq.resignLeaderShip(this.getClass());
		} else {
			LOGGER.info("======= I NOT doing my Task");
		}
	}

}
