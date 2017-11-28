package com.amx.jax.ui.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.model.UserSessionInfo;
import com.amx.jax.ui.response.LoginData;
import com.amx.jax.ui.response.UIResponse;
import com.amx.jax.ui.response.VerifyIdData;
import com.amx.jax.ui.service.RegistrationService;

@RestController
public class RegisterController {

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private UserSessionInfo userSessionInfo;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@RequestMapping(value = "/register/api/verifyid", method = { RequestMethod.POST })
	public UIResponse<VerifyIdData> verifyID(@RequestParam String civilid) {
		return registrationService.verifyId(civilid);
	}

	@RequestMapping(value = "/register/api/verifycuser", method = { RequestMethod.POST, RequestMethod.GET })
	public UIResponse<LoginData> verifyCustomer(@RequestParam String civilid, @RequestParam String otp,
			HttpServletRequest request) {

		UIResponse<LoginData> response = new UIResponse<LoginData>();
		LoginData data = registrationService.loginWithOtp(civilid, otp);

		if (userSessionInfo.isValid()) {
			response.setMessage("Authed");
			response.setStatusKey("ALREADY_LOGGED_IN");
		} else {
			UsernamePasswordAuthenticationToken token = null;
			try {
				if (userSessionInfo.isValid(civilid, otp)) {
					token = new UsernamePasswordAuthenticationToken(civilid, otp);
					token.setDetails(new WebAuthenticationDetails(request));
					Authentication authentication = this.customerAuthProvider.authenticate(token);
					response.setStatusCode("200");
					response.setMessage("Authing");
					response.setStatusKey("VERIFY_SUCCESS");
					userSessionInfo.setValid(true);
					SecurityContextHolder.getContext().setAuthentication(authentication);
				} else {
					response.setStatusCode("403");
					response.setMessage("NoAuthing");
					response.setStatusKey("VERIFY_FAILED");
				}

			} catch (Exception e) {
				token = null;
				response.setStatusCode("403");
				response.setMessage("NoAuthing");
				response.setStatusKey("VERIFY_FAILED");
			}
			SecurityContextHolder.getContext().setAuthentication(token);
		}

		response.setData(data);
		return response;
	}

}
