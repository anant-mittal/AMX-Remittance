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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.ApiHttpExceptions.ApiHttpNotFoundException;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.service.SessionService;

/**
 * The Class WebAuthFilter.
 */
@Component
public class WebAuthFilter implements Filter {

	/** The session service. */
	@Autowired
	SessionService sessionService;

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

		if (!sessionService.isRequestAuthorized()) {
			HttpServletResponse response = ((HttpServletResponse) resp);
			response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", "/logout");
		} else {

			String referrer = req.getParameter(UIConstants.REFERRER);
			if (referrer != null) {
				sessionService.getUserSession().setReferrer(referrer);
			}

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
