package com.amx.jax.open.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.AuthState;
import com.amx.jax.user.UserDevice;

/**
 * The Class UserMetaData.
 */
public class EmployeeMetaData extends AbstractModel {

	private static final long serialVersionUID = 1243745569228714127L;

	public Boolean validSession = false;

	public Boolean active = false;

	UserDevice device = null;

	AuthState state = null;

	Tenant tenant = null;

	String tenantCode = null;

	Language lang = null;

	String cdnUrl = null;

	List<String> subscriptions = new ArrayList<String>();

	String notifyRangeLong = null;

	String notifyRangeShort = null;

	BigDecimal customerId = null;

	String notificationGap = null;

	String returnUrl = null;

	/**
	 * Gets the tenant.
	 *
	 * @return the tenant
	 */
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * Sets the tenant.
	 *
	 * @param tenant
	 *            the new tenant
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public AuthState getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state
	 *            the new state
	 */
	public void setState(AuthState state) {
		this.state = state;
	}

	/**
	 * Gets the device.
	 *
	 * @return the device
	 */
	public UserDevice getDevice() {
		return device;
	}

	/**
	 * Sets the device.
	 *
	 * @param device
	 *            the new device
	 */
	public void setDevice(UserDevice device) {
		this.device = device;
	}

	/**
	 * Gets the valid session.
	 *
	 * @return the valid session
	 */
	public Boolean getValidSession() {
		return validSession;
	}

	/**
	 * Sets the valid session.
	 *
	 * @param validSession
	 *            the new valid session
	 */
	public void setValidSession(Boolean validSession) {
		this.validSession = validSession;
	}

	/**
	 * Gets the active.
	 *
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * Sets the active.
	 *
	 * @param active
	 *            the new active
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public Language getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang
	 *            the new lang
	 */
	public void setLang(Language lang) {
		this.lang = lang;
	}

	/**
	 * Gets the cdn url.
	 *
	 * @return the cdn url
	 */
	public String getCdnUrl() {
		return cdnUrl;
	}

	/**
	 * Sets the cdn url.
	 *
	 * @param cdnUrl
	 *            the new cdn url
	 */
	public void setCdnUrl(String cdnUrl) {
		this.cdnUrl = cdnUrl;
	}

	/**
	 * Gets the subscriptions.
	 *
	 * @return the subscriptions
	 */
	public List<String> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * Gets the tenant code.
	 *
	 * @return the tenant code
	 */
	public String getTenantCode() {
		return tenantCode;
	}

	/**
	 * Sets the tenant code.
	 *
	 * @param tenantCode
	 *            the new tenant code
	 */
	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}

	/**
	 * Gets the return url.
	 *
	 * @return the return url
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * Sets the return url.
	 *
	 * @param returnUrl
	 *            the new return url
	 */
	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
