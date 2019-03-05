package com.amx.jax.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.WebUtils;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.UserClient;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.dict.UserClient.DevicePlatform;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.UserDevice;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.HttpUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Component
public class CommonHttpRequest {

	public static class CommonMediaType extends MediaType {

		private static final long serialVersionUID = -540693758487754320L;

		/**
		 * Public constant media type for {@code application/v0+json}.
		 * 
		 * @see #APPLICATION_JSON_UTF8
		 */
		public final static MediaType APPLICATION_V0_JSON;

		/**
		 * A String equivalent of {@link MediaType#APPLICATION_V0_JSON}.
		 * 
		 * @see #APPLICATION_JSON_UTF8_VALUE
		 */
		public final static String APPLICATION_V0_JSON_VALUE = "application/v0+json";

		public CommonMediaType(String type, String subtype) {
			super(type, subtype);
		}

		public final static String[] APPLICATION_JSON_VALUES = { MediaType.APPLICATION_JSON_VALUE,
				APPLICATION_V0_JSON_VALUE };

		static {
			APPLICATION_V0_JSON = valueOf(APPLICATION_JSON_VALUE);
		}

	}

	private static Logger LOGGER = LoggerService.getLogger(CommonHttpRequest.class);

	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;

	@Autowired
	private AppConfig appConfig;

	private static Map<String, ApiRequest> API_REQUEST_MAP = Collections
			.synchronizedMap(new HashMap<String, ApiRequest>());
	private static boolean IS_API_REQUEST_MAPPED = false;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	CommonHttpRequest init(HttpServletRequest request, HttpServletResponse response, AppConfig appConfig) {
		this.request = request;
		this.response = response;
		this.appConfig = appConfig;
		return this;
	}

	public CommonHttpRequest instance(HttpServletRequest request, HttpServletResponse response, AppConfig appConfig) {
		CommonHttpRequest commonHttpRequest = new CommonHttpRequest();
		return commonHttpRequest.init(request, response, appConfig);
	}

	public String getIPAddress() {
		String deviceIp = null;
		if (appConfig.isSwaggerEnabled()) {
			deviceIp = request.getHeader(AppConstants.DEVICE_IP_XKEY);
			if (!ArgUtil.isEmpty(deviceIp)) {
				return deviceIp;
			}
		}
		return HttpUtils.getIPAddress(request);
	}

	public Language getLanguage() {
		return (Language) ArgUtil.parseAsEnum(request.getLocale().getLanguage(), Language.DEFAULT);
	}

	public Device getCurrentDevice() {
		return DeviceUtils.getCurrentDevice(request);
	}

	public String getDeviceId() {
		String deviceId = null;
		if (request != null) {
			deviceId = request.getHeader(AppConstants.DEVICE_ID_XKEY);
			if (ArgUtil.isEmpty(deviceId)) {
				Cookie cookie = WebUtils.getCookie(request, AppConstants.DEVICE_ID_KEY);
				if (cookie != null) {
					deviceId = cookie.getValue();
				}
			}
		}
		return deviceId;
	}

	public String get(String contextKey) {
		String value = AppContextUtil.get(contextKey);
		if (request != null) {
			value = request.getParameter(contextKey);
			if (ArgUtil.isEmpty(value)) {
				value = request.getHeader(contextKey);
				if (ArgUtil.isEmpty(value)) {
					Cookie cookie = WebUtils.getCookie(request, contextKey);
					if (cookie != null) {
						value = cookie.getValue();
					}
				}
			}
			AppContextUtil.set(contextKey, value);
		}
		return value;
	}

	public void clearSessionCookie() {
		Cookie cookie = WebUtils.getCookie(request, AppConstants.SESSIONID);
		if (cookie != null) {
			cookie.setMaxAge(0);
		}
	}

	public void setCookie(Cookie kooky) {
		if (response != null) {
			response.addCookie(kooky);
		}
	}

	public Cookie getCookie(String name) {
		return WebUtils.getCookie(request, name);
	}

