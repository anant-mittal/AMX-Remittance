package com.amx.jax.sso.client;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.sso.SSOConstants;
import com.amx.jax.sso.SSOConstants.SSOAuthStep;
import com.amx.jax.sso.SSOTranx;
import com.amx.jax.sso.SSOUser;
import com.amx.utils.URLBuilder;

@Component
public class SSOLoginUrlEntry extends LoginUrlAuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOLoginUrlEntry.class);

	@Autowired
	private SSOTranx sSOTranx;

	@Autowired
	private SSOUser ssoUser;

	@Autowired
	public SSOLoginUrlEntry() {
		super(SSOConstants.APP_LOGIN_URL_CHECK);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {
		String tranxId = ssoUser.ssoTranxId();
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		sSOTranx.setReturnUrl(request.getServletPath());
		URLBuilder builder = new URLBuilder();
		builder.path(SSOConstants.APP_LOGIN_URL_CHECK).pathParam(SSOConstants.PARAM_STEP, SSOAuthStep.CHECK)
				.queryParam(AppConstants.TRANX_ID_XKEY, tranxId);
		try {
			redirectStrategy.sendRedirect(request, response, builder.getRelativeURL());
		} catch (URISyntaxException e) {
			LOGGER.error("SSOLoginUrlEntry commence error", e);
			redirectStrategy.sendRedirect(request, response, SSOConstants.APP_LOGIN_URL_CHECK);
		}
	}

}
