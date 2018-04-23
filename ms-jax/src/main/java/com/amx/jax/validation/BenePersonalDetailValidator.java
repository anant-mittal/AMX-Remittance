package com.amx.jax.validation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;
import com.amx.jax.util.JaxUtil;

@Component
public class BenePersonalDetailValidator implements Validator {

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;

	@Autowired
	MetaData metaData;

	@Override
	public boolean supports(Class clazz) {
		return BenePersonalDetailModel.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors e) {
		ValidationUtils.rejectIfEmpty(e, "mobileNumber", "mobileNumber.empty");
		BeneficiaryTrnxModel beneficiaryTrnxModel = (BeneficiaryTrnxModel) target;
		BenePersonalDetailModel benePersonalDetailModel = beneficiaryTrnxModel.getBenePersonalDetailModel();
		validateMobile(benePersonalDetailModel, beneficiaryTrnxModel);
	}

	private void validateMobile(BenePersonalDetailModel benePersonalDetailModel,
			BeneficiaryTrnxModel beneficiaryTrnxModel) {

		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getServiceApplicabilityRule(
				metaData.getCountryId(), benePersonalDetailModel.getCountryId(),
				beneficiaryTrnxModel.getBeneAccountModel().getCurrencyId());

		int benePhoneLength = benePersonalDetailModel.getMobileNumber().toString().length();
		serviceAppList.forEach(i -> {
			if (JaxUtil.isNullZeroBigDecimalCheck(i.getMinLenght())) {
				int minLength = i.getMinLenght().intValue();
				if (minLength > 0 && benePhoneLength < minLength) {
					throw new GlobalException(JaxError.VALIDATION_MINIMUM_LENGTH, minLength);
				}

			}
			if (JaxUtil.isNullZeroBigDecimalCheck(i.getMaxLenght())) {
				int maxLength = i.getMaxLenght().intValue();
				if (maxLength > 0 && benePhoneLength > maxLength) {
					throw new GlobalException(JaxError.VALIDATION_MAXIMUM_LENGTH, maxLength);
				}
			}
		});
		
	}

}
