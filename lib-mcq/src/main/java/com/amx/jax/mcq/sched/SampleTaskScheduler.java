package com.amx.jax.mcq.sched;

import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;

import net.javacrumbs.shedlock.core.SchedulerLock;

//@Configuration
//@EnableScheduling
//@Component
//@Service
public class SampleTaskScheduler {
	private static final Logger LOGGER = LoggerService.getLogger(SampleTaskScheduler.class);

	@SchedulerLock(name = "SampleTaskScheduler",
			lockAtLeastFor = 2000,
			lockAtMostFor = 2000)
	@Scheduled(fixedDelay = 2000)
	public void doTask() throws InterruptedException {
		LOGGER.info("Started");
		Thread.sleep(5000);
		LOGGER.info("FInished");
	}

}
