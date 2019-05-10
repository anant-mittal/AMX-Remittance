package com.amx.jax.ui.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.ui.session.UserDeviceBean;

/**
 * The Class SessionAuditFilter.
 */
@Component
public class SessionAuditFilter implements AuditFilter<SessionEvent> {

	/** The user device. */
	@Autowired(required = false)
	UserDeviceBean userDevice;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.logger.client.AuditFilter#doFilter(com.amx.jax.logger.AuditEvent)
	 */
	@Override
	public void doFilter(SessionEvent event) {
		event.setSessionId(AppContextUtil.getSessionId(false));
		RequestAttributes attribs = RequestContextHolder.getRequestAttributes();
		if (attribs instanceof NativeWebRequest) {
			if (userDevice != null) {
				if (userDevice.getUserDevice().getFingerprint() == null) {
					userDevice.resolve();
				}
				event.setDevice(userDevice.getUserDevice().getUserAgent());
			}
		}
	}

}
