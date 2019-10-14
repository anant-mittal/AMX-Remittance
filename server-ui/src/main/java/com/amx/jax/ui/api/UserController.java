package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.AnnualIncomeRangeDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.IncomeDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.CustomerProfileClient;
import com.amx.jax.client.JaxPushNotificationClient;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.UserClient.AppType;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditActor;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.model.response.customer.CustomerFlags;
import com.amx.jax.model.response.customer.CustomerModelSignupResponse;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.ui.UIConstants.Features;
import com.amx.jax.ui.UIConstants.MileStone;
import com.amx.jax.ui.WebAppConfig;
import com.amx.jax.ui.auth.AuthLibContext;
import com.amx.jax.ui.config.OWAStatus.ApiOWAStatus;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequestFingerprint;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.ResponseWrapperM;
import com.amx.jax.ui.service.GeoHotPoints;
import com.amx.jax.ui.service.HotPointService;
import com.amx.jax.ui.service.JaxService;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantService;
import com.amx.jax.ui.service.UserService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class UserController.
 */
@RestController
@Api(value = "User APIs")
public class UserController {

	private static Logger LOGGER = LoggerService.getLogger(UserController.class);

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
	private CommonHttpRequest httpService;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/** The web app config. */
	@Autowired
	private WebAppConfig webAppConfig;

	/** The fb push client. */
	@Autowired
	private PushNotifyClient pushNotifyClient;

	/** The hot point service. */
	@Autowired
	private HotPointService hotPointService;

	@Autowired
	AuthLibContext authLibContext;

	@Autowired
	private CustomerProfileClient customerProfileClient;

	@Autowired
	AuditService auditService;

	@ApiOWAStatus(OWAStatusStatusCodes.INCOME_UPDATE_REQUIRED)
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<UserMetaData> getMeta(HttpServletResponse response,
			@RequestParam(required = false) AppType appType,
			@RequestParam(required = false) String appVersion, @RequestParam(required = false) String milestone,
			@RequestParam(required = false) Language lang) {
		return this.getMetaV2(response, appType, appVersion, milestone, lang, false, false);
	}

