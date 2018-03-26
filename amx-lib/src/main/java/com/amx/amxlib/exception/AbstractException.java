package com.amx.amxlib.exception;

import org.apache.commons.lang.StringUtils;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.response.ApiError;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

public abstract class AbstractException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String errorMessage;

	protected String errorKey;

	protected JaxError error;

	public AbstractException(ApiError error) {
		try {
			this.error = JaxError.valueOf(error.getErrorId());
		} catch (Exception e) {
		}
		this.errorKey = error.getErrorId();
		this.errorMessage = error.getErrorMessage();
	}

	public AbstractException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public AbstractException(String errorMessage, String errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.errorKey = errorCode;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public JaxError getError() {
		return error;
	}

	public void setError(JaxError error) {
		this.error = error;
	}

	public String getErrorKey() {
		if (StringUtils.isNotBlank(errorKey)) {
			return errorKey;
		} else {
			return ArgUtil.parseAsString(error, Constants.BLANK);
		}
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
}
