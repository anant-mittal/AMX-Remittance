package com.amx.jax.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.UniqueID;
import com.amx.utils.Urly;

@Component
// @PropertySource("classpath:application-logger.properties")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLogFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public String header(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			sb.append(headerName + "=[" + request.getHeader(headerName) + "],");
		}
		sb.append("}");
		return sb.toString();
	}

	private Object header(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Collection<String> headerNames = response.getHeaderNames();
		for (String headerName : headerNames) {
			sb.append(headerName + "=[" + response.getHeader(headerName) + "],");
		}
		sb.append("}");
		return sb.toString();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			HttpServletRequest req = ((HttpServletRequest) request);
			HttpServletResponse resp = ((HttpServletResponse) response);

			String siteId = req.getHeader(TenantContextHolder.TENANT);
			if (StringUtils.isEmpty(siteId)) {
				siteId = ArgUtil.parseAsString(request.getParameter(TenantContextHolder.TENANT));
				if (siteId == null) {
					siteId = Urly.getSubDomainName(request.getServerName());
				}
			}

			if (!StringUtils.isEmpty(siteId)) {
				TenantContextHolder.setCurrent(siteId, null);
			}

			Tenant tnt = TenantContextHolder.currentSite();

			String tranxId = req.getHeader(AppConstants.TRANX_ID_XKEY);
			if (StringUtils.isEmpty(tranxId)) {
				tranxId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRANX_ID_XKEY));
			}

			if (!StringUtils.isEmpty(tranxId)) {
				ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, tranxId);
			}

			String traceId = req.getHeader(AppConstants.TRACE_ID_XKEY);
			if (StringUtils.isEmpty(traceId)) {
				traceId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRACE_ID_XKEY));
			}
			if (StringUtils.isEmpty(traceId)) {
				String sessionID = null;
				HttpSession session = req.getSession(false);
				if (session == null) {
					sessionID = UniqueID.generateString();
				} else {
					sessionID = ArgUtil.parseAsString(session.getAttribute(AppConstants.SESSION_ID_XKEY),
							UniqueID.generateString());
				}
				traceId = ContextUtil.getTraceId(true, sessionID);
				MDC.put(ContextUtil.TRACE_ID, traceId);
				MDC.put(TenantContextHolder.TENANT, tnt);
				ContextUtil.map().put(AppConstants.SESSION_ID_XKEY, sessionID);
				req.getSession().setAttribute(AppConstants.SESSION_ID_XKEY, sessionID);
			} else {
				ContextUtil.setTraceId(traceId);
				MDC.put(ContextUtil.TRACE_ID, traceId);
				MDC.put(TenantContextHolder.TENANT, tnt);
			}
			LOGGER.info("REQT-IN {}={} : {}", req.getMethod(), req.getRequestURI(), header(req));
			// String mdcData = String.format("trace : %s", traceId);
			chain.doFilter(request, new AppResponseWrapper(resp));
			LOGGER.info("RESP-OUT {}={} : {}", resp.getStatus(), req.getRequestURI(), header(resp));
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
