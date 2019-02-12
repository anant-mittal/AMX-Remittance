package com.amx.jax.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * dynamic field coming from db enum name represents field name in database
 * 
 * @author Prashant
 *
 */
public enum JaxDynamicField {

	BENE_BANK_IBAN_NUMBER, BENE_HOUSE_NO("BNFADDR3"), BENE_FLAT_NO("BNFADDR3"), BENE_STREET_NO("BNFADDR3");

	/**
	 * corresponding flex field in db
	 */
	private String flexField;

	public String getFlexField() {
		return flexField;
	}

	public void setFlexField(String flexField) {
		this.flexField = flexField;
	}

	private JaxDynamicField(String flexField) {
		this.flexField = flexField;
	}

	private JaxDynamicField() {
	}

	public static String[] getAllAdditionalFlexFields() {
		Set<String> set = new HashSet<>();
		JaxDynamicField[] allValues = JaxDynamicField.values();
		for (JaxDynamicField val : allValues) {
			if (val.getFlexField() != null) {
				set.add(val.getFlexField());
			}
		}
		return set.toArray(new String[set.size()]);
	}

}
