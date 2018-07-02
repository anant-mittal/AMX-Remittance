package com.amx.amxlib.exception;

import java.io.IOException;
import java.util.List;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.jax.exception.AmxApiError;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AdditionalFlexRequiredException extends AbstractJaxException {

	public AdditionalFlexRequiredException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdditionalFlexRequiredException(AmxApiError error) {
		super(error);
	}

	public AdditionalFlexRequiredException(String errorMessage, JaxError error) {
		super(errorMessage, error.getCode());
	}

	@Override
	public void deserializeMeta(AmxApiError amxApiError) {
		try {
			List meta = (List) amxApiError.getMeta();
			String jsonString = JsonUtil.toJson(meta);
			List<JaxConditionalFieldDto> model = new ObjectMapper().readValue(jsonString, new TypeReference<List<JaxConditionalFieldDto>>(){});
			this.setMeta(model);
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JaxConditionalFieldDto> getMeta() {
		return (List<JaxConditionalFieldDto>) super.getMeta();
	}

}
