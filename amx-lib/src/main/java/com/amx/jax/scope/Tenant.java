package com.amx.jax.scope;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public enum Tenant {

	/** Dev Environments **/
	KWTDEV("kw", "91"), BRNDEV("bhr", "83"), OMNDEV("om", "84"),

	/** Kuwait */
	KWT("kw", "91"),

	/** Oman */
	OMN("om", "84"),

	/** Baharain */
	BRN("bhr", "83"),

	/** India */
	IND("in", "94");

	public static final Tenant DEFAULT = KWT;

	public static Map<String, Tenant> mapping = new HashMap<String, Tenant>();

	static {
		// Additional Mappings
		mapping.put("app-dev", KWT);
		mapping.put("app-devq", KWTDEV);
		mapping.put("app-devb", BRN);
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
		if (mapping.containsKey(siteId)) {
			return mapping.get(siteId);
		}
		for (Tenant site : Tenant.values()) {
			if (site.toString().equalsIgnoreCase(siteId)) {
				return site;
			}
		}
		return Tenant.DEFAULT;
	}

}