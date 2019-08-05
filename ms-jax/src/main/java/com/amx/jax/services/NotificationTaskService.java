package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amx.jax.client.task.CustomerDocUploadNotificationTaskData;
import com.amx.utils.JsonUtil;

@Service
public class NotificationTaskService {

	private static final Logger log = LoggerFactory.getLogger(NotificationTaskService.class);

	public void notifyBranchUserForDocumentUpload(CustomerDocUploadNotificationTaskData data) {
		log.debug("notifyBranchUserForDocumentUpload service data: {} ", JsonUtil.toJson(data));
	}

}
