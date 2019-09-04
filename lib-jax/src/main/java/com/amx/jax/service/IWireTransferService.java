package com.amx.jax.service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;

public interface IWireTransferService {
	public static class Path {
		public static final String PREFIX = "/payat-branch";
		public static final String PAYMENT_MODES = "/payment-modes";
		
		public static final String WT_TRNX_LIST = "/pb-trnx-list";
		public static final String WT_TRNX_LIST_BRANCH = "/pb-trnx-list-branch";
		
	}

	public static class Params {

	}

	AmxApiResponse<ResourceDTO, Object> getPaymentModes();

	

	AmxApiResponse<PayAtBranchTrnxListDTO, Object> getWtTrnxList();

	

	AmxApiResponse<PayAtBranchTrnxListDTO, Object> getWtTrnxListBranch();

}
