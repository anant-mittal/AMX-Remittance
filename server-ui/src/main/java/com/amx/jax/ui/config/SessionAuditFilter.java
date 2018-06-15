package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.ui.session.UserDeviceBean;

@Component
public class SessionAuditFilter implements AuditFilter<SessionEvent> {

	@Autowired(required = false)
	UserDeviceBean userDevice;

	@Override
	public void doFilter(SessionEvent event) {
		if(userDevice!=null) {
			if (userDevice.getFingerprint() == null) {
				userDevice.resolve();
			}
			event.setDevice(userDevice.toMap());
		}
		event.setSessionId(AppContextUtil.getSessionId());
		
	}

}
