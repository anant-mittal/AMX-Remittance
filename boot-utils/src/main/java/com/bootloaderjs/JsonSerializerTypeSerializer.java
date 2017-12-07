package com.bootloaderjs;

import java.io.IOException;

//import org.codehaus.jackson.JsonGenerator;
//import org.codehaus.jackson.JsonProcessingException;
//import org.codehaus.jackson.map.JsonSerializer;
//import org.codehaus.jackson.map.SerializerProvider;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

//TODO: Auto-generated Javadoc
/**
 * The Class JsonSerializerTypeSerializer.
 */
@SuppressWarnings("rawtypes")
public class JsonSerializerTypeSerializer extends JsonSerializer<JsonSerializerType> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 * org.codehaus.jackson.JsonGenerator,
	 * org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(JsonSerializerType value, JsonGenerator jgen, SerializerProvider sp)
			throws IOException, JsonProcessingException {
		jgen.writeObject(value.toObject());
	}
}