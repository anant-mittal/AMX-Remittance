package com.amx.jax.api;

import java.io.Serializable;

/**
 * The Class ResponseError.
 */
public class AmxFieldError implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3253269120058295762L;

	/** The obzect. */
	String obzect = null;

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
	 * @param obzect
	 *            the new obzect
	 */
	public void setObzect(String obzect) {
		this.obzect = obzect;
	}

	/** The field. */
	String field = null;

	/** The description. */
	String description = null;

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
	 * @param description
	 *            the new description
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
	 * @param field
	 *            the new field
	 */
	public void setField(String field) {
		this.field = field;
	}

}
