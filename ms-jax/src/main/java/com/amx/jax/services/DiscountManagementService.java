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
import com.amx.jax.repository.DiscountManagementRepository;

@Service
public class DiscountManagementService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(DiscountManagementService.class);
	
	@Autowired
	DiscountManagementRepository discountManagementRepository;

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
}
