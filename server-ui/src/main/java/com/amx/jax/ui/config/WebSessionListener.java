package com.amx.jax.ui.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.UniqueID;

@Component
public class WebSessionListener implements HttpSessionListener {

	@Autowired
	private AppConfig appConfig;

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setComponent(appConfig.getAppName());
		evt.setType(SessionEvent.Type.SESSION_CREATED);
		evt.setSessionId(AppContextUtil.getSessionId());
		AuditServiceClient.logStatic(evt);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		SessionEvent evt = new SessionEvent();
		evt.setComponent(appConfig.getAppName());
		evt.setType(SessionEvent.Type.SESSION_DESTROYED);

		HttpSession session = se.getSession();
		if (session != null) {
			String sessionID = ArgUtil.parseAsString(session.getAttribute(AppConstants.SESSION_ID_XKEY),
					UniqueID.generateString());
			evt.setSessionId(sessionID);
			AppContextUtil.setSessionId(sessionID);
			String traceId = ContextUtil.getTraceId(true, sessionID);
			MDC.put(ContextUtil.TRACE_ID, traceId);
			MDC.put(TenantContextHolder.TENANT, AppContextUtil.getTenant());

		}
		AuditServiceClient.logStatic(evt);
	}

}
