package com.amx.jax.radar.jobs.sample;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.rates.AmxCurConstants;

@Configuration
@EnableScheduling
@Component
@Service
//@ConditionalOnExpression(TestSizeApp.ENABLE_JOBS)
public class SampleTask {

	private static final Logger LOGGER = LoggerService.getLogger(SampleTask.class);

	@Autowired
	private RedissonClient redisson;

	@Scheduled(fixedDelay = AmxCurConstants.INTERVAL_SEC * 2)
	public void doTask() throws InterruptedException {
		RLock lock = redisson.getLock("anyLock");
		lock.lock();
		Thread.sleep(AmxCurConstants.INTERVAL_SEC * 3);
		LOGGER.info("=======");
		lock.unlock();
	}

}
