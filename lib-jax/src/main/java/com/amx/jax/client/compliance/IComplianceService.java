package com.amx.jax.client.compliance;

import com.amx.jax.api.AmxApiResponse;

public interface IComplianceService {
	public static class Path {

		public static final String LIST_HVT = "/list-high-value-trnx";
	}

	public static class Params {

		public static final String TRANSACTION_BLOCK_TYPE = "trnxBlockType";
	}

	AmxApiResponse<HighValueTrnxDto, Object> listHighValueTransaction(ComplianceBlockedTrnxType trnxType);

}
