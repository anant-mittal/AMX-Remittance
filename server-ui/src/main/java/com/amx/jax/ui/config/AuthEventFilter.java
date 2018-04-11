package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.ui.auth.CAuthEvent;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.utils.ArgUtil;

@Component
public class AuthEventFilter implements AuditFilter<CAuthEvent> {

	@Autowired
	UserDeviceBean userDevice;

	@Autowired
	GuestSession guestSession;

	@Autowired
	AppConfig appConfig;

	@Override
	public void doFilter(CAuthEvent event) {
		event.setIdentiy(guestSession.getIdentity());
		if (guestSession.getCustomerModel() != null) {
			event.setUserId(ArgUtil.parseAsString(guestSession.getCustomerModel().getCustomerId()));
		}
		if (userDevice.getFingerprint() == null) {
			userDevice.resolve();
		}
		event.setDevice(userDevice.toMap());
	}

}
