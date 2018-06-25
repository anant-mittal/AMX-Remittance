
package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppConstants;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.RegistrationService;
import com.amx.jax.ui.service.SessionService;

import io.swagger.annotations.Api;

/**
 * @deprecated Only for backward compatibility
 * 
 * @author lalittanwar
 *
 */
@RestController
@Api(value = "Deprecated Auth APIs")
@Deprecated
@Validated
public class OldController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private RegistrationService registrationService;

	/**
	 * These APIS ends needs to be move to appropriate Controller
	 */
	/**
	 * Some changes
	 * 
	 * @param identity
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/pub/user/login", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> login(
			@RequestParam(required = false) @Pattern(regexp = AppConstants.Validator.IDENTITY) String identity,
			@RequestParam(required = false) String password) {
		return loginService.login(identity, password);
	}

	@RequestMapping(value = "/pub/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginSecQues(@RequestBody SecurityQuestionModel guestanswer) {
		return loginService.loginSecQues(guestanswer, null);
	}

	@RequestMapping(value = "/pub/user/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> initReset(@RequestParam String identity,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		if (mOtp == null && eOtp == null) {
			return loginService.initResetPassword(identity);
		} else {
			return loginService.verifyResetPassword(identity, mOtp, eOtp);
		}
	}

	@RequestMapping(value = "/pub/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return loginService.updatepwd(password, mOtp, eOtp);
	}

	@RequestMapping(value = "/pub/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(WebResponseStatus.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/api/secques/get", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues(false);
	}

	/**
	 * @param securityquestions
	 * @return
	 */
	@RequestMapping(value = "/api/secques/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> postSecQues(@RequestBody UserUpdateData userUpdateData) {
		return registrationService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * 
	 * @param imageUrl
	 * @param caption
	 * @param mOtp
	 * @param eOtp
	 * @return
	 */
	@RequestMapping(value = "/api/phising/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> updatePhising(@RequestParam String imageUrl, @RequestParam String caption,
			@RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.updatePhising(imageUrl, caption, mOtp, eOtp);
	}

	/**
	 * 
	 * @param loginId
	 * @param password
	 * @param mOtp
	 * @param eOtp
	 * @return
	 */
	@RequestMapping(value = "/api/creds/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp,
			@RequestParam(required = false) String email) {
		return registrationService.setCredentials(loginId, password, mOtp, eOtp, email, false);
	}
}
