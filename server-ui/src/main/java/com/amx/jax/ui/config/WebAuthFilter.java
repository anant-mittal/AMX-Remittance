package com.amx.jax.ui.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.ui.service.SessionService;
import com.bootloaderjs.ContextUtil;

@Component
public class WebAuthFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthFilter.class);

	@Autowired
	SessionService sessionService;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		long time = System.currentTimeMillis();

		try {
			if (sessionService.validatedUser() && !sessionService.indexedUser()) {
				LOGGER.info("User is logged in somewhere else so logging out this one, traceid={}",
						ContextUtil.getTraceId());
				sessionService.unauthorize();
				HttpServletResponse response = ((HttpServletResponse) resp);
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/logout");
			} else {
				chain.doFilter(req, resp);
			}
		} finally {
			time = System.currentTimeMillis() - time;
			LOGGER.info("Trace Id in filter end {} time taken was {}", ContextUtil.getTraceId(), time);
		}

	}

	@Override
	public void destroy() {
		// empty
	}
}
