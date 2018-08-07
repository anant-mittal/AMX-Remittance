package com.amx.jax.sso;

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
import com.amx.jax.AppContextUtil;
import com.amx.utils.URLBuilder;

@Component
public class SSOLoginUrlEntry extends LoginUrlAuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOLoginUrlEntry.class);

	@Autowired
	SSOTranx sSOTranx;

	@Autowired
	public SSOLoginUrlEntry() {
		super(SSOUtils.LOGIN_URL);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		sSOTranx.setReturnUrl(request.getServletPath());
		URLBuilder builder = new URLBuilder();
		builder.setPath(SSOUtils.LOGIN_URL).addParameter(AppConstants.TRANX_ID_XKEY, AppContextUtil.getTranxId());
		try {
			redirectStrategy.sendRedirect(request, response, builder.getRelativeURL());
		} catch (URISyntaxException e) {
			LOGGER.error("SSOLoginUrlEntry commence error", e);
			redirectStrategy.sendRedirect(request, response, SSOUtils.LOGIN_URL);
		}
	}

}
