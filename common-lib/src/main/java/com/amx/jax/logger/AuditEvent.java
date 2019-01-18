package com.amx.jax.logger;

import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.amx.jax.exception.IExceptionEnum;
import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "description", "component", "category", "type", "timestamp", "message" })
public abstract class AuditEvent extends AbstractEvent {

	private static final long serialVersionUID = -1539116953165424464L;
	protected Result result;
	protected IExceptionEnum errorCode;
	protected long tranxTime;
	protected long traceTime;
	protected long eventTime;
	protected String description = null;
	protected String message;
	protected String exception;
	protected String exceptionType;
	protected String actorId;
	protected Object data;
	protected UserDeviceClient client;

	public static enum Result {
		DONE, REJECTED, FAIL, ERROR, PASS;
	}

	public AuditEvent() {
		super();
		this.result = Result.DONE;
	}

	public AuditEvent(EventType type, Result result) {
		super(type);
		this.result = result;
	}

	public AuditEvent(EventType type) {
		this(type, Result.DONE);
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDescription() {
		if (this.description == null) {
			return String.format("%s_%s", this.type, this.result);
		}
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getTranxTime() {
		return tranxTime;
	}

	public void setTranxTime(long tranxTime) {
		this.tranxTime = tranxTime;
	}

	public long getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(long traceTime) {
		this.traceTime = traceTime;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public IExceptionEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(IExceptionEnum errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public void clean() {

	}

	public UserDeviceClient getClient() {
		return client;
	}

	public void setClient(UserDeviceClient client) {
		this.client = client;
	}

	public AuditEvent result(Result result) {
		this.setResult(result);
		return this;
	}

	public AuditEvent message(Object message) {
		this.setMessage(ArgUtil.parseAsString(message));
		return this;
	}

}
