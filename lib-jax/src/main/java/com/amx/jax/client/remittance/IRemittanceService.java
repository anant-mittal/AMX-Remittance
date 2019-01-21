package com.amx.jax.client.remittance;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;

public interface IRemittanceService extends  IJaxService {

	
	public static class Path {
		public static final String PREFIX = "/branch/remittance/";
		//public static final String BR_REMITTANCE_USER_WISE_COUNT = PREFIX + "/total-count/";
		
	}

	public static class Params {
		public static final String TRNX_DATE = "transactiondate";
	}


}
