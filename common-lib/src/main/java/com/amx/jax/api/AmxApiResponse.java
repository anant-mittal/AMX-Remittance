package com.amx.jax.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiResponse<T, M> extends AResponse<M> implements Serializable {

	private static final long serialVersionUID = 2026047322050489651L;

	/** The status key. */
	protected String statusKey = "SUCCESS";

	/** The data. */
	protected T data = null;

	/** The data. */
	protected List<T> results = null;

	public AmxApiResponse() {
		super();
		this.data = null;
		this.results = new ArrayList<T>();
	}

	/**
	 * Gets the status key.
	 *
	 * @return the status key
	 */
	public String getStatusKey() {
		return statusKey;
	}

	/**
	 * Sets the status key.
	 *
	 * @param statusKey
	 *            the new status key
	 */
	public void setStatusKey(String statusKey) {
		this.statusKey = statusKey;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	@JsonIgnore
	public void setHttpStatus(HttpStatus status) {
		if (status.is5xxServerError() || status.is4xxClientError() || status.is3xxRedirection()) {
			this.statusKey = status.series().name();
		}
		this.status = ArgUtil.parseAsString(status.value());
		this.message = status.getReasonPhrase();
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

	public void addResult(T result) {
		this.results.add(result);
	}

	public static <TS> AmxApiResponse<TS, Object> build(TS result) {
		AmxApiResponse<TS, Object> resp = new AmxApiResponse<TS, Object>();
		resp.addResult(result);
		return null;
	}

	public static <TS, MS> AmxApiResponse<TS, MS> build(TS result, MS meta) {
		AmxApiResponse<TS, MS> resp = new AmxApiResponse<TS, MS>();
		resp.addResult(result);
		resp.setMeta(meta);
		return resp;
	}

}
