package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.pricer.PricerServiceClient;
import com.amx.jax.pricer.dto.ExchangeRateBreakup;
import com.amx.jax.pricer.dto.ExchangeRateDetails;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.repository.DiscountManagementRepository;
import com.amx.jax.util.RoundUtil;

@Service
public class DiscountManagementService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscountManagementService.class);
	
	@Autowired
	DiscountManagementRepository discountManagementRepository;
	@Autowired
	PricerServiceClient pricerServiceClient;

	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranch(BigDecimal countryId) {

		List<CountryBranch> countryBranchList = discountManagementRepository.getCountryBranch(countryId);
		
		LOGGER.info("COUNT OF BRANCHES : - " +countryBranchList.size());
		
		if(countryBranchList.isEmpty()) {
			throw new GlobalException("Country Branch not found");
		}
		
		List<CountryBranchDTO> branchList = convertCountryBranch(countryBranchList);
		
		return AmxApiResponse.buildList(branchList);
	}
	
	
	List<CountryBranchDTO> convertCountryBranch(List<CountryBranch> countryBranchList) {
		List<CountryBranchDTO> list = new ArrayList<>();
		for(CountryBranch incomeRange: countryBranchList) {
			CountryBranchDTO countryBranch = new CountryBranchDTO();
			countryBranch.setBranchId(incomeRange.getBranchId());;
			countryBranch.setBranchName(incomeRange.getBranchName());
			countryBranch.setCountryBranchId(incomeRange.getCountryBranchId());
			
			list.add(countryBranch);
		}
		return list;
	}
	
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(PricingRequestDTO pricingRequestDTO) {
		AmxApiResponse<PricingResponseDTO, Object> response = pricerServiceClient.fetchDiscountedRates(pricingRequestDTO);
		response.getResult().getSellRateDetails().forEach(i -> applyRoundingLogic(i));
		return response;
	}


	private void applyRoundingLogic(ExchangeRateDetails i) {
		//applyRoundingLogic(i.getSellRateBase());
		applyRoundingLogic(i.getSellRateNet());
	}


	private void applyRoundingLogic(ExchangeRateBreakup sellRateNet) {
		BigDecimal fcAmount = sellRateNet.getConvertedFCAmount();
		sellRateNet.setConvertedFCAmount(RoundUtil.roundToZeroDecimalPlaces(fcAmount));
		BigDecimal lcAmount = sellRateNet.getConvertedLCAmount();
		int lcIndex = sellRateNet.getConvertedLCAmount().intValue();
		sellRateNet.setConvertedLCAmount(RoundUtil.roundBigDecimal(lcAmount, lcIndex));
		BigDecimal invRate = sellRateNet.getInverseRate();
		sellRateNet.setInverseRate(RoundUtil.roundBigDecimal(invRate, 6));
	}
	
	
}
