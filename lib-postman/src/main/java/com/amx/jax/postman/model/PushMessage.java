package com.amx.jax.postman.model;

import java.math.BigDecimal;

import com.amx.jax.scope.TenantContextHolder;

public class PushMessage extends Message {

	public static final String TOPICS_PREFIX = "/topics/";
	public static final String FORMAT_TO_ALL = "%s-all";
	public static final String FORMAT_TO_NATIONALITY = "%s-nationality-%s";
	public static final String FORMAT_TO_USER = "%s-user-%s";
	public static final String CONDITION_SEPRATOR = " || ";

	private static final long serialVersionUID = -1354844357577261297L;

	Object result = null;

	String image = null;
	String link = null;

	boolean condition;

	public PushMessage() {
		super();
		this.condition = false;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void addTopic(String topic) {
		this.addTo(TOPICS_PREFIX + topic);
	}

	public void addToUser(BigDecimal userid) {
		this.addTo(
				TOPICS_PREFIX + String.format(FORMAT_TO_USER, TenantContextHolder.currentSite(), userid).toLowerCase());
	}

	public void addToCountry(BigDecimal nationalityId) {
		this.addTo(TOPICS_PREFIX
				+ String.format(FORMAT_TO_NATIONALITY, TenantContextHolder.currentSite(), nationalityId).toLowerCase());
	}

	public void addToEveryone() {
		this.addTo(TOPICS_PREFIX + String.format(FORMAT_TO_ALL, TenantContextHolder.currentSite()).toLowerCase());
	}

	public boolean isCondition() {
		return condition;
	}

	public void setCondition(boolean condition) {
		this.condition = condition;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

}
