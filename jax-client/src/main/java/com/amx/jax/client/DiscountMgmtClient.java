package com.amx.jax.client;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.rest.RestService;

@Component
public class DiscountMgmtClient extends AbstractJaxServiceClient implements IDiscManagementService {
	private static final Logger LOGGER = Logger.getLogger(DiscountMgmtClient.class);
	
	@Autowired
	RestService restService;
	
	@Autowired
	PricerServiceClient pricerServiceClient;

	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranchList() {
		
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndPoints.GET_COUNTRY_BRANCH)
					.meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CountryBranchDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getCountryBranchList : ", ae);
			return JaxSystemError.evaluate(ae);
		} 
	}
	
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndPoints.GET_DISCOUTN_RATE).meta(new JaxMetaInfo())
					.post().as(new ParameterizedTypeReference<AmxApiResponse<PricingResponseDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in fetchDiscountedRates : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}
	
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemetDetails(DiscountMgmtReqDTO discountMgmtReqDTO) {
		return pricerServiceClient.getDiscountManagemet(discountMgmtReqDTO);
	}

	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(BigDecimal countryId,
			BigDecimal currencyId) {
		return pricerServiceClient.getRbanksAndServices(countryId, currencyId);
	}
	
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(DiscountDetailsReqRespDTO discountMgmtReqDTO){
		return pricerServiceClient.saveDiscountDetails(discountMgmtReqDTO);
	}
	
}
