package com.amx.jax.validation;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.service.CountryService;

/**
 * @author Prashant
 *         <p>
 *         This class is used to validate country meta data
 *         </p>
 */
@Component
public class CountryMetaValidation {

	@Autowired
	CountryService countryService;

	/**
	 * @param countryId
	 * @param mobile
	 *            - mobile number to validate
	 * 
	 */
	public void validateMobileNumberLength(BigDecimal countryId, String mobile) {
		BigDecimal mobileLength = countryService.getCountryMaster(countryId).getCountryMobileLength();
		int userMobLength = mobile.length();

		if (mobileLength != null && userMobLength != mobileLength.intValue()) {
			throw new GlobalException("Mobile Number length is not correct.", JaxError.INCORRECT_LENGTH, mobileLength);
		}
	}
}
