package com.amx.jax.radar.logger;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.CActivityEvent;

import com.amx.jax.tunnel.TunnelService;

/**
 * The Class LoggerController.
 */
@RestController
public class LoggerController {

	@Autowired
	TunnelService tunnelService;

	@Autowired
	AuditService auditService;

	/**
	 * Sync perms meta.
	 *
	 * @param map
	 *            the map
	 */
	@RequestMapping(value = "/api/event/map", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody Map<String, Object> map) {
		AuditServiceClient.publishAbstractEvent(map);
	}

	/**
	 * Sync perms meta.
	 *
	 * @param event
	 *            the event
	 */
	@RequestMapping(value = "/api/event/CActivityEvent", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody CActivityEvent event) {
		auditService.log(event);
	}

	/**
	 * Sync perms meta.
	 *
	 * @param event
	 *            the event
	 */
	@RequestMapping(value = "/api/event/AuditEvent", method = RequestMethod.POST)
	public void syncPermsMeta(@RequestBody AuditEvent event) {
		auditService.log(event);
	}

}
