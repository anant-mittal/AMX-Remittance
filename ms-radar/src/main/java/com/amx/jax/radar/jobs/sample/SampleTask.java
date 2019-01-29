package com.amx.jax.radar.jobs.sample;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQ;
import com.amx.jax.rates.AmxCurConstants;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class SampleTask {

	private static final Logger LOGGER = LoggerService.getLogger(SampleTask.class);
	private static final Candidate lock = new Candidate().fixedDelay(AmxCurConstants.INTERVAL_SEC * 2)
			.maxAge(AmxCurConstants.INTERVAL_MIN).queue(SampleTask.class);

	@Autowired
	private MCQ mcq;

	@Autowired
	AppConfig appConfig;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 2)
	public void doTask() throws InterruptedException {
		if (mcq.lead(lock)) {
			Thread.sleep(AmxCurConstants.INTERVAL_SEC * 5);
			LOGGER.info("======= I DID my Task @ {} # {}", appConfig.getSpringAppName(),
					Math.round(System.currentTimeMillis() / 7000) - 309737000);
			mcq.resign(lock);
		}
	}

}
