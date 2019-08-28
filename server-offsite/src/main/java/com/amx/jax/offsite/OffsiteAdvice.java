package com.amx.jax.offsite;

import org.springframework.http.HttpStatus;

import com.amx.jax.exception.AmxAdvice;
import com.amx.jax.exception.AmxApiException;

//@ControllerAdvice
public class OffsiteAdvice extends AmxAdvice {

	@Override
	public HttpStatus getHttpStatus(AmxApiException exp) {
		return super.getHttpStatus(exp); //HttpStatus.OK;
	}

}
