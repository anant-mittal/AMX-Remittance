package com.amx.jax.validation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IServiceApplicabilityRuleDao;

@Component
public class BenePersonalDetailValidator implements Validator {

	@Autowired
	IServiceApplicabilityRuleDao serviceApplicabilityRuleDao;

	@Autowired
	MetaData metaData;
	
	@Autowired
	BlackListDao blackListDao;

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
	    validateBeneBlacklist(benePersonalDetailModel);

		//TODO : add blakc listed checki. use benePersonalDetailModel obj and concat
		// call blacklistr dao and throw exp
	}

	private void validateBeneBlacklist(BenePersonalDetailModel benePersonalDetailModel)
	{
		StringBuilder beneName = new StringBuilder();
		if(StringUtils.isNotBlank(benePersonalDetailModel.getFirstName())) {
			beneName.append(benePersonalDetailModel.getFirstName().trim());
		}
		if(StringUtils.isNotBlank(benePersonalDetailModel.getSecondName())) {
			beneName.append(benePersonalDetailModel.getSecondName().trim());
		}
		if(StringUtils.isNotBlank(benePersonalDetailModel.getThirdName())) {
			beneName.append(benePersonalDetailModel.getThirdName().trim());
		}
		if(StringUtils.isNotBlank(benePersonalDetailModel.getFourthName())) {
			beneName.append(benePersonalDetailModel.getFourthName().trim());
		}
		if(StringUtils.isNotBlank(benePersonalDetailModel.getFifthName())) {
			beneName.append(benePersonalDetailModel.getFifthName().trim());
		}
	
		List<BlackListModel> blist =blackListDao.getBlackByName(beneName.toString());
		
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException("Beneficiary name found matching with black list ",
					JaxError.BLACK_LISTED_BENEFICIARY.getCode());
		}
	}
	
	private void validateMobile(BenePersonalDetailModel benePersonalDetailModel,
			BeneficiaryTrnxModel beneficiaryTrnxModel) {

		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getBeneTelServiceApplicabilityRule(
				metaData.getCountryId(), benePersonalDetailModel.getCountryId(),
				beneficiaryTrnxModel.getBeneAccountModel().getCurrencyId());

		int benePhoneLength = benePersonalDetailModel.getMobileNumber().toString().length();
		
		int minLength = serviceAppList.stream().mapToInt(i -> {
			if (i.getMinLenght()!=null) {
			   return i.getMinLenght().intValue();
			}else { 
			  return -1;
			}  
		}).min().orElse(-1);
		
		int maxLength = serviceAppList.stream().mapToInt(i -> {
			if (i.getMaxLenght()!=null) {
				return i.getMaxLenght().intValue();
			}else {
				return -1;
			}
		}).max().orElse(-1);

		if (maxLength > 0 && benePhoneLength > maxLength) {
			throw new GlobalException(JaxError.VALIDATION_LENGTH_MOBILE, minLength, maxLength);
		}
		if (minLength > 0 && benePhoneLength < minLength) {
			throw new GlobalException(JaxError.VALIDATION_LENGTH_MOBILE, minLength, maxLength);
		}
	}

}
