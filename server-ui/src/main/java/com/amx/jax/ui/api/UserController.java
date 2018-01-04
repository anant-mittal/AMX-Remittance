
package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.model.LoginData;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.UserService;
import com.amx.jax.ui.session.UserSession;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "User APIs")
public class UserController {

	@Autowired
	private UserSession userSession;

	@Autowired
	private LoginService loginService;

	@Autowired
	SessionService sessionService;

	@Autowired
	UserService userService;

	/**
	 * Asks for user login and password
	 * 
	 * @param identity
	 * @param password
	 * @return
	 */
	@ApiOperation(value = "Validates login/pass but does not creates session")
	@RequestMapping(value = "/pub/user/login", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> login(@RequestParam(required = false) String identity,
			@RequestParam(required = false) String password) {
		return loginService.login(identity, password);
	}

	@ApiOperation(value = "Logins User & creates session")
	@RequestMapping(value = "/pub/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> loginSecQues(@RequestBody SecurityQuestionModel guestanswer,
			HttpServletRequest request) {
		return loginService.loginSecQues(guestanswer, request);
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/pub/user/reset", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<LoginData> reset(@RequestParam String identity, @RequestParam(required = false) String otp) {
		ResponseWrapper<LoginData> wrapper = loginService.reset(identity, otp);
		return wrapper;
	}

	@ApiOperation(value = "Logout User & Terminates session")
	@RequestMapping(value = "/pub/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		sessionService.unauthorize();

		wrapper.setMessage(ResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	@ApiOperation(value = "Get UserMeta Data")
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> getMeta() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setValidSession(userSession.isValid());

		if (userSession.getCustomerModel() != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setInfo(userSession.getCustomerModel().getPersoninfo());
		}

		return wrapper;
	}

	@ApiOperation(value = "Sends OTP and resets password")
	@RequestMapping(value = "/api/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePassword(String password) {
		return loginService.updatepwd(password);
	}

	@RequestMapping(value = "/api/user/profile", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerDto> profile() {
		return userService.getProfileDetails();
	}

}
