package com.amx.jax.client.task;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;

public interface INotificationtaskService {
	public static class Path {

		public static final String NOTIFY_BU_FOR_CUSTOMER_DOC_UPLOAD = "/notify-bu-cust-doc-upload";
		public static final String LIST_USER_NOTIFICATION_TASKS = "/list-user-notification-tasks";
	}

	public static class Params {

		public static final String TRANSACTION_BLOCK_TYPE = "trnxBlockType";
		public static final String TRANSACTION_ID = "trnxId";
	}

	AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data);

	AmxApiResponse<NotificationTaskDto, Object> listUserNotificationTasks();

}
