package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.JaxMetaInfo;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.UserClient;
import com.amx.jax.ui.model.UserSessionInfo;

@Component
public class JaxService extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(JaxService.class);

	public static final String DEFAULT_LANGUAGE_ID = "1";

	public static final String DEFAULT_COUNTRY_ID = "91";

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private UserSessionInfo userSessionInfo;

	@Autowired
	private UserClient userclient;

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
		jaxMetaInfo.setCountryId(new Integer(JaxService.DEFAULT_COUNTRY_ID));
		if (userSessionInfo.getCustomerModel() != null) {
			jaxMetaInfo.setCustomerId(userSessionInfo.getCustomerModel().getCustomerId());
		}
		return this;
	}

}
