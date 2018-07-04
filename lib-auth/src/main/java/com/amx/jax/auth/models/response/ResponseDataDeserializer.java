package com.amx.jax.auth.models.response;

import java.io.IOException;
import java.util.List;

import com.amx.amxlib.model.response.ResponseData;
import com.amx.jax.auth.dto.EmployeeDetailsDTO;
import com.amx.jax.auth.dto.UserDetailsDTO;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ResponseDataDeserializer extends StdDeserializer<ResponseData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ResponseDataDeserializer(Class<?> vc) {
		super(vc);
	}

	public ResponseDataDeserializer() {
		this(null);
	}

	@Override
	public ResponseData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String type = node.get("type").asText();
		String values = node.get("values").toString();
		ResponseData responseData = new ResponseData();
		responseData.setMetaInfo(0);
		responseData.setType(type);
		List<Object> models = null;
		switch (type) {
		case "employee-detail":
			models = new ObjectMapper().readValue(values, new TypeReference<List<EmployeeDetailsDTO>>() {
			});
			break;
		case "user-detail":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserDetailsDTO>>() {
			});
			break;	
	}
		responseData.setValues(models);
		return responseData;
	}

}
