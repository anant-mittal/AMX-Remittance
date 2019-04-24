package com.amx.jax.dict;

import org.apache.log4j.Logger;

import com.amx.jax.api.ResponseCodeDetailDTO;
import com.amx.jax.scope.TenantContextHolder;

public class PayGRespCodeJSONConverter {
	
	private static final Logger LOGGER = Logger.getLogger(PayGRespCodeJSONConverter.class);

	public static ResponseCodeDetailDTO getResponseCodeDetail(String errorCategory) {
		ResponseCodeDetailDTO responseCodeDetail = new ResponseCodeDetailDTO();
		if (errorCategory != null) {
			switch (TenantContextHolder.currentSite()) {
			case BHR:
				ResponseCodeBHR enumBHR = ResponseCodeBHR.valueOf(errorCategory);
				if(enumBHR != null) {
					responseCodeDetail.setResponseCode(enumBHR.getResponseCode());
					responseCodeDetail.setResponseDesc(enumBHR.getResponseDesc());
					responseCodeDetail.setAlmullaErrorCode(enumBHR.getAlmullaErrorCode());
					responseCodeDetail.setCategory(enumBHR.getCategory());
					responseCodeDetail.setType(enumBHR.getCategory().getType().toString());
					responseCodeDetail.setStatus(enumBHR.getCategory().getStatus().toString());
					
					LOGGER.info("Response Code Details BHR JSON : " +responseCodeDetail.toString());
				}
				
				break;
			case OMN:
				ResponseCodeOMN enumOMN = ResponseCodeOMN.valueOf(errorCategory);
				if(enumOMN != null) {
					responseCodeDetail.setResponseCode(enumOMN.getResponseCode());
					responseCodeDetail.setResponseDesc(enumOMN.getResponseDesc());
					responseCodeDetail.setAlmullaErrorCode(enumOMN.getAlmullaErrorCode());
					responseCodeDetail.setCategory(enumOMN.getCategory());
					
					LOGGER.info("Response Code Details OMN JSON : " +responseCodeDetail.toString());
				}
				
				break;
			
			default:
				break;
			}
		}
		return responseCodeDetail;

	}
}
