package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);

	@ExceptionHandler(AbstractException.class)
	@ResponseBody
	public ApiResponse handleInvalidInputException(AbstractException ex) {

		ApiResponse response = getApiResponse(ex);
		response.setResponseStatus(ResponseStatus.BAD_REQUEST);
		logger.info("Exception occured in controller " + ex.getClass().getName(), ex);
		return response;
	}

	private ApiResponse getApiResponse(AbstractException ex) {
		ApiResponse response = new ApiResponse();
		List<ApiError> errors = new ArrayList<>();
		ApiError error = new ApiError(ex.getErrorCode(), ex.getErrorMessage());
		errors.add(error);
		response.setError(errors);
		return response;
	}
}
