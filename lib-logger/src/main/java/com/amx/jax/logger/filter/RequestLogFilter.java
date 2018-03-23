package com.amx.jax.logger.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.AppConstants;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;
import com.amx.utils.UniqueID;

@Component
// @PropertySource("classpath:application-logger.properties")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLogFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = ((HttpServletRequest) request);

			String siteId = request.getParameter(TenantContextHolder.TENANT);
			if (siteId != null && !Constants.BLANK.equals(siteId)) {
				TenantContextHolder.setCurrent(siteId);
			}

			String traceId = req.getHeader(AppConstants.TRACE_ID_KEY);
			if (StringUtils.isEmpty(traceId)) {
				traceId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRACE_ID_KEY));
			}
			if (StringUtils.isEmpty(traceId)) {
				String sessionID = null;
				HttpSession session = req.getSession(false);
				if (session == null) {
					sessionID = UniqueID.generateString();
				} else {
					sessionID = ArgUtil.parseAsString(session.getAttribute(AppConstants.SESSION_ID_KEY),
							UniqueID.generateString());
					// if (StringUtils.isEmpty(sessionID)) {
					// sessionID = UniqueID.generateString();
					// }
				}
				traceId = ContextUtil.getTraceId(true, sessionID);
				MDC.put("traceId", traceId);
				ContextUtil.map().put(AppConstants.SESSION_ID_KEY, sessionID);
				req.getSession().setAttribute(AppConstants.SESSION_ID_KEY, sessionID);
			} else {
				ContextUtil.setTraceId(traceId);
				MDC.put("traceId", traceId);
			}
			LOGGER.info("Request IN {}", req.getRequestURI());
			// String mdcData = String.format("trace : %s", traceId);
			chain.doFilter(request, response);
			LOGGER.info("Request OUT {}", req.getRequestURI());
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
