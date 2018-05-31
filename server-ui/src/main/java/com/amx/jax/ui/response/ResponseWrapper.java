package com.amx.jax.ui.response;

public class ResponseWrapper<T> extends ResponseWrapperM<T, Object> {

	private static final long serialVersionUID = 2494519734309640070L;

	public ResponseWrapper() {
		super();
		this.timestamp = System.currentTimeMillis();
		this.traceId = ContextUtil.getTraceId();
	}

	public ResponseWrapper(T data) {
		super(data);
	}
	 
        	private String exception = null;
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
