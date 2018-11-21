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

import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;

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
		Tenant tnt = TenantContextHolder.currentSite(false);

		HttpSession session = request.getSession(false);
		/**
		 * Not able to use session scoped bean here hence using typical session
		 * attribute;
		 */
		if (tnt == null && session != null) {
			String tntStr = ArgUtil.parseAsString(session.getAttribute(TenantContextHolder.TENANT));
			tnt = TenantContextHolder.fromString(tntStr, null);
			if (tnt != null) {
				request.getSession().setAttribute(TenantContextHolder.TENANT, tnt.toString());
			}
		}

		if (tnt == null) {
			TenantContextHolder.setDefault();
		} else {
			TenantContextHolder.setCurrent(tnt);

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
