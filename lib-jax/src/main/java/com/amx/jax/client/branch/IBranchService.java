package com.amx.jax.client.branch;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.cache.box.CustomerOnCall.CustomerCall;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

public interface IBranchService extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/branch/user/";
		public static final String BR_REMITTANCE_USER_WISE_COUNT = PREFIX + "/total-count/";
		public static final String BRANCH_USER_CUSTOMER_CALL_STATUS = "/branch-user/customer-call-status";
		public static final String BRANCH_USER_CUSTOMER_CALL_SESSION = "/branch-user/customer-call-session";
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
		public static final String SESSION_ID = "sessionId";
		static final String COMMENT2 = "comment";
		static final String MOBILE2 = "mobile";
		static final String AGENT_ID = "agentId";
		static final String STATUS2 = "status";
		static final String LEAD_ID = "leadId";
		static final String CUSTOMER_ID = "customerId";
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_CURRENCY_ID,
			JaxError.INVALID_COMPANY_ID })
	AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(String transactiondate);

	AmxApiResponse<CustomerCall, Object> customerCallSession(BigDecimal agentId, BigDecimal customerId, String mobile,
			String leadId);

	AmxApiResponse<CustomerCall, Object> customerCallStatus(BigDecimal agentId, String mobile, String sessionId,
			String leadId, String status, String comment, BigDecimal customerId);
}
