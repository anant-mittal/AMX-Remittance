package com.amx.jax.offsite;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.amx.jax.exception.AmxAdvice;
import com.amx.jax.exception.AmxApiException;

@ControllerAdvice
public class OffsiteAdvice extends AmxAdvice {

	@Override
	public HttpStatus getHttpStatus(AmxApiException exp) {
		return HttpStatus.OK;
	}

}
