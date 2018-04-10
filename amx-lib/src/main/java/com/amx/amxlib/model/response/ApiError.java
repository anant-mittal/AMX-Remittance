package com.amx.amxlib.model.response;

public class ApiError {

	private String errorId;

	private String errorMessage;

	// warning error
	private String errorType;

	private JaxFieldError validationErrorField;

	public ApiError(String errorId, String errorMessage, String errorType) {
		super();
		this.errorId = errorId;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}

	public ApiError(String errorId, String errorMessage) {
		super();
		this.errorId = errorId;
		this.errorMessage = errorMessage;
		this.errorType = "Error";
	}

	public ApiError() {
		super();
	}

	public String getErrorId() {
		return errorId;
	}

	public void setErrorId(String errorId) {
		this.errorId = errorId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public JaxFieldError getValidationErrorField() {
		return validationErrorField;
	}

	public void setValidationErrorField(JaxFieldError validationErrorField) {
		this.validationErrorField = validationErrorField;
	}

}
