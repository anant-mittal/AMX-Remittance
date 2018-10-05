/**  AlMulla Exchange
  *  
  */
package com.amx.jax.sso.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.sso.SSOConfig;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.JsonUtil;
import com.amx.utils.Random;
import com.amx.utils.Urly;

import io.swagger.annotations.ApiParam;

@Controller
public class SSOServerController {

	private Logger LOGGER = Logger.getLogger(SSOServerController.class);

	@Autowired
	private SSOTranx sSOTranx;

	@Autowired
	private SSOConfig sSOConfig;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	private PostManService postManService;

	private Map<String, Object> getModelMap() {
		ssoUser.ssoTranxId();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(AppConstants.TRANX_ID_XKEY_CLEAN, AppContextUtil.getTranxId());
		map.put(SSOConstants.PARAM_SSO_LOGIN_URL, SSOConstants.SSO_LOGIN_URL_DO);
		return map;
	}

	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_HTML, method = RequestMethod.GET)
	public String authLoginView(Model model,
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep html) {
		model.addAllAttributes(getModelMap());
		return SSOConstants.SSO_INDEX_PAGE;
	}

	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_JSON, method = RequestMethod.GET, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String authLoginJson(Model model,
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "REQUIRED") SSOAuthStep json) {
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(getModelMap());
		result.setStatusKey("AUTH_REQIURED");
		return JsonUtil.toJson(result);
	}

	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_HTML, method = RequestMethod.POST)
	public String sendOTP(@RequestParam String username, @RequestParam String password, Model model,
			@PathVariable(required = false, value = "htmlstep") @ApiParam(defaultValue = "DO") SSOAuthStep html)
			throws MalformedURLException, URISyntaxException {
		model.addAllAttributes(getModelMap());
		if (sSOConfig.getAdminuser().equals(username) && sSOConfig.getAdminpass().equals(password)) {
			// UsernamePasswordAuthenticationToken token = new
			// UsernamePasswordAuthenticationToken(username, password);
			// token.setDetails(new WebAuthenticationDetails(request));
			// SecurityContextHolder.getContext().setAuthentication(token);
			return SSOConstants.REDIRECT + Urly.parse(sSOTranx.get().getAppUrl())
					.addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
					.addParameter(SSOConstants.PARAM_STEP, SSOAuthStep.DONE)
					.addParameter(SSOConstants.PARAM_SOTP, sSOTranx.get().getSotp()).getURL();
		}
		return SSOConstants.SSO_INDEX_PAGE;
	}

	@RequestMapping(value = SSOConstants.SSO_LOGIN_URL_JSON, method = { RequestMethod.POST }, produces = {
			CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE })
	@ResponseBody
	public String loginJson(@RequestBody SSOLoginFormData formdata,
			@PathVariable(required = false, value = "jsonstep") @ApiParam(defaultValue = "DO") SSOAuthStep json,
			HttpServletResponse resp) throws URISyntaxException, IOException {

		if (json == SSOAuthStep.DO) {
			json = formdata.getStep();
		} else {
			formdata.setStep(json);
		}

		Map<String, Object> model = getModelMap();
		if (sSOTranx.get() == null) {
			sSOTranx.init();
		}
		AmxApiResponse<Object, Map<String, Object>> result = AmxApiResponse.buildMeta(model);
		if (sSOTranx.get() != null) {
			if (SSOAuthStep.CREDS == json) {
				String prefix = Random.randomAlpha(3);
				String motp = Random.randomNumeric(6);

				Notipy msg = new Notipy();
				msg.setMessage("SSO LOGIN");
				msg.addLine(String.format("OTP = %s-%s", prefix, motp));
				msg.setChannel(Channel.NOTIPY);
				postManService.notifySlack(msg);

				model.put("mOtpPrefix", prefix);

				sSOTranx.setMOtp(motp);
			} else if ((SSOAuthStep.OTP == json) && sSOTranx.get().getMotp() != null
					&& sSOTranx.get().getMotp().equals(formdata.getMotp())) {
				String redirectUrl = Urly.parse(sSOTranx.get().getAppUrl())
						.addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId())
						.addParameter(SSOConstants.PARAM_STEP, SSOAuthStep.DONE)
						.addParameter(SSOConstants.PARAM_SOTP, sSOTranx.get().getSotp()).getURL();
				model.put(SSOConstants.PARAM_REDIRECT, redirectUrl);
				// resp.sendRedirect(redirectUrl);

				resp.setHeader("Location", redirectUrl);
				resp.setStatus(302);
			}
		}
		return JsonUtil.toJson(result);
	}

}
