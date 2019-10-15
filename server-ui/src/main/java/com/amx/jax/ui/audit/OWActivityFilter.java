package com.amx.jax.ui.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.session.SessionContextService;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.utils.ArgUtil;

@Component
public class OWActivityFilter implements AuditFilter<CActivityEvent> {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	SessionContextService sessionContextService;

	/** The user device. */
	@Autowired
	UserDeviceBean userDevice;

	/** The guest session. */
	@Autowired
	GuestSession guestSession;

	/** The app config. */
	@Autowired
	AppConfig appConfig;

	@Override
	public void doFilter(CActivityEvent event) {

		if (guestSession.getCustomerModel() != null) {
			event.setCustomerId(guestSession.getCustomerModel().getCustomerId());
		}

		AuditActorInfo x = sessionContextService.getContext(AuditActorInfo.class);
		if (ArgUtil.is(x)) {
			event.setActor(x);
		}

	}

}
