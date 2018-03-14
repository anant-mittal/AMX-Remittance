package com.amx.jax.ui.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.stereotype.Component;

import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.SessionEvent;

@Component
public class WebSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_CREATED);
		AuditServiceClient.staticLogger(evt);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setType(SessionEvent.Type.SESSION_DESTROYED);
		AuditServiceClient.staticLogger(evt);
	}

}
