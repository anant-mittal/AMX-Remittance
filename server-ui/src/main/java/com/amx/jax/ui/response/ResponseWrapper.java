package com.amx.jax.ui.response;

public class ResponseWrapper<T> extends ResponseWrapperM<T, Object> {

	private static final long serialVersionUID = 2494519734309640070L;

	public ResponseWrapper() {
		super();
	}

	public ResponseWrapper(T data) {
		super(data);
	}

	public ResponseWrapper(T data, Object meta) {
		super(data, meta);
	}

}
