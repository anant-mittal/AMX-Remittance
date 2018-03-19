package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.ui.auth.AuthEvent;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDevice;
import com.bootloaderjs.ArgUtil;

@Component
public class AuthEventFilter implements AuditFilter<AuthEvent> {

	@Autowired
	UserDevice userDevice;

	@Autowired
	GuestSession guestSession;

	@Override
	public void doFilter(AuthEvent event) {
		event.setComponent("Server-UI");
		userDevice.resolve();
		event.setIdentiy(guestSession.getIdentity());
		if (guestSession.getCustomerModel() != null) {
			event.setUserId(ArgUtil.parseAsString(guestSession.getCustomerModel().getCustomerId()));
		}
	}

}
