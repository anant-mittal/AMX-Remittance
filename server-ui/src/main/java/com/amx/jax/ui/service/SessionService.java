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
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.ui.beans.TenantBean;
import com.amx.jax.ui.config.CustomerAuthProvider;
import com.amx.jax.ui.session.GuestSession;
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
	private TenantBean tenantBean;

	public TenantBean getTenantBean() {
		return tenantBean;
	}

	@Autowired
	RedissonClient redisson;

	@SuppressWarnings("rawtypes")
	LocalCachedMapOptions localCacheOptions = LocalCachedMapOptions.defaults().evictionPolicy(EvictionPolicy.NONE)
			.cacheSize(1000).reconnectionStrategy(ReconnectionStrategy.NONE).syncStrategy(SyncStrategy.INVALIDATE)
			.timeToLive(10000).maxIdle(10000);

	@SuppressWarnings("unchecked")
	private RLocalCachedMap<String, String> getLoggedInUsers() {
		return redisson.getLocalCachedMap("LoggedInUsers", localCacheOptions);
	}

	public GuestSession getGuestSession() {
		return guestSession;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void authorize(CustomerModel customerModel, Boolean valid) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				customerModel.getIdentityId(), customerModel.getPassword());

		customerModel.getCustomerId();

		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authentication = this.customerAuthProvider.authenticate(token);
		userSession.setCustomerModel(customerModel);
		this.indexUser();
		userSession.setValid(true);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	public void validate(Boolean valid) {
		userSession.setValid(valid);
	}

	public Boolean validatedUser() {
		return userSession.isValid();
	}

	private String getUserKeyString() {
		if (userSession.getCustomerModel() == null) {
			return null;
		}
		BigDecimal customerId = userSession.getCustomerModel().getCustomerId();
		return String.format(USER_KEY_FORMAT, TenantContextHolder.currentSite().getId(), customerId.toString());

	}

	@SuppressWarnings("unchecked")
	public void indexUser() {
		String userKeyString = getUserKeyString();
		if (userKeyString != null) {
			RLocalCachedMap<String, String> map = this.getLoggedInUsers();
			String uuidToken = UUID.randomUUID().toString();
			userSession.setUuidToken(uuidToken);
			map.fastPut(userKeyString, uuidToken);
		}
	}

	@SuppressWarnings("unchecked")
	public Boolean indexedUser() {
		String userKeyString = getUserKeyString();
		if (userKeyString != null) {

			RLocalCachedMap<String, String> map = this.getLoggedInUsers();

			String uuidToken = userSession.getUuidToken();
			if (map.containsKey(userKeyString) && uuidToken != null) {
				String uuidTokenTemp = map.get(userKeyString);
				return uuidToken.equals(uuidTokenTemp);
			}
		}
		return Boolean.FALSE;
	}

	@SuppressWarnings("unchecked")
	public void unauthorize() {
		if (this.indexedUser()) {
			RLocalCachedMap<String, String> map = this.getLoggedInUsers();
			String userKeyString = getUserKeyString();
			map.fastRemove(userKeyString);
		}
		this.clear();
		SecurityContextHolder.getContext().setAuthentication(null);
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
