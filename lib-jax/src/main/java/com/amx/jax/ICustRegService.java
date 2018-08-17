package com.amx.jax;

import java.math.BigDecimal;

import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AmxApiResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public interface ICustRegService {

	public static class CustRegApiEndPoints {
		public static final String GET_MODES = "/api/reg/modes";
		public static final String GET_ID_FIELDS = "/api/reg/id/fields";
	}

	@JsonDeserialize(as = CustRegRequestModel.class)
	public interface RegModeModel {
		public BigDecimal getMode();

		public void setMode(BigDecimal modeId);
	}

	/**
	 * Get List of modes, available for customer registration
	 * 
	 * @return
	 */
	AmxApiResponse<BigDecimal, Object> getModes();

	/**
	 * Get List of fields required for identity validation, for particular mode id
	 * 
	 * @return
	 */
	AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel modeId);

}
