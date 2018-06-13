/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.sso.SSOController.SSOAuth;
import com.amx.utils.Urly;

@Controller
public class SSOLoginController {

	private Logger logger = Logger.getLogger(SSOLoginController.class);

	@Autowired
	SSOTranx sSOTranx;

	@Value("${amx.server.username}")
	String adminuser;

	@Value("${amx.server.password}")
	String adminpass;

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL, method = RequestMethod.GET)
	public String authLogin(Model model) {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		return "index";
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL, method = RequestMethod.POST)
	public String sendOTP(@RequestParam String username, @RequestParam String password, Model model)
			throws MalformedURLException, URISyntaxException {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		if (adminuser.equals(username) && adminpass.equals(password)) {
			// UsernamePasswordAuthenticationToken token = new
			// UsernamePasswordAuthenticationToken(username, password);
			// token.setDetails(new WebAuthenticationDetails(request));
			// SecurityContextHolder.getContext().setAuthentication(token);
			return "redirect:" + Urly.parse(sSOTranx.get().getLandingUrl())
					.addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
					.addParameter("auth", SSOAuth.DONE).addParameter("sotp", sSOTranx.get().getSotp()).getURL();
		}

		return "index";
	}

}
