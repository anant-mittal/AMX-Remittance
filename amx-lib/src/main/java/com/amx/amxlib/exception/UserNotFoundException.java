package com.amx.amxlib.exception;

public class UserNotFoundException extends AbstractJaxException {

	public UserNotFoundException(String errorMessage) {
		super(errorMessage);
	}
	
	public UserNotFoundException() {
		super();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	

}
