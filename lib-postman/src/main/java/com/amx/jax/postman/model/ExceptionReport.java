package com.amx.jax.postman.model;

public class ExceptionReport extends Exception {

	private static final long serialVersionUID = -2576530798373715398L;
	private String title;
	private String exception;

	public ExceptionReport(String title, Exception e) {
		super(e.getMessage());
		this.exception = e.getClass().getName();
		this.title = title;
		this.setStackTrace(e.getStackTrace());
	}

	public ExceptionReport(Exception e) {
		super(e.getMessage());
		this.setStackTrace(e.getStackTrace());
	}

	public ExceptionReport(Exception e, Email email) {
		super(e.getMessage());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

}
