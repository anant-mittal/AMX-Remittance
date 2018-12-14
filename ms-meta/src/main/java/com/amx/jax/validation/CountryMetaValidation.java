package com.amx.jax.validation;

import java.math.BigDecimal;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.ExceptionMessageKey;
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
	 * @param mobile    - mobile number to validate
	 * 
	 */
	public void validateMobileNumberLength(BigDecimal countryId, String mobile) {
		final String mobileLength = countryService.getCountryMaster(countryId).getCountryMobileLength();
		boolean isValid = true;
		if (StringUtils.isNotBlank(mobileLength)) {
			IntPredicate predicate = (i) -> i == mobile.length();
			isValid = Stream.of(StringUtils.split(mobileLength, ",")).mapToInt(Integer::parseInt).anyMatch(predicate);
		}
		if (!isValid) {
			throw new GlobalException(JaxError.INCORRECT_LENGTH,
					ExceptionMessageKey.build(JaxError.INCORRECT_LENGTH, mobileLength.replaceAll(",", ":")),
					"Mobile Number length is not correct.");
		}
	}

	public void validateMobileNumber(BigDecimal countryId, String mobile) {
		// String CountryCode =
		// countryService.getCountryMaster(countryId).getCountryCode();
		String countryAlpha2Code = countryService.getCountryMaster(countryId).getCountryAlpha2Code();
		if (countryAlpha2Code.toString().equals("KW")) {
			final Pattern pattern = Pattern.compile("^[5679]\\d+$");
			if (!pattern.matcher(mobile).matches()) {
				throw new GlobalException(JaxError.INVALID_MOBILE_NUMBER, "Invalid Mobile Number");
			}
		}

		if (countryAlpha2Code.toString().equals("BH")) {
			final Pattern pattern = Pattern.compile("^[367]\\d+$");
			if (!pattern.matcher(mobile).matches()) {
				throw new GlobalException(JaxError.INVALID_MOBILE_NUMBER, "Invalid Mobile Number");
			}
		}

		if (countryAlpha2Code.toString().equals("OM")) {
			final Pattern pattern = Pattern.compile("^[79]\\d+$");
			if (!pattern.matcher(mobile).matches()) {
				throw new GlobalException(JaxError.INVALID_MOBILE_NUMBER, "Invalid Mobile Number");
			}
		}
	}
}
