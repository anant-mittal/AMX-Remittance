package com.amx.jax.ui.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserSession;

@Component
public class SessionService {

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private GuestSession guestSession;

	@Autowired
	private UserSession userSession;

	public GuestSession getGuestSession() {
		return guestSession;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void authorize(CustomerModel customerModel, Boolean valid) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				customerModel.getIdentityId(), customerModel.getPassword());
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = this.customerAuthProvider.authenticate(token);
		userSession.setCustomerModel(customerModel);
		// userSession.setValid(true);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void validate(Boolean valid) {
		userSession.setValid(valid);
	}

	/**
	 * Clear Existing user session if any
	 */
	public void clear() {
		userSession.setValid(Boolean.FALSE);
		userSession.setCustomerModel(null);
		userSession.setUserid(null);
		guestSession.setValid(Boolean.FALSE);
		guestSession.setCustomerModel(null);

	}

}
