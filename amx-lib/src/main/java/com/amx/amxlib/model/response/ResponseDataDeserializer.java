package com.amx.amxlib.model.response;

import java.io.IOException;
import java.util.List;

import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.response.ResponseData;
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
	public ResponseData deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String type = node.get("type").asText();
		String values = node.get("values").toString();
		ResponseData responseData = new ResponseData();
		responseData.setMetaInfo(0);
		responseData.setType(type);
		List<Object> models = null;
		switch (type) {
		case "user":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserModel>>() {
			});
			break;
		case "otp":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CivilIdOtpModel>>() {
			});
			break;
		case "customer":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CustomerModel>>() {
			});
			break;
		}

		responseData.setValues(models);
		return responseData;
	}

}
