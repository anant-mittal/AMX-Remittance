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

@RestController
public class RegisterController {

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(value = "/register/api/verifyid", method = { RequestMethod.POST })
	public ResponseWrapper<RegistrationdData> verifyID(@RequestParam String civilid) {
		return registrationService.verifyId(civilid);
	}

	@RequestMapping(value = "/register/api/verifycuser", method = { RequestMethod.POST, RequestMethod.GET })
	public ResponseWrapper<RegistrationdData> verifyCustomer(@RequestParam String civilid, @RequestParam String otp,
			HttpServletRequest request) {
		return registrationService.loginWithOtp(civilid, otp, request);
	}

	@RequestMapping(value = "/register/api/secques/get", method = { RequestMethod.GET })
	public ResponseWrapper<RegistrationdData> getSecQues(HttpServletRequest request) {
		return registrationService.getSecQues();
	}

	@RequestMapping(value = "/register/api/secques/save", method = { RequestMethod.POST, })
	public ResponseWrapper<RegistrationdData> postSecQues(@RequestBody List<SecurityQuestionModel> securityquestions) {
		return registrationService.updateSecQues(securityquestions);
	}

}
