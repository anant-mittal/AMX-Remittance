package com.amx.jax.logger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.logger.repository.AuditLogRepository;
import com.amx.jax.tunnel.TunnelClient;
import com.amx.jax.tunnel.TunnelService;

@RestController
public class LoggerController {

	private static final Logger LOGGER = LoggerService.getLogger(LoggerController.class);

	@Autowired
	AuditLogRepository auditLogRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	TunnelService tunnelService;

	/**
	 * @task Sync DB perms
	 */
	@RequestMapping(value = "/api/event", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody SessionEvent event) {
		// mongoTemplate.save(event, "Event");
		// auditLogRepository.save(event);
		tunnelService.send(TunnelClient.TEST_TOPIC, event);
	}

}
