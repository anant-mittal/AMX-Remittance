package com.amx.jax.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AmxApiResponseM<T> extends AmxApiResponse<T, Object> {

	private static final long serialVersionUID = 2026047322050489651L;

	public AmxApiResponseM() {
		super();
	}

}
