package com.amx.jax.logger.model;

import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.amx.jax.logger.events.SessionEvent;

@Document
public class SessionLog extends AbstractLogMessage {

	private String userType;

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@TextIndexed
	private String userId;

	public SessionLog(SessionEvent sessionEvent) {
		super(sessionEvent);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SessionLog{" + "customerId='" + userId + "'" + "} " + super.toString();
	}
}
