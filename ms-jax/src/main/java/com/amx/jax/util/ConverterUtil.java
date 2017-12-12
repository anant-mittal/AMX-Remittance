package com.amx.jax.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.exception.InvalidJsonInputException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConverterUtil {

	private Logger logger = Logger.getLogger(ConverterUtil.class);

	public Object unmarshall(String json, Class<?> c) {
		try {
			return new ObjectMapper().readValue(json, c);
		} catch (IOException e) {
			logger.error("Error in parsing json", e);
			throw new InvalidJsonInputException("Unable to parse request body json");
		}
	}

	public String marshall(Object o) {
		try {
			return new ObjectMapper().writeValueAsString(o);
		} catch (JsonProcessingException e) {
		}
		return null;
	}
}
