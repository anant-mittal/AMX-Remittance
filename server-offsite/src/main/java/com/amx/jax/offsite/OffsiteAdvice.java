package com.amx.jax.offsite;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.amx.jax.exception.AmxAdvice;

@ControllerAdvice
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class OffsiteAdvice extends AmxAdvice {

	private Logger logger = Logger.getLogger(OffsiteAdvice.class);

}
