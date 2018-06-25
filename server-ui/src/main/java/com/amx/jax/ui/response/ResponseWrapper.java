package com.amx.jax.ui.response;

/**
 * The Class ResponseWrapper.
 *
 * @param <T>
 *            the generic type
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
	 * @param data
	 *            the data
	 */
	public ResponseWrapper(T data) {
		super(data);
	}

	/**
	 * Instantiates a new response wrapper.
	 *
	 * @param data
	 *            the data
	 * @param meta
	 *            the meta
	 */
	public ResponseWrapper(T data, Object meta) {
		super(data, meta);
	}

}
