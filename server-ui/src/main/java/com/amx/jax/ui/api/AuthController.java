
package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "User Auth APIs")
public class AuthController {

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
	public ResponseWrapper<AuthData> login(@RequestParam(required = false) String identity,
			@RequestParam(required = false) String password) {
		return loginService.login(identity, password);
	}

	@RequestMapping(value = "/pub/auth/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> loginSecQues(@RequestBody SecurityQuestionModel guestanswer) {
		return loginService.loginSecQues(guestanswer);
	}

	@RequestMapping(value = "/pub/auth/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> initReset(@RequestParam String identity,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		if (mOtp == null && eOtp == null) {
			return loginService.initResetPassword(identity);
		} else {
			return loginService.verifyResetPassword(identity, mOtp, eOtp);
		}
	}

	@RequestMapping(value = "/pub/auth/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return loginService.updatepwd(password, mOtp, eOtp);
	}

	@RequestMapping(value = "/pub/auth/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.unauthorize();
		wrapper.setMessage(ResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

}
