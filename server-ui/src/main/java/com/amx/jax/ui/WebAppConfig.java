package com.amx.jax.ui;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.jax.ui.UIConstants.Features;

/**
 * The Class WebAppConfig.
 */
@TenantScoped
@Component
public class WebAppConfig {

	/** The clean CDN url. */
	@Value("${jax.cdn.url}")
	private String cleanCDNUrl;

	/** The fcm sender id. */
	@Value("${fcm.senderid}")
	private String fcmSenderId;

	@TenantValue("${ui.ios.appid}")
	private String iosAppId;

	/** The features. */
	@TenantValue("${ui.features}")
	private Features[] features;

	/** The app title. */
	@TenantValue("${application.title}")
	private String appTitle;

	/** The notification gap. */
	@TenantValue("${notification.sleep.gap}")
	private String notificationGap;

	/** The notify range long. */
	@TenantValue("${notification.range.long}")
	private String notifyRangeLong;

	/** The notify range short. */
	@TenantValue("${notification.range.short}")
	private String notifyRangeShort;

	@TenantValue("${notification.geo.enabled}")
	private boolean notifyGeoEnabled;

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public Features[] getFeatures() {
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

	public String getCleanCDNUrl() {
		return cleanCDNUrl;
	}

	public String getFcmSenderId() {
		return fcmSenderId;
	}

	public String getIosAppId() {
		return iosAppId;
	}

	@TenantValue("${app.meta.country.id}")
	BigDecimal countryId;

	@TenantValue("${app.meta.company.id}")
	BigDecimal companyId;

	@TenantValue("${app.meta.lang.id}")
	BigDecimal languageId;

	@TenantValue("${app.meta.country.branch.id}")
	BigDecimal countrybranchId;

	public BigDecimal getCountryId() {
		return countryId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public BigDecimal getCountrybranchId() {
		return countrybranchId;
	}

}
