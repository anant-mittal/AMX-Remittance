package com.amx.jax.ui.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.response.RegistrationdData;
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
	 * 
	 * @param civilid
	 * @return
	 */
	@ApiOperation(value = "Verify KYC and sneds OTP to registered Mobile")
	@RequestMapping(value = "/register/api/verifyid", method = { RequestMethod.POST })
	public ResponseWrapper<RegistrationdData> verifyID(@RequestParam String civilid) {
		return registrationService.verifyId(civilid);
	}

	/**
	 * 
	 * Verifies OTP for civilID
	 * 
	 * 
	 * @param civilid
	 * @param otp
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register/api/verifycuser", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<RegistrationdData> verifyCustomer(@RequestParam String civilid, @RequestParam String otp,
			HttpServletRequest request) {
		return registrationService.loginWithOtp(civilid, otp, request);
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/register/api/secques/get", method = { RequestMethod.GET })
	public ResponseWrapper<RegistrationdData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues();
	}

	/**
	 * 
	 * @param securityquestions
	 * @return
	 */
	@RequestMapping(value = "/register/api/secques/set", method = { RequestMethod.POST, })
	public ResponseWrapper<RegistrationdData> postSecQues(@RequestBody List<SecurityQuestionModel> securityquestions) {
		return registrationService.updateSecQues(securityquestions);
	}

	@RequestMapping(value = "/register/api/phising/set", method = { RequestMethod.POST, })
	public ResponseWrapper<RegistrationdData> updatePhising(@RequestParam String imageUrl,
			@RequestParam String caption) {
		return registrationService.updatePhising(imageUrl, caption);
	}

	@RequestMapping(value = "/register/api/creds/set", method = { RequestMethod.POST, })
	public ResponseWrapper<RegistrationdData> saveLoginIdAndPassword(@RequestParam String loginId,
			@RequestParam String password) {
		return registrationService.saveLoginIdAndPassword(loginId, password);
	}
}
