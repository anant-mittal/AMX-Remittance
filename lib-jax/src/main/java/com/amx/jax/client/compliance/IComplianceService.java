package com.amx.jax.client.compliance;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.customer.ComplianceTrnxDocumentInfo;

public interface IComplianceService {
	public static class Path {

		public static final String LIST_HVT = "/list-high-value-trnx";
		public static final String GET_TRANSACTION_DOCUMENT = "/view-trnx-doc";
		public static final String APPROVE_TRANSACTOIN_DOCUMENT = "/approve-trnx-doc";
		public static final String REJECT_TRANSACTOIN_DOCUMENT = "/reject-trnx-doc";
	}

	public static class Params {

		public static final String TRANSACTION_BLOCK_TYPE = "trnxBlockType";
		public static final String TRANSACTION_ID = "trnxId";
	}

	AmxApiResponse<HighValueTrnxDto, Object> listHighValueTransaction(ComplianceBlockedTrnxType trnxType);

	AmxApiResponse<ComplianceTrnxDocumentInfo, Object> getTransactionDocuments(Long trnxId);

	AmxApiResponse<BoolRespModel, Object> approveTrnxDoc(ApproveDocRequest request);

}
