package com.amx.jax.validation;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.utils.NumberUtil;

@Component
public class BranchDetailValidation {


	public void validateCountryBranchId(BigDecimal countryBranchId) {
		if (countryBranchId == null) {
			throw new GlobalException("CountrybranchId can not be null");
		}
		if (!NumberUtil.isIntegerValue(countryBranchId)) {
			throw new GlobalException("CountrybranchId must be an integer");
		}
	}

}
