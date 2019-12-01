package com.amx.jax.client.task;

import java.util.Date;

public class NotificationTaskDto {

	String message;
	Date creationDate;
	String requestId;
	Object data;
	Object messageData;
	JaxNotificationTaskType taskType;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Object getMessageData() {
		return messageData;
	}

	public void setMessageData(Object messageData) {
		this.messageData = messageData;
	}

	public JaxNotificationTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(JaxNotificationTaskType taskType) {
		this.taskType = taskType;
	}

}