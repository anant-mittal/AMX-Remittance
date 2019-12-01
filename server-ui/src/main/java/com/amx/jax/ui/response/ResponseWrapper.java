package com.amx.jax.ui.response;

import java.util.List;

import com.amx.jax.api.AmxApiResponse;

/**
 * The Class ResponseWrapper.
 *
 * @param <T> the generic type
 */
public class ResponseWrapper<T> extends ResponseWrapperM<T, Object> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2494519734309640070L;

	/**
	 * Instantiates a new response wrapper.
	 */
	public ResponseWrapper() {
		super();
	}

	/**
	 * Instantiates a new response wrapper.
	 *
	 * @param data the data
	 */
	public ResponseWrapper(T data) {
		super(data);
	}

	/**
	 * Instantiates a new response wrapper.
	 *
	 * @param data the data
	 * @param meta the meta
	 */
	public ResponseWrapper(T data, Object meta) {
		super(data, meta);
	}

	public static <TD> ResponseWrapper<TD> build(AmxApiResponse<TD, ?> resultResponse) {
		ResponseWrapper<TD> wrapper = new ResponseWrapper<TD>(resultResponse.getResult());
		return wrapper;
	}

	public static <TD> ResponseWrapper<List<TD>> buildList(AmxApiResponse<TD, ?> resultResponse) {
		ResponseWrapper<List<TD>> wrapper = new ResponseWrapper<List<TD>>(resultResponse.getResults());
		return wrapper;
	}

}
