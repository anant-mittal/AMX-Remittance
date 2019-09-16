package com.amx.jax.client;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.jax.client.task.INotificationtaskService;
import com.amx.jax.client.task.NotificationTaskDto;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.rest.RestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

@Component
public class NotificationTaskClient implements INotificationtaskService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTaskClient.class);

	@Autowired
	RestService restService;
	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<BoolRespModel, Object> notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data) {
		try {
			LOGGER.debug("in notifyBranchUserForDocumentUpload :  ");
			String url = appConfig.getJaxURL() + Path.NOTIFY_BU_FOR_CUSTOMER_DOC_UPLOAD;
			return restService.ajax(url).meta(new JaxMetaInfo()).post(data)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in notifyBranchUserForDocumentUpload : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

	@Override
	public AmxApiResponse<NotificationTaskDto, Object> listUserNotificationTasks() {
		try {
			LOGGER.debug("in listUserNotificationTasks :  ");
			String url = appConfig.getJaxURL() + Path.LIST_USER_NOTIFICATION_TASKS;
			return restService.ajax(url).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<NotificationTaskDto, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error("exception in listUserNotificationTasks : ", e);
			return JaxSystemError.evaluate(e);
		}
	}

}
