package com.amx.jax.validation;

import java.math.BigDecimal;
import java.util.function.IntPredicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
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
		final String mobileLength = countryService.getCountryMaster(countryId).getCountryMobileLength();
		boolean isValid = true;
		if (StringUtils.isNotBlank(mobileLength)) {
			IntPredicate predicate = (i) -> i == mobile.length();
			isValid = Stream.of(StringUtils.split(mobileLength, ",")).mapToInt(Integer::parseInt).anyMatch(predicate);
		}
		if (!isValid) {
			throw new GlobalException("Mobile Number length is not correct.", JaxError.INCORRECT_LENGTH,
					mobileLength.replaceAll(",", ":"));
		}
	}

	public void validateMobileNumber(BigDecimal countryId, String mobile) {
		String CountryCode = countryService.getCountryMaster(countryId).getCountryCode();
		if(CountryCode.toString().equals("001")) {
			final Pattern pattern = Pattern.compile("^[5679]\\d+$");
			if (!pattern.matcher(mobile).matches()) {
				throw new GlobalException("Invalid Mobile Number", JaxError.INVALID_MOBILE_NUMBER);
			}
		}
	}
}
