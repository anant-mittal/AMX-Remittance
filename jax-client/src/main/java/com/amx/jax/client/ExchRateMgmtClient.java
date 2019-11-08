package com.amx.jax.client;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchRateEnquiryReqDto;
import com.amx.jax.pricer.dto.ExchangeRateEnquiryRespDto;
import com.amx.jax.rest.RestService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExchRateMgmtClient extends AbstractJaxServiceClient {
	private static final Logger LOGGER = Logger.getLogger(DiscountMgmtClient.class);

	@Autowired
	RestService restService;

	@Autowired
	PricerServiceClient pricerServiceClient;

	public AmxApiResponse<ExchangeRateEnquiryRespDto, Object> enquireExchangeRates(ExchRateEnquiryReqDto rateEnquiryReqDto) {
		return pricerServiceClient.enquireExchangeRates(rateEnquiryReqDto);
	}

}
