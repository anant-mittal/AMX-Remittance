package com.amx.jax.broker;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;

@Configuration
@EnableScheduling
@Component
@Service
public class BrokerScheduler {

	Logger logger = LoggerService.getLogger(BrokerScheduler.class);

	@Autowired
	private BrokerService brokerService;

	@Scheduled(fixedDelay = BrokerConstants.PUSH_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {
		brokerService.pushNewEventNotifications();
	}

	@Scheduled(
			fixedDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY,
			initialDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY)
	public void cleanUpEventNotificationRecords() {
		brokerService.cleanUpEventNotificationRecords();
	}
}