	/**
	 * Gets the meta.
	 *
	 * @param appType    the app type
	 * @param appVersion the app version
	 * @return the meta
	 */
	@ApiOWAStatus(OWAStatusStatusCodes.INCOME_UPDATE_REQUIRED)
	@RequestMapping(value = "/pub/v2/user/meta", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<UserMetaData> getMetaV2(HttpServletResponse response,
			@RequestParam(required = false) AppType appType,
			@RequestParam(required = false) String appVersion,
			@RequestParam(required = false) String milestone,
			@RequestParam(required = false) Language lang,
			@RequestParam(required = false, defaultValue = "false") boolean refresh,
			@RequestParam(required = false, defaultValue = "false") boolean validate) {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		sessionService.getAppDevice().resolve();
		if (appType != null) {
			sessionService.getAppDevice().getUserDevice().setAppType(appType);
		}

		if (appVersion != null) {
			sessionService.getAppDevice().getUserDevice().setAppVersion(appVersion);
		}

		Cookie kooky = httpService.getCookie("S");

		if (ArgUtil.isEmpty(kooky) || ArgUtil.isEmpty(kooky.getValue())) {
			String serverVersion = null;

			MileStone milestoneEnum = (MileStone) ArgUtil.parseAsEnum(milestone, MileStone.ZERO, MileStone.FUTRUE);

			if (milestoneEnum == MileStone.ZERO || milestoneEnum.isLegacy()) {
				serverVersion = "O";
			} else {
				serverVersion = "N";
			}

			httpService.setCookie("S", serverVersion, 60 * 60 * 2);

			String s = httpService.getRequestParam("S");
			if (ArgUtil.isEmpty(s)) {
				response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/pub/v2/user/meta?S=" + serverVersion + "&milestone=" + milestoneEnum);
				return wrapper;
			}
		}

		lang = httpService.getLanguage();
		Language sessionLang = sessionService.getGuestSession().getLanguage();
		boolean isLangChange = ArgUtil.is(lang) && !lang.equals(sessionLang);

		wrapper.getData().setTenant(AppContextUtil.getTenant());
		wrapper.getData().setTenantCode(AppContextUtil.getTenant().getCode());
		wrapper.getData().setCdnUrl(appConfig.getCdnURL());

		wrapper.getData().setDevice(sessionService.getAppDevice().getUserDevice().toSanitized());
		wrapper.getData().setState(sessionService.getGuestSession().getState());
		wrapper.getData().setValidSession(sessionService.getUserSession().isValid());

		CustomerModel customer = sessionService.getUserSession().getCustomerModel();

		if (customer != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setCustomerId(sessionService.getUserSession().getCustomerModel().getCustomerId());
			wrapper.getData().setInfo(sessionService.getUserSession().getCustomerModel().getPersoninfo());

			Language profileLang = customer.getPersoninfo().getLang();

			if (false
					/**
					 * This is language Change request after Login
					 */
					|| isLangChange
					/**
					 * profile language is empty
					 */
					|| (ArgUtil.isEmpty(profileLang) && !Language.EN.equals(lang))
					/**
					 * Client language is NON-English and different than profile Language
					 */
					|| (!Language.EN.equals(lang) && !lang.equals(profileLang))

			) {
				customerProfileClient.saveLanguage(customer.getCustomerId(), lang.getBDCode());
				refresh = true;
			} else {
				lang = profileLang;
			}

			if (refresh) {
				userService.updateCustoemrModel();
			}
			CustomerFlags customerFlags = sessionService.getUserSession().getCustomerModel().getFlags();

			if (validate) {
				customerFlags = authLibContext.get().checkUserMeta(sessionService.getGuestSession().getState(),
						customerFlags);
			}

			wrapper.getData().setFlags(customerFlags);

			if (!ArgUtil.isEmpty(customerFlags) && customerFlags.getAnnualIncomeExpired()) {
				wrapper.setStatusEnum(OWAStatusStatusCodes.INCOME_UPDATE_REQUIRED);
			}

			wrapper.getData().setDomCurrency(tenantContext.getDomCurrency());
			wrapper.getData().setConfig(jaxService.setDefaults().getMetaClient().getJaxMetaParameter().getResult());
			wrapper.getData().getSubscriptions().addAll(userService.getNotifyTopics("/topics/"));
			wrapper.getData().setReturnUrl(sessionService.getGuestSession().getReturnUrl());

			wrapper.getData().setFeatures(
					authLibContext.get().filterFeatures(sessionService.getGuestSession().getState(), customerFlags,
							webAppConfig.getFeaturesList()));
		} else {
			wrapper.getData().setFeatures(webAppConfig.getFeaturesList());
		}

		/**
		 * Language Changed - Change it in session, cookie and meta
		 */
		sessionService.getGuestSession().setLanguage(lang);
		wrapper.getData().setLang(sessionService.getGuestSession().getLanguage());
		httpService.setCookie("lang", lang.toString(), 30 * 60 * 60 * 2);

		wrapper.getData().setMileStones(MileStone.LIST);
		wrapper.getData().setNotifyRangeShort(webAppConfig.getNotifyRangeShort());
		wrapper.getData().setNotifyRangeLong(webAppConfig.getNotifyRangeLong());
		wrapper.getData().setNotificationGap(webAppConfig.getNotificationGap());

		return wrapper;
	}

	/**
	 * Me notify.
	 *
	 * @param token      the token
	 * @param hotpoint   the hotpoint
	 * @param customerId the customer id
	 * @return the response wrapper
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = "/pub/user/notify/hotpoint", method = { RequestMethod.POST })
	public ResponseWrapper<Object> meNotify(@RequestParam(required = false) String token,
			@RequestParam(required = false) GeoHotPoints hotpoint, @RequestParam BigDecimal customerId,
			HttpServletRequest request) throws PostManException {
		AppContextUtil.setActorId(new AuditActor(AuditActor.ActorType.GUEST, customerId));
		if (ArgUtil.isEmpty(hotpoint)) {
			LOGGER.error("HOTPOINT:{} not defined for customer {} ", request.getParameter("hotpoint"), customerId);
		}
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
	 * @param token the token
	 * @return the response wrapper
	 * @throws PostManException the post man exception
	 */
	@RequestMapping(value = "/api/user/notify/register", method = { RequestMethod.POST })
	public ResponseWrapper<Object> registerNotify(@RequestParam String token) throws PostManException {
		for (String topic : userService.getNotifyTopics("")) {
			pushNotifyClient.subscribe(token, topic + "_web");
		}
		return new ResponseWrapper<Object>();
	}

	/**
	 * Unregister notify.
	 *
	 * @param token the token
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/notify/unregister", method = { RequestMethod.POST })
	public ResponseWrapper<Object> unregisterNotify(@RequestParam String token) {
		return new ResponseWrapper<Object>();
	}

	@RequestMapping(value = "/api/user/perms/{feature}", method = { RequestMethod.GET })
	public AmxApiResponse<CustomerFlags, Object> perms(@PathVariable Features feature) {
		return userService.checkModule(feature);
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
	 * @param oldPassword the old password
	 * @param password    the password
	 * @param mOtp        the m otp
	 * @param eOtp        the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOperation(value = "Old API to update password with Form")
	@RequestMapping(value = "/api/user/password", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> changePassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @Deprecated @RequestParam String mOtp,
			@Deprecated @RequestParam(required = false) String eOtp) {
		JaxAuthContext.mOtp(mOtp);
		JaxAuthContext.eOtp(eOtp);
		return userService.updatepwd(password, mOtp, eOtp);
	}

	/**
	 * Change password JSON.
	 *
	 * @param userUpdateRequest the user update request
	 * @return the response wrapper
	 */
	@ApiOperation(value = "new API to update password with Object")
	@RequestMapping(value = "/api/user/password/**", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> changePasswordJSON(
			@Deprecated @RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@Deprecated @RequestParam(required = false) String mOtp, @RequestBody UserUpdateRequest userUpdateRequest) {
		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();

		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(userUpdateRequest.getmOtp(), mOtp, mOtpHeader));

		// mOtp = (mOtp == null) ? (mOtpHeader == null ? userUpdateRequest.getmOtp() :
		// mOtpHeader) : mOtp;

		if (mOtp == null) {
			wrapper.setMeta(new AuthData());
			wrapper.getMeta().setmOtpPrefix(loginService.sendOTP(null, null).getData().getmOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.MOTP_REQUIRED);
		} else {
			wrapper.setData(userService.updatepwd(userUpdateRequest.getPassword(), mOtp, null));
			wrapper.setStatusEnum(OWAStatusStatusCodes.USER_UPDATE_SUCCESS);
		}
		return wrapper;
//		return userService.updatepwd(userUpdateRequest.getPassword(), userUpdateRequest.getmOtp(),
//				userUpdateRequest.geteOtp());
	}

	/**
	 * Update email.
	 *
	 * @param email the email
	 * @param mOtp  the m otp
	 * @param eOtp  the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/api/user/email", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> updateEmail(@RequestParam String email,
			@Deprecated @RequestParam(required = false) String mOtp,
			@Deprecated @RequestParam(required = false) String eOtp) {
		mOtp = JaxAuthContext.mOtp(mOtp);
		eOtp = JaxAuthContext.eOtp(eOtp);
		return userService.updateEmail(email, mOtp, eOtp);
	}

	/**
	 * Update email JSON.
	 *
	 * @param userUpdateRequest the user update request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/email/**", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> updateEmailJSON(
			@Deprecated @RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@Deprecated @RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@Deprecated @RequestParam(required = false) String mOtp,
			@Deprecated @RequestParam(required = false) String eOtp, @RequestBody UserUpdateRequest userUpdateRequest) {

		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(userUpdateRequest.getmOtp(), mOtp, mOtpHeader));
		eOtp = JaxAuthContext.eOtp(ArgUtil.ifNotEmpty(userUpdateRequest.geteOtp(), eOtp, eOtpHeader));

//		mOtp = (mOtp == null) ? (mOtpHeader == null ? userUpdateRequest.getmOtp() : mOtpHeader) : mOtp;
//		eOtp = (eOtp == null) ? (eOtpHeader == null ? userUpdateRequest.geteOtp() : eOtpHeader) : eOtp;

		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();
		if (mOtp == null && eOtp == null) {
			CivilIdOtpModel model = jaxService.setDefaults().getUserclient()
					.sendOtpForEmailUpdate(userUpdateRequest.getEmail()).getResult();
			wrapper.setMeta(new AuthData());
			wrapper.getMeta().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getMeta().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.DOTP_REQUIRED);
		} else {
			CustomerModel model = jaxService.setDefaults().getUserclient()
					.saveEmail(userUpdateRequest.getEmail(), mOtp, eOtp).getResult();
			sessionService.getUserSession().getCustomerModel().setEmail(model.getEmail());
			sessionService.getUserSession().getCustomerModel().getPersoninfo().setEmail(model.getEmail());
			wrapper.setStatusEnum(OWAStatusStatusCodes.USER_UPDATE_SUCCESS);
			userService.updateCustoemrModel();
		}
		return wrapper;
	}

	/**
	 * Update phone.
	 *
	 * @param phone the phone
	 * @param mOtp  the m otp
	 * @param eOtp  the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/api/user/phone", method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseWrapper<UserUpdateResponse> updatePhone(@RequestParam String phone,
			@Deprecated @RequestParam(required = false) String mOtp,
			@Deprecated @RequestParam(required = false) String eOtp) {
		mOtp = JaxAuthContext.mOtp(mOtp);
		eOtp = JaxAuthContext.eOtp(eOtp);
		;
		return userService.updatePhone(phone, mOtp, eOtp);
	}

	/**
	 * Update phone JSON.
	 *
	 * @param userUpdateRequest the user update request
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/phone/**", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> updatePhoneJSON(
			@Deprecated @RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@Deprecated @RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@Deprecated @RequestParam(required = false) String mOtp,
			@Deprecated @RequestParam(required = false) String eOtp, @RequestBody UserUpdateRequest userUpdateRequest) {

		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(userUpdateRequest.getmOtp(), mOtp, mOtpHeader));
		eOtp = JaxAuthContext.eOtp(ArgUtil.ifNotEmpty(userUpdateRequest.geteOtp(), eOtp, eOtpHeader));
		LOGGER.debug("OTPS M:{}, O:{}", mOtp, eOtp);

		// mOtp = (mOtp == null) ? (mOtpHeader == null ? userUpdateRequest.getmOtp() :
		// mOtpHeader) : mOtp;
		// eOtp = (eOtp == null) ? (eOtpHeader == null ? userUpdateRequest.geteOtp() :
		// eOtpHeader) : eOtp;

		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();
		if (ArgUtil.isEmpty(mOtp) && ArgUtil.isEmpty(eOtp)) {
			CivilIdOtpModel model = jaxService.setDefaults().getUserclient()
					.sendOtpForMobileUpdate(userUpdateRequest.getPhone()).getResult();
			wrapper.setMeta(new AuthData());
			wrapper.getMeta().setmOtpPrefix(model.getmOtpPrefix());
			wrapper.getMeta().seteOtpPrefix(model.geteOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.DOTP_REQUIRED);
		} else {
			CustomerModel model = jaxService.setDefaults().getUserclient()
					.saveMobile(userUpdateRequest.getPhone(), mOtp, eOtp).getResult();
			sessionService.getUserSession().getCustomerModel().setMobile(model.getMobile());
			sessionService.getUserSession().getCustomerModel().getPersoninfo().setMobile(model.getMobile());
			wrapper.setStatusEnum(OWAStatusStatusCodes.USER_UPDATE_SUCCESS);
		}
		return wrapper;
	}

	@RequestMapping(value = "/api/secques/get/v2", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return userService.getSecQues();
	}

	/**
	 * Reg sec ques.
	 *
	 * @param userUpdateData the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/secques", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> regSecQues(
			@Deprecated @RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@Deprecated @RequestParam(required = false) String mOtp, @RequestBody UserUpdateRequest userUpdateData) {
		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();
		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(userUpdateData.getmOtp(), mOtp, mOtpHeader));
//		mOtp = (mOtp == null) ? (mOtpHeader == null ? userUpdateData.getmOtp() : mOtpHeader) : mOtp;
		if (mOtp == null) {
			wrapper.setMeta(new AuthData());
			wrapper.getMeta().setmOtpPrefix(loginService.sendOTP(null, null).getData().getmOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.MOTP_REQUIRED);
		} else {
			wrapper.setData(userService.updateSecQues(userUpdateData.getSecQuesAns(), mOtp, null));
			wrapper.setStatusEnum(OWAStatusStatusCodes.USER_UPDATE_SUCCESS);
		}
		return wrapper;
	}

	/**
	 * Reg sec ques.
	 *
	 * @param userUpdateData the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/secques/v2", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> regSecQuesV2(@RequestBody UserUpdateRequest userUpdateData) {
		return userService.updateSecQues(userUpdateData.getSecQuesAns());
	}

	/**
	 * Update phising.
	 *
	 * @param userUpdateData the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/user/phising", method = { RequestMethod.POST })
	public ResponseWrapperM<Object, AuthResponseOTPprefix> updatePhising(
			@Deprecated @RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@Deprecated @RequestParam(required = false) String mOtp, @RequestBody UserUpdateRequest userUpdateData) {
		ResponseWrapperM<Object, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<>();
		mOtp = JaxAuthContext.mOtp(ArgUtil.ifNotEmpty(userUpdateData.getmOtp(), mOtp, mOtpHeader));
		// mOtp = (mOtp == null) ? (mOtpHeader == null ? userUpdateData.getmOtp() :
		// mOtpHeader) : mOtp;
		if (mOtp == null) {
			wrapper.setMeta(new AuthData());
			wrapper.getMeta().setmOtpPrefix(loginService.sendOTP(null, null).getData().getmOtpPrefix());
			wrapper.setStatusEnum(OWAStatusStatusCodes.MOTP_REQUIRED);
		} else {
			wrapper.setData(
					userService.updatePhising(userUpdateData.getImageUrl(), userUpdateData.getCaption(), mOtp, null));
			wrapper.setStatusEnum(OWAStatusStatusCodes.USER_UPDATE_SUCCESS);
		}
		return wrapper;
	}

	/**
	 * Send OTP.
	 *
	 * @param mOtp the m otp
	 * @param eOtp the e otp
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/api/user/otpsend", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> sendOTP(@RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String eOtp) {
		mOtp = JaxAuthContext.mOtp(mOtp);
		eOtp = JaxAuthContext.eOtp(eOtp);
		if (mOtp == null) {
			return loginService.sendOTP(null, null);
		} else {
			return loginService.verifyResetPassword(null, mOtp, null);
		}
	}

	@RequestMapping(value = "/api/user/income", method = { RequestMethod.POST })
	public ResponseWrapper<IncomeDto> saveAnnualIncome(@RequestBody IncomeDto incomeDto) {
		try {
			return ResponseWrapper.build(jaxService.setDefaults().getUserclient().saveAnnualIncome(incomeDto));
		} finally {
			userService.updateCustoemrModel();
		}
	}

	@RequestMapping(value = "/api/user/income", method = { RequestMethod.GET })
	public ResponseWrapper<IncomeDto> getAnnualIncomeDetais() {
		return ResponseWrapper.build(jaxService.setDefaults().getUserclient().getAnnualIncomeDetais());
	}

	@RequestMapping(value = "/api/user/trnx_limit", method = { RequestMethod.POST })
	public ResponseWrapper<BoolRespModel> saveAnnualTransactionLimit(
			@RequestBody IncomeDto incomeDto) {
		try {
			return ResponseWrapper
					.build(jaxService.setDefaults().getUserclient().saveAnnualTransactionLimit(incomeDto));
		} finally {
			userService.updateCustoemrModel();
		}
	}

	@RequestMapping(value = "/api/user/trnx_limit", method = { RequestMethod.GET })
	public ResponseWrapper<AnnualIncomeRangeDTO> getAnnualTransactionLimit() {
		return ResponseWrapper.build(jaxService.setDefaults().getUserclient().getAnnualTransactionLimit());
	}

	@RequestMapping(value = "/api/user/device/link", method = { RequestMethod.POST })
	public ResponseWrapper<UserFingerprintResponseModel> linkDevice() {
		return ResponseWrapper.build(jaxService.getUserclient().linkDeviceIdLoggedinUser());
	}

	@RequestMapping(value = { "/api/user/device/delink" }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> delinkDevice() {
		return ResponseWrapper.build(jaxService.getUserclient().delinkFingerprint());
	}

	@RequestMapping(value = { "/pub/user/device/reset" }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> resetDevice(@Valid @RequestBody AuthRequestFingerprint authData) {
		return ResponseWrapper.build(jaxService.getUserclient().resetFingerprint(authData.getLockId()));
	}

	@Deprecated
	@RequestMapping(value = { "/pub/user/otpflags" }, method = { RequestMethod.GET })
	public AmxApiResponse<CustomerModelSignupResponse, Object> getOtpFlags(@RequestParam String identity) {
		return ResponseWrapper.build(jaxService.setDefaults().getUserclient().getCustomerModelSignupResponse(identity));
	}

}
