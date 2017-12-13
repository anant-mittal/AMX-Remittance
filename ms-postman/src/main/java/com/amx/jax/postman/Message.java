package com.amx.jax.postman;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bootloaderjs.Utils;

public class Message {

	private List<String> to;

	private String template = null;

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setTemplate(Templates template) {
		this.template = template.getFileName();
	}

	private Map<String, Object> model = new HashMap<String, Object>();

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
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
	public void setTo(String recieverId) {
		this.to.add(recieverId);
	}

	public String getToAsList() {
		return Utils.concatenate(this.to, ",");
	}
}