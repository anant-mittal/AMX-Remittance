package com.amx.jax.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

/**
 * The Class WebAppConfig.
 */
@TenantScoped
@Component
public class WebAppConfig {

	/** The features. */
	@TenantValue("${ui.features}")
	private String[] features;

	/** The app title. */
	@TenantValue("${application.title}")
	private String appTitle;

	/** The notification gap. */
	@Value("${notification.sleep.gap}")
	private String notificationGap;

	/** The notify range long. */
	@Value("${notification.range.long}")
	private String notifyRangeLong;

	/** The notify range short. */
	@Value("${notification.range.short}")
	private String notifyRangeShort;

	@Value("${notification.geo.enabled}")
	private boolean notifyGeoEnabled;

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public String[] getFeatures() {
		return features;
	}

	/**
	 * Gets the app title.
	 *
	 * @return the app title
	 */
	public String getAppTitle() {
		return appTitle;
	}

	/**
	 * Gets the notification gap.
	 *
	 * @return the notification gap
	 */
	public String getNotificationGap() {
		return notificationGap;
	}

	/**
	 * Gets the notify range long.
	 *
	 * @return the notify range long
	 */
	public String getNotifyRangeLong() {
		return notifyRangeLong;
	}

	/**
	 * Gets the notify range short.
	 *
	 * @return the notify range short
	 */
	public String getNotifyRangeShort() {
		return notifyRangeShort;
	}

	public boolean isNotifyGeoEnabled() {
		return notifyGeoEnabled;
	}

}
