package com.amx.jax.ui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.ui.session.UserDevice;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class SessionAuditFilter implements AuditFilter<SessionEvent> {

	@Autowired
	UserDevice userDevice;

	@Override
	public void doFilter(SessionEvent event) {
		if (userDevice.getFingerprint() == null) {
			userDevice.resolve();
		}
		event.setSessionId(ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.SESSION_ID_KEY)));
		event.setDevice(userDevice.toMap());
	}

}
