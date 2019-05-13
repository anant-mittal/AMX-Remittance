package com.amx.jax.auditlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger log = LoggerFactory.getLogger(getClass());

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
		log.info("Here Ia am ");
		if (CActivityEvent.Type.TRANSACTION_CREATED.equals(event.getType())
				|| CActivityEvent.Type.APPLICATION_CREATED.equals(event.getType())) {
			AuditActorInfo x = sessionContextService.getContext(AuditActorInfo.class);
			event.setActor(x);
		}

	}

}
