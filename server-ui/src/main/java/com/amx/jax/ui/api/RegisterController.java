package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ui.model.AuthData;
import com.amx.jax.ui.model.UserUpdateData;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.RegistrationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Controller responsible for providing online registration of offline user
 * 
 * @author lalittanwar
 *
 */
@RestController
@Api(value = "Registration APIs")
public class RegisterController {

	@Autowired
	private RegistrationService registrationService;

	/**
	 * @param civilid
	 * @return
	 */
	@ApiOperation(value = "Verify KYC and sneds OTP to registered Mobile")
	@RequestMapping(value = "/pub/register/verifyid", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> verifyID(@RequestParam String civilid) {
		return registrationService.verifyId(civilid);
	}

	/**
	 * Verifies OTP for civilID
	 * 
	 * @param civilid
	 * @param otp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/pub/register/verifycuser", method = { RequestMethod.POST })
	public ResponseWrapper<UserUpdateData> verifyCustomer(@RequestParam String civilid, @RequestParam String mOtp) {
		return registrationService.loginWithOtp(civilid, mOtp);
	}

	/**
	 * @param securityquestions
	 * @return
	 */
	@RequestMapping(value = "/pub/register/secques", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regSecQues(@RequestBody UserUpdateData userUpdateData) {
		return registrationService.updateSecQues(userUpdateData.getSecQuesAns(), userUpdateData.getmOtp(),
				userUpdateData.geteOtp());
	}

	/**
	 * @param imageUrl
	 * @param caption
	 * @param mOtp
	 * @param eOtp
	 * @return
	 */
	@RequestMapping(value = "/pub/register/phising", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regPhising(@RequestParam String imageUrl, @RequestParam String caption,
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
	@RequestMapping(value = "/pub/register/creds", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> regLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.saveLoginIdAndPassword(loginId, password, mOtp, eOtp);
	}

	/**
	 * These APIS ends needs to be move to appropriate Controller
	 */

	/**
	 * @param request
	 * @return
	 */
	@Deprecated
	@RequestMapping(value = "/api/secques/get", method = { RequestMethod.GET })
	public ResponseWrapper<UserUpdateData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues();
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
	@Deprecated
	@RequestMapping(value = "/api/creds/set", method = { RequestMethod.POST, })
	public ResponseWrapper<UserUpdateData> saveLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password, @RequestParam String mOtp, @RequestParam(required = false) String eOtp) {
		return registrationService.saveLoginIdAndPassword(loginId, password, mOtp, eOtp);
	}
}
