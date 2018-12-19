package com.amx.jax.postman.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.dict.Language;
import com.amx.jax.postman.model.ITemplates.ITemplate;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message implements Serializable {

	private static final long serialVersionUID = 1363933600245334964L;
	public static final String DATA_KEY = "data";
	public static final String RESULTS_KEY = "results";

	public static enum Status {
		INIT, SENT, DELIVERED, READ, FAILED
	}

	protected long timestamp;
	protected Language lang = null;
	protected String subject;
	protected String message = null;
	protected List<String> to = null;
	private String template = null;
	private Map<String, Object> model = new HashMap<String, Object>();
	private MessageType messageType = null;
	private Status status = null;

	private List<String> lines = new ArrayList<String>();

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	@JsonIgnore
	public void setObject(Object object) {
		this.model = JsonUtil.fromJson(JsonUtil.toJson(object), Map.class);
	}

	@JsonIgnore
	public void setModelData(Object object) {
		this.getModel().put(DATA_KEY, object);
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String text) {
		this.message = text;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@JsonIgnore
	public void setITemplate(ITemplate template) {
		this.template = template.toString();
	}

	@JsonIgnore
	public ITemplate getITemplate() {
		return ITemplates.getTemplate(this.template);
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public Message() {
		this.timestamp = System.currentTimeMillis();
		this.status = Status.INIT;
		this.to = new ArrayList<String>();
	}

	/**
	 * @return the to
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * @param to
	 *            the to to set
	 */
	public void addTo(String... recieverIds) {
		for (String recieverId : recieverIds) {
			this.to.add(recieverId);
		}
	}

	public void addLine(String... lines) {
		for (String line : lines) {
			this.lines.add(line);
		}
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
