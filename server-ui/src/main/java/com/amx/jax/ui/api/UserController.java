
package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.AuthDataInterface.UserUpdateRequest;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.SessionService;
import com.amx.jax.ui.service.TenantService;
import com.amx.jax.ui.service.UserService;
import com.amx.jax.ui.session.UserDevice;
import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
	@RequestMapping(value = "/pub/user/meta", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<UserMetaData> getMeta(@RequestParam(required = false) UserDevice.AppType appType,
			@RequestParam(required = false) String appVersion) {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());

		if (appType != null) {
			sessionService.getAppDevice().setAppType(appType);
		}

		if (appVersion != null) {
			sessionService.getAppDevice().setAppVersion(appVersion);
		}

		wrapper.getData().setDevice(sessionService.getAppDevice().toUserDevice());
		wrapper.getData().setState(sessionService.getGuestSession().getState());
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

	@Deprecated
	@ApiOperation(value = "Old API to update password with Form")
	@RequestMapping(value = "/api/user/password", method = {
			RequestMethod.POST }, consumes = "application/x-www-form-urlencoded")
	public ResponseWrapper<UserUpdateData> changePassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatepwd(password, mOtp, eOtp);
	}

	@ApiOperation(value = "new API to update password with Object")
	@RequestMapping(value = "/api/user/password/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> changePasswordJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updatepwd(userUpdateRequest.getPassword(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	@Deprecated
	@RequestMapping(value = "/api/user/email", method = {
			RequestMethod.POST }, consumes = "application/x-www-form-urlencoded")
	public ResponseWrapper<UserUpdateData> updateEmail(@RequestParam String email,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updateEmail(email, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/email/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updateEmailJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updateEmail(userUpdateRequest.getEmail(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	@RequestMapping(value = "/api/user/phone", method = {
			RequestMethod.POST }, consumes = "application/x-www-form-urlencoded")
	public ResponseWrapper<UserUpdateData> updatePhone(@RequestParam String phone,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		return userService.updatePhone(phone, mOtp, eOtp);
	}

	@RequestMapping(value = "/api/user/phone/**", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> updatePhoneJSON(@RequestBody UserUpdateRequest userUpdateRequest) {
		return userService.updatePhone(userUpdateRequest.getPhone(), userUpdateRequest.getmOtp(),
				userUpdateRequest.geteOtp());
	}

	/**
	 * @param securityquestions
	 * @return
	 */
	@RequestMapping(value = "/api/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> regSecQues(@RequestBody UserUpdateData userUpdateData) {
		return userService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	@RequestMapping(value = "/api/user/otpsend", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> sendOTP(@RequestParam(required = false) String mOtp,
			@RequestParam(required = false) String eOtp) {
		if (mOtp == null) {
			return loginService.sendOTP(null, null);
		} else {
			return loginService.verifyResetPassword(null, mOtp, null);
		}

	}
}
