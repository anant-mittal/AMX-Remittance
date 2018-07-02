package com.amx.jax.api;

import java.io.Serializable;
import java.util.List;

import com.amx.utils.Constants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiResponse<T, M> implements Serializable {

	private static final long serialVersionUID = 2026047322050489651L;

	protected Long timestamp;

	protected String status;

	protected String message = Constants.BLANK;
	protected String path;

	/** The data. */
	protected T data = null;

	/** The data. */
	protected List<T> results = null;

	/** The meta. */
	protected M meta = null;

	/** The errors. */
	private List<AmxFieldError> errors = null;

	public AmxApiResponse() {
		this.timestamp = System.currentTimeMillis();
	}

	/**
	 * Gets the timestamp.
	 *
	 * @return the timestamp
	 */
	public Long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 *
	 * @param timestamp
	 *            the new timestamp
	 */
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the meta.
	 *
	 * @return the meta
	 */
	public M getMeta() {
		return meta;
	}

	/**
	 * Sets the meta.
	 *
	 * @param meta
	 *            the new meta
	 */
	public void setMeta(M meta) {
		this.meta = meta;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *            the new data
	 */
	public void setData(T data) {
		this.data = data;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	@JsonIgnore
	public T getResult() {
		if (results == null) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Gets the errors.
	 *
	 * @return the errors
	 */
	public List<AmxFieldError> getErrors() {
		return errors;
	}

	/**
	 * Sets the errors.
	 *
	 * @param errors
	 *            the new errors
	 */
	public void setErrors(List<AmxFieldError> errors) {
		this.errors = errors;
	}
}
