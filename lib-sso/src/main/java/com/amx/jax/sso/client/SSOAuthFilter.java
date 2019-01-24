package com.amx.jax.sso.client;

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

import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.sso.SSOUserSessions;

/**
 * The Class SSOAuthFilter.
 */
@Component
public class SSOAuthFilter implements Filter {

	@Autowired
	SSOUser sSOUser;

	@Autowired
	SSOUserSessions sSOUserSessions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		if (sSOUser.isAuthDone() && !sSOUserSessions.isValidUnique()) {
			sSOUser.setAuthDone(false);
			HttpServletResponse response = ((HttpServletResponse) resp);
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", SSOConstants.APP_LOGOUT_URL);
		} else {
			chain.doFilter(req, resp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// empty
	}
}
