package com.amx.jax.client.bene;

import com.amx.jax.api.AmxApiResponse;

public interface IBeneficiaryService {

	public static class Path {
		public static final String GET_BENE_STATUS_MASTER = "/status-master/";
	}

	public static class Params {
	}

	AmxApiResponse<BeneficaryStatusDto, Object> getBeneStatusMaster();


}
