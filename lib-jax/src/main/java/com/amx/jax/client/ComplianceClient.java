package com.amx.jax.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.client.compliance.HighValueTrnxDto;
import com.amx.jax.client.compliance.IComplianceService;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.customer.CustomerManagementClient;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;
import com.amx.jax.rest.RestService;

public class ComplianceClient implements IComplianceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagementClient.class);

	@Autowired
	RestService restService;
	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<HighValueTrnxDto, Object> listHighValueTransaction(ComplianceBlockedTrnxType trnxType) {
		try {
			LOGGER.debug("in listHighValueTransaction :  ");
			String url = appConfig.getJaxURL() + Path.LIST_HVT;
			return restService.ajax(url).meta(new JaxMetaInfo()).queryParam(Params.TRANSACTION_BLOCK_TYPE, trnxType).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<HighValueTrnxDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listHighValueTransaction : ", e);
			return JaxSystemError.evaluate(e);
		}

	}

	@Override
	public AmxApiResponse<ComplianceTrnxDocumentInfo, Object> getTransactionDocuments(Long trnxId) {
		try {
			LOGGER.debug("in getTransactionDocuments :  ");
			String url = appConfig.getJaxURL() + Path.GET_TRANSACTION_DOCUMENT;
			return restService.ajax(url).meta(new JaxMetaInfo()).queryParam(Params.TRANSACTION_ID, trnxId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ComplianceTrnxDocumentInfo, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listHighValueTransaction : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
