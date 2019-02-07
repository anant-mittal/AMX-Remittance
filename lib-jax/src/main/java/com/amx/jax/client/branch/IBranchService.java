package com.amx.jax.client.branch;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

public interface IBranchService extends IJaxService{

	
	
	public static class Path {
		public static final String PREFIX = "/branch/user/";
		public static final String BR_REMITTANCE_USER_WISE_COUNT = PREFIX + "/total-count/";
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
	}

	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_CURRENCY_ID,JaxError.INVALID_COMPANY_ID })
	AmxApiResponse<UserwiseTransactionDto, Object> getTotalCount(String transactiondate);
}
