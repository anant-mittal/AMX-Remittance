package com.amx.jax.validation;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;

@Component
public class BankBranchSearchRequestlValidator implements Validator {

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;

	@Autowired
	MetaData metaData;

	@Override
	public boolean supports(Class clazz) {
		return GetBankBranchRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		GetBankBranchRequest getBankBranchRequest = (GetBankBranchRequest) target;
		if (StringUtils.isNotEmpty(getBankBranchRequest.getSwift())) {
			validateSwiftCode(getBankBranchRequest.getSwift());
		}
		if (StringUtils.isNotEmpty(getBankBranchRequest.getIfscCode())) {
			validateIfscCode(getBankBranchRequest.getIfscCode());
		}
	}

	private void validateIfscCode(String ifscCode) {
		if (StringUtils.isNotBlank(ifscCode) && ifscCode.length() < 3) {
			throw new GlobalException(JaxError.INVALID_BANK_IFSC, "Invalid ifsc");
		}

	}

	private void validateSwiftCode(String swift) {
		if (StringUtils.isNotBlank(swift) && swift.length() < 3) {
			throw new GlobalException(JaxError.INVALID_BANK_SWIFT, "Invalid swift");
		}

	}

}
	
	/*private void validateIfscCode(String ifscCode) {
		final Pattern pattern = Pattern.compile("^[A-Za-z]{4}0[a-zA-Z0-9]{6}$");
		if (!pattern.matcher(ifscCode).matches()) {
			throw new GlobalException("Invalid ifsc", JaxError.INVALID_BANK_IFSC);
		}

	}

	private void validateSwiftCode(String swift) {
		final Pattern pattern = Pattern.compile("^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$");
		if (!pattern.matcher(swift).matches()) {
			throw new GlobalException("Invalid swift", JaxError.INVALID_BANK_SWIFT);
		}*/



