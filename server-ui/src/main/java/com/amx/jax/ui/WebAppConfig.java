package com.amx.jax.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantScoped;
import com.amx.jax.scope.TenantValue;

@TenantScoped
@Component
public class WebAppConfig {

	@TenantValue("${ui.features}")
	private String[] features;

	@TenantValue("${application.title}")
	private String appTitle;

	@Value("${ui.features}")
	private String[] elementToSearch;

	@Value("${notification.sleep.gap}")
	private String notificationGap;

	@Value("${notification.range.long}")
	private String notifyRangeLong;

	@Value("${notification.range.short}")
	private String notifyRangeShort;

	public String[] getFeatures() {
		return features;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public String getNotificationGap() {
		return notificationGap;
	}

	public String[] getElementToSearch() {
		return elementToSearch;
	}

	public String getNotifyRangeLong() {
		return notifyRangeLong;
	}

	public String getNotifyRangeShort() {
		return notifyRangeShort;
	}

}
