
package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.model.LoginData;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseStatus;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantContext;
import com.amx.jax.ui.service.UserService;
import com.amx.jax.ui.session.UserSession;

import io.swagger.annotations.Api;

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

	@Autowired
	TenantContext tenantContext;

	/**
	 * Asks for user login and password
	 * 
	 * @param identity
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/pub/user/login", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> login(@RequestParam(required = false) String identity,
			@RequestParam(required = false) String password) {
		return loginService.login(identity, password);
	}

	@RequestMapping(value = "/pub/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> loginSecQues(@RequestBody SecurityQuestionModel guestanswer,
			HttpServletRequest request) {
		return loginService.loginSecQues(guestanswer, request);
	}

	@RequestMapping(value = "/pub/user/reset", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<LoginData> initReset(@RequestParam String identity,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		if (mOtp == null && eOtp == null) {
			return loginService.initResetPassword(identity);
		} else {
			return loginService.verifyResetPassword(identity, mOtp, eOtp);
		}
	}

	@RequestMapping(value = "/pub/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		sessionService.unauthorize();

		wrapper.setMessage(ResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> getMeta() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setValidSession(userSession.isValid());

		if (userSession.getCustomerModel() != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setInfo(userSession.getCustomerModel().getPersoninfo());
			wrapper.getData().setDomCurrency(tenantContext.getDomCurrency());
		}

		return wrapper;
	}

	@RequestMapping(value = "/api/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return loginService.updatepwd(password, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/profile", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerDto> profile() {
		return userService.getProfileDetails();
	}

	@RequestMapping(value = "/api/user/email", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updateEmail(@RequestParam(required = false) String email,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updateEmail(email, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/phone", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updatePhone(@RequestParam(required = false) String phone,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatePhone(phone, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/otpsend", method = { RequestMethod.POST })
	public ResponseWrapper<LoginData> sendOTP(@RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String eOtp) {
		if (mOtp == null) {
			return loginService.sendOTP(null);
		} else {
			return loginService.verifyResetPassword(null, mOtp, null);
		}

	}
}
