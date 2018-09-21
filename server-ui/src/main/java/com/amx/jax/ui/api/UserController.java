
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.FBPushClient;
import com.amx.jax.service.HttpService;
import com.amx.jax.ui.WebAppConfig;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.HotPointService;
import com.amx.jax.ui.service.HotPointService.HotPoints;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantService;
import com.amx.jax.ui.service.UserService;
import com.amx.jax.ui.session.UserDeviceBean;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class UserController.
 */
@RestController
@Api(value = "User APIs")
public class UserController {

	/** The login service. */
	@Autowired
	private LoginService loginService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The user service. */
	@Autowired
	private UserService userService;

	/** The jax service. */
	@Autowired
	private JaxService jaxService;

	/** The tenant context. */
	@Autowired
	private TenantService tenantContext;

	/** The http service. */
	@Autowired
	private HttpService httpService;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The fb push client. */
	@Autowired
	private FBPushClient fbPushClient;

	/** The hot point service. */
	@Autowired
	private HotPointService hotPointService;

	/**
	 * Gets the meta.
	 *
	 * @param appType
	 *            the app type
	 * @param appVersion
	 *            the app version
	 * @return the meta
	 */
	@Timed
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<UserMetaData> getMeta(@RequestParam(required = false) UserDeviceBean.AppType appType,
			@RequestParam(required = false) String appVersion) {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		if (appType != null) {
			sessionService.getAppDevice().setAppType(appType);
		}

		if (appVersion != null) {
			sessionService.getAppDevice().setAppVersion(appVersion);
		}

		wrapper.getData().setTenant(AppContextUtil.getTenant());
		wrapper.getData().setTenantCode(AppContextUtil.getTenant().getCode());
		wrapper.getData().setLang(httpService.getLanguage());
		wrapper.getData().setCdnUrl(appConfig.getCdnURL());
		wrapper.getData().setFeatures(webAppConfig.getFeatures());

		wrapper.getData().setDevice(sessionService.getAppDevice().toUserDevice());
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		wrapper.getData().setValidSession(sessionService.getUserSession().isValid());

		if (sessionService.getUserSession().getCustomerModel() != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setCustomerId(sessionService.getUserSession().getCustomerModel().getCustomerId());
			wrapper.getData().setInfo(sessionService.getUserSession().getCustomerModel().getPersoninfo());
			wrapper.getData().setDomCurrency(tenantContext.getDomCurrency());
			wrapper.getData().setConfig(jaxService.setDefaults().getMetaClient().getJaxMetaParameter().getResult());
			wrapper.getData().getSubscriptions().addAll(userService.getNotifyTopics("/topics/"));
			wrapper.getData().setReturnUrl(sessionService.getGuestSession().getReturnUrl());
		}

		wrapper.getData().setNotifyRangeShort(webAppConfig.getNotifyRangeShort());
		wrapper.getData().setNotifyRangeLong(webAppConfig.getNotifyRangeLong());
		wrapper.getData().setNotificationGap(webAppConfig.getNotificationGap());

		return wrapper;
	}

	/**
	 * Me notify.
	 *
	 * @param token
	 *            the token
	 * @param hotpoint
	 *            the hotpoint
	 * @param customerId
	 *            the customer id
	 * @return the response wrapper
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = "/pub/user/notify/hotpoint", method = { RequestMethod.POST })
	public ResponseWrapper<Object> meNotify(@RequestParam(required = false) String token,
			@RequestParam(required = false) HotPoints hotpoint, @RequestParam BigDecimal customerId)
			throws PostManException {
		AppContextUtil.setActorId(new AuditActor(AuditActor.ActorType.GUEST, customerId));
		return new ResponseWrapper<Object>(hotPointService.notify(customerId, token, hotpoint));
	}

	@Autowired
	JaxPushNotificationClient notificationClient;

	@RequestMapping(value = "/pub/user/notifications", method = { RequestMethod.GET })
	public ResponseWrapper<List<CustomerNotificationDTO>> getNotifications(@RequestParam BigDecimal customerId) {
		AppContextUtil.setActorId(new AuditActor(AuditActor.ActorType.GUEST, customerId));
		return new ResponseWrapper<List<CustomerNotificationDTO>>(notificationClient.get(customerId).getResults());
	}

	/**
	 * Register notify.
	 *
	 * @param token
	 *            the token
	 * @return the response wrapper
	 * @throws PostManException
	 *             the post man exception
	 */
	@RequestMapping(value = "/api/user/notify/register", method = { RequestMethod.POST })
	public ResponseWrapper<Object> registerNotify(@RequestParam String token) throws PostManException {
		for (String topic : userService.getNotifyTopics("")) {
			fbPushClient.subscribe(token, topic + "_web");
		}
		return new ResponseWrapper<Object>();
	}

	/**
	 * Unregister notify.
	 *
	 * @param token
	 *            the token
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/notify/unregister", method = { RequestMethod.POST })
	public ResponseWrapper<Object> unregisterNotify(@RequestParam String token) {
		return new ResponseWrapper<Object>();
	}

	/**
	 * Profile.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/profile", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerDto> profile() {
		return userService.getProfileDetails();
	}

	/**
	 * Change password.
	 *
	 * @param oldPassword
	 *            the old password
	 * @param password
	 *            the password
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOperation(value = "Old API to update password with Form")
	@RequestMapping(value = "/api/user/password", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> changePassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatepwd(password, mOtp, eOtp);
	}

	/**
	 * Change password JSON.
	 *
	 * @param userUpdateRequest
	 *            the user update request
	 * @return the response wrapper
	 */
	@ApiOperation(value = "new API to update password with Object")
	@RequestMapping(value = "/api/user/password/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateResponse> changePasswordJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updatepwd(userUpdateRequest.getPassword(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	/**
	 * Update email.
	 *
	 * @param email
	 *            the email
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/api/user/email", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> updateEmail(@RequestParam String email,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updateEmail(email, mOtp, eOtp);
	}

	/**
	 * Update email JSON.
	 *
	 * @param userUpdateRequest
	 *            the user update request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/email/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateResponse> updateEmailJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updateEmail(userUpdateRequest.getEmail(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	/**
	 * Update phone.
	 *
	 * @param phone
	 *            the phone
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/api/user/phone", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> updatePhone(@RequestParam String phone,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatePhone(phone, mOtp, eOtp);
	}

	/**
	 * Update phone JSON.
	 *
	 * @param userUpdateRequest
	 *            the user update request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/phone/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateResponse> updatePhoneJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updatePhone(userUpdateRequest.getPhone(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	/**
	 * Reg sec ques.
	 *
	 * @param userUpdateData
	 *            the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateResponse> regSecQues(@RequestBody UserUpdateRequest userUpdateData) {
		return userService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * Update phising.
	 *
	 * @param userUpdateData
	 *            the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/phising", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updatePhising(@RequestBody UserUpdateRequest userUpdateData) {
		return userService.updatePhising(userUpdateData.getImageUrl(), userUpdateData.getCaption(),
				userUpdateData.getmOtp(), userUpdateData.geteOtp());
	}

	/**
	 * Send OTP.
	 *
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/otpsend", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> sendOTP(@RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String eOtp) {
		if (mOtp == null) {
			return loginService.sendOTP(null, null);
		} else {
			return loginService.verifyResetPassword(null, mOtp, null);
		}

	}
}
