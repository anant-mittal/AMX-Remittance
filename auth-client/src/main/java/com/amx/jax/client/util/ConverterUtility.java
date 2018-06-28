package com.amx.jax.client.util;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConverterUtility {

	private Logger logger = Logger.getLogger(ConverterUtility.class);

	public String marshall(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (IOException e) {
			logger.error("error in marshalling ", e);
		}
		return null;
	}

	public Object unmarshall(String json, Class<?> c) {
		try {
			return new ObjectMapper().readValue(json, c);
		} catch (IOException e) {
			logger.error("error in unmarshalling ", e);
		}
		return null;
	}

}
