package com.amx.jax.postman.service;

import java.util.List;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;

/**
 * The Class PMGaugeEvent.
 */
public class PMGaugeEvent extends AuditEvent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6667775998834926934L;

	/**
	 * The Enum Type.
	 */
	public static enum Type implements EventType {

		/** The pm event. */
		PM_EVENT,

		/** The sms sent not. */
		// Sms Events
		SMS_SENT_NOT,
		/** The sms sent success. */
		SMS_SENT_SUCCESS,
		/** The sms sent error. */
		SMS_SENT_ERROR,

		/** The email sent not. */
		// Email Events
		EMAIL_SENT_NOT,
		/** The email sent success. */
		EMAIL_SENT_SUCCESS,
		/** The email sent error. */
		EMAIL_SENT_ERROR,

		/** The pdf created. */
		// PDF Events
		PDF_CREATED,
		/** The pdf error. */
		PDF_ERROR,

		/** The notifcation android. */
		// NOTIFCATION Events
		NOTIFCATION_ANDROID,
		/** The notifcation ios. */
		NOTIFCATION_IOS,
		/** The notifcation web. */
		NOTIFCATION_WEB;

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.amx.jax.logger.AbstractEvent.EventType#marker()
		 */
		@Override
		public EventMarker marker() {
			return null;
		}

	}

	/** The template. */
	Templates template = null;

	/** The to. */
	private List<String> to = null;

	/** The responseText. */
	private String responseText;

	/**
	 * Instantiates a new PM gauge event.
	 */
	public PMGaugeEvent() {
		super();
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 */
	public PMGaugeEvent(EventType type) {
		super(type);
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 * @param description
	 *            the description
	 * @param message
	 *            the message
	 */
	public PMGaugeEvent(Type type, String description, String message) {
		super(type, description, message);
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 * @param description
	 *            the description
	 */
	public PMGaugeEvent(Type type, String description) {
		super(type, description);
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 * @param sms
	 *            the sms
	 */
	public PMGaugeEvent(Type type, SMS sms) {
		super(type);
		this.fillDetail(type, sms);
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 * @param email
	 *            the email
	 */
	public PMGaugeEvent(Type type, Email email) {
		super(type);
		this.fillDetail(type, email);
	}

	/**
	 * Instantiates a new PM gauge event.
	 *
	 * @param type
	 *            the type
	 * @param file
	 *            the file
	 */
	public PMGaugeEvent(Type type, File file) {
		super(type);
		this.fillDetail(type, file);
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public Templates getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template
	 *            the new template
	 */
	public void setTemplate(Templates template) {
		this.template = template;
	}

	/**
	 * Gets the to.
	 *
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * Sets the to.
	 *
	 * @param to
	 *            the new to
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * Fill detail.
	 *
	 * @param type
	 *            the type
	 * @param file
	 *            the file
	 * @return the PM gauge event
	 */
	public PMGaugeEvent fillDetail(Type type, File file) {
		this.type = type;
		this.template = file.getTemplate();
		return this;
	}

	/**
	 * Fill detail.
	 *
	 * @param type
	 *            the type
	 * @param sms
	 *            the sms
	 * @return the PM gauge event
	 */
	public PMGaugeEvent fillDetail(Type type, SMS sms) {
		this.type = type;
		this.template = sms.getTemplate();
		this.to = sms.getTo();
		return this;
	}

	/**
	 * Fill detail.
	 *
	 * @param type
	 *            the type
	 * @param email
	 *            the email
	 * @return the PM gauge event
	 */
	public PMGaugeEvent fillDetail(Type type, Email email) {
		this.type = type;
		this.template = email.getTemplate();
		this.to = email.getTo();
		return this;
	}

	/**
	 * Fill detail.
	 *
	 * @param type
	 *            the type
	 * @param msg
	 *            the msg
	 * @param message
	 *            the message
	 * @param response
	 *            the response
	 * @return the audit event
	 */
	public AuditEvent fillDetail(Type type, PushMessage msg, String message, String responseText) {
		this.type = type;
		this.to = msg.getTo();
		this.message = message;
		this.responseText = responseText;
		return this;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

}
