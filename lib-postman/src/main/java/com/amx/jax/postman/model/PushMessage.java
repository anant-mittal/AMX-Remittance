package com.amx.jax.postman.model;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amx.jax.AppParam;
import com.amx.jax.dict.ContactType;
import com.amx.jax.dict.Tenant;
import com.amx.jax.scope.TenantContextHolder;

public class PushMessage extends Message {

	public static final String TOPICS_PREFIX = "/topics/";
	private static final String FORMAT_TO_ALL = "%s-%s-all";
	private static final String FORMAT_TO_NATIONALITY = "%s-%s-nationality-%s";
	private static final String FORMAT_TO_USER = "%s-%s-user-%s";
	public static final String CONDITION_SEPRATOR = " || ";

	public static final Pattern FORMAT_TO_ALL_PATTERN = Pattern.compile("/topics/(.+)-all$");
	public static final Pattern FORMAT_TO_NATIONALITY_PATTERN = Pattern.compile("/topics/(.+)-nationality-(.+)$");
	public static final Pattern FORMAT_TO_USER_PATTERN = Pattern.compile("/topics/(.+)-user-(.+)$");

	public static final Pattern FORMAT_TO_ALL_PATTERN_V2 = Pattern.compile("/topics/(.+)-(.+)-all$");
	public static final Pattern FORMAT_TO_NATIONALITY_PATTERN_V2 = Pattern
			.compile("/topics/(.+)-(.+)-nationality-(.+)$");
	public static final Pattern FORMAT_TO_USER_PATTERN_V2 = Pattern.compile("/topics/(.+)-(.+)-user-(.+)$");

	private static final long serialVersionUID = -1354844357577261297L;

	Object result = null;

	String image = null;
	String link = null;

	boolean condition;

	public PushMessage() {
		super(ContactType.FBPUSH);
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

	public void addToTenant(Tenant tenant) {
		this.addTopic(
				String.format(PushMessage.FORMAT_TO_ALL, AppParam.APP_ENV.getValue(), tenant.toString()).toLowerCase());
	}

	public void addToUser(BigDecimal userid) {
		this.addTo(TOPICS_PREFIX
				+ String.format(FORMAT_TO_USER, AppParam.APP_ENV.getValue(), TenantContextHolder.currentSite(), userid)
						.toLowerCase().replaceAll("\\s+", ""));
	}

	public void addToCountry(Tenant tenant, Object nationalityId) {
		this.addTo(TOPICS_PREFIX
				+ String.format(PushMessage.FORMAT_TO_NATIONALITY, AppParam.APP_ENV.getValue(), tenant.toString(),
						nationalityId).toLowerCase());
	}

	public void addToCountry(Object nationalityId) {
		this.addTo(TOPICS_PREFIX
				+ String.format(FORMAT_TO_NATIONALITY, AppParam.APP_ENV.getValue(), TenantContextHolder.currentSite(),
						nationalityId).toLowerCase());
	}

	public void addToEveryone() {
		this.addTo(TOPICS_PREFIX
				+ String.format(FORMAT_TO_ALL, AppParam.APP_ENV.getValue(), TenantContextHolder.currentSite())
						.toLowerCase());
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

	public static Contact toContact(String topic) {
		Contact c = new Contact();

		Matcher m = PushMessage.FORMAT_TO_USER_PATTERN_V2.matcher(topic);
		if (m.find()) {
			c.setUserid(m.group(3));
			Tenant tenant = Tenant.fromString(m.group(2), Tenant.DEFAULT);
			c.setTenant(tenant);
		} else {
			m = PushMessage.FORMAT_TO_NATIONALITY_PATTERN_V2.matcher(topic);
			if (m.find()) {
				c.setCountry(m.group(3));
				Tenant tenant = Tenant.fromString(m.group(2), Tenant.DEFAULT);
				c.setTenant(tenant);
			} else {
				m = PushMessage.FORMAT_TO_ALL_PATTERN_V2.matcher(topic);
				if (m.find()) {
					Tenant tenant = Tenant.fromString(m.group(2), Tenant.DEFAULT);
					c.setTenant(tenant);
				} else {
					return toContactOld(topic);
				}
			}
		}
		return c;
	}

	private static Contact toContactOld(String topic) {
		Contact c = new Contact();

		Matcher m = PushMessage.FORMAT_TO_USER_PATTERN.matcher(topic);
		if (m.find()) {
			c.setUserid(m.group(2));
			Tenant tenant = Tenant.fromString(m.group(1), Tenant.DEFAULT);
			c.setTenant(tenant);
		} else {
			m = PushMessage.FORMAT_TO_NATIONALITY_PATTERN.matcher(topic);
			if (m.find()) {
				c.setCountry(m.group(2));
				Tenant tenant = Tenant.fromString(m.group(1), Tenant.DEFAULT);
				c.setTenant(tenant);
			} else {
				m = PushMessage.FORMAT_TO_ALL_PATTERN.matcher(topic);
				if (m.find()) {
					Tenant tenant = Tenant.fromString(m.group(1), Tenant.DEFAULT);
					c.setTenant(tenant);
				}
			}
		}
		return c;
	}
}
