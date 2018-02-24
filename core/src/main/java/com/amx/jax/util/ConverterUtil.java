package com.amx.jax.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.amx.jax.exception.InvalidJsonInputException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ConverterUtil {

	public Object unmarshall(String json, Class<?> c) {
		try {
			return new ObjectMapper().readValue(json, c);
		} catch (IOException e) {
			throw new InvalidJsonInputException("Unable to parse request body json");
		}
	}

	public <T> T readValue(String json, Class<T> c) {
		try {
			return new ObjectMapper().readValue(json, c);
		} catch (IOException e) {
			throw new InvalidJsonInputException("Unable to parse request body json");
		}
	}

	public <T> List<T> fromIterableToArrayList(Iterable<T> iterable) {
		List<T> list = new ArrayList<>();
		iterable.forEach(i -> list.add(i));
		return list;
	}

	public String marshall(Object c) {
		try {
			return new ObjectMapper().writeValueAsString(c);
		} catch (IOException e) {
			throw new InvalidJsonInputException("Unable to parse request body json");
		}
	}
}
