package com.amx.jax.branch.controller;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.NotificationTaskClient;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.NotificationTaskDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Compliance  APIs")
public class BranchNotificationsController {

	@Autowired
	private NotificationTaskClient notificationTaskClient;

	@RequestMapping(value = "/api/notifications/list", method = { RequestMethod.GET })
	public AmxApiResponse<NotificationTaskDto, Object> getNotificationList() {
		return notificationTaskClient.listUserNotificationTasks();
	}

	@RequestMapping(value = "/api/notifications/send", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(
			@RequestBody CustomerDocUploadNotificationTaskData data) {
		return notificationTaskClient.notifyBranchUserForDocumentUpload(data);
	}

}
