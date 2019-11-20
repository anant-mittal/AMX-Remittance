package com.amx.jax.client.compliance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.customer.CustomerManagementClient;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;
import com.amx.jax.rest.RestService;

@Component
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

	@Override
	public AmxApiResponse<BoolRespModel, Object> approveTrnxDoc(ApproveDocRequest request) {
		try {
			LOGGER.debug("in approveTrnxDoc :  ");
			String url = appConfig.getJaxURL() + Path.APPROVE_TRANSACTOIN_DOCUMENT;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in approveTrnxDoc : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> rejectTrnxDoc(RejectDocRequest request) {
		try {
			LOGGER.debug("in rejectTrnxDoc :  ");
			String url = appConfig.getJaxURL() + Path.REJECT_TRANSACTOIN_DOCUMENT;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in rejectTrnxDoc : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateCustomer(DeactivateCustomerRequest request) {
		try {
			LOGGER.debug("in deactivateCustomer :  ");
			String url = appConfig.getJaxURL() + Path.DEACTIVATE_CUSTOMER;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(request)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in deactivateCustomer : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
