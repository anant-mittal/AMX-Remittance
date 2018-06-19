package com.amx.jax.ui.config;

import java.io.IOException;

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

import com.amx.jax.ui.session.GuestSession;

@Component
public class WebLoginUrlEntry extends LoginUrlAuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebLoginUrlEntry.class);
	private static final String LOGIN_URL = "/login";

	@Autowired
	GuestSession guestSession;

	@Autowired
	public WebLoginUrlEntry() {
		super(LOGIN_URL);
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		String returnPath = request.getServletPath();
		if (returnPath.startsWith("/app")) {
			guestSession.setReturnUrl(request.getServletPath());
		}
		redirectStrategy.sendRedirect(request, response, LOGIN_URL);
	}

}