package com.amx.jax.ui.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.JaxFieldClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.PlaceOrderClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

/**
 * The Class JaxService.
 */
@Component
public class JaxService extends AbstractJaxServiceClient {

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String DEFAULT_COMPANY_ID = "1";

	public static final String DEFAULT_CURRENCY_ID = "1";

	public static final String DEFAULT_COUNTRY_BRANCH_ID = "78"; // online

	@Autowired
	private SessionService sessionService;

	@Autowired
	private UserClient userclient;

	@Autowired
	private RemitClient remitClient;

	@Autowired
	private ExchangeRateClient xRateClient;

	@Autowired
	private BeneClient beneClient;

	@Autowired
	private RateAlertClient rateAlertClient;

	@Autowired
	private JaxFieldClient jaxFieldClient;

	@Autowired
	PlaceOrderClient placeOrderClient;

	@Autowired
	CustomerRegistrationClient customerRegistrationClient;

	/**
	 * Gets the jax field client.
	 *
	 * @return the jax field client
	 */
	public JaxFieldClient getJaxFieldClient() {
		return jaxFieldClient;
	}

	/**
	 * Gets the rate alert client.
	 *
	 * @return the rate alert client
	 */
	public RateAlertClient getRateAlertClient() {
		return rateAlertClient;
	}

	/**
	 * Gets the bene client.
	 *
	 * @return the bene client
	 */
	public BeneClient getBeneClient() {
		return beneClient;
	}

	/**
	 * Gets the x rate client.
	 *
	 * @return the x rate client
	 */
	public ExchangeRateClient getxRateClient() {
		return xRateClient;
	}

	/**
	 * Gets the remit client.
	 *
	 * @return the remit client
	 */
	public RemitClient getRemitClient() {
		return remitClient;
	}

	/**
	 * Gets the userclient.
	 *
	 * @return the userclient
	 */
	public UserClient getUserclient() {
		return userclient;
	}

	/**
	 * Gets the meta client.
	 *
	 * @return the meta client
	 */
	public MetaClient getMetaClient() {
		return metaClient;
	}

	/**
	 * Gets the cust reg client.
	 *
	 * @return the cust reg client
	 */
	public CustomerRegistrationClient getCustRegClient() {
		return customerRegistrationClient;
	}

	/**
	 * Gets the place order client.
	 *
	 * @return the place order client
	 */
	public PlaceOrderClient getPlaceOrderClient() {
		return placeOrderClient;
	}

	/** The meta client. */
	@Autowired
	private MetaClient metaClient;

	/**
	 * Jax meta.
	 *
	 * @return the jax meta info
	 */
	public JaxMetaInfo jaxMeta() {
		return jaxMetaInfo;
	}

	/**
	 * Sets the defaults.
	 *
	 * @param customerId
	 *            the customer id
	 * @return the jax service
	 */
	public JaxService setDefaults(BigDecimal customerId) {
		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(sessionService.getGuestSession().getLanguage().getBDCode());

		jaxMetaInfo.setCompanyId(new BigDecimal(JaxService.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxService.DEFAULT_COUNTRY_BRANCH_ID));
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());
		jaxMetaInfo.setReferrer(sessionService.getUserSession().getReferrer());
		jaxMetaInfo.setDeviceId(sessionService.getAppDevice().getFingerprint());
		jaxMetaInfo.setDeviceIp(sessionService.getAppDevice().getIp());
		jaxMetaInfo.setDeviceType(ArgUtil.parseAsString(sessionService.getAppDevice().getType()));
		jaxMetaInfo.setAppType(ArgUtil.parseAsString(sessionService.getAppDevice().getAppType()));

		jaxMetaInfo.setCustomerId(customerId);

		return this;
	}

	/**
	 * Sets the defaults.
	 *
	 * @return the jax service
	 */
	public JaxService setDefaults() {
		if (sessionService.getUserSession().getCustomerModel() != null) {
			return this.setDefaults(sessionService.getUserSession().getCustomerModel().getCustomerId());
		} else if (sessionService.getGuestSession().getCustomerModel() != null) {
			return this.setDefaults(sessionService.getGuestSession().getCustomerModel().getCustomerId());
		}
		return this.setDefaults(null);
	}

}
