package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);

	@Autowired
	private HttpServletResponse httpResponse;

	@ExceptionHandler(AbstractException.class)
	@ResponseBody
	public ApiResponse handleInvalidInputException(AbstractException ex) {

		ApiResponse response = getApiResponse(ex);
		setErrorHeaders(ex);
		response.setResponseStatus(ResponseStatus.BAD_REQUEST);
		logger.info("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: "+ ex.getErrorCode(), ex);
		return response;
	}

	private void setErrorHeaders(AbstractException ex) {
		httpResponse.addHeader("ERROR_CODE", ex.getErrorCode());
		httpResponse.addHeader("ERROR_MESSAGE", ex.getErrorMessage());
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