	public String setBrowserId(String browserIdNew) {
		String browserId = null;
		if (request != null) {
			// Cookie cookie = WebUtils.getCookie(request, AppConstants.BROWSER_ID_KEY);
			// if (cookie != null) {
			// browserId = cookie.getValue();
			// } else
			if (response != null && browserIdNew != null) {
				browserId = browserIdNew;
				Cookie kooky = new Cookie(AppConstants.BROWSER_ID_KEY, browserIdNew);
				kooky.setMaxAge(31622400);
				kooky.setHttpOnly(appConfig.isCookieHttpOnly());
				kooky.setSecure(appConfig.isCookieSecure());
				kooky.setPath("/");
				response.addCookie(kooky);
			}
		}
		return browserId;
	}

	public UserAgent getUserAgent() {
		UserAgent agent = new UserAgent(OperatingSystem.UNKNOWN, Browser.UNKNOWN);
		if (request != null) {
			String browserDetails = request.getHeader("User-Agent");
			return UserAgent.parseUserAgentString(browserDetails);
		}
		return agent;
	}

	public UserDevice getUserDevice() {
		Object userDeviceObj = AppContextUtil.get(AppConstants.USER_DEVICE_XKEY);
		if (!ArgUtil.isEmpty(userDeviceObj)) {
			return (UserDevice) userDeviceObj;
		}

		UserDevice userDevice = new UserDevice();

		userDevice.setIp(this.getIPAddress());

		Device currentDevice = this.getCurrentDevice();
		UserAgent userAgent = this.getUserAgent();

		boolean isMobile = false;
		boolean isTablet = false;
		boolean isAndroid = false;
		boolean isIOS = false;
		if (currentDevice != null) {
			isMobile = currentDevice.isMobile();
			isTablet = currentDevice.isTablet();
			isAndroid = (currentDevice.getDevicePlatform() == org.springframework.mobile.device.DevicePlatform.ANDROID);
			isIOS = (currentDevice.getDevicePlatform() == org.springframework.mobile.device.DevicePlatform.IOS);
		} else {
			isMobile = userAgent.getOperatingSystem().getDeviceType()
					.equals(eu.bitwalker.useragentutils.DeviceType.MOBILE);
			isTablet = userAgent.getOperatingSystem().getDeviceType()
					.equals(eu.bitwalker.useragentutils.DeviceType.TABLET);
			isAndroid = userAgent.getOperatingSystem().getGroup()
					.equals(eu.bitwalker.useragentutils.OperatingSystem.ANDROID);
			isIOS = userAgent.getOperatingSystem().getGroup().equals(eu.bitwalker.useragentutils.OperatingSystem.IOS);
		}

		userDevice.setType(
				(isMobile ? UserClient.DeviceType.MOBILE
						: (isTablet ? UserClient.DeviceType.TABLET
								: UserClient.DeviceType.COMPUTER)));

		DevicePlatform devicePlatform = DevicePlatform.UNKNOWN;
		if (isAndroid
				|| userAgent.getOperatingSystem().getGroup() == OperatingSystem.ANDROID) {
			devicePlatform = DevicePlatform.ANDROID;
		} else if (isIOS
				|| userAgent.getOperatingSystem().getGroup() == OperatingSystem.IOS) {
			devicePlatform = DevicePlatform.IOS;
		} else if (isIOS
				|| userAgent.getOperatingSystem().getGroup() == OperatingSystem.MAC_OS
				|| userAgent.getOperatingSystem().getGroup() == OperatingSystem.MAC_OS_X) {
			devicePlatform = DevicePlatform.MAC;
		} else if (userAgent.getOperatingSystem().getGroup() == OperatingSystem.WINDOWS) {
			devicePlatform = DevicePlatform.WINDOWS;
		}
		userDevice.setPlatform(devicePlatform);

		UserClient.DeviceType deviceType = UserClient.DeviceType.UNKNOWN;
		if (isMobile || userAgent.getOperatingSystem()
				.getDeviceType() == eu.bitwalker.useragentutils.DeviceType.MOBILE) {
			deviceType = UserClient.DeviceType.MOBILE;
		} else if (isTablet || userAgent.getOperatingSystem()
				.getDeviceType() == eu.bitwalker.useragentutils.DeviceType.TABLET) {
			deviceType = UserClient.DeviceType.TABLET;
			if ((devicePlatform == DevicePlatform.MAC || devicePlatform == DevicePlatform.IOS)) {
				deviceType = UserClient.DeviceType.IPAD;
			}
		} else if (userAgent.getOperatingSystem()
				.getDeviceType() == eu.bitwalker.useragentutils.DeviceType.COMPUTER) {
			deviceType = UserClient.DeviceType.COMPUTER;
		}
		userDevice.setType(deviceType);

		userDevice.setFingerprint(this.getDeviceId());

		userDevice.setId(ArgUtil.parseAsString(userAgent.getId()));

		userDevice.setUserAgent(userAgent);
		AppType appType = userDevice.getAppType();

		if (appType == null) {
			appType = null;
			if(userDevice.getUserAgent().getBrowser() != Browser.UNKNOWN) {
				appType = AppType.WEB;
			} else if (userDevice.getPlatform() == DevicePlatform.ANDROID
					&& userDevice.getUserAgent().getBrowser() == Browser.UNKNOWN) {
				appType = AppType.ANDROID;
			} else if (userDevice.getPlatform() == DevicePlatform.IOS
					&& userDevice.getUserAgent().getBrowser() == Browser.UNKNOWN) {
				appType = AppType.IOS;
			} else if (userDevice.getPlatform() == DevicePlatform.UNKNOWN
					&& userDevice.getUserAgent().getBrowser() == Browser.UNKNOWN
					&& userDevice.getUserAgent().getOperatingSystem() == OperatingSystem.UNKNOWN) {
				appType = AppType.IOS;
			} else if (userDevice.getFingerprint() != null
					&& !Constants.BLANK.equalsIgnoreCase(userDevice.getFingerprint())) {
				if (userDevice.getFingerprint().length() == 16) {
					appType = AppType.ANDROID;
				} else if (userDevice.getFingerprint().length() == 40) {
					appType = AppType.IOS;
				} else if (userDevice.getFingerprint().length() == 32) {
					appType = AppType.WEB;
				}
			} else if (userDevice.getType() == DeviceType.COMPUTER) {
				appType = AppType.WEB;
			}
		}
		userDevice.setAppType(appType);
		AppContextUtil.set(AppConstants.USER_DEVICE_XKEY, userDevice);
		return userDevice;
	}

