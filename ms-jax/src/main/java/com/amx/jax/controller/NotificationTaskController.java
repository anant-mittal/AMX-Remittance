package com.amx.jax.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.INotificationtaskService;
import com.amx.jax.client.task.NotificationTaskDto;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.NotificationTaskService;
import com.jax.amxlib.exception.jax.GlobaLException;

@RestController
public class NotificationTaskController implements INotificationtaskService {

	@Autowired
	NotificationTaskService notificationTaskService;
	@Autowired
	MetaData metaData;

	@RequestMapping(path = Path.NOTIFY_BU_FOR_CUSTOMER_DOC_UPLOAD, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(@Valid @RequestBody CustomerDocUploadNotificationTaskData data) {
		notificationTaskService.notifyBranchUserForDocumentUpload(data);
		return AmxApiResponse.build();
	}

	@RequestMapping(path = Path.LIST_USER_NOTIFICATION_TASKS, method = RequestMethod.GET)
	@Override
	public AmxApiResponse<NotificationTaskDto, Object> listUserNotificationTasks() {
		List<NotificationTaskDto> notifications = notificationTaskService.listUserNotificationTasks();
		return AmxApiResponse.buildList(notifications);
	}

}
