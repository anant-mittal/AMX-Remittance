
package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantService;
import com.amx.jax.ui.service.UserService;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;

@RestController
@Api(value = "User APIs")
public class UserController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserService userService;

	@Autowired
	private TenantService tenantContext;

	@Timed
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> getMeta() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		wrapper.getData().setDevice(sessionService.getAppDevice().toMap());
		wrapper.getData().setValidSession(sessionService.getUserSession().isValid());

		if (sessionService.getUserSession().getCustomerModel() != null) {
			wrapper.getData().setActive(true);
			wrapper.getData().setInfo(sessionService.getUserSession().getCustomerModel().getPersoninfo());
			wrapper.getData().setDomCurrency(tenantContext.getDomCurrency());
		}

		return wrapper;
	}

	@RequestMapping(value = "/api/user/profile", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerDto> profile() {
		return userService.getProfileDetails();
	}

	@RequestMapping(value = "/api/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return loginService.updatepwd(password, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/email", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updateEmail(@RequestParam String email,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updateEmail(email, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/phone", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updatePhone(@RequestParam String phone,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatePhone(phone, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> updateSecQues(@RequestBody SecurityQuestionModel guestanswer,
			@CookieValue(value = UIConstants.SEQ_KEY, defaultValue = UIConstants.BLANK) String seqValue) {
		return loginService.loginSecQues(guestanswer);
	}

	@RequestMapping(value = "/api/user/otpsend", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> sendOTP(@RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String eOtp) {
		if (mOtp == null) {
			return loginService.sendOTP(null);
		} else {
			return loginService.verifyResetPassword(null, mOtp, null);
		}

	}
}
