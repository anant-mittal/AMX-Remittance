/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sso;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.utils.JsonUtil;
import com.amx.utils.Random;
import com.amx.utils.URLBuilder;
import com.amx.utils.Urly;

@Controller
public class SSOLoginController {

	private Logger LOGGER = Logger.getLogger(SSOLoginController.class);

	@Autowired
	SSOTranx sSOTranx;

	@Autowired
	SSOConfig sSOConfig;

	@Autowired
	PostManService postManService;

	private static final String REDIRECT = "redirect:";

	@Autowired
	CommonHttpRequest httpService;

	@Autowired
	SSOAuthProvider authProvider;

	@Autowired
	SSOUser ssoUser;

	@Autowired
	AppConfig appConfig;

	public enum SSOAuth {
		DONE, INIT, NONE
	}

	@RequestMapping(value = SSOUtils.APP_LOGIN_URL, method = { RequestMethod.GET })
	public String loginJPage(@RequestParam(required = false) SSOAuth auth, @RequestParam(required = false) String sotp,
			Model model, HttpServletRequest request, HttpServletResponse response)
			throws MalformedURLException, URISyntaxException {
		String tranxId = AppContextUtil.getTranxId();
		if (auth == null) {
			auth = SSOAuth.NONE;
		}

		if (auth == SSOAuth.DONE && sotp != null && sotp.equals(sSOTranx.get().getSotp())) {
			LOGGER.debug("auth == SSOAuth.DONE");
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(tranxId, sotp);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authProvider.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			ssoUser.setAuthDone(true);
		}

		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			sSOTranx.setLandingUrl(request.getRequestURL().toString(), Random.randomAlphaNumeric(6));
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOUtils.SSO_LOGIN_URL_REDIRECT).addParameter(AppConstants.TRANX_ID_XKEY, tranxId);
			return REDIRECT + builder.getURL();
		}
		return REDIRECT + sSOTranx.get().getReturnUrl();
	}

	private Model getSSOLoginUrl(Model model) {
		if (AppContextUtil.getTranxId() == null) {
			sSOTranx.init();
		}
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		return model;
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL_HTML, method = RequestMethod.GET)
	public String authLoginView(Model model, @PathVariable(required = false) String html) {
		model = getSSOLoginUrl(model);
		return SSOUtils.SSO_INDEX_PAGE;
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL_JSON, method = RequestMethod.GET, headers = {
			"Accept=application/json", "Accept=application/v0+json" })
	@ResponseBody
	public String authLoginJson(Model model, @PathVariable(required = false) String json) {
		model = getSSOLoginUrl(model);
		return JsonUtil.toJson(model.asMap());
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL_HTML, method = RequestMethod.POST)
	public String sendOTP(@RequestParam String username, @RequestParam String password, Model model)
			throws MalformedURLException, URISyntaxException {
		model.addAttribute(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		model.addAttribute("SSO_LOGIN_URL", SSOUtils.SSO_LOGIN_URL_DO);
		if (sSOConfig.getAdminuser().equals(username) && sSOConfig.getAdminpass().equals(password)) {
			// UsernamePasswordAuthenticationToken token = new
			// UsernamePasswordAuthenticationToken(username, password);
			// token.setDetails(new WebAuthenticationDetails(request));
			// SecurityContextHolder.getContext().setAuthentication(token);
			return "redirect:" + Urly.parse(sSOTranx.get().getLandingUrl())
					.addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
					.addParameter("auth", SSOAuth.DONE).addParameter("sotp", sSOTranx.get().getSotp()).getURL();
		}
		return SSOUtils.SSO_INDEX_PAGE;
	}

	@RequestMapping(value = SSOUtils.SSO_LOGIN_URL_JSON, method = {
			RequestMethod.POST }, consumes = MediaType.APPLICATION_JSON_VALUE, produces = {
					CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String loginJson(@RequestBody SSOLoginFormData formdata)
			throws MalformedURLException, URISyntaxException, PostManException {
		Map<String, String> model = new HashMap<String, String>();
		model.put(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId());
		model.put("SSO_LOGIN_URL", SSOUtils.SSO_LOGIN_URL_DO);
		if (sSOTranx.get() == null) {
			sSOTranx.init();
		}
		if (sSOTranx.get() != null) {
			if ("send_otp".equalsIgnoreCase(formdata.getAction())) {
				String prefix = Random.randomAlpha(3);
				String motp = Random.randomNumeric(6);
				Notipy msg = new Notipy();
				msg.setMessage("SSO LOGIN");
				msg.addLine(String.format("OTP = %s-%s", prefix, motp));
				msg.setChannel(Channel.NOTIPY);
				postManService.notifySlack(msg);
				model.put("motpPrefix", prefix);
				sSOTranx.setMOtp(motp);
			} else if ("submit".equalsIgnoreCase(formdata.getAction()) && sSOTranx.get().getMotp() != null
					&& sSOTranx.get().getMotp().equals(formdata.getMotp())) {
				model.put("redirect", Urly.parse(sSOTranx.get().getLandingUrl())
						.addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
						.addParameter("auth", SSOAuth.DONE).addParameter("sotp", sSOTranx.get().getSotp()).getURL());
			}
		}

		return JsonUtil.toJson(model);
	}

	@RequestMapping(value = SSOUtils.APP_LOGGEDIN_URL, method = { RequestMethod.GET })
	public String loggedinJPage(Model model, @RequestParam boolean done)
			throws MalformedURLException, URISyntaxException {
		if (!ssoUser.isAuthDone()) {
			LOGGER.debug("ssoUser.isAuthDone() is false");
			sSOTranx.init();
			URLBuilder builder = new URLBuilder(appConfig.getSsoURL());
			builder.setPath(SSOUtils.SSO_LOGIN_URL_REDIRECT).addParameter(AppConstants.TRANX_ID_XKEY,
					AppContextUtil.getTranxId());
			return REDIRECT + builder.getURL();
		}
		return "home";
	}
}
