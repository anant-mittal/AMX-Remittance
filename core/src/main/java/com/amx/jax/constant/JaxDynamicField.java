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

	BENE_BANK_IBAN_NUMBER, BENE_HOUSE_NO("BNFADDR3"), BENE_FLAT_NO("BNFADDR3"), BENE_STREET_NO("BNFADDR3"),AML_MESSAGE("AML_AUTH"),AML_REMARKS("AML_AUTH"),AML_USER_NAME("AML_AUTH"),
	AML_PASSWORD("AML_AUTH"),BENE_ZIP_CODE("BNFADDR3"),BENE_CITY_NAME("BNFADDR3"),BENEFICIARY_SWIFT_BANK1("BENEFICIARY_SWIFT_BANK1"),
	BENEFICIARY_SWIFT_BANK2("BENEFICIARY_SWIFT_BANK2"),INSTRUCTION("INSTRUCTION"),BENE_TELE_NO("BNFTELLAB");

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
