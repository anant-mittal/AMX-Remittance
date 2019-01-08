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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.jax.model.UserDevice;
import com.amx.jax.rest.AppRequestContextInFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.UniqueID;
import com.amx.utils.Urly;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AppRequestFilter implements Filter {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("Filter Intialzed");
	}

	/**
	 * DO NOT REMOVE THIS ONE AS IT WILL DO VERY IMPORTANT STUFF LIKE TENANT BEAN
	 * INIT
	 */
	@Autowired
	AppConfig appConfig;

	@Autowired
	CommonHttpRequest commonHttpRequest;

	@Autowired(required = false)
	AppRequestContextInFilter appContextInFilter;

	private boolean doesTokenMatch(HttpServletRequest req, HttpServletResponse resp, String traceId) {
		String authToken = req.getHeader(AppConstants.AUTH_KEY_XKEY);
		if (StringUtils.isEmpty(authToken)
				|| (CryptoUtil.validateHMAC(appConfig.getAppAuthKey(), traceId, authToken) == false)) {
			return false;
		}
		return true;
	}

	private boolean isRequestValid(
			RequestType reqType, HttpServletRequest req, HttpServletResponse resp,
			String traceId) {
		if (reqType.isAuth() && appConfig.isAppAuthEnabled() && !doesTokenMatch(req, resp, traceId)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse resp = ((HttpServletResponse) response);
		try {
			RequestType reqType = commonHttpRequest.getApiRequestType(req);
			AppContextUtil.setRequestType(reqType);

			// Tenant Tracking
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

			// Tranx Id Tracking
			String tranxId = req.getHeader(AppConstants.TRANX_ID_XKEY);
			if (StringUtils.isEmpty(tranxId)) {
				tranxId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRANX_ID_XKEY));
			}

			if (!StringUtils.isEmpty(tranxId)) {
				AppContextUtil.setTranxId(tranxId);
			}

			// User Id Tracking
			String actorId = req.getHeader(AppConstants.ACTOR_ID_XKEY);
			if (StringUtils.isEmpty(actorId)) {
				actorId = ArgUtil.parseAsString(req.getParameter(AppConstants.ACTOR_ID_XKEY));
			}

			if (!StringUtils.isEmpty(actorId)) {
				AppContextUtil.setActorId(actorId);
			}

			// UserClient Tracking
			String userClientJson = req.getHeader(AppConstants.USER_CLIENT_XKEY);
			if (!StringUtils.isEmpty(userClientJson)) {
				AppContextUtil.setUserClient(JsonUtil.fromJson(userClientJson, UserDeviceClient.class));
			} else {
				UserDevice userDevice = commonHttpRequest.instance(req, resp, appConfig).getUserDevice();
				UserDeviceClient userClient = AppContextUtil.getUserClient();
				userClient.setDeviceType(userDevice.getType());
				userClient.setAppType(userDevice.getAppType());
				userClient.setIp(userDevice.getIp());
				userClient.setFingerprint(userDevice.getFingerprint());
				AppContextUtil.setUserClient(userClient);
			}

			String requestParamsJson = req.getHeader(AppConstants.REQUEST_PARAMS_XKEY);
			if (!ArgUtil.isEmpty(requestParamsJson)) {
				AppContextUtil.setParams(requestParamsJson, null);
			} else {
				requestParamsJson = req.getParameter(AppConstants.REQUEST_PARAMS_XKEY);
				if (!ArgUtil.isEmpty(requestParamsJson)) {
					AppContextUtil.setParams(requestParamsJson, null);
				} else {
					AppContextUtil.setParams(requestParamsJson, req.getParameter(AppConstants.REQUESTD_PARAMS_XKEY));
				}
			}

			if (appContextInFilter != null) {
				appContextInFilter.appRequestContextInFilter();
			}

			// Trace Id Tracking
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
					sessionID = ArgUtil.parseAsString(
							session.getAttribute(AppConstants.SESSION_ID_XKEY),
							UniqueID.generateString());
				}

				AppContextUtil.setSessionId(sessionID);
				traceId = AppContextUtil.getTraceId();
				AppContextUtil.init();

				if (session != null) {
					req.getSession().setAttribute(AppConstants.SESSION_ID_XKEY, sessionID);
					req.getSession().setAttribute(TenantContextHolder.TENANT, tnt);
				}
			} else {
				AppContextUtil.setTranceId(traceId);
				AppContextUtil.init();
			}

			// Actual Request Handling
			AppContextUtil.setTraceTime(startTime);
			if (reqType.isTrack()) {
				AuditServiceClient.trackStatic(new RequestTrackEvent(req));
				AppRequestUtil.printIfDebug(req);
			}
			try {
				if (isRequestValid(reqType, req, resp, traceId)) {
					chain.doFilter(request, new AppResponseWrapper(resp));
				} else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			} finally {
				if (reqType.isTrack()) {
					AuditServiceClient
							.trackStatic(new RequestTrackEvent(resp, req, System.currentTimeMillis() - startTime));
					AppRequestUtil.printIfDebug(resp);
				}
			}

		} finally {
			// Tear down MDC data:
			// ( Important! Cleans up the ThreadLocal data again )
			AppContextUtil.clear();
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Filter Destroyed");
	}

}
