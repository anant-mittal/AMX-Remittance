package com.amx.jax.postman;

import java.util.ArrayList;
import java.util.List;

public class SMS {

	private List<String> to;

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public void setTo(String to) {
		this.to.add(to);
	}

	private String text;

	private String template;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public SMS() {
		this.to = new ArrayList<String>();
	}

}
