package com.amx.jax.client.task;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;

public interface INotificationtaskService {
	public static class Path {

		public static final String NOTIFY_BU_FOR_CUSTOMER_DOC_UPLOAD = "/notify-bu-cust-doc-upload";
	}

	public static class Params {

		public static final String TRANSACTION_BLOCK_TYPE = "trnxBlockType";
		public static final String TRANSACTION_ID = "trnxId";
	}

	AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data);

}
