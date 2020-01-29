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
import com.amx.jax.VendorAuthConfig;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.ApiRequestDetail;
import com.amx.jax.http.RequestType;
import com.amx.jax.logger.client.AuditServiceClient;
import com.amx.jax.logger.events.RequestTrackEvent;
import com.amx.jax.model.MapModel;
import com.amx.jax.rest.AppRequestContextInFilter;
import com.amx.jax.rest.AppRequestInterfaces.VendorAuthFilter;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.scope.VendorProperties;
import com.amx.jax.session.SessionContextService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
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

	@Autowired(required = false)
	SessionContextService sessionContextService;

	@Autowired(required = false)
	VendorAuthConfig appVendorConfig;

	@Autowired(required = false)
	VendorAuthFilter vaendorAuthFilter;

	private boolean doesTokenMatch(CommonHttpRequest localCommonHttpRequest, HttpServletRequest req,
			HttpServletResponse resp, String traceId, boolean checkHMAC) {
		String authToken = localCommonHttpRequest.get(AppConstants.AUTH_TOKEN_XKEY);
		if (checkHMAC) {
			if (StringUtils.isEmpty(authToken)
					|| (CryptoUtil.validateHMAC(appConfig.getAppAuthKey(), traceId, authToken) == false)) {
				return false;
			}
			return true;
		} else {
			if (StringUtils.isEmpty(authToken) || !authToken.equalsIgnoreCase(appConfig.getAppAuthToken())) {
				return false;
			}
			return true;
		}
	}

	private boolean isRequestValid(CommonHttpRequest localCommonHttpRequest, ApiRequestDetail apiRequest,
			HttpServletRequest req, HttpServletResponse resp, String traceId) {
		String authVendor = localCommonHttpRequest.get(AppConstants.AUTH_ID_XKEY);

		if (ArgUtil.is(authVendor)) {
			AppContextUtil.setVendor(VendorProperties.class, authVendor);
			AppContextUtil.setVendor(VendorAuthConfig.class, authVendor);
			String authToken = localCommonHttpRequest.get(AppConstants.AUTH_TOKEN_XKEY);
			if (ArgUtil.is(authToken)) {
				if (vaendorAuthFilter != null) {
					return vaendorAuthFilter.isAuthVendorRequest(apiRequest, localCommonHttpRequest, traceId,
							authToken);
				} else {
					return appVendorConfig.isRequestValid(apiRequest, localCommonHttpRequest, traceId, authToken);
				}
			}
			return false;
		}

		if (apiRequest.isUseAuthKey() && appConfig.isAppAuthEnabled()
				&& !doesTokenMatch(localCommonHttpRequest, req, resp, traceId, true)) {
			return false;
		} else if (!appConfig.isAppAuthEnabled() && apiRequest.isUseAuthToken()
				&& !doesTokenMatch(localCommonHttpRequest, req, resp, traceId, false)) {
			return false;
		} else {
			return true;
		}
	}

	public void setFlow(HttpServletRequest req, ApiRequestDetail apiRequest) {
		String url = ArgUtil.ifNotEmpty(apiRequest.getFlow(), req.getRequestURI());
		AppContextUtil.setFlow(url);
		AppContextUtil.setFlowfix(url.toLowerCase().replace("pub", "b").replace("api", "p").replace("user", "")
				.replace("get", "").replace("post", "").replace("save", "").replaceAll("[AaEeIiOoUuYyWwHh]", ""));
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		long startTime = System.currentTimeMillis();
		HttpServletRequest req = ((HttpServletRequest) request);
		HttpServletResponse resp = ((HttpServletResponse) response);
		try {
			ApiRequestDetail apiRequest = commonHttpRequest.getApiRequest(req);
			CommonHttpRequest localCommonHttpRequest = commonHttpRequest.instance(req, resp, appConfig);
			RequestType reqType = apiRequest.getType();

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

			// ***** SESSION ID Tracking ********
			String sessionId = ArgUtil.parseAsString(req.getParameter(AppConstants.SESSION_ID_XKEY));
			if (StringUtils.isEmpty(sessionId)) {
				sessionId = req.getHeader(AppConstants.SESSION_ID_XKEY);
			}
			if (!StringUtils.isEmpty(sessionId)) {
				AppContextUtil.setSessionId(sessionId);
			}

			// Tranx Id Tracking
			String tranxId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRANX_ID_XKEY));
			if (StringUtils.isEmpty(tranxId)) {
				tranxId = req.getHeader(AppConstants.TRANX_ID_XKEY);
			}

			if (!StringUtils.isEmpty(tranxId)) {
				AppContextUtil.setTranxId(tranxId);
			}

			// User Id Tracking
			String actorId = ArgUtil.parseAsString(req.getParameter(AppConstants.ACTOR_ID_XKEY));
			if (StringUtils.isEmpty(actorId)) {
				actorId = req.getHeader(AppConstants.ACTOR_ID_XKEY);
			}

			if (!StringUtils.isEmpty(actorId)) {
				AppContextUtil.setActorId(actorId);
			}

			// User Language Tracking
			Language lang = localCommonHttpRequest.getLanguage();

			if (!StringUtils.isEmpty(lang)) {
				AppContextUtil.setLang(lang);
			}

			String fp = Constants.BLANK;
			// UserClient Tracking
			String userClientJson = req.getHeader(AppConstants.USER_CLIENT_XKEY);
			if (!StringUtils.isEmpty(userClientJson)) {
				UserDeviceClient x = JsonUtil.fromJson(userClientJson, UserDeviceClient.class);
				AppContextUtil.setUserClient(x);
				fp = x.getFingerprint();
			} else {
				UserDeviceClient userDevice = localCommonHttpRequest.getUserDevice().toUserDeviceClient();
				UserDeviceClient userClient = AppContextUtil.getUserClient();
				userClient.importFrom(userDevice);
				fp = userClient.getFingerprint();
				AppContextUtil.setUserClient(userClient);
			}

			// Session Actor Tracking
			if (!ArgUtil.isEmpty(sessionId) && !ArgUtil.isEmpty(sessionContextService)) {
				String actorInfoJson = req.getHeader(AppConstants.ACTOR_INFO_XKEY);
				if (!StringUtils.isEmpty(actorInfoJson)) {
					sessionContextService.setContext(new MapModel(actorInfoJson));
				}
			}

			String requestdParamsJson = ArgUtil.ifNotEmpty(req.getParameter(AppConstants.REQUESTD_PARAMS_XKEY),
					req.getHeader(AppConstants.REQUESTD_PARAMS_XKEY));
			if (!ArgUtil.isEmpty(requestdParamsJson)) {
				AppContextUtil.setParams(null, requestdParamsJson);
			} else {
				AppContextUtil.setParams(ArgUtil.ifNotEmpty(req.getParameter(AppConstants.REQUEST_PARAMS_XKEY),
						req.getHeader(AppConstants.REQUEST_PARAMS_XKEY)), requestdParamsJson);
			}

			if (appContextInFilter != null) {
				appContextInFilter.appRequestContextInFilter(localCommonHttpRequest);
			}

			// Trace Id Tracking
			String traceId = req.getHeader(AppConstants.TRACE_ID_XKEY);
			if (StringUtils.isEmpty(traceId)) {
				traceId = ArgUtil.parseAsString(req.getParameter(AppConstants.TRACE_ID_XKEY));
			}
			if (StringUtils.isEmpty(traceId)) {
				setFlow(req, apiRequest);
				HttpSession session = req.getSession(appConfig.isAppSessionEnabled());
				if (ArgUtil.isEmpty(sessionId)) {
					if (ArgUtil.isEmpty(fp)) {
						fp = localCommonHttpRequest.getRequestParam(AppConstants.DEVICE_XID_KEY);
						if (ArgUtil.isEmpty(fp)) {
							fp = UniqueID.generateString62();
							localCommonHttpRequest.setCookie(AppConstants.DEVICE_XID_KEY, fp);
						}
					}
					AppContextUtil.setSessionPrefix(fp);
					AppContextUtil.setRequestUser(localCommonHttpRequest.getTraceUserIdentifier());

					if (session == null) {
						sessionId = AppContextUtil.getSessionId(true);
					} else {
						sessionId = AppContextUtil.getSessionId(
								ArgUtil.parseAsString(session.getAttribute(AppConstants.SESSION_ID_XKEY)));
					}
				}

				AppContextUtil.setSessionId(sessionId);
				traceId = AppContextUtil.getTraceId();
				AppContextUtil.init();

				if (session != null) {
					req.getSession().setAttribute(AppConstants.SESSION_ID_XKEY, sessionId);
					req.getSession().setAttribute(TenantContextHolder.TENANT, tnt);
				}
			} else {
				AppContextUtil.setTranceId(traceId);
				AppContextUtil.init();
			}

			// Actual Request Handling
			AppContextUtil.setTraceTime(startTime);
			if (reqType.isTrack() || AuditServiceClient.isDebugEnabled()) {
				AuditServiceClient.trackStatic(new RequestTrackEvent(req).debug(reqType.isDebugOnly()));
				AppRequestUtil.printIfDebug(req);
			}
			try {
				if (isRequestValid(localCommonHttpRequest, apiRequest, req, resp, traceId)) {
					chain.doFilter(request, new AppResponseWrapper(resp));
				} else {
					resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
				}
			} finally {
				if (reqType.isTrack() || AuditServiceClient.isDebugEnabled()) {
					AuditServiceClient
							.trackStatic(new RequestTrackEvent(resp, req, System.currentTimeMillis() - startTime)
									.debug(reqType.isDebugOnly()));
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
