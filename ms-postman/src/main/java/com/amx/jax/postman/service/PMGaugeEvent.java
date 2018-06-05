package com.amx.jax.postman.service;

import java.util.List;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.utils.EnumType;

public class PMGaugeEvent extends AuditEvent {

	public static enum Type implements EnumType {
		PM_EVENT,
		// Sms Events
		SMS_SENT_NOT, SMS_SENT_SUCCESS, SMS_SENT_ERROR,
		// Email Events
		EMAIL_SENT_NOT, EMAIL_SENT_SUCCESS, EMAIL_SENT_ERROR,
		// PDF Events
		PDF_CREATED, PDF_ERROR
	}

	Templates template = null;
	private List<String> to = null;
	private Object data = null;

	public PMGaugeEvent(EnumType type) {
		super(type);
	}

	public PMGaugeEvent(Type type, String description, String message) {
		super(type, description, message);
	}

	public PMGaugeEvent(Type type, String description) {
		super(type, description);
	}

	public PMGaugeEvent(Type type, SMS sms) {
		super(type);
		this.template = sms.getTemplate();
		this.to = sms.getTo();
	}

	public PMGaugeEvent(Type type, Email email) {
		super(type);
		this.template = email.getTemplate();
		this.to = email.getTo();
	}

	public PMGaugeEvent(Type type, File file) {
		super(type);
		this.template = file.getTemplate();
		if (type == Type.PDF_ERROR) {
			this.data = file.getModel();
		}
	}

	public Templates getTemplate() {
		return template;
	}

	public void setTemplate(Templates template) {
		this.template = template;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
