package com.amx.jax.postman.audit;

import java.util.List;

import com.amx.jax.logger.AuditEvent;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;

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

		// Sms Events
		SEND_SMS,
		// Email Events
		SEND_EMAIL,
		// WhatsApp Events
		SEND_WHATSAPP,
		// PDF Events
		CREATE_PDF,

		// NOTIFCATION Events
		NOTIFCATION, NOTIFCATION_ANDROID,
		/** The notifcation ios. */
		NOTIFCATION_IOS,
		/** The notifcation web. */
		NOTIFCATION_WEB,

		NOTIFCATION_SUBSCRIPTION;

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
	String template = null;

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
	 * @param sms
	 *            the sms
	 */
	public PMGaugeEvent(Type type, SMS sms) {
		super(type);
		this.set(sms);
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
		this.set(email);
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
		this.set(file);
	}

	/**
	 * Gets the template.
	 *
	 * @return the template
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 *
	 * @param template
	 *            the new template
	 */
	public void setTemplate(String template) {
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

	public PMGaugeEvent set(Result result) {
		this.result = result;
		return this;
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
	public PMGaugeEvent set(File file) {
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
	public PMGaugeEvent set(SMS sms) {
		this.template = sms.getTemplate();
		this.to = sms.getTo();
		return this;
	}

	public PMGaugeEvent set(SMS sms, String responseText) {
		this.template = sms.getTemplate();
		this.to = sms.getTo();
		this.responseText = responseText;
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
	public PMGaugeEvent set(Email email) {
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
	public AuditEvent set(PushMessage msg, String message, String responseText) {
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
