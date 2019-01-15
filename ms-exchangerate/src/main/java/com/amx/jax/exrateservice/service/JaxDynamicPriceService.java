package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.var.PricerServiceConstants.PRICE_BY;
import com.amx.jax.service.BankMetaService;

@Service
public class JaxDynamicPriceService {

	@Autowired
	PricerServiceClient pricerServiceClient;
	@Autowired
	MetaData metaData;
	@Autowired
	BankMetaService bankMetaService;
	@Autowired
	ExchangeRateService exchangeRateService;

	public ExchangeRateResponseModel getExchangeRatese(BigDecimal fromCurrency, BigDecimal toCurrency,
			BigDecimal lcAmount) {
		PricingRequestDTO pricingRequestDTO = new PricingRequestDTO();
		pricingRequestDTO.setCustomerId(metaData.getCustomerId());
		pricingRequestDTO.setChannel(metaData.getChannel().getClientChannel());
		pricingRequestDTO.setCountryBranchId(metaData.getCountryBranchId());
		pricingRequestDTO.setForeignCurrencyId(toCurrency);
		pricingRequestDTO.setLocalAmount(lcAmount);
		pricingRequestDTO.setLocalCountryId(metaData.getCountryId());
		pricingRequestDTO.setLocalCurrencyId(fromCurrency);
		pricingRequestDTO.setPricingLevel(PRICE_BY.COUNTRY);
		pricingRequestDTO.setForeignCountryId(new BigDecimal(94));

		AmxApiResponse<PricingResponseDTO, Object> apiResponse = pricerServiceClient
				.fetchPriceForCustomer(pricingRequestDTO);
		ExchangeRateResponseModel exchangeRateResponseModel = new ExchangeRateResponseModel();
		List<BankMasterDTO> bankWiseRates = new ArrayList<>();
		List<ExchangeRateDetails> sellRateDetails = apiResponse.getResult().getSellRateDetails();
		for (ExchangeRateDetails sellRateDetail : sellRateDetails) {
			BankMasterDTO dto = bankMetaService.convert(bankMetaService.getBankMasterbyId(sellRateDetail.getBankId()));
			dto.setExRateBreakup(
					exchangeRateService.createBreakUp(sellRateDetail.getSellRateNet().getInverseRate(), lcAmount));
			bankWiseRates.add(dto);
		}
		exchangeRateResponseModel.setBankWiseRates(bankWiseRates);
		if (CollectionUtils.isNotEmpty(bankWiseRates)) {
			exchangeRateResponseModel.setExRateBreakup(bankWiseRates.get(0).getExRateBreakup());
		}
		return exchangeRateResponseModel;
	}

}
