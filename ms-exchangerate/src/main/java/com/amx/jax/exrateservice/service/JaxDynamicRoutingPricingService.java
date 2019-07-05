package com.amx.jax.exrateservice.service;
/**
 * @author rabil
 */
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;
import com.amx.jax.service.BankMetaService;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.JsonUtil;


@Service
public class JaxDynamicRoutingPricingService {

	
	private static final Logger LOGGER = LoggerService.getLogger(JaxDynamicRoutingPricingService.class);

	@Autowired
	PricerServiceClient pricerServiceClient;
	@Autowired
	MetaData metaData;
	@Autowired
	BankMetaService bankMetaService;
	@Autowired
	ExchangeRateService exchangeRateService;


	
	
	
	public  AmxApiResponse<ExchangeRateAndRoutingResponse,Object> getDynamicRoutingAndPrice(BigDecimal toCurrency , BigDecimal fromCurrency, BigDecimal lcAmount,
			BigDecimal foreignAmount, BigDecimal beneBankCountryId, BigDecimal routingBankId,BigDecimal serviceIndicatorId,BigDecimal beneficiaryBankId,BigDecimal beneficiaryBankBranchId,String serviceGroup,BigDecimal beneRelationId,BigDecimal exclusiveBankId) {
	
		ExchangeRateAndRoutingRequest routingPricingRequestDTO = createRoutingPricingRequest(fromCurrency, toCurrency, lcAmount, foreignAmount,beneBankCountryId,routingBankId,serviceIndicatorId,beneficiaryBankId,beneficiaryBankBranchId,serviceGroup,beneRelationId,exclusiveBankId);
	    AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResponse = null;
	    DynamicRoutingPricingResponse dynamicRoutingPricingResponse =new DynamicRoutingPricingResponse();

		try {
			LOGGER.debug("userDeviceClient : {}", JsonUtil.toJson(AppContextUtil.getUserClient()));
			LOGGER.debug("Dyanamic Routing Pricing request json : {}", JsonUtil.toJson(routingPricingRequestDTO));
			apiResponse = pricerServiceClient.fetchRemitRoutesAndPrices(routingPricingRequestDTO);
		} catch (Exception e) {
			LOGGER.debug("getDynamicRoutingAndPrice No exchange data found from pricer, error is: ", e.getMessage());
			throw new GlobalException(JaxError.EXCH_ROUTING_DEAILS_NOT_AVAIL, "Routing service is not available at this time. Please contact support");
		}
		
		
		if (apiResponse != null) {
			Map<PRICE_TYPE, List<String>> bestExchangeRatePaths =apiResponse.getResult().getBestExchangeRatePaths();
			List<Map<String,List<DynamicRoutingPricingDto>>> dynamicRoutingPricingList = new ArrayList<>();
			
			if(bestExchangeRatePaths!=null && !bestExchangeRatePaths.isEmpty()) {
				for (Map.Entry<PRICE_TYPE, List<String>> mapEntry : bestExchangeRatePaths.entrySet()) {
					dynamicRoutingPricingResponse = new DynamicRoutingPricingResponse();
					if(PRICE_TYPE.BENE_DEDUCT.equals(mapEntry.getKey())) {
						List<String> beneDeductList=mapEntry.getValue();
						Map<String,List<DynamicRoutingPricingDto>> dynamicRoutingPricingMap= new HashMap<>();
						List<DynamicRoutingPricingDto> dynamicRoutingPricingDtoList= new ArrayList<>();
						for(String beneDed : beneDeductList) {
							DynamicRoutingPricingDto dto =  dynamicxchangeRateResponseModel(apiResponse,beneDed,foreignAmount,lcAmount);
							dynamicRoutingPricingDtoList.add(dto);
							
						}
						dynamicRoutingPricingMap.put(PRICE_TYPE.BENE_DEDUCT.toString(), dynamicRoutingPricingDtoList);
						dynamicRoutingPricingList.add(dynamicRoutingPricingMap);
					}
					if(PRICE_TYPE.NO_BENE_DEDUCT.equals(mapEntry.getKey())) {
						List<String> nonBeneDeduct =mapEntry.getValue();
						Map<String,List<DynamicRoutingPricingDto>> dynamicRoutingPricingMap= new HashMap<>();
						List<DynamicRoutingPricingDto> dynamicRoutingPricingDtoList= new ArrayList<>();
						for(String noBeneDed : nonBeneDeduct) {
							DynamicRoutingPricingDto dto =  dynamicxchangeRateResponseModel(apiResponse,noBeneDed,foreignAmount,lcAmount);
							dynamicRoutingPricingDtoList.add(dto);
						}
						dynamicRoutingPricingMap.put(PRICE_TYPE.NO_BENE_DEDUCT.toString(), dynamicRoutingPricingDtoList);
						dynamicRoutingPricingList.add(dynamicRoutingPricingMap);
						
					}
		        }
			}
			
			dynamicRoutingPricingResponse.setDynamicRoutingPricingList(dynamicRoutingPricingList);
	}
		
		return	apiResponse;
	}
	
