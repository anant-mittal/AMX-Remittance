package com.amx.jax.client.bene;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;

public interface IBeneficiaryService {

	public static class Path {
		public static final String GET_BENE_STATUS_MASTER = "/status-master/";
	}

	public static class Params {
		public static final String SERVICE_GROUP_ID = "serviceGroupId";
	}

	AmxApiResponse<BeneficaryStatusDto, Object> getBeneStatusMaster(BigDecimal serviceGroupId);

}
