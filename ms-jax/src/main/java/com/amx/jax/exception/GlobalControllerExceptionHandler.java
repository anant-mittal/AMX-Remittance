package com.amx.jax.exception;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.amx.amxlib.exception.CommonJaxException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.notification.alert.IAlert;
import com.amx.jax.util.JaxContextUtil;
import com.amx.utils.JsonUtil;

@ControllerAdvice
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger logger = Logger.getLogger(GlobalControllerExceptionHandler.class);
	@Autowired
	private ApplicationContext appContext;
	@Autowired
	private HttpServletResponse httpResponse;

	@ExceptionHandler(AbstractJaxException.class)
	@ResponseBody
	public ApiResponse handleInvalidInputException(AbstractJaxException ex) {

		ApiResponse response = getApiResponse(ex);
		AmxApiError error = (AmxApiError) response.getError().get(0);
		error.setErrorClass(CommonJaxException.class.getName());
		setErrorHeaders(error);
		response.setResponseStatus(ResponseStatus.BAD_REQUEST);
		logger.info("Exception occured in controller " + ex.getClass().getName() + " error message: "
				+ ex.getErrorMessage() + " error code: " + ex.getErrorCode(), ex);
		raiseAlert(ex);
		return response;
	}

	private void raiseAlert(AbstractJaxException ex) {
		JaxEvent event = JaxContextUtil.getJaxEvent();
		if (event != null) {
			IAlert alert = appContext.getBean(event.getAlertBean());
			if (alert.isEnabled()) {
				alert.sendAlert(ex);
			}
		}
	}

	private void setErrorHeaders(AmxApiError error) {
		httpResponse.addHeader("apiErrorJson", JsonUtil.toJson(error));
	}

	private ApiResponse getApiResponse(AbstractJaxException ex) {
		ApiResponse response = new ApiResponse();
		List<AmxApiError> errors = new ArrayList<>();
		AmxApiError error = new AmxApiError(ex.getErrorCode(), ex.getErrorMessage());
		errors.add(error);
		response.setError(errors);
		return response;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		JaxFieldValidationException exception = new JaxFieldValidationException(ex.getBindingResult().toString());
		ApiResponse apiResponse = getApiResponse(exception);
		List<AmxApiError> errors = apiResponse.getError();
		AmxApiError error = errors.get(0);
		error.setErrorClass(ex.getClass().getName());
		// JaxFieldError validationErrorField = new
		// JaxFieldError(ex.getBindingResult().getFieldError().getField());
		// errors.get(0).setValidationErrorField(validationErrorField);
		setErrorHeaders(error);
		return new ResponseEntity(apiResponse, HttpStatus.BAD_REQUEST);
	}
}
