package com.amx.jax.validation;

import java.util.regex.Pattern;

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

	public void validateIfscCode(String ifscCode) {
		if (StringUtils.isNotBlank(ifscCode) && ifscCode.length() < 3) {
			throw new GlobalException(JaxError.INVALID_BANK_IFSC, "Invalid ifsc");
		}
		String ifscRegex = "^[A-Za-z]{4}\\d{7}$";
		Pattern pattern = Pattern.compile(ifscRegex);
		if (!pattern.matcher(ifscCode).matches()) {
			throw new GlobalException(JaxError.INVALID_BANK_IFSC, "Invalid ifsc");
		}
	}

	private void validateSwiftCode(String swift) {
		if (StringUtils.isNotBlank(swift) && swift.length() < 3) {
			throw new GlobalException(JaxError.INVALID_BANK_SWIFT, "Invalid swift");
		}

	}

}
