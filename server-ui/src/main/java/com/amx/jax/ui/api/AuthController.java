
package com.amx.jax.ui.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.exception.AmxApiError;
import com.amx.jax.ui.config.UIServerError;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequest;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponseOTPprefix;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.ResponseWrapperM;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

/**
 * The Class AuthController.
 */
@RestController
@Api(value = "User Auth APIs")
public class AuthController {

	/** The Constant SECURITYQUESTIONMODEL. */
	public static final String SECURITYQUESTIONMODEL = "" + SecurityQuestionModel.class.getName();

	/** The login service. */
	@Autowired
	private LoginService loginService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/**
	 * Asks for user login and password.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/auth/login", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> login(@Valid @RequestBody AuthRequest authData) {

		if (!ArgUtil.isEmpty(authData.getLockId()) && authData.getLockId().equalsIgnoreCase(authData.getIdentity())) {
			throw new UIServerError(WebResponseStatus.DEVICE_LOCKED);
		}

		if (!ArgUtil.isEmpty(authData.getDeviceToken())) {
			return loginService.loginByDevice(authData.getIdentity(), authData.getDeviceToken());
		} else {
			return loginService.login(authData.getIdentity(), authData.getPassword());
		}
	}

	/**
	 * Login sec ques.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/auth/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginSecQues(@Valid @RequestBody AuthRequest authData,
			@RequestParam(required = false) boolean useOTP) {
		useOTP = ArgUtil.parseAsBoolean(useOTP, false);
		String otp = authData.getmOtp();
		if (useOTP) {
			otp = JaxAuthContext.mOtp(otp);
			if (ArgUtil.isEmpty(otp)) {
				AuthResponse model = loginService.sendOTP(authData.getIdentity(), null).getData();
				throw new UIServerError(new AmxApiError(WebResponseStatus.MOTP_REQUIRED).meta(model));
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
	 */
	@RequestMapping(value = "/pub/auth/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> initReset(@Valid @RequestBody AuthRequest authData) {
		if (authData.getmOtp() == null && authData.geteOtp() == null) {
			return loginService.initResetPassword(authData.getIdentity());
		} else {
			return loginService.verifyResetPassword(authData.getIdentity(), authData.getmOtp(), authData.geteOtp());
		}
	}

	/**
	 * Reset password.
	 *
	 * @param authData the auth data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/auth/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@Valid @RequestBody AuthRequest authData) {
		return loginService.updatepwd(authData.getPassword(), authData.getmOtp(), authData.geteOtp());
	}

	/**
	 * Logout.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/auth/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(WebResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

}
