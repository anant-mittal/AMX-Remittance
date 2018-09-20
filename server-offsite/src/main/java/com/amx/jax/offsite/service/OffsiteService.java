package com.amx.jax.offsite.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.AmxFieldError;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.response.FieldListDto;


/**
 * The Class OffsiteService.
 */
@Service
public class OffsiteService {

	/** The log. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	/**
	 * Gets the Field by tenant Id and nationality Id and component Id and compare component data.
	 *
	 * @return the list of Fields
	 */
	public AmxApiResponse<FieldListDto, Object> getFieldList(DynamicFieldRequest model) {

		List<FieldListDto> fieldOutput = new ArrayList<>();

		AmxApiResponse<Map<String, FieldListDto>, Object> fieldResponse = offsiteCustRegClient.getFieldList(model);
		
		logger.info("Field List from JAX: " + fieldResponse.toString());
		
		AmxApiResponse<FieldListDto, Object> fieldRequest = new AmxApiResponse<>();

		if(fieldResponse != null) {
			if(fieldResponse.getStatusKey() != null && fieldResponse.getStatusKey().equalsIgnoreCase("SUCCESS")) {
				for (Map<String, FieldListDto> fieldList : fieldResponse.getResults()) {
					for (Entry<String, FieldListDto> fieldListDto : fieldList.entrySet())
					{
						if(fieldListDto.getKey().toLowerCase().contains(model.getComponentDataDesc().toLowerCase())) {
							fieldOutput.add(fieldListDto.getValue());
						}
					}
				}

				fieldRequest.setData((FieldListDto)fieldResponse.getData());
				fieldRequest.setError((String)fieldResponse.getError());
				fieldRequest.setErrors((List<AmxFieldError>)fieldResponse.getErrors());
				fieldRequest.setException((String)fieldResponse.getException());
				fieldRequest.setMessage((String)fieldResponse.getMessage());
				fieldRequest.setMessageKey((String)fieldResponse.getMessageKey());
				fieldRequest.setMeta((FieldListDto)fieldResponse.getMeta());
				fieldRequest.setPath((String)fieldResponse.getPath());
				fieldRequest.setResults(fieldOutput);
				fieldRequest.setStatus((String)fieldResponse.getStatus());
				fieldRequest.setStatusKey((String)fieldResponse.getStatusKey());
				fieldRequest.setTimestamp((Long)fieldResponse.getTimestamp());
			}
		}
		
		logger.info("Field List final : " + fieldRequest.toString());

		return fieldRequest;
	}


}
