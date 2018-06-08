package com.amx.jax.ui.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.service.SessionService;

@Component
public class WebAuthFilter implements Filter {

	@Autowired
	SessionService sessionService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		// HttpServletRequest httpRequest = (HttpServletRequest) req;
		// System.out.println(
		// "===[" + httpRequest.getRequestedSessionId() + "]=====[" +
		// httpRequest.isRequestedSessionIdValid()
		//
		// + "]===========[" + sessionService.getUserSession().isValid());

		if (!sessionService.validateSessionUnique()) {
			HttpServletResponse response = ((HttpServletResponse) resp);
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", "/logout");
		} else {
			String referrer = req.getParameter(UIConstants.REFERRER);
			if (referrer != null) {
				sessionService.getUserSession().setReferrer(referrer);
			}
			AppContextUtil.setActorId(
					new AuditActor(AuditActor.ActorType.CUSTOMER, sessionService.getUserSession().getUserid()));
			chain.doFilter(req, resp);
		}

	}

	@Override
	public void destroy() {
		// empty
	}
}
