package com.amx.jax.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.INotificationtaskService;
import com.amx.jax.services.NotificationTaskService;

@RestController
public class NotificationTaskController implements INotificationtaskService {

	@Autowired
	NotificationTaskService notificationTaskService;

	@RequestMapping(path = Path.NOTIFY_BU_FOR_CUSTOMER_DOC_UPLOAD, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(@RequestBody CustomerDocUploadNotificationTaskData data) {
		notificationTaskService.notifyBranchUserForDocumentUpload(data);
		return AmxApiResponse.build();
	}

}
