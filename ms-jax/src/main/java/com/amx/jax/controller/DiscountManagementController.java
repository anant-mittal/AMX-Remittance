package com.amx.jax.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.services.DiscountManagementService;

@RestController
public class DiscountManagementController implements IDiscManagementService {
	
	@Autowired
	DiscountManagementService discountManagementService;
	
	@Autowired
	MetaData metaData;
	
	@RequestMapping(value = ApiEndPoints.GET_COUNTRY_BRANCH, method = RequestMethod.GET)
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranch() {
		BigDecimal applicationCountryId = metaData.getCountryId();
		return discountManagementService.getCountryBranch(applicationCountryId);
	}
}
