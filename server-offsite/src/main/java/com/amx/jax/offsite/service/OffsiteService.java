package com.amx.jax.offsite.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.response.FieldListDto;


/**
 * The Class OffsiteService.
 */
@Service
public class OffsiteService {
	
	/** The log. */
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;
	
	/**
	 * Gets the Field by tenant Id and nationality Id and component Id and compare component data.
	 *
	 * @return the list of Fields
	 */
	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {

		Map<String, FieldListDto> fieldOutput = new HashMap<>();
		List<Map<String, FieldListDto>> componentList = new ArrayList<>();
		
		AmxApiResponse<Map<String, FieldListDto>, Object> fieldResponse = offsiteCustRegClient.getFieldList(model);

		if(fieldResponse != null) {
			if(fieldResponse.getStatusKey() != null && fieldResponse.getStatusKey().equalsIgnoreCase("SUCCESS")) {
				for (Map<String, FieldListDto> fieldList : fieldResponse.getResults()) {
					FieldListDto field = fieldList.get(model.getComponentData());
					if(field != null) {
						fieldOutput.put(model.getComponentData(), field);
						componentList.add(fieldOutput);
					}
				}
				
				if(componentList != null && componentList.size() != 0) {
					fieldResponse.setResults(null);
					fieldResponse.setResults(componentList);
				}
			}
		}
		
		return fieldResponse;
	}
	

}
