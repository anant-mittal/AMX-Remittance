package com.amx.jax.validation;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ServiceApplicabilityRule;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.ExceptionMessageKey;
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
		validateBeneArabicBlacklist(benePersonalDetailModel);
	}

	private void validateBeneBlacklist(BenePersonalDetailModel benePersonalDetailModel) {
		StringBuilder beneName = new StringBuilder();
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFirstName())) {
			beneName.append(benePersonalDetailModel.getFirstName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getSecondName())) {
			beneName.append(benePersonalDetailModel.getSecondName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getThirdName())) {
			beneName.append(benePersonalDetailModel.getThirdName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFourthName())) {
			beneName.append(benePersonalDetailModel.getFourthName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getFifthName())) {
			beneName.append(benePersonalDetailModel.getFifthName().trim());
		}

		List<BlackListModel> blist = blackListDao.getBlackByName(beneName.toString());

		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException(JaxError.BLACK_LISTED_BENEFICIARY.getStatusKey(),
					"The beneficiary you have selected has been black-listed by CBK ");
		}
	}

	private void validateBeneArabicBlacklist(BenePersonalDetailModel benePersonalDetailModel) {
		StringBuilder beneArabicName = new StringBuilder();
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFirstName())) {
			beneArabicName.append(benePersonalDetailModel.getLocalFirstName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalSecondName())) {
			beneArabicName.append(benePersonalDetailModel.getLocalSecondName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalThirdName())) {
			beneArabicName.append(benePersonalDetailModel.getLocalThirdName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFourthName())) {
			beneArabicName.append(benePersonalDetailModel.getLocalFourthName().trim());
		}
		if (StringUtils.isNotBlank(benePersonalDetailModel.getLocalFifthName())) {
			beneArabicName.append(benePersonalDetailModel.getLocalFifthName().trim());
		}

		if (beneArabicName.toString() != null && !beneArabicName.toString().isEmpty()) {
			List<BlackListModel> blist = blackListDao.getBlackByLocalName(beneArabicName.toString());

			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(JaxError.BLACK_LISTED_ARABIC_BENEFICIARY.getStatusKey(),
						"Beneficiary Arabic name found matching with black list ");
			}
		}
	}

	private void validateMobile(BenePersonalDetailModel benePersonalDetailModel,
			BeneficiaryTrnxModel beneficiaryTrnxModel) {

		List<ServiceApplicabilityRule> serviceAppList = serviceApplicabilityRuleDao.getBeneTelServiceApplicabilityRule(
				metaData.getCountryId(), benePersonalDetailModel.getCountryId(),
				beneficiaryTrnxModel.getBeneAccountModel().getCurrencyId());

		int benePhoneLength = benePersonalDetailModel.getMobileNumber().toString().length();

		int minLength = serviceAppList.stream().filter(i -> i.getMinLenght()  != null).mapToInt(i -> {
            return i.getMinLenght().intValue();
		}).min().orElse(-1);
            
		int maxLength = serviceAppList.stream().filter(i -> i.getMaxLenght()  != null).mapToInt(i -> {
			return i.getMaxLenght().intValue();
		}).max().orElse(-1);

		if (maxLength > 0 && benePhoneLength > maxLength) {
			throw new GlobalException(JaxError.VALIDATION_LENGTH_MOBILE,
					ExceptionMessageKey.build(JaxError.VALIDATION_LENGTH_MOBILE, minLength, maxLength));
		}
		if (minLength > 0 && benePhoneLength < minLength) {
			throw new GlobalException(JaxError.VALIDATION_LENGTH_MOBILE,
					ExceptionMessageKey.build(JaxError.VALIDATION_LENGTH_MOBILE, minLength, maxLength));
		}
		
	}

}
