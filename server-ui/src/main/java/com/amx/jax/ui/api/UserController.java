
package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.EnumUtil;
import com.amx.jax.ui.model.UserSession;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.UserMetaData;
import com.amx.jax.ui.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User APIs")
public class UserController {

	@Autowired
	private UserSession userSession;

	@Autowired
	private LoginService loginService;

	@ApiOperation(value = "Get UserMeta Data")
	@RequestMapping(value = "/api/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> getMeta() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setValid(userSession.isValid());

		if (userSession.getCustomerModel() != null) {
			wrapper.getData().setActive(true);
		}
		return wrapper;
	}

	@ApiOperation(value = "Logout User & Terminates session")
	@RequestMapping(value = "/api/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		userSession.setValid(false);
		userSession.setCustomerModel(null);
		SecurityContextHolder.getContext().setAuthentication(null);

		wrapper.setMessage(EnumUtil.StatusCode.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/api/user/reset", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> reset(@RequestParam String identity,
			@RequestParam(required = false) String otp) {
		return loginService.reset(identity, otp);
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/api/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePassword(String password) {
		return loginService.updatepwd(password);
	}

	@ApiOperation(value = "Login User & Creates session")
	@RequestMapping(value = "/api/user/login", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> login(@RequestParam String identity, @RequestParam String password) {
		return loginService.login(identity, password);
	}

	@ApiOperation(value = "Login User & Creates session")
	@RequestMapping(value = "/api/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> loginSecQues(@RequestBody SecurityQuestionModel guestanswer,
			HttpServletRequest request) {
		return loginService.loginSecQues(guestanswer, request);
	}

}