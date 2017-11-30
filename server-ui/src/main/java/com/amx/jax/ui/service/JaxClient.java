package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.AbstractJaxServiceClient;
import com.amx.jax.client.JaxMetaInfo;
import com.amx.jax.ui.model.UserSessionInfo;

@Component
public class JaxClient extends AbstractJaxServiceClient {

	private Logger log = Logger.getLogger(JaxClient.class);

	public static final String DEFAULT_LANGUAGE_ID = "1";

	public static final String DEFAULT_COUNTRY_ID = "91";

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private UserSessionInfo userSessionInfo;

	public JaxMetaInfo jaxMeta() {
		return jaxMetaInfo;
	}

	public void setDefaults() {
		jaxMetaInfo.setCountryId(new Integer(JaxClient.DEFAULT_COUNTRY_ID));
		if (userSessionInfo.getCustomerModel() != null) {
			jaxMetaInfo.setCustomerId(userSessionInfo.getCustomerModel().getCustomerId());
		}
	}

}
