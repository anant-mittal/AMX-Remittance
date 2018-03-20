package com.amx.jax.ui.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.config.AppConfig;
import com.amx.jax.logger.AuditConstant;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.ui.session.UserDevice;
import com.bootloaderjs.ArgUtil;
import com.bootloaderjs.ContextUtil;

@Component
public class WebSessionListener implements HttpSessionListener {

	@Autowired(required = false)
	UserDevice userDevice;

	@Autowired
	private AppConfig appConfig;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setComponent(appConfig.getAppName());
		evt.setType(SessionEvent.Type.SESSION_CREATED);
		evt.setSessionId(ArgUtil.parseAsString(ContextUtil.map().get(AuditConstant.SESSION_ID_KEY)));
		AuditServiceClient.staticLogger(evt);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setComponent(appConfig.getAppName());
		evt.setType(SessionEvent.Type.SESSION_DESTROYED);
		HttpSession session = se.getSession();
		if (session != null) {
			evt.setSessionId(ArgUtil.parseAsString(session.getAttribute(AuditConstant.SESSION_ID_KEY)));
		}

		AuditServiceClient.staticLogger(evt);
	}

}
