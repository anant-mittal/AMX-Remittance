package com.amx.jax.scope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Tenant {

	/** Dev Environments **/
	KWT2("kw", "91"), BRNDEV("bhr", "83"), OMNDEV("om", "84"),

	/** Kuwait */
	KWT("kw", "91"),

	/** Oman */
	OMN("om", "84"),

	/** Baharain */
	BRN("bhr", "83"),

	/** India */
	IND("in", "94"), NONE("none", "0");

	public static final Tenant DEFAULT = KWT;

	public static Map<String, Tenant> mapping = new HashMap<String, Tenant>();
	public static final Pattern pattern = Pattern.compile("^(.+?)-(.+?)$");

	static {
		// Additional Mappings
		mapping.put("app-dev", KWT);
		mapping.put("app-devq", KWT2);
		mapping.put("app-devb", BRN);
		mapping.put("appq-kwt", KWT2);

		for (Tenant site : Tenant.values()) {
			mapping.put(site.toString().toLowerCase(), site);
			mapping.put("app-" + site.toString().toLowerCase(), site);
		}

	}

	private String id;
	private String code;

	Tenant(String id, String code) {
		this.id = id;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public BigDecimal getBDCode() {
		return new BigDecimal(code);
	}

	public static Tenant fromString(String siteId) {
		if (siteId != null) {
			String siteIdStr = siteId.toLowerCase();
			Matcher matcher = pattern.matcher(siteIdStr);
			if (matcher.find()) {
				siteIdStr = matcher.group(2);
			}

			if (mapping.containsKey(siteIdStr)) {
				return mapping.get(siteIdStr);
			}
			for (Tenant site : Tenant.values()) {
				if (site.toString().equalsIgnoreCase(siteIdStr)) {
					return site;
				}
			}
		}
		return Tenant.DEFAULT;
	}

}