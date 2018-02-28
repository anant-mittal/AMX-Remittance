package com.amx.jax.logger.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.MDC;

import org.springframework.stereotype.Component;

@Component
public class RequestLogFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			// Setup MDC data:
			String mdcData = String.format("[userId:%s | requestId:%s] ", "", "");
			MDC.put("mdcData", mdcData); // Variable 'mdcData' is referenced in Spring Boot's logging.pattern.level
			chain.doFilter(request, response);
		} finally {
			// Tear down MDC data:
			// ( Important! Cleans up the ThreadLocal data again )
			MDC.clear();
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
