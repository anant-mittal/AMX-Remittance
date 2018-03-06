package com.amx.jax.logger.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bootloaderjs.ArgUtil;
import com.bootloaderjs.ContextUtil;
import com.bootloaderjs.UniqueID;

@Component
// @PropertySource("classpath:application-logger.properties")
@Order(Ordered.HIGHEST_PRECEDENCE)
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
			HttpServletRequest req = ((HttpServletRequest) request);

			String traceId = req.getHeader("x-trace-id");
			if (StringUtils.isEmpty(traceId)) {
				String sessionID = ArgUtil.parseAsString(req.getSession().getAttribute("x-session-id"));
				if (StringUtils.isEmpty(sessionID)) {
					sessionID = UniqueID.generateString();
					req.getSession().setAttribute("x-session-id", sessionID);
				}
				traceId = ContextUtil.getTraceId(true, sessionID);
			}

			//String mdcData = String.format("trace : %s", traceId);
			MDC.put("traceId", traceId); // Variable 'mdcData' is referenced in Spring Boot's logging.pattern.level
			chain.doFilter(request, response);
		} finally {
			// Tear down MDC data:
			// ( Important! Cleans up the ThreadLocal data again )
			MDC.clear();
			ContextUtil.clear();
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
