package com.amx.jax.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.logger.events.AuditActorInfo;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.meta.MetaData;
import com.amx.jax.session.SessionContextService;
import com.amx.utils.ArgUtil;

@Component
public class JaxAuditFilter implements AuditFilter<CActivityEvent> {

	@Autowired
	MetaData metaData;

	@Autowired
	SessionContextService sessionContextService;

	@Override
	public void doFilter(CActivityEvent event) {
		if (!ArgUtil.isEmpty(metaData.getCustomerId())
				&& ArgUtil.isEmpty(event.getCustomerId())) {
			event.setCustomerId(metaData.getCustomerId());
		}

		if (CActivityEvent.Type.TRANSACTION_CREATED.equals(event.getType())) {
			// MapModel x = sessionContextService.getContext();
		} else if (CActivityEvent.Type.APPLICATION_CREATED.equals(event.getType())) {
			AuditActorInfo x = sessionContextService.getContext(AuditActorInfo.class);
			event.setActor(x);
		}

	}

}
