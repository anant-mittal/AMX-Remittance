package com.amx.jax.ui.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.UserClient;
import com.bootloaderjs.ContextUtil;

@Component
public class JaxService extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(JaxService.class);

	public static final String DEFAULT_LANGUAGE_ID = "1";

	public static final String DEFAULT_COUNTRY_ID = "91";

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

	public BeneClient getBeneClient() {
		return beneClient;
	}

	public void setBeneClient(BeneClient beneClient) {
		this.beneClient = beneClient;
	}

	public ExchangeRateClient getxRateClient() {
		return xRateClient;
	}

	public void setxRateClient(ExchangeRateClient xRateClient) {
		this.xRateClient = xRateClient;
	}

	public RemitClient getRemitClient() {
		return remitClient;
	}

	public void setRemitClient(RemitClient remitClient) {
		this.remitClient = remitClient;
	}

	public UserClient getUserclient() {
		return userclient;
	}

	public void setUserclient(UserClient userclient) {
		this.userclient = userclient;
	}

	public MetaClient getMetaClient() {
		return metaClient;
	}

	public void setMetaClient(MetaClient metaClient) {
		this.metaClient = metaClient;
	}

	@Autowired
	private MetaClient metaClient;

	public JaxMetaInfo jaxMeta() {
		return jaxMetaInfo;
	}

	public JaxService setDefaults() {

		jaxMetaInfo.setCountryId(new BigDecimal(JaxService.DEFAULT_COUNTRY_ID));
		jaxMetaInfo.setLanguageId(new BigDecimal(JaxService.DEFAULT_LANGUAGE_ID));
		jaxMetaInfo.setCompanyId(new BigDecimal(JaxService.DEFAULT_COMPANY_ID));
		jaxMetaInfo.setTraceId(ContextUtil.getTraceId());
		jaxMetaInfo.setCountryBranchId(new BigDecimal(JaxService.DEFAULT_COUNTRY_BRANCH_ID));

		if (sessionService.getUserSession().getCustomerModel() != null) {
			jaxMetaInfo.setCustomerId(sessionService.getUserSession().getCustomerModel().getCustomerId());
		} else if (sessionService.getGuestSession().getCustomerModel() != null) {
			jaxMetaInfo.setCustomerId(sessionService.getGuestSession().getCustomerModel().getCustomerId());
		}
		return this;
	}

}
