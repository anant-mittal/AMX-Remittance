package com.amx.jax.admin.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.BeneClient;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.RateAlertClient;
import com.amx.jax.client.RemitClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.dict.Language;
import com.amx.jax.scope.TenantContextHolder;
import com.bootloaderjs.ContextUtil;

@Component
public class JaxService extends AbstractJaxServiceClient {
	
	public static final BigDecimal DEFAULT_COUNTRY_ID = new BigDecimal(91);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

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
		jaxMetaInfo.setCountryId(DEFAULT_COUNTRY_ID);
		return this;
	}

}
