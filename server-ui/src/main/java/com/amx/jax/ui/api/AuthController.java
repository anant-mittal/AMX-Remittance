
package com.amx.jax.ui.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.model.AuthDataInterface.AuthRequest;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "User Auth APIs")
public class AuthController {

	public static final String SECURITYQUESTIONMODEL = "" + SecurityQuestionModel.class.getName();

	@Autowired
	private LoginService loginService;

	@Autowired
	private SessionService sessionService;

	/**
	 * Asks for user login and password
	 * 
	 * @param identity
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/pub/auth/login", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> login(@Valid @RequestBody AuthRequest authData) {
		return loginService.login(authData.getIdentity(), authData.getPassword());
	}

	@RequestMapping(value = "/pub/auth/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginSecQues(@Valid @RequestBody AuthRequest authData) {
		return loginService.loginSecQues(authData.getAnswer(), authData.getmOtp());
	}

	@RequestMapping(value = "/pub/auth/otp", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> sendOTP(@Valid @RequestBody AuthRequest authData) {
		return loginService.sendOTP(authData.getIdentity(), null);
	}

	@RequestMapping(value = "/pub/auth/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> initReset(@Valid @RequestBody AuthRequest authData) {
		if (authData.getmOtp() == null && authData.geteOtp() == null) {
			return loginService.initResetPassword(authData.getIdentity());
		} else {
			return loginService.verifyResetPassword(authData.getIdentity(), authData.getmOtp(), authData.geteOtp());
		}
	}

	@RequestMapping(value = "/pub/auth/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@Valid @RequestBody AuthRequest authData) {
		return loginService.updatepwd(authData.getPassword(), authData.getmOtp(), authData.geteOtp());
	}

	@RequestMapping(value = "/pub/auth/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(WebResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

}
