package com.amx.jax.ui.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.CustomerRegistrationClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.JaxFieldClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class JaxService extends AbstractJaxServiceClient {

	public static final String DEFAULT_COMPANY_ID = "1";

	public static final String DEFAULT_CURRENCY_ID = "1";

	public static final String DEFAULT_COUNTRY_BRANCH_ID = "78"; // online

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

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
	CustomerRegistrationClient customerRegistrationClient;

	public JaxFieldClient getJaxFieldClient() {
		return jaxFieldClient;
	}

	public RateAlertClient getRateAlertClient() {
		return rateAlertClient;
	}

	public BeneClient getBeneClient() {
		return beneClient;
	}

	public ExchangeRateClient getxRateClient() {
		return xRateClient;
	}

	public RemitClient getRemitClient() {
		return remitClient;
	}

	public UserClient getUserclient() {
		return userclient;
	}

	public MetaClient getMetaClient() {
		return metaClient;
	}

	public CustomerRegistrationClient getCustRegClient() {
		return customerRegistrationClient;
	}

	@Autowired
	private MetaClient metaClient;

	public JaxMetaInfo jaxMeta() {
		return jaxMetaInfo;
	}

	public JaxService setDefaults(BigDecimal customerId) {
		jaxMetaInfo.setCountryId(TenantContextHolder.currentSite().getBDCode());
		jaxMetaInfo.setTenant(TenantContextHolder.currentSite());
		jaxMetaInfo.setLanguageId(sessionService.getGuestSession().getLang().getBDCode());

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

	public JaxService setDefaults() {
		if (sessionService.getUserSession().getCustomerModel() != null) {
			return this.setDefaults(sessionService.getUserSession().getCustomerModel().getCustomerId());
		} else if (sessionService.getGuestSession().getCustomerModel() != null) {
			return this.setDefaults(sessionService.getGuestSession().getCustomerModel().getCustomerId());
		}
		return this.setDefaults(null);
	}

}
