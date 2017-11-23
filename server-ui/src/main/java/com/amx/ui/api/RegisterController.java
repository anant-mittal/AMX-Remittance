package com.amx.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.ui.response.UIResponse;
import com.amx.ui.response.VerifyIdData;
import com.amx.ui.service.RegistrationService;

@RestController
public class RegisterController {

	@Autowired
	private RegistrationService registrationService;

	@RequestMapping(value = "/register/api/verifyid", method = { RequestMethod.POST })
	public UIResponse verifyID(@RequestParam String civilid) {
		UIResponse response = new UIResponse();
		VerifyIdData data = registrationService.verifyId(civilid);
		response.setData(data);
		return response;
	}

	@RequestMapping(value = "/register/api/verifycuser", method = { RequestMethod.POST })
	public UIResponse verifyCustomer(@RequestParam String civilid, @RequestParam String otp) {
		UIResponse response = new UIResponse();
		VerifyIdData data = registrationService.verifyCustomer(civilid);
		response.setData(data);
		return response;
	}

}
