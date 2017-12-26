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

	@SuppressWarnings("unchecked")
	public void indexUser() {

		if (userSession.getCustomerModel() == null) {
			return;
		}

		BigDecimal customerId = userSession.getCustomerModel().getCustomerId();
		RLocalCachedMap<String, String> map = redisson.getLocalCachedMap("LoggedInUsers", localCacheOptions);
		String userKeyString = String.format("{}#{}", TenantContextHolder.currentSite().getId(), customerId.toString());

		String uuidToken = UUID.randomUUID().toString();
		userSession.setUuidToken(uuidToken);
		map.fastPut(userKeyString, uuidToken);
	}

	public Boolean indexedUser() {

		if (userSession.getCustomerModel() == null) {
			return Boolean.FALSE;
		}
		BigDecimal customerId = userSession.getCustomerModel().getCustomerId();
		RLocalCachedMap<String, String> map = redisson.getLocalCachedMap("LoggedInUsers", localCacheOptions);
		String userKeyString = String.format("{}#{}", TenantContextHolder.currentSite().getId(), customerId.toString());

		String uuidToken = userSession.getUuidToken();
		if (map.containsKey(userKeyString) && uuidToken != null) {
			String uuidTokenTemp = map.get(userKeyString);
			return uuidToken.equals(uuidTokenTemp);
		}
		return false;
	}

	public void unauthorize() {
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
