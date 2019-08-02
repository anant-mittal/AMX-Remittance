
package com.amx.jax.ui.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.service.ICustomerService;
import com.amx.jax.AppContextUtil;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.dict.ContactType;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.AuthState.AuthFlow;
import com.amx.jax.postman.client.GoogleService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.config.OWAStatus.ApiOWAStatus;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.config.HttpUnauthorizedException;
import com.amx.jax.ui.config.UIServerError;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequest;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.session.Transactions;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

/**
 * The Class AuthController.
 */
@RestController
@Api(value = "User Auth APIs")
@ApiStatusService(ICustomerService.class)
public class AuthController {

	/** The login service. */
	@Autowired
	private LoginService loginService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	@Autowired
	private GoogleService googleService;

	@Autowired
	private CommonHttpRequest httpService;

	/**
	 * Asks for user login and password.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@Deprecated
	@ApiOWAStatus({ OWAStatusStatusCodes.DEVICE_LOCKED, OWAStatusStatusCodes.AUTH_DONE,
			OWAStatusStatusCodes.AUTH_FAILED, OWAStatusStatusCodes.AUTH_OK })
	@RequestMapping(value = "/pub/auth/login", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> login(@Valid @RequestBody AuthRequest authData) {

		if (!ArgUtil.isEmpty(authData.getLockId()) && !authData.getLockId().equalsIgnoreCase(authData.getIdentity())) {
			throw new UIServerError(OWAStatusStatusCodes.DEVICE_LOCKED);
		}

		if (!ArgUtil.isEmpty(authData.getDeviceToken())) {
			return loginService.loginByDevice(authData.getIdentity(), authData.getDeviceToken());
		} else if (!ArgUtil.isEmpty(authData.getPassword())) {
			return loginService.login(authData.getIdentity(), authData.getPassword());
		} else {
			throw new UIServerError(OWAStatusStatusCodes.MISSING_CREDENTIALS);
		}
	}

	/**
	 * Asks for user login and password.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@ApiOWAStatus({ OWAStatusStatusCodes.DEVICE_LOCKED, OWAStatusStatusCodes.AUTH_DONE,
			OWAStatusStatusCodes.AUTH_FAILED, OWAStatusStatusCodes.AUTH_OK })
	@RequestMapping(value = "/pub/auth/login/v2", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginUserPass(@Valid @RequestBody AuthRequest authData) {

		if (!ArgUtil.isEmpty(authData.getLockId()) && !authData.getLockId().equalsIgnoreCase(authData.getIdentity())) {
			throw new UIServerError(OWAStatusStatusCodes.DEVICE_LOCKED);
		}

		if (!ArgUtil.isEmpty(authData.getDeviceToken())) {
			return loginService.loginByDevice(authData.getIdentity(), authData.getDeviceToken());
		} else if (!ArgUtil.isEmpty(authData.getPassword())) {
			if (!ArgUtil.isEmpty(authData.getCaptachKey()) &&
					googleService.verifyCaptcha(authData.getCaptachKey(), httpService.getIPAddress())) {
				return loginService.loginUserPass(authData.getIdentity(), authData.getPassword());
			} else {
				throw new UIServerError(OWAStatusStatusCodes.CAPTCHA_REQUIRED);
			}
		} else {
			throw new UIServerError(OWAStatusStatusCodes.MISSING_CREDENTIALS);
		}
	}

	/**
	 * Login sec ques.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@ApiOWAStatus({ OWAStatusStatusCodes.MOTP_REQUIRED })
	@RequestMapping(value = "/pub/auth/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginSecQues(@Valid @RequestBody AuthRequest authData,
			@RequestParam(required = false) boolean useOTP) {
		useOTP = ArgUtil.parseAsBoolean(useOTP, false);
		String otp = authData.getmOtp();
		if (useOTP) {
			otp = JaxAuthContext.mOtp(otp);
			if (ArgUtil.isEmpty(otp)) {
				AuthResponse model = loginService.sendOTP(authData.getIdentity(), null).getData();
				throw new UIServerError(new AmxApiError(OWAStatusStatusCodes.MOTP_REQUIRED).meta(model));
			}
		}
		return loginService.loginSecQues(authData.getAnswer(), otp);
	}

	/**
	 * Send OTP.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@Deprecated
	@RequestMapping(value = "/pub/auth/otp", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> sendOTP(@Valid @RequestBody AuthRequest authData) {
		return loginService.sendOTP(authData.getIdentity(), null);
	}

	/**
	 * Inits the reset.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 * @deprecated - use : /pub/auth/reset/v2
	 */
	@Deprecated
	@RequestMapping(value = "/pub/auth/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> initReset(@Valid @RequestBody AuthRequest authData) {
		if (authData.getmOtp() == null && authData.geteOtp() == null) {
			return loginService.initResetPassword(authData.getIdentity());
		} else {
			return loginService.verifyResetPassword(authData.getIdentity(), authData.getmOtp(), authData.geteOtp());
		}
	}

	@Autowired
	Transactions transactions;

	@RequestMapping(value = "/pub/auth/password/v2/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> resetPasswordFlow(@Valid @RequestBody AuthRequest authData,
			@RequestParam(required = false) ContactType contactType) {
		AppContextUtil.setFlow(AuthFlow.RESET_PASS.toString());
		transactions.create(AuthFlow.RESET_PASS);
		sessionService.getGuestSession().setIdentity(authData.getIdentity());
		return loginService.initResetPassword2(authData.getIdentity(), authData.getPassword());
	}

	@ApiOWAStatus({ OWAStatusStatusCodes.USER_UPDATE_SUCCESS })
	@RequestMapping(value = "/pub/auth/password/v2/update", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPasswordV2(@Valid @RequestBody AuthRequest authData) {
		if (transactions.validate(AuthFlow.RESET_PASS)) {
			throw new HttpUnauthorizedException(HttpUnauthorizedException.UN_SEQUENCE);
		}
		return loginService.updatepwdV2(authData.getPassword());
	}

	/**
	 * Reset password.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 * 
	 * @deprecated - use : /pub/auth/password/v2
	 */
	@Deprecated
	@ApiOWAStatus({ OWAStatusStatusCodes.USER_UPDATE_SUCCESS })
	@RequestMapping(value = "/pub/auth/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@Valid @RequestBody AuthRequest authData) {
		return loginService.updatepwd(authData.getPassword(), authData.getmOtp(), authData.geteOtp());
	}

	/**
	 * Logout.
	 *
	 * @return the response wrapper
	 */
	@ApiOWAStatus({ OWAStatusStatusCodes.LOGOUT_DONE })
	@RequestMapping(value = "/pub/auth/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(OWAStatusStatusCodes.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

}
