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

	private static final long serialVersionUID = 3705286852744314145L;

	public AdditionalFlexRequiredException() {
		super();
	}

	public AdditionalFlexRequiredException(AmxApiError error) {
		super(error);
	}

	public AdditionalFlexRequiredException(String errorMessage, JaxError error) {
		super(errorMessage, error.getCode());
	}

	// public void deserializeMeta(AmxApiError amxApiError) {
	// try {
	// List meta = (List) amxApiError.getMeta();
	// String jsonString = JsonUtil.toJson(meta);
	// List<JaxConditionalFieldDto> model = new ObjectMapper().readValue(jsonString,
	// new TypeReference<List<JaxConditionalFieldDto>>() {
	// });
	// this.setMeta(model);
	// } catch (Exception e) {
	// }
	// }

	@SuppressWarnings("unchecked")
	public List<JaxConditionalFieldDto> getConditionalFileds() {
		return (List<JaxConditionalFieldDto>) this.getApiError().getMeta();
	}

}
