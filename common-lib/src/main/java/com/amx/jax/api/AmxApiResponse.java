package com.amx.jax.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiResponse<T, M> extends AResponse<M> implements Serializable {

	private static final long serialVersionUID = 2026047322050489651L;

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
	 * Instantiates a new amx api response.
	 *
	 * @param resultList
	 *            the result list
	 */
	public AmxApiResponse(List<T> resultList) {
		super();
		this.data = null;
		this.results = resultList;
	}

	/**
	 * Instantiates a new amx api response.
	 *
	 * @param resultList
	 *            the result list
	 * @param meta
	 *            the meta
	 */
	public AmxApiResponse(List<T> resultList, M meta) {
		super();
		this.data = null;
		this.results = resultList;
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
		if (results != null && !results.isEmpty()) {
			return results.get(0);
		}
		return null;
	}

	public void addResult(T result) {
		this.results.add(result);
	}

	public static <TS> AmxApiResponse<TS, Object> build() {
		return new AmxApiResponse<TS, Object>();
	}

	public static <TS> AmxApiResponse<TS, Object> buildList(List<TS> results) {
		AmxApiResponse<TS, Object> resp = new AmxApiResponse<TS, Object>();
		resp.setResults(results);
		return resp;
	}

	public static <TS> AmxApiResponse<TS, Object> build(TS result) {
		AmxApiResponse<TS, Object> resp = new AmxApiResponse<TS, Object>();
		resp.addResult(result);
		return resp;
	}

	public static <TS, MS> AmxApiResponse<TS, MS> build(TS result, MS meta) {
		AmxApiResponse<TS, MS> resp = new AmxApiResponse<TS, MS>();
		resp.addResult(result);
		resp.setMeta(meta);
		return resp;
	}

	/**
	 * Builds the list.
	 *
	 * @param <TS>
	 *            the generic type
	 * @param resultList
	 *            the result list
	 * @return the amx api response
	 */
	public static <TS> AmxApiResponse<TS, Object> buildList(List<TS> resultList) {
		return new AmxApiResponse<TS, Object>(resultList);
	}

	/**
	 * Builds the list.
	 *
	 * @param <TS>
	 *            the generic type
	 * @param <MS>
	 *            the generic type
	 * @param resultList
	 *            the result list
	 * @param meta
	 *            the meta
	 * @return the amx api response
	 */
	public static <TS, MS> AmxApiResponse<TS, MS> buildList(List<TS> resultList, MS meta) {
		return new AmxApiResponse<TS, MS>(resultList, meta);
	}

}
