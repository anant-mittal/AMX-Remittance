package com.amx.jax.logger.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.TextScore;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.EnumType;

public class AbstractLogMessage extends AuditEvent {

	@Id
	private String id;

	@TextIndexed
	private String moduleName;

	@TextIndexed
	private String message;

	@TextScore
	private Float score;

	private String loggerName;

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public AbstractLogMessage(AuditEvent event) {
		this.timestamp = event.getTimestamp();
		this.moduleName = event.getComponent();
		this.type = event.getType();
		this.message = event.getMessage();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public EnumType getType() {
		return type;
	}

	public void setType(EnumType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "AbstractLogMessage{" + "id=" + id + ", message='" + message + '\'' + ", time=" + timestamp
				+ ", moduleName='" + moduleName + '\'' + ", loggerName='" + loggerName + '\'' + ", score=" + score
				+ '}';
	}

}
