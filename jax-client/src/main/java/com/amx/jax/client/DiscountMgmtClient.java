package com.amx.jax.client;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.amx.amxlib.exception.JaxSystemError;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.AppContextUtil;
import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.AbstractProbotInterface.ApiEndPoints;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.CurrencyMasterDTO;
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
	
	public AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndPoints.GET_DISCOUTN_RATE).meta(new JaxMetaInfo())
					.post(pricingRequestDTO).as(new ParameterizedTypeReference<AmxApiResponse<PricingAndCostResponseDTO, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in fetchDiscountedRates : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	public AmxApiResponse<PricingResponseDTO, Object> fetchCustomerRates(PricingRequestDTO pricingRequestDTO) {
		return pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);
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
	
	public AmxApiResponse<CurrencyMasterDTO, Object> updateCurrencyGroupId(BigDecimal groupId, BigDecimal currencyId) {
		return pricerServiceClient.updateCurrencyGroupId(groupId, currencyId);
	}
	
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {
		return pricerServiceClient.getCurrencyGroupingData();
	}
	
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByGroupId(BigDecimal groupId) {
		return pricerServiceClient.getCurrencyByGroupId(groupId);
	}
	
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(
			OnlineMarginMarkupReq OnlineMarginMarkupReq) {
			try {
				return restService.ajax(appConfig.getJaxURL()).path(ApiEndPoints.GET_MARKUP_DETAILS).meta(new JaxMetaInfo())
						.post(OnlineMarginMarkupReq).as(new ParameterizedTypeReference<AmxApiResponse<OnlineMarginMarkupInfo, Object>>() {
						});
			}catch (Exception ae) {
				LOGGER.error("exception in getOnlineMarginMarkupData : ", ae);
				return JaxSystemError.evaluate(ae);
			}
	}

	
	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(
			OnlineMarginMarkupInfo onlineMarginMarkupInfo) {
		try {
			
			return restService.ajax(appConfig.getJaxURL()).path(ApiEndPoints.SAVE_MARKUP_DETAILS).meta(new JaxMetaInfo())
					.post(onlineMarginMarkupInfo)
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
			} catch (Exception ae) {
			LOGGER.error("exception in saveOnlineMarginMarkupData : ", ae);
			return JaxSystemError.evaluate(ae);
		}
		
	}
	

	
	

}
