package com.amx.jax.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class RbaacConfig {

	@Value("${rbaac.device.sessiontimeout}")
	Long deviceSessionTimeout;

	@Value("${rbaac.device.pairotpvalidationtime}")
	Integer paireOtpvalidationTime;

	public Long getDeviceSessionTimeout() {
		return deviceSessionTimeout;
	}

	public void setDeviceSessionTimeout(Long deviceSessionTimeout) {
		this.deviceSessionTimeout = deviceSessionTimeout;
	}

	public Integer getPaireOtpvalidationTime() {
		return paireOtpvalidationTime;
	}

	public void setPaireOtpvalidationTime(Integer paireOtpvalidationTime) {
		this.paireOtpvalidationTime = paireOtpvalidationTime;
	}

}
