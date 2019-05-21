package com.amx.jax.pricer;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;

public interface PartnerDataService extends AbstractProbotInterface{
	
	public AmxApiResponse<SrvPrvFeeInqResDTO, Object> getPartnerFeeinquiry(SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO);

}
