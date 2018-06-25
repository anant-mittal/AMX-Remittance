package com.amx.jax.logger;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.logger.repository.AuditLogRepository;
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

	@Autowired
	AuditService auditService;

	@RequestMapping(value = "/api/event/map", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody Map<String, Object> map) {
		AuditServiceClient.publishAbstractEvent(map);
	}

	@RequestMapping(value = "/api/event/SessionEvent", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody SessionEvent event) {
		auditService.log(event);
	}

	@RequestMapping(value = "/api/event/CActivityEvent", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody CActivityEvent event) {
		auditService.log(event);
	}

	@RequestMapping(value = "/api/event/AuditEvent", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody AuditEvent event) {
		auditService.log(event);
	}

}
