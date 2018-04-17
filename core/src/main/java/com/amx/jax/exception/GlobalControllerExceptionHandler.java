package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amx.amxlib.model.response.ApiError;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.JaxFieldError;
import com.amx.amxlib.model.response.ResponseStatus;

@ControllerAdvice
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);

	@Autowired
	private HttpServletResponse httpResponse;

	@ExceptionHandler(AbstractAppException.class)
	@ResponseBody
	public ApiResponse handleInvalidInputException(AbstractException ex) {

		ApiResponse response = getApiResponse(ex);
		
		response.setResponseStatus(ResponseStatus.BAD_REQUEST);
		logger.info("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorCode(), ex);
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
		setErrorHeaders(ex);
		return response;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		JaxFieldValidationException exception = new JaxFieldValidationException(ex.getBindingResult().toString());
		ApiResponse apiResponse = getApiResponse(exception);
		List<ApiError> errors = apiResponse.getError();
		JaxFieldError validationErrorField = new JaxFieldError(ex.getBindingResult().getFieldError().getField());
		errors.get(0).setValidationErrorField(validationErrorField);
		return new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
	}
}
