package com.amx.jax.branchbene;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.model.request.benebranch.AddNewBankBranchRequest;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.utils.AgeUtil;

@Component
public class BeneBranchValidation {

	@Autowired
	MetaService metaService;
	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;

	public void validateaddBeneBank(AddBeneBankRequest request) {
		ServiceGroupMaster bankserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_BANK);
		if (!bankserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to bank");
		}
		validateAbtractBeneDetail(request);
	}

	public void validateaddBenecash(AddBeneCashRequest request) {
		ServiceGroupMaster cashserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_CASH);
		if (!cashserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to cash");
		}
		validateAbtractBeneDetail(request);
	}

	public void validateAbtractBeneDetail(AbstractBeneDetailDto request) {
		if (request.getDateOfBirth() == null) {
			if (request.getAge() == null || request.getYearOfBirth() == null) {
				throw new GlobalException("dob or age or year of birth is mandatory");
			}
		} else {
			request.setAge(AgeUtil.calculateAgeInYears(request.getDateOfBirth()));
			request.setYearOfBirth(AgeUtil.getYearOfBirthInt(request.getDateOfBirth()));
		}
		BeneficiaryTrnxModel beneTrnxModel = request.createBeneficiaryTrnxModelObject();
		BeneAccountModel beneAccountModel = beneTrnxModel.getBeneAccountModel();
		beneficiaryValidationService.validateIFscCode(beneAccountModel);
		// swift validation is already done inside online flow
	}

	public void validateAddNewBankBranchRequest(AddNewBankBranchRequest request) {
		BigDecimal beneBankCountryId = request.getCountryId();
		BigDecimal beneBankCurrencyId = request.getCurrencyId();
		String ifscCode = request.getIfscCode();
		beneficiaryValidationService.validateIFscCode(beneBankCountryId, beneBankCurrencyId, ifscCode);
		String swiftCode = request.getSwift();
		beneficiaryValidationService.validateSwiftCode(beneBankCountryId, beneBankCurrencyId, swiftCode);

	}
}