	private ApiRequest getApiRequestModel(HttpServletRequest req) {
		createApiRequestModels();
		HandlerExecutionChain handlerExeChain;
		try {
			handlerExeChain = requestMappingHandlerMapping.getHandler(req);
			HandlerMethod handlerMethod = null;

			if (!ArgUtil.isEmpty(handlerExeChain)) {
				handlerMethod = (HandlerMethod) handlerExeChain.getHandler();
				if (!ArgUtil.isEmpty(handlerMethod)) {
					String handlerKey = handlerMethod.getShortLogMessage() + "#" +
							ArgUtil.parseAsString(handlerMethod.hashCode());
					handlerKey = handlerMethod.getMethod().toGenericString();
					return API_REQUEST_MAP.get(handlerKey);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean createApiRequestModels() {
		if (IS_API_REQUEST_MAPPED) {
			return true;
		}
		try {
			Set<Entry<RequestMappingInfo, HandlerMethod>> x = requestMappingHandlerMapping.getHandlerMethods()
					.entrySet();
			for (Entry<RequestMappingInfo, HandlerMethod> requestMappingInfo : x) {
				HandlerMethod handlerMethod = requestMappingInfo.getValue();
				ApiRequest apiRequest = handlerMethod.getMethodAnnotation(ApiRequest.class);
				if (apiRequest == null) {
					apiRequest = handlerMethod.getBeanType().getAnnotation(ApiRequest.class);
				}
				if (apiRequest != null) {
					String handlerKey = handlerMethod.getShortLogMessage() + "#" +
							ArgUtil.parseAsString(handlerMethod.hashCode());
					handlerKey = handlerMethod.getMethod().toGenericString();
					API_REQUEST_MAP.put(
							handlerKey,
							apiRequest);
				}

			}
			IS_API_REQUEST_MAPPED = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public RequestType getApiRequestType(HttpServletRequest req) {
		RequestType reqType = RequestType.from(req);
		if (reqType == RequestType.DEFAULT) {
			ApiRequest x = getApiRequestModel(req);
			if (x != null) {
				return x.type();
			}
		}
		return reqType;
	}
}
