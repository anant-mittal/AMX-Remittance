package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.postman.PostManException;
import com.amx.jax.task.events.GeoNotifyTask;
import com.amx.jax.tunnel.TunnelService;
import com.amx.jax.ui.WebAppConfig;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * The Class HotPointService.
 */
@Component
public class HotPointService {

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The b push service. */
	@Autowired
	private TunnelService tunnelService;

	/**
	 * Notify.
	 *
	 * @param customerId
	 *            the customer id
	 * @param hotpoint
	 * @param token
	 * @return the list
	 * @throws PostManException
	 *             the post man exception
	 */
	public GeoNotifyTask notify(BigDecimal customerId, String token, GeoHotPoints hotpoint) throws PostManException {
		GeoNotifyTask task = new GeoNotifyTask();
		task.setCustomerId(customerId);
		task.setGeoPoint(hotpoint.toString());
		task.setAppTitle(webAppConfig.getAppTitle());
		if (webAppConfig.isNotifyGeoEnabled() || true) {
			tunnelService.task(task);
				}
		return task;
	}

}
