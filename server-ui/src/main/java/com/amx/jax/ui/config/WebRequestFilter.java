package com.amx.jax.ui.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantContextHolder;
import com.bootloaderjs.Constants;
import com.bootloaderjs.ContextUtil;
import com.bootloaderjs.Urly;

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
		HttpServletRequest request = ((HttpServletRequest) req);
		String url = request.getRequestURI();
		LOGGER.info("Trace Id in th begining {} {}", ContextUtil.getTraceId(), url);
		String siteId = request.getParameter(TenantContextHolder.TENANT);
		if (siteId == null) {
			siteId = Urly.getSubDomainName(request.getServerName());
		}

		/**
		 * Not able to use session scoped bean here hence using typical session
		 * attribute;
		 */
		if (siteId == null) {
			siteId = (String) request.getSession().getAttribute(TenantContextHolder.TENANT);
		}

		if (siteId != null && !Constants.BLANK.equals(siteId)) {
			request.getSession().setAttribute(TenantContextHolder.TENANT, siteId);
			TenantContextHolder.setCurrent(siteId);
		} else {
			TenantContextHolder.setDefault();
		}

		// LOGGER.info("Tenant {}", sessionService.getTenantBean().getTenant().getId());

		try {
			chain.doFilter(req, resp);
		} finally {
			time = System.currentTimeMillis() - time;
			LOGGER.info("Trace Id in filter end {} {}  time taken was {}", ContextUtil.getTraceId(), url, time);
		}
	}

	@Override
	public void destroy() {
		// empty
	}
}
