package com.amx.jax.dict;

import org.apache.log4j.Logger;

import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.JsonUtil;

public class PayGRespCodeJSONConverter {
	
	private static final Logger LOGGER = Logger.getLogger(PayGRespCodeJSONConverter.class);

	public static String getResponseCodeDetail(String errorCategory) {
		String responseCodeDetail = null;
		if (errorCategory != null) {
			switch (TenantContextHolder.currentSite()) {
			case BHR:
				responseCodeDetail = JsonUtil.toJson(ResponseCodeBHR.valueOf(errorCategory));
				LOGGER.info("Response Code Details JSON : " +responseCodeDetail.toString());
				break;
			default:
				break;
			}
		}
		return responseCodeDetail;

	}
}
