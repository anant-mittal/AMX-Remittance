package com.amx.jax.util;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.amx.jax.exception.InvalidJsonInputException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConverterUtil {

	public Object unmarshall(String json, Class<?> c) {
		try {
			return new ObjectMapper().readValue(json, c);
		}
		catch (IOException e) {
			throw new InvalidJsonInputException("Unable to parse request body json");
		}
	}
}
