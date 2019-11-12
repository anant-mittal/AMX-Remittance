package com.amx.jax.constants;

public enum DocumentScanIndic {

	ARCMATE("A"), DB_SCAN("D"), DOCUMENT_ERA("E");

	String indicValue;

	private DocumentScanIndic(String indicValue) {
		this.indicValue = indicValue;
	}

	public String getIndicValue() {
		return indicValue;
	}

	public void setIndicValue(String indicValue) {
		this.indicValue = indicValue;
	}

}
