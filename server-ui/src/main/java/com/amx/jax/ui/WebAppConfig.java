package com.amx.jax.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantProperties;
import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.utils.ArgUtil;

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

	@TenantValue("${company.idtype}")
	private String companyIdtype;

	@TenantValue("${company.tnt}")
	private String companyTnt;

	/**
	 * Gets the features.
	 *
	 * @return the features
	 */
	public Features[] getFeatures() {
		return features;
	}

	@Autowired
	TenantProperties tenantProperties;
	private List<Features> featuresList = null;

	public List<Features> getFeaturesList() {
		if (null == featuresList) {
			featuresList = new ArrayList<Features>();
			Features[] allFeatures = Features.values();
			Map<Features, Boolean> map = new HashMap<Features, Boolean>();

			for (int i = 0; i < allFeatures.length; i++) {
				boolean isFeature = ArgUtil.parseAsBoolean(
						tenantProperties.getProperties().getProperty("ui.features." + allFeatures[i]), false);
				if (isFeature) {
					map.put(allFeatures[i], true);
				} else {
					isFeature = ArgUtil.parseAsBoolean(
							tenantProperties.getProperties()
									.getProperty("ui.features." + allFeatures[i].toString().toUpperCase()),
							false);
					if (isFeature) {
						map.put(allFeatures[i], true);
					}
				}
			}
			for (int i = 0; i < features.length; i++) {
				map.put(features[i], true);
			}
			for (Entry<Features, Boolean> features : map.entrySet()) {
				featuresList.add(features.getKey());
			}
		}
		return featuresList;
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

	public String getCompanyIdtype() {
		return this.companyIdtype;
	}

	public String getCompanyTnt() {
		return this.companyTnt;
	}

}
