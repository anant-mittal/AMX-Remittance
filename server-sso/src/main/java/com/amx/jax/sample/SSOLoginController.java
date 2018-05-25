/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sample;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUtils;
import com.amx.jax.sso.SSOController.SSOAuth;
import com.amx.utils.Urly;

@Controller
public class SSOLoginController {

	private Logger logger = Logger.getLogger(SSOLoginController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	SSOTranx sSOTranx;

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL, method = RequestMethod.GET)
	public String authLogin(Model model) {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		return "index";
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL, method = RequestMethod.POST)
	public String sendOTP(@RequestParam String username, @RequestParam String password, Model model)
			throws MalformedURLException, URISyntaxException {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		if ("admin".equals(username) && "admin".equals(password)) {
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
