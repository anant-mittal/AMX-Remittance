/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sample;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUtils;

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
	public String sendOTP(@RequestParam String username, @RequestParam String password, Model model) {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		if ("admin".equals(username) && "admin".equals(password)) {
			// UsernamePasswordAuthenticationToken token = new
			// UsernamePasswordAuthenticationToken(username, password);
			// token.setDetails(new WebAuthenticationDetails(request));
			// SecurityContextHolder.getContext().setAuthentication(token);
			return "redirect:" + sSOTranx.get().getLandingUrl();
		}

		return "index";
	}

}
