package com.amx.jax.client.branch;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.rest.RestService;

@Component
public class BranchUserClient implements IBranchService {

	private static final Logger LOGGER = Logger.getLogger(BranchUserClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(String transactiondate) {
		
			String url = appConfig.getJaxURL() + Path.BR_REMITTANCE_USER_WISE_COUNT;
			return restService.ajax(url).meta(new JaxMetaInfo()).queryParam(Params.TRNX_DATE, transactiondate).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<UserwiseTransactionDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<CustomerCall, Object> customerCallSession(BigDecimal agentId, BigDecimal customerId,
			BigDecimal leadId) {
		return restService.ajax(appConfig.getJaxURL() + Path.BRANCH_USER_CUSTOMER_CALL_SESSION).meta(new JaxMetaInfo())
				.field(Params.AGENT_ID, agentId)
				.field(Params.CUSTOMER_ID, customerId)
				.field(Params.LEAD_ID, leadId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<CustomerCall, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<CustomerCall, Object> customerCallStatus(BigDecimal agentId, BigDecimal customerId,
			BigDecimal leadId, String followUpCode, String remark, String sessionId) {
		return restService.ajax(appConfig.getJaxURL() + Path.BRANCH_USER_CUSTOMER_CALL_STATUS).meta(new JaxMetaInfo())
				.field(Params.AGENT_ID, agentId)
				.field(Params.SESSION_ID, sessionId)
				.field(Params.LEAD_ID, leadId)
				.field(Params.STATUS2, followUpCode)
				.field(Params.COMMENT2, remark)
				.field(Params.CUSTOMER_ID, customerId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<CustomerCall, Object>>() {
				});
	}

}
