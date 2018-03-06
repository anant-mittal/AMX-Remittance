package com.amx.jax.postman;

public class PostManException extends Exception {

	private static final long serialVersionUID = -4630097170366238398L;

	public PostManException(Exception e) {
		super(e);
	}

	public PostManException(String msg) {
		super(msg);
	}

}
