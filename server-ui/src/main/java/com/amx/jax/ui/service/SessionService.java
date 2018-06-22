package com.amx.jax.ui.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.redisson.api.RLocalCachedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.service.HttpService;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.auth.AuthState.AuthFlow;
import com.amx.jax.ui.auth.AuthState.AuthStep;
import com.amx.jax.ui.auth.CAuthEvent;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.LoggedInUsers;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.jax.ui.session.UserSession;

@Component
public class SessionService {

	private static final String USER_KEY_FORMAT = "%s#%s";

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	@Autowired
	private GuestSession guestSession;

	@Autowired
	private UserSession userSession;

	@Autowired
	private UserDeviceBean appDevice;

	@Autowired
	private HttpService httpService;

	public UserDeviceBean getAppDevice() {
		return appDevice;
	}

	public void setAppDevice(UserDeviceBean appDevice) {
		this.appDevice = appDevice;
	}

	@Autowired
	private TenantService tenantContext;

	@Autowired
	private AuditService auditService;

	public TenantService getTenantContext() {
		return tenantContext;
	}

	@Autowired
	LoggedInUsers loggedInUsers;

	public GuestSession getGuestSession() {
		return guestSession;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	/**
	 * authorize user based on customerModel
	 * 
	 * @param customerModel
	 * @param valid
	 */
	public void authorize(CustomerModel customerModel, Boolean valid) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				customerModel.getIdentityId(), customerModel.getPassword());

		customerModel.getCustomerId();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = this.customerAuthProvider.authenticate(token);
		userSession.setCustomerModel(customerModel);
		this.indexUser(authentication);
		userSession.setValid(valid);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (valid) {
			SessionEvent sessionEvent = new SessionEvent();
			sessionEvent.setUserKey(getUserKeyString());
			sessionEvent.setType(SessionEvent.Type.SESSION_AUTHED);
			auditService.log(sessionEvent);
		}

	}

	/**
	 * Validates/Invalidates curent Session.
	 * 
	 * @param valid
	 *            - true/false
	 * 
	 * 
	 */
	public void validate(Boolean valid) {
		userSession.setValid(valid);
	}

	/**
	 * True - if Current sesison is for validated user otherwise fasle.
	 * 
	 * @return
	 */
	public Boolean validatedUser() {
		return userSession.isValid();
	}

	private String getUserKeyString() {
		if (userSession.getCustomerModel() == null) {
			return null;
		}
		// BigDecimal customerId = userSession.getCustomerModel().getCustomerId();
		return String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().toString(), guestSession.getIdentity());

	}

	/**
	 * Creates Index for current user and session. which will be maintained across
	 * multiple deployments.
	 * 
	 */
	public void indexUser(Authentication authentication) {
		String userKeyString = getUserKeyString();
		if (userKeyString != null) {
			RLocalCachedMap<String, String> map = loggedInUsers.map();
			String uuidToken = UUID.randomUUID().toString();
			userSession.setUuidToken(uuidToken);
			map.fastPut(userKeyString, uuidToken);
		}
	}

	public void unIndexUser() {
		if (guestSession.getIdentity() != null) {
			// BigDecimal customerId = guestSession.getCustomerModel().getCustomerId();
			String userKeyString = String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().toString(),
					guestSession.getIdentity());
			if (userKeyString != null) {
				RLocalCachedMap<String, String> map = loggedInUsers.map();
				map.fastRemove(userKeyString);
				auditService.log(new CAuthEvent(AuthFlow.LOGOUT, AuthStep.LOCKED));
			}
		}
	}

	/**
	 * Check if user is index in SessionTable and his UUID key matches with Key in
	 * SessionTable, It returns true if user is indexed and has not started another
	 * session.
	 * 
	 * @return
	 */
	public Boolean indexedUser() {
		String userKeyString = getUserKeyString();
		if (userKeyString != null) {

			RLocalCachedMap<String, String> map = loggedInUsers.map();

			String uuidToken = userSession.getUuidToken();
			if (map.containsKey(userKeyString) && uuidToken != null) {
				String uuidTokenTemp = map.get(userKeyString);
				return uuidToken.equals(uuidTokenTemp);
			}
		}
		return Boolean.FALSE;
	}

	public boolean validateSessionUnique() {
		if (this.validatedUser() && !this.indexedUser()) {
			auditService.log(new CAuthEvent(AuthFlow.LOGOUT, AuthStep.MISSING));
			this.unauthorize();
			return false;
		}
		return true;
	}

	public void logout() {
		this.unauthorize();
	}

	/**
	 * Unauthorizes current user & deletes current session completely.
	 * 
	 */
	public void unauthorize() {
		String userKeyString = getUserKeyString();

		if (this.indexedUser()) {
			RLocalCachedMap<String, String> map = loggedInUsers.map();
			map.fastRemove(userKeyString);
			auditService.log(new CAuthEvent(AuthFlow.LOGOUT, AuthStep.UNAUTH));
		}

		SessionEvent sessionEvent = new SessionEvent();
		sessionEvent.setUserKey(userKeyString);
		sessionEvent.setType(SessionEvent.Type.SESSION_UNAUTHED);
		auditService.log(sessionEvent);

		this.clear();
		this.invalidate();
	}

	/**
	 * Invalidates session from spring's context.
	 * 
	 */
	public void invalidate() {
		SecurityContextHolder.getContext().setAuthentication(null);
		HttpSession session = request.getSession(false);
		SecurityContextHolder.clearContext();
		session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		httpService.clearSessionCookie();
	}

	/**
	 * Clear Existing user session if any. Does only cleaning part for compelte
	 * un-authorization use {@link #unauthorize()}
	 */
	public void clear() {
		LOGGER.info("Session is being cleared userId={}", userSession.getUserid());
		userSession.setValid(Boolean.FALSE);
		userSession.setCustomerModel(null);
		guestSession.setState(new AuthState());
		guestSession.setCustomerModel(null);
	}

}
