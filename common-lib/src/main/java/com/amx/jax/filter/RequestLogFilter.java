package com.amx.jax.filter;

import java.io.IOException;

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
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
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

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			long startTime = System.currentTimeMillis();
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
			AuditServiceClient.trackStatic(new RequestTrackEvent(req));
			chain.doFilter(request, new AppResponseWrapper(resp));
			AuditServiceClient.trackStatic(new RequestTrackEvent(resp, req,System.currentTimeMillis()-startTime));

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
