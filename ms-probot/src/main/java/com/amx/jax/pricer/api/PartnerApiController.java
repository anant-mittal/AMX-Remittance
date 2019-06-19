package com.amx.jax.pricer.api;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.partner.dto.SrvPrvFeeInqReqDTO;
import com.amx.jax.partner.dto.SrvPrvFeeInqResDTO;
import com.amx.jax.pricer.PartnerDataService;
import com.amx.jax.pricer.service.PartnerExchDataService;

//@SpringBootApplication
@RestController
public class PartnerApiController implements PartnerDataService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PartnerApiController.class);

	@Autowired
	PartnerExchDataService partnerExchDataService;

	@Override
	@RequestMapping(value = ApiEndPoints.GET_SERVICE_PROVIDER_QUOTE, method = RequestMethod.POST)
	public AmxApiResponse<SrvPrvFeeInqResDTO, Object> getServiceProviderQuotation(
			@RequestBody @Valid SrvPrvFeeInqReqDTO srvPrvFeeInqReqDTO) {
		LOGGER.info("In Get API of fee inquiry " + " with TraceId: " + AppContextUtil.getTraceId());
		
		SrvPrvFeeInqResDTO serviceProviderResp = partnerExchDataService.getPartnerFeeinquiry(srvPrvFeeInqReqDTO);
		return AmxApiResponse.build(serviceProviderResp);
	}

}
