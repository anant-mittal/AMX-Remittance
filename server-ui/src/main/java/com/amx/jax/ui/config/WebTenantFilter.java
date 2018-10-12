package com.amx.jax.ui.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.Urly;

/**
 * The Class WebTenantFilter.
 */
@Component
public class WebTenantFilter implements Filter {

	/** The logger. */
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
		HttpServletRequest request = ((HttpServletRequest) req);
		String siteId = request.getParameter(TenantContextHolder.TENANT);
		if (siteId == null) {
			siteId = Urly.getSubDomainName(request.getServerName());
		}
		HttpSession session = request.getSession(false);
		/**
		 * Not able to use session scoped bean here hence using typical session
		 * attribute;
		 */
		if (siteId == null && session != null) {
			siteId = ArgUtil.parseAsString(session.getAttribute(TenantContextHolder.TENANT));
		}

		if (siteId != null && !Constants.BLANK.equals(siteId) && session != null) {
			request.getSession().setAttribute(TenantContextHolder.TENANT, siteId);
			TenantContextHolder.setCurrent(siteId);
		} else {
			TenantContextHolder.setDefault();
		}
		chain.doFilter(req, resp);
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
