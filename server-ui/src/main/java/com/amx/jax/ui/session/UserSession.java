package com.amx.jax.ui.session;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.AuditActor;

/**
 * The Class UserSession.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {

	private static final long serialVersionUID = -6354887590466374869L;

	private boolean valid = false;

	private String uuidToken = null;

	private String referrer = null;

	/**
	 * Sets the referrer.
	 *
	 * @param referrer the new referrer
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * Gets the referrer.
	 *
	 * @return the referrer
	 */
	public String getReferrer() {
		return referrer;
	}

	/**
	 * Gets the uuid token.
	 *
	 * @return the uuid token
	 */
	public String getUuidToken() {
		return uuidToken;
	}

	/**
	 * Sets the uuid token.
	 *
	 * @param uuidToken the new uuid token
	 */
	public void setUuidToken(String uuidToken) {
		this.uuidToken = uuidToken;
	}

	private CustomerModel customerModel = null;

	/**
	 * Gets the customer model.
	 *
	 * @return the customer model
	 */
	public CustomerModel getCustomerModel() {
		return customerModel;
	}

	/**
	 * Sets the customer model.
	 *
	 * @param customerModel the new customer model
	 */
	public void setCustomerModel(CustomerModel customerModel) {
		this.customerModel = customerModel;
		if (customerModel != null) {
			AppContextUtil.setActorId(new AuditActor(AuditActor.ActorType.C, customerModel.getCustomerId()));
		}
	}

	/**
	 * Gets the userid.
	 *
	 * @return the userid
	 */
	public BigDecimal getUserid() {
		if (this.customerModel != null) {
			return this.customerModel.getCustomerId();
		}
		return null;
	}

	/**
	 * Checks if is valid.
	 *
	 * @return true, if is valid
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * Sets the valid.
	 *
	 * @param valid the new valid
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

}