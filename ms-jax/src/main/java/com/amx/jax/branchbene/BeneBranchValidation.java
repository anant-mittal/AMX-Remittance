package com.amx.jax.branchbene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.model.request.AbstractBeneDetailDto;
import com.amx.jax.model.request.benebranch.AddBeneBankRequest;
import com.amx.jax.model.request.benebranch.AddBeneCashRequest;
import com.amx.jax.service.MetaService;
import com.amx.utils.AgeUtil;

@Component
public class BeneBranchValidation {

	@Autowired
	MetaService metaService;

	public void validateaddBeneBank(AddBeneBankRequest request) {
		ServiceGroupMaster bankserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_BANK);
		if (!bankserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to bank");
		}

	}

	public void validateaddBenecash(AddBeneCashRequest request) {
		ServiceGroupMaster cashserviceMaster = metaService.getServiceGroupMasterByCode(ConstantDocument.SERVICE_GROUP_CODE_CASH);
		if (!cashserviceMaster.getServiceGroupId().equals(request.getServiceGroupId())) {
			throw new GlobalException("service group id does not belong to cash");
		}
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
	}
}
