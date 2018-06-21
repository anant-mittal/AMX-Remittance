package com.amx.jax.logger;

import java.util.HashMap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.repository.AuditLogRepository;

@RestController
public class LoggerController {

	private static final Logger LOGGER = LoggerService.getLogger(LoggerController.class);

	@Autowired
	AuditLogRepository auditLogRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	/**
	 * @task Sync DB perms
	 */
	@RequestMapping(value = "/api/event", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody HashMap<String, Object> event) {
		mongoTemplate.save(event, "Event");
		// auditLogRepository.save(event);
	}

}
