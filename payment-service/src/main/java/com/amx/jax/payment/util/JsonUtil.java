/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.amx.jax.payment.response.AResponseModel;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Viki Sangani
 * 06-Nov-2017
 * JsonUtil.java
 */
public class JsonUtil {

	public static Map<String, Object> fromJson(String jsonParamString) {

		Map<String, Object> map = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			// convert JSON string to Map	
			map = mapper.readValue(jsonParamString, new TypeReference<Map<String, Object>>(){});
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	

		return map;
	} //end of fromJson

	public static String toJson(AResponseModel response) throws JsonProcessingException{
		String	json =  new ObjectMapper().writeValueAsString(response);
		return json;
	}
}
