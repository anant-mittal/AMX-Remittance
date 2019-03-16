package com.amx.jax.sso.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.client.AuditFilter;
import com.amx.jax.sso.SSOAuditEvent;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.ArgUtil;

/**
 * The Class SessionAuditFilter.
 */
@Component
public class SSOAuditFilter implements AuditFilter<SSOAuditEvent> {

	/** The user device. */
	@Autowired(required = false)
	private SSOUser ssoUser;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.logger.client.AuditFilter#doFilter(com.amx.jax.logger.AuditEvent)
	 */
	@Override
	public void doFilter(SSOAuditEvent event) {

		if (ArgUtil.isEmpty(AppContextUtil.getUserClient().getClientType())) {
			// Do SOmthing
		} else {
			event.clientType(AppContextUtil.getUserClient().getClientType());
		}

	}

}
