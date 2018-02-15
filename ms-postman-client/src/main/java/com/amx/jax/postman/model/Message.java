package com.amx.jax.postman.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Null;

import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Message {

	protected Langs lang = null;
	protected String subject;
	protected String message = null;
	private List<String> to;
	private Templates template = null;
	private Map<String, Object> model = new HashMap<String, Object>();

	private List<String> lines = new ArrayList<String>();

	protected Channel channel = Channel.DEFAULT;

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	@Null
	@JsonIgnore
	private String object;

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	@JsonIgnore
	public void setObject(Object object) {
		this.model = JsonUtil.toMap(object);
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

	public Templates getTemplate() {
		return template;
	}

	public void setTemplate(Templates template) {
		this.template = template;
	}

	public Langs getLang() {
		return lang;
	}

	public void setLang(Langs lang) {
		this.lang = lang;
	}

	public Message() {
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
}