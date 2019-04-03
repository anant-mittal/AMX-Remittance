package com.amx.jax.broker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.mcq.Candidate;
import com.amx.jax.mcq.MCQLocker;
import com.amx.utils.UniqueID;

@Configuration
@EnableScheduling
@Component
@Service
public class BrokerScheduler {

	Logger logger = LoggerService.getLogger(BrokerScheduler.class);

	private static Map<String, Candidate> LOCK_MAP = Collections.synchronizedMap(new HashMap<String, Candidate>());

	@Autowired
	private BrokerService brokerService;

	@Autowired
	BrokerConfig brokerConfig;

	@Autowired
	private MCQLocker mcq;

	private Candidate getLock(String tenant) {
		if (!LOCK_MAP.containsKey(tenant)) {
			LOCK_MAP.put(tenant, new Candidate().fixedDelay(BrokerConstants.PUSH_NOTIFICATION_FREQUENCY)
					.maxAge(BrokerConstants.PUSH_NOTIFICATION_FREQUENCY * 30).queue(
							"broker.scheduler.task." + tenant));
		}
		return LOCK_MAP.get(tenant);
	}

	@Scheduled(fixedDelay = BrokerConstants.PUSH_NOTIFICATION_FREQUENCY)
	public void pushNewEventNotifications() {
		Tenant[] tenants = brokerConfig.getTenants();
		for (Tenant tenant : tenants) {
			try {
				AppContextUtil.setTenant(tenant);
				String sessionId = UniqueID.generateString();
				AppContextUtil.setSessionId(sessionId);
				AppContextUtil.getTraceId(true, true);
				AppContextUtil.init();
				Candidate candidate = getLock(tenants.toString());
				if (mcq.lead(candidate)) {
					brokerService.pushNewEventNotifications(tenant, sessionId);
					mcq.resign(candidate);
				}
			} catch (Exception e) {
				logger.error("Scheduler Fetch ERROR", e);
			}
		}
	}

	@Scheduled(
			fixedDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY,
			initialDelay = BrokerConstants.DELETE_NOTIFICATION_FREQUENCY)
	public void cleanUpEventNotificationRecords() {

		Tenant[] tenants = brokerConfig.getTenants();
		for (Tenant tenant : tenants) {
			try {
				AppContextUtil.setTenant(tenant);
				String sessionId = UniqueID.generateString();
				AppContextUtil.setSessionId(sessionId);
				AppContextUtil.getTraceId(true, true);
				AppContextUtil.init();
				brokerService.cleanUpEventNotificationRecords(tenant, sessionId);
			} catch (Exception e) {
				logger.error("Scheduler Delete ERROR", e);
			}
		}
	}
}
