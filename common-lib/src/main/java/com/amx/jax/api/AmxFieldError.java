package com.amx.jax.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class ResponseError.
 */
public class AmxFieldError implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3253269120058295762L;

	/** The obzect. */
	String obzect = null;
	String field = null;
	String description = null;
	String code = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	List<String> codes = null;

	public AmxFieldError() {
		this.codes = new ArrayList<String>();
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	/**
	 * Gets the obzect.
	 *
	 * @return the obzect
	 */
	public String getObzect() {
		return obzect;
	}

	/**
	 * Sets the obzect.
	 *
	 * @param obzect the new obzect
	 */
	public void setObzect(String obzect) {
		this.obzect = obzect;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the field.
	 *
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * Sets the field.
	 *
	 * @param field the new field
	 */
	public void setField(String field) {
		this.field = field;
	}

}
