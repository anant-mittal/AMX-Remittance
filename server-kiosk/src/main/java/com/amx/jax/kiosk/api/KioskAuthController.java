
package com.amx.jax.kiosk.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.service.ICustomerService;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.postman.client.GoogleService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.UIConstants.FLOW;
import com.amx.jax.ui.config.OWAStatus.ApiOWAStatus;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.config.UIServerError;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequestPinLogin;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

/**
 * The Class AuthController.
 */
@RestController
@Api(value = "User Auth APIs")
@ApiStatusService(ICustomerService.class)
public class KioskAuthController {

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
	@ApiRequest(flow = FLOW.LOGIN_V2)
	@ApiOWAStatus({ OWAStatusStatusCodes.DEVICE_LOCKED, OWAStatusStatusCodes.AUTH_DONE,
			OWAStatusStatusCodes.AUTH_FAILED, OWAStatusStatusCodes.AUTH_OK })
	@RequestMapping(value = "/pub/auth/login/v2", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginUserPass(@Valid @RequestBody AuthRequestPinLogin authData) {

		if (sessionService.validatedUser()) {
			// throw new UIServerError(OWAStatusStatusCodes.ACTIVE_SESSION);
		}

		String captcha = JaxAuthContext.captcha(authData.getCaptachKey());

		if (!ArgUtil.isEmpty(captcha) &&
				!googleService.verifyCaptcha(captcha, httpService.getIPAddress())) {
			throw new UIServerError(OWAStatusStatusCodes.CAPTCHA_REQUIRED).redirectUrl("/pub/recaptcha/DASHBOARD");
		}

		if (!ArgUtil.isEmpty(authData.getPin())) {
			return loginService.loginUserPass(authData.getIdentity(), authData.getPin());
		} else {
			throw new UIServerError(OWAStatusStatusCodes.MISSING_CREDENTIALS);
		}
	}

	/**
	 * Logout.
	 *
	 * @return the response wrapper
	 */
	@ApiRequest(flow = FLOW.LOGOUT)
	@ApiOWAStatus({ OWAStatusStatusCodes.LOGOUT_DONE })
	@RequestMapping(value = "/pub/auth/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(OWAStatusStatusCodes.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

}
