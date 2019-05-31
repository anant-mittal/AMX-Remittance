
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

import com.amx.jax.AppConstants;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.ui.config.OWAStatus.OWAStatusStatusCodes;
import com.amx.jax.ui.model.AuthDataInterface.AuthResponse;
import com.amx.jax.ui.model.UserMetaData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.LoginService;
import com.amx.jax.ui.service.RegistrationService;
import com.amx.jax.ui.service.SessionService;

import io.swagger.annotations.Api;

/**
 * The Class OldController.
 *
 * @author lalittanwar
 * @deprecated Only for backward compatibility
 */
@RestController
@Api(value = "Deprecated Auth APIs")
@Deprecated
@Validated
public class OldController {

	/** The login service. */
	@Autowired
	private LoginService loginService;

	/** The session service. */
	@Autowired
	private SessionService sessionService;

	/** The registration service. */
	@Autowired
	private RegistrationService registrationService;

	/**
	 * These APIS ends needs to be move to appropriate Controller.
	 *
	 * @param identity
	 *            the identity
	 * @param password
	 *            the password
	 * @return the response wrapper
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

	/**
	 * Login sec ques.
	 *
	 * @param guestanswer
	 *            the guestanswer
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/user/secques", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> loginSecQues(@RequestBody SecurityQuestionModel guestanswer) {
		return loginService.loginSecQues(guestanswer, null);
	}

	/**
	 * Inits the reset.
	 *
	 * @param identity
	 *            the identity
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/user/reset", method = { RequestMethod.POST })
	public ResponseWrapper<AuthResponse> initReset(@RequestParam String identity,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp) {
		if (mOtp == null && eOtp == null) {
			return loginService.initResetPassword(identity);
		} else {
			return loginService.verifyResetPassword(identity, mOtp, eOtp);
		}
	}

	/**
	 * Reset password.
	 *
	 * @param oldPassword
	 *            the old password
	 * @param password
	 *            the password
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/user/password", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> resetPassword(@RequestParam(required = false) String oldPassword,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return loginService.updatepwd(password, mOtp, eOtp);
	}

	/**
	 * Logout.
	 *
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/pub/user/logout", method = { RequestMethod.POST })
	public ResponseWrapper<UserMetaData> logout() {
		ResponseWrapper<UserMetaData> wrapper = new ResponseWrapper<UserMetaData>(new UserMetaData());
		sessionService.logout();
		wrapper.setMessage(OWAStatusStatusCodes.LOGOUT_DONE, "User logged out successfully");
		return wrapper;
	}

	/**
	 * Gets the sec ques.
	 *
	 * @param request
	 *            the request
	 * @return the sec ques
	 */
	@RequestMapping(value = "/api/secques/get", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues(false);
	}

	/**
	 * Post sec ques.
	 *
	 * @param userUpdateData
	 *            the user update data
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/secques/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> postSecQues(@RequestBody UserUpdateData userUpdateData) {
		return registrationService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * Update phising.
	 *
	 * @param imageUrl
	 *            the image url
	 * @param caption
	 *            the caption
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/phising/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> updatePhising(@RequestParam String imageUrl, @RequestParam String caption,
			@RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.updatePhising(imageUrl, caption, mOtp, eOtp);
	}

	/**
	 * Save login id and password.
	 *
	 * @param loginId
	 *            the login id
	 * @param password
	 *            the password
	 * @param mOtp
	 *            the m otp
	 * @param eOtp
	 *            the e otp
	 * @param email
	 *            the email
	 * @return the response wrapper
	 */
	@RequestMapping(value = "/api/creds/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp,
			@RequestParam(required = false) String email) {
		return registrationService.setCredentials(loginId, password, mOtp, eOtp, email, false);
	}
}
