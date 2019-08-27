package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dict.UserClient.Channel;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.remittance.DynamicRoutingPricingDto;
import com.amx.jax.model.response.remittance.branch.DynamicRoutingPricingResponse;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.CustomerCategoryDetails;
import com.amx.jax.pricer.dto.ExchangeDiscountInfo;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingRequest;
import com.amx.jax.pricer.dto.ExchangeRateAndRoutingResponse;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.TrnxRoutingDetails;
import com.amx.jax.pricer.var.PricerServiceConstants.CUSTOMER_CATEGORY;
import com.amx.jax.pricer.var.PricerServiceConstants.DISCOUNT_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_TYPE;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;
import com.amx.jax.service.BankMetaService;
import com.amx.utils.JsonUtil;

@Service
public class JaxDynamicPriceService {

	private static final Logger LOGGER = LoggerService.getLogger(JaxDynamicPriceService.class);

	@Autowired
	PricerServiceClient pricerServiceClient;
	@Autowired
	MetaData metaData;
	@Autowired
	BankMetaService bankMetaService;
	@Autowired
	ExchangeRateService exchangeRateService;

	public ExchangeRateResponseModel getExchangeRatesWithDiscount(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal lcAmount, BigDecimal foreignAmount, BigDecimal countryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {
		PricingRequestDTO pricingRequestDTO = createPricingRequest(fromCurrency, toCurrency, lcAmount, foreignAmount,
				countryId, routingBankId);
		AmxApiResponse<PricingResponseDTO, Object> apiResponse = null;
		try {
			LOGGER.debug("userDeviceClient : {}", JsonUtil.toJson(AppContextUtil.getUserClient()));
			LOGGER.debug("Pricing request json : {}", JsonUtil.toJson(pricingRequestDTO));
			apiResponse = pricerServiceClient.fetchPriceForCustomer(pricingRequestDTO);
		} catch (Exception e) {
			LOGGER.debug("No exchange data found from pricer, error is: ", e);
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found" + e.getMessage());
		}
		ExchangeRateResponseModel exchangeRateResponseModel = createExchangeRateResponseModel(apiResponse, lcAmount,foreignAmount, serviceIndicatorId);
		return exchangeRateResponseModel;
	}

	
	public ExchangeRateResponseModel getBaseExchangeRates(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal lcAmount, BigDecimal foreignAmount, BigDecimal countryId, BigDecimal routingBankId,
			BigDecimal serviceIndicatorId) {
		PricingRequestDTO pricingRequestDTO = createPricingRequest(fromCurrency, toCurrency, lcAmount, foreignAmount,
				countryId, routingBankId);
		AmxApiResponse<PricingResponseDTO, Object> apiResponse = null;
		try {
			apiResponse = pricerServiceClient.fetchBasePrice(pricingRequestDTO);
		} catch (Exception e) {
			LOGGER.debug("No exchange data found from pricer, error is: ", e);
			throw new GlobalException(JaxError.EXCHANGE_RATE_NOT_FOUND, "No exchange data found");
		}
		ExchangeRateResponseModel exchangeRateResponseModel = createExchangeRateResponseModel(apiResponse, lcAmount,
				foreignAmount, serviceIndicatorId);
		return exchangeRateResponseModel;
	}

	private ExchangeRateResponseModel createExchangeRateResponseModel(AmxApiResponse<PricingResponseDTO, Object> apiResponse, BigDecimal lcAmount, BigDecimal foreignAmount,BigDecimal serviceIndicatorId) {
		ExchangeRateResponseModel exchangeRateResponseModel = new ExchangeRateResponseModel();
		List<BankMasterDTO> bankWiseRates = new ArrayList<>();
		Map<DISCOUNT_TYPE, ExchangeDiscountInfo> customerDiscountDetails = new HashMap<>();
		Boolean discountAvailed=false;
		Boolean costRateLimitReached=false;
		
		exchangeRateResponseModel.setBankWiseRates(bankWiseRates);
		if (apiResponse != null) {
			List<ExchangeRateDetails> sellRateDetails = apiResponse.getResult().getSellRateDetails();
			for (ExchangeRateDetails sellRateDetail : sellRateDetails) {
				if (serviceIndicatorId != null && !serviceIndicatorId.equals(sellRateDetail.getServiceIndicatorId())) {
					continue;
				}
				BankMasterDTO dto = bankMetaService.convert(bankMetaService.getBankMasterbyId(sellRateDetail.getBankId()));
				if (foreignAmount != null) {
					dto.setExRateBreakup(exchangeRateService.createBreakUpFromForeignCurrency(sellRateDetail.getSellRateNet().getInverseRate(), foreignAmount));
				} else {
					dto.setExRateBreakup(exchangeRateService.createBreakUp(sellRateDetail.getSellRateNet().getInverseRate(), lcAmount));
				}
				bankWiseRates.add(dto);
				customerDiscountDetails =sellRateDetail.getCustomerDiscountDetails();
				discountAvailed=sellRateDetail.isDiscountAvailed();
				costRateLimitReached = sellRateDetail.isCostRateLimitReached();
			}
			exchangeRateResponseModel.setBankWiseRates(bankWiseRates);
			if (CollectionUtils.isNotEmpty(bankWiseRates)) {
				exchangeRateResponseModel.setExRateBreakup(bankWiseRates.get(0).getExRateBreakup());
				exchangeRateResponseModel.setCustomerDiscountDetails(customerDiscountDetails);
				exchangeRateResponseModel.setDiscountAvailed(discountAvailed);
				exchangeRateResponseModel.setCostRateLimitReached(costRateLimitReached);
			}
		}
		return exchangeRateResponseModel;
	}

	private PricingRequestDTO createPricingRequest(BigDecimal fromCurrency, BigDecimal toCurrency, BigDecimal lcAmount,
			BigDecimal foreignAmount, BigDecimal beneBankCountryId, BigDecimal routingBankId) {
		PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();
		pricingRequestDTO.setCustomerId(metaData.getCustomerId());
		Channel channel = Channel.valueOf(metaData.getChannel().toString());
		if (AppContextUtil.getUserClient() != null && AppContextUtil.getUserClient().getClientType() != null) {
			channel = AppContextUtil.getUserClient().getClientType().getChannel();
		}
		pricingRequestDTO.setChannel(channel);
		pricingRequestDTO.setCountryBranchId(metaData.getCountryBranchId());
		pricingRequestDTO.setForeignCurrencyId(toCurrency);
		pricingRequestDTO.setLocalAmount(lcAmount);
		pricingRequestDTO.setForeignAmount(foreignAmount);
		pricingRequestDTO.setLocalCountryId(metaData.getCountryId());
		pricingRequestDTO.setLocalCurrencyId(fromCurrency);
		if (routingBankId != null) {
			pricingRequestDTO.setRoutingBankIds(Arrays.asList(routingBankId));
			pricingRequestDTO.setPricingLevel(PRICE_BY.ROUTING_BANK);
		} else {
			pricingRequestDTO.setPricingLevel(PRICE_BY.COUNTRY);
		}
		pricingRequestDTO.setForeignCountryId(beneBankCountryId);
		return pricingRequestDTO;
	}


}
