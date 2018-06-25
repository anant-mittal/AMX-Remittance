package com.amx.jax.ui.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.dict.Language;
import com.amx.jax.dict.Tenant;
import com.amx.jax.ui.auth.AuthState;
import com.amx.jax.ui.service.HotPointService.HotPoints;
import com.amx.jax.user.UserDevice;

/**
 * The Class UserMetaData.
 */
public class UserMetaData extends AbstractModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1243745569228714127L;

	/** The valid session. */
	public Boolean validSession = false;

	/** The active. */
	public Boolean active = false;

	/** The info. */
	PersonInfo info = null;

	/** The dom currency. */
	CurrencyMasterDTO domCurrency = null;

	/** The device. */
	UserDevice device = null;

	/** The state. */
	AuthState state = null;

	/** The tenant. */
	Tenant tenant = null;

	/** The tenant code. */
	String tenantCode = null;

	/** The lang. */
	Language lang = null;

	/** The config. */
	JaxMetaParameter config = null;

	/** The cdn url. */
	String cdnUrl = null;

	/** The features. */
	// List<String> features = new ArrayList<String>();
	String[] features = null;

	/** The subscriptions. */
	List<String> subscriptions = new ArrayList<String>();

	/** The notify range long. */
	String notifyRangeLong = null;

	/** The notify range short. */
	String notifyRangeShort = null;

	/** The customer id. */
	BigDecimal customerId = null;

	/** The notification gap. */
	String notificationGap = null;

	/** The return url. */
	String returnUrl = null;

	/** The hot points. */
	HotPoints[] hotPoints = HotPoints.values();

	/**
	 * Gets the config.
	 *
	 * @return the config
	 */
	public JaxMetaParameter getConfig() {
		return config;
	}

	/**
	 * Sets the config.
	 *
	 * @param config
	 *            the new config
	 */
	public void setConfig(JaxMetaParameter config) {
		this.config = config;
	}

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
	 * Gets the dom currency.
	 *
	 * @return the dom currency
	 */
	public CurrencyMasterDTO getDomCurrency() {
		return domCurrency;
	}

	/**
	 * Sets the dom currency.
	 *
	 * @param domCurrency
	 *            the new dom currency
	 */
	public void setDomCurrency(CurrencyMasterDTO domCurrency) {
		this.domCurrency = domCurrency;
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
	 * Sets the info.
	 *
	 * @param personinfo
	 *            the new info
	 */
	public void setInfo(PersonInfo personinfo) {
		this.info = personinfo;
	}

	/**
	 * Gets the info.
	 *
	 * @return the info
	 */
	public PersonInfo getInfo() {
		return this.info;
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
	 * Gets the features.
	 *
	 * @return the features
	 */
	public String[] getFeatures() {
		return features;
	}

	/**
	 * Sets the features.
	 *
	 * @param features
	 *            the new features
	 */
	public void setFeatures(String[] features) {
		this.features = features;
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
	 * Sets the subscriptions.
	 *
	 * @param subscriptions
	 *            the new subscriptions
	 */
	public void setSubscriptions(List<String> subscriptions) {
		this.subscriptions = subscriptions;
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
	 * Gets the hot points.
	 *
	 * @return the hot points
	 */
	public HotPoints[] getHotPoints() {
		return hotPoints;
	}

	/**
	 * Sets the hot points.
	 *
	 * @param hotPoints
	 *            the new hot points
	 */
	public void setHotPoints(HotPoints[] hotPoints) {
		this.hotPoints = hotPoints;
	}

	/**
	 * Gets the notify range long.
	 *
	 * @return the notify range long
	 */
	public String getNotifyRangeLong() {
		return notifyRangeLong;
	}

	/**
	 * Sets the notify range long.
	 *
	 * @param notifyRangeLong
	 *            the new notify range long
	 */
	public void setNotifyRangeLong(String notifyRangeLong) {
		this.notifyRangeLong = notifyRangeLong;
	}

	/**
	 * Gets the notify range short.
	 *
	 * @return the notify range short
	 */
	public String getNotifyRangeShort() {
		return notifyRangeShort;
	}

	/**
	 * Sets the notify range short.
	 *
	 * @param notifyRangeShort
	 *            the new notify range short
	 */
	public void setNotifyRangeShort(String notifyRangeShort) {
		this.notifyRangeShort = notifyRangeShort;
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

	/**
	 * Gets the customer id.
	 *
	 * @return the customer id
	 */
	public BigDecimal getCustomerId() {
		return customerId;
	}

	/**
	 * Sets the customer id.
	 *
	 * @param customerId
	 *            the new customer id
	 */
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	/**
	 * Gets the notification gap.
	 *
	 * @return the notification gap
	 */
	public String getNotificationGap() {
		return notificationGap;
	}

	/**
	 * Sets the notification gap.
	 *
	 * @param notificationGap
	 *            the new notification gap
	 */
	public void setNotificationGap(String notificationGap) {
		this.notificationGap = notificationGap;
	}

}
