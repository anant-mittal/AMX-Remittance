package com.amx.jax.ui.config;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bootloaderjs.ContextUtil;

@Component
public class WebRequestFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// empty
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		long time = System.currentTimeMillis();
		LOGGER.info("Trace Id in th begining {} {}", ContextUtil.getTraceId(),
				((HttpServletRequest) req).getRequestURI());
		try {
			chain.doFilter(req, resp);
		} finally {
			time = System.currentTimeMillis() - time;
			LOGGER.trace("{}: {} ms ", ((HttpServletRequest) req).getRequestURI(), time);
			LOGGER.info("Trace Id in filter end {} {}", ContextUtil.getTraceId(),
					((HttpServletRequest) req).getRequestURI());
		}
	}

	@Override
	public void destroy() {
		// empty
	}
}
