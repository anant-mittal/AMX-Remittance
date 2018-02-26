package com.amx.jax.ui.service;

import java.math.BigDecimal;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.logger.AuditLoggerService;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.session.LoggedInUsers;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.session.GuestSession;
import com.amx.jax.ui.session.UserDevice;
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
	private UserDevice appDevice;

	public UserDevice getAppDevice() {
		return appDevice;
	}

	public void setAppDevice(UserDevice appDevice) {
		this.appDevice = appDevice;
	}

	@Autowired
	private TenantContext tenantContext;

	@Autowired
	private AuditLoggerService auditLoggerService;

	public TenantContext getTenantContext() {
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

		SessionEvent sessionEvent = new SessionEvent();
		if (valid) {
			auditLoggerService.log(sessionEvent);
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
		BigDecimal customerId = userSession.getCustomerModel().getCustomerId();
		return String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().toString(), customerId.toString());

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

	/**
	 * Unauthorizes current user & deletes current session completely.
	 * 
	 */
	public void unauthorize() {
		if (this.indexedUser()) {
			RLocalCachedMap<String, String> map = loggedInUsers.map();
			String userKeyString = getUserKeyString();
			map.fastRemove(userKeyString);
			LOGGER.info("User is being unauthorized from current session userKeyString={}", userKeyString);
		}
		this.clear();
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Clear Existing user session if any. Does only cleaning part for compelte
	 * un-authorization use {@link #unauthorize()}
	 */
	public void clear() {
		LOGGER.info("Session is being cleared userId={}", userSession.getUserid());
		userSession.setValid(Boolean.FALSE);
		userSession.setCustomerModel(null);
		userSession.setUserid(null);
		guestSession.setFlow(null);
		guestSession.setAuthStep(null);
		guestSession.setCustomerModel(null);
	}

}
