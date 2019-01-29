package com.amx.jax.radar.jobs.sample;

import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.radar.TestSizeApp;
import com.amx.jax.rates.AmxCurConstants;

import net.javacrumbs.shedlock.core.SchedulerLock;

@Configuration
@EnableScheduling
@Component
@Service
@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS_DEBUG)
public class SampleTaskScheduler {

	private static final Logger LOGGER = LoggerService.getLogger(SampleTaskScheduler.class);

	@SchedulerLock(name = "SampleTaskScheduler",
			lockAtLeastFor = AmxCurConstants.INTERVAL_SEC * 2,
			lockAtMostFor = AmxCurConstants.INTERVAL_MIN)
	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 2)
	public void doTask() throws InterruptedException {
		// Thread.sleep(AmxCurConstants.INTERVAL_SEC * 5);
	}

}