	private DynamicRoutingPricingDto dynamicxchangeRateResponseModel(AmxApiResponse<ExchangeRateAndRoutingResponse,Object> apiResponse,String key,BigDecimal foreignAmount, BigDecimal lcAmount) {
		DynamicRoutingPricingDto dto = new DynamicRoutingPricingDto();
		Map<String, TrnxRoutingDetails> trnxRoutingPathList = apiResponse.getResult().getTrnxRoutingPaths();
		Map<BigDecimal, Map<BigDecimal, ExchangeRateDetails>> bankServiceModeSellRates = apiResponse.getResult().getBankServiceModeSellRates();
		
		Channel channel = Channel.valueOf(metaData.getChannel().toString());
		if (AppContextUtil.getUserClient() != null && AppContextUtil.getUserClient().getClientType() != null) {
			channel = AppContextUtil.getUserClient().getClientType().getChannel();
		}
		TrnxRoutingDetails trnxRoutingDetails = trnxRoutingPathList.get(key);
		if(trnxRoutingDetails!=null) {
			dto.setTrnxRoutingPaths(trnxRoutingDetails);
		}
		ExchangeRateDetails sellRateDetail= bankServiceModeSellRates.get(trnxRoutingDetails.getRoutingBankId()).get(trnxRoutingDetails.getServiceMasterId());
		
		if(sellRateDetail!=null) {
			dto.setCustomerDiscountDetails(sellRateDetail.getCustomerDiscountDetails());
			dto.setDiscountAvailed(sellRateDetail.isDiscountAvailed());
			dto.setCostRateLimitReached(sellRateDetail.isCostRateLimitReached());
			dto.setTxnFee(trnxRoutingDetails.getChargeAmount());
			if (foreignAmount != null) {
				dto.setExRateBreakup(exchangeRateService.createBreakUpFromForeignCurrency(sellRateDetail.getSellRateNet().getInverseRate(), foreignAmount));
			} else {
				dto.setExRateBreakup(exchangeRateService.createBreakUp(sellRateDetail.getSellRateNet().getInverseRate(), lcAmount));
			}
			
			
		}
		return dto;
	}
	
	
	private ExchangeRateAndRoutingRequest createRoutingPricingRequest(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal foreignAmount, BigDecimal beneBankCountryId, BigDecimal routingBankId,BigDecimal serviceIndicatorId,
			BigDecimal beneficiaryBankId,BigDecimal beneficiaryBankBranchId,String serviceGroup,BigDecimal beneRelationId,BigDecimal exclusiveBankId) {
		ExchangeRateAndRoutingRequest routingPricingRequestDTO = new ExchangeRateAndRoutingRequest();
		
		routingPricingRequestDTO.setCustomerId(metaData.getCustomerId());
		Channel channel = Channel.valueOf(metaData.getChannel().toString());
		if (AppContextUtil.getUserClient() != null && AppContextUtil.getUserClient().getClientType() != null) {
			channel = AppContextUtil.getUserClient().getClientType().getChannel();
		}
		routingPricingRequestDTO.setChannel(channel);
		routingPricingRequestDTO.setCountryBranchId(metaData.getCountryBranchId());
		routingPricingRequestDTO.setBeneficiaryBankId(beneficiaryBankId);
		routingPricingRequestDTO.setBeneficiaryBranchId(beneficiaryBankBranchId);
		if (routingBankId != null) {
			routingPricingRequestDTO.setRoutingBankIds(Arrays.asList(routingBankId));
			routingPricingRequestDTO.setPricingLevel(PRICE_BY.ROUTING_BANK);
		} else {
			routingPricingRequestDTO.setPricingLevel(PRICE_BY.COUNTRY);
		}
		if(JaxUtil.isNullZeroBigDecimalCheck(exclusiveBankId)) {
			routingPricingRequestDTO.setExcludeCorBanks(Arrays.asList(exclusiveBankId));
		}
		
		routingPricingRequestDTO.setLocalCountryId(metaData.getCountryId());
		routingPricingRequestDTO.setLocalCurrencyId(toCurrency);
		routingPricingRequestDTO.setLocalAmount(lcAmount);
		routingPricingRequestDTO.setForeignCountryId(beneBankCountryId);
		routingPricingRequestDTO.setForeignCurrencyId(fromCurrency);
		routingPricingRequestDTO.setForeignAmount(foreignAmount);
		routingPricingRequestDTO.setBeneficiaryId(beneRelationId);
		if(serviceGroup!=null && serviceGroup.equals(ConstantDocument.SERVICE_GROUP_CODE_BANK)) {
		routingPricingRequestDTO.setServiceGroup(SERVICE_GROUP.BANK);
		}else if(serviceGroup!=null && serviceGroup.equals(ConstantDocument.SERVICE_GROUP_CODE_CASH)) {
			routingPricingRequestDTO.setServiceGroup(SERVICE_GROUP.CASH);
		}
		
		return routingPricingRequestDTO;
	}
	

}
