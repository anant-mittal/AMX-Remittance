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
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.model.AuthState;
import com.amx.jax.model.AuthState.AuthFlow;
import com.amx.jax.model.AuthState.AuthStep;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.ui.audit.CAuthEvent;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.config.WebSecurityConfig;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.LoggedInUsers;
import com.amx.jax.ui.session.UserDeviceBean;
import com.amx.jax.ui.session.UserSession;

/**
 * The Class SessionService.
 */
@Component
public class SessionService {

	/** The Constant USER_KEY_FORMAT. */
	private static final String USER_KEY_FORMAT = "%s#%s";

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionService.class);

	/** The request. */
	@Autowired
	private HttpServletRequest request;

	/** The customer auth provider. */
	@Autowired
	private CustomerAuthProvider customerAuthProvider;

	/** The guest session. */
	@Autowired
	private GuestSession guestSession;

	/** The user session. */
	@Autowired
	private UserSession userSession;

	/** The app device. */
	@Autowired
	private UserDeviceBean appDevice;

	/** The http service. */
	@Autowired
	private CommonHttpRequest httpService;

	/**
	 * Gets the app device.
	 *
	 * @return the app device
	 */
	public UserDeviceBean getAppDevice() {
		return appDevice;
	}

	/**
	 * Sets the app device.
	 *
	 * @param appDevice the new app device
	 */
	public void setAppDevice(UserDeviceBean appDevice) {
		this.appDevice = appDevice;
	}

	/** The tenant context. */
	@Autowired
	private TenantService tenantContext;

	/** The audit service. */
	@Autowired
	private AuditService auditService;

	/**
	 * Gets the tenant context.
	 *
	 * @return the tenant context
	 */
	public TenantService getTenantContext() {
		return tenantContext;
	}

	/** The logged in users. */
	@Autowired
	LoggedInUsers loggedInUsers;

	/**
	 * Gets the guest session.
	 *
	 * @return the guest session
	 */
	public GuestSession getGuestSession() {
		return guestSession;
	}

	/**
	 * Gets the user session.
	 *
	 * @return the user session
	 */
	public UserSession getUserSession() {
		return userSession;
	}

	/**
	 * authorize user based on customerModel.
	 *
	 * @param customerModel the customer model
	 * @param valid         the valid
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

	}

	/**
	 * Validates/Invalidates curent Session.
	 * 
	 * @param valid - true/false
	 * 
	 * 
	 */
	public void validate(Boolean valid) {
		userSession.setValid(valid);
	}

	/**
	 * True - if Current sesison is for validated user otherwise fasle.
	 *
	 * @return the boolean
	 */
	public Boolean validatedUser() {
		return userSession.isValid();
	}

	/**
	 * Gets the user key string.
	 *
	 * @return the user key string
	 */
	private String getUserKeyString() {
		if (userSession.getCustomerModel() == null) {
			return null;
		}
		return String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().toString(), guestSession.getIdentity());

	}

	/**
	 * Creates Index for current user and session. which will be maintained across
	 * multiple deployments.
	 *
	 * @param authentication the authentication
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

	/**
	 * Un index user.
	 */
	public void unIndexUser() {
		if (guestSession.getIdentity() != null) {
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
	 * @return the boolean
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

	public boolean isRequestAuthorized() {

		if (WebSecurityConfig.isPublicUrl(request.getRequestURI())) {
			return true;
		}
		if (!this.getAppDevice().isAuthorized()) {
			auditService.log(new CAuthEvent(AuthFlow.LOGOUT, AuthStep.UNAUTH_DEVICE));
			this.unauthorize();
			return false;
		}
		if (!this.validateSessionUnique()) {
			auditService.log(new CAuthEvent(AuthFlow.LOGOUT, AuthStep.MISSING));
			this.unauthorize();
			return false;
		}
		return !WebSecurityConfig.isSecuredUrl(request.getRequestURI()) || this.validatedUser();
	}

	/**
	 * Validate session unique.
	 *
	 * @return true, if successful
	 */
	public boolean validateSessionUnique() {
		if (this.validatedUser() && !this.indexedUser()) {
			return false;
		}
		return true;
	}

	/**
	 * Logout.
	 */
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
