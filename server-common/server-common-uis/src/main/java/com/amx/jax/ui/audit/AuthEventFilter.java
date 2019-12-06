package com.amx.jax.ui.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.jax.session.SessionContextService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.utils.ArgUtil;

/**
 * The Class AuthEventFilter.
 */
@Component
public class AuthEventFilter implements AuditFilter<CAuthEvent> {

	/** The user device. */
	@Autowired
	UserDeviceBean userDevice;

	/** The guest session. */
	@Autowired
	GuestSession guestSession;

	/** The app config. */
	@Autowired
	AppConfig appConfig;

	@Autowired(required = false)
	SessionContextService sessionContextService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.logger.client.AuditFilter#doFilter(com.amx.jax.logger.AuditEvent)
	 */
	@Override
	public void doFilter(CAuthEvent event) {
		event.setIdentiy(guestSession.getIdentity());

		if (guestSession.getCustomerModel() != null) {
			event.setUserId(ArgUtil.parseAsString(guestSession.getCustomerModel().getCustomerId()));
			event.setCustomerId(guestSession.getCustomerModel().getCustomerId());
		}

		if (userDevice.getUserDevice().getFingerprint() == null) {
			userDevice.resolve();
		}

		if (sessionContextService != null) {
			AuditActorInfo x = sessionContextService.getContext(AuditActorInfo.class);
			if (ArgUtil.is(x)) {
				event.setActor(x);
			}
		}
		event.setAgent(userDevice.getUserAgent());
	}

}
