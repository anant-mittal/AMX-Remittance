package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;

public interface ProbotDataService  extends AbstractProbotInterface{

	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemet(DiscountMgmtReqDTO discountMgmtReqDTO);
	
}
