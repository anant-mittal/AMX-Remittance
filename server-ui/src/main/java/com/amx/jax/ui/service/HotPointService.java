package com.amx.jax.ui.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManException;
import com.amx.jax.task.events.GeoNotifyTask;
import com.amx.jax.tunnel.TunnelService;
import com.amx.jax.ui.WebAppConfig;
import com.amx.utils.ArgUtil;

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
		task.setGeoPoint(ArgUtil.parseAsString(hotpoint));
		task.setAppTitle(webAppConfig.getAppTitle());
		if (webAppConfig.isNotifyGeoEnabled()) {
			tunnelService.task(task);
				}
		return task;
	}

}
