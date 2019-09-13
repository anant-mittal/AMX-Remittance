package com.amx.jax.branchbene;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.AbtractUpdateBeneDetailDto;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.trnx.BeneficiaryTrnxManager;
import com.amx.jax.util.JaxUtil;
import com.jax.amxlib.exception.jax.GlobaLException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneBranchManager {

	private static final Logger log = LoggerFactory.getLogger(BeneBranchManager.class);

	@Autowired
	IBeneficiaryRelationshipDao iBeneficiaryRelationshipDao;
	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	MetaService metaService;
	@Autowired
	JaxDBService jaxDBService;
	@Autowired
	IBeneficaryContactDao beneficaryContactRepository;
	@Autowired
	BankService bankService;
	@Autowired
	BeneficiaryTrnxManager beneficiaryTrnxManager;

	public void updateBeneStatus(UpdateBeneStatusRequest request) {
		BeneficaryRelationship beneRel = iBeneficiaryRelationshipDao.findOne(BigDecimal.valueOf(request.getBeneRelationshipSeqId()));
		if (beneRel == null) {
			throw new GlobaLException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Bene not found");
		}
		beneRel.setIsActive(request.getStatusCode().getDbFlag());
		iBeneficiaryRelationshipDao.save(beneRel);
	}

	public void updateBeneMaster(BeneficaryRelationship beneRelationship, BenePersonalDetailModel benePersonalDetail,
			AbtractUpdateBeneDetailDto request) {
		BeneficaryMaster beneMaster = beneficiaryService.getBeneficiaryMasterBybeneficaryMasterSeqId(beneRelationship.getBeneficaryMasterId());
		if (benePersonalDetail.getCityId() != null) {
			beneMaster.setFsCityMaster(benePersonalDetail.getCityId());
		}
		if (benePersonalDetail.getCountryId() != null) {
			beneMaster.setFsCountryMaster(benePersonalDetail.getCountryId());
		}
		if (benePersonalDetail.getDistrictId() != null) {
			String districtName = metaService.getDistrictMasterById(benePersonalDetail.getDistrictId()).getDistrictDesc();
			beneMaster.setFsDistrictMaster(benePersonalDetail.getDistrictId());
			beneMaster.setDistrictName(districtName);
		}
		if (benePersonalDetail.getFifthName() != null) {
			beneMaster.setFifthName(benePersonalDetail.getFifthName());
		}
		if (benePersonalDetail.getFirstName() != null) {
			beneMaster.setFirstName(benePersonalDetail.getFirstName());
		}

		if (benePersonalDetail.getFourthName() != null) {
			beneMaster.setFourthName(benePersonalDetail.getFourthName());
		}
		if (benePersonalDetail.getLocalFifthName() != null) {
			beneMaster.setLocalFifthName(benePersonalDetail.getLocalFifthName());
		}
		if (benePersonalDetail.getLocalFirstName() != null) {
			beneMaster.setLocalFirstName(benePersonalDetail.getLocalFirstName());
		}
		if (benePersonalDetail.getLocalFourthName() != null) {
			beneMaster.setLocalFourthName(benePersonalDetail.getLocalFourthName());
		}
		if (benePersonalDetail.getLocalSecondName() != null) {
			beneMaster.setLocalSecondName(benePersonalDetail.getLocalSecondName());
		}
		if (benePersonalDetail.getLocalThirdName() != null) {
			beneMaster.setLocalThirdName(benePersonalDetail.getLocalThirdName());
		}
		if (benePersonalDetail.getNationality() != null) {
			beneMaster.setNationality(benePersonalDetail.getNationality());
		}
		if (benePersonalDetail.getSecondName() != null) {
			beneMaster.setSecondName(benePersonalDetail.getSecondName());
		}
		if (benePersonalDetail.getStateId() != null) {
			beneMaster.setFsStateMaster(benePersonalDetail.getStateId());
			String stateName = metaService.getStateMasterById(benePersonalDetail.getStateId()).getStateName();
			beneMaster.setStateName(stateName);
		}
		if (benePersonalDetail.getThirdName() != null) {
			beneMaster.setThirdName(benePersonalDetail.getThirdName());
		}
		if (request.getAge() != null) {
			beneMaster.setAge(BigDecimal.valueOf(request.getAge()));
		}
		try {
			boolean isModified = !JaxUtil.checkNull(benePersonalDetail);
			if (isModified) {
				beneMaster.setModifiedBy(jaxDBService.getCreatedOrUpdatedBy());
				beneMaster.setModifiedDate(new Date());
				beneficiaryService.saveBeneMaster(beneMaster);
			}
		} catch (IllegalAccessException e) {
			log.error("error in updateBeneAccount", e);
		}
	}

	public void updateBeneContact(BeneficaryRelationship beneRelationship, BenePersonalDetailModel beneDetails) {
		boolean isModified = false;
		BeneficaryContact beneficaryContact = beneficiaryService.getBeneficiaryContactByMasterId(beneRelationship.getBeneficaryMasterId());
		if (beneDetails.getCountryTelCode() != null) {
			beneficaryContact.setCountryTelCode(beneDetails.getCountryTelCode());
			isModified = true;
		}
		String beneMobileNo = null;
		if (beneDetails.getMobileNumber() != null) {
			beneMobileNo = beneDetails.getMobileNumber().toString();
			beneficaryContact.setTelephoneNumber((beneDetails.getTelephoneNumber() == null) ? beneMobileNo : beneDetails.getTelephoneNumber());
			isModified = true;
		}
		log.info("in updateBeneContact isModified: {}", isModified);
		if (isModified) {
			beneficaryContact.setModifiedBy(jaxDBService.getCreatedOrUpdatedBy());
			beneficaryContact.setModifiedDate(new Date());
			beneficaryContactRepository.save(beneficaryContact);
		}
	}

	public void updateBeneAccount(BeneficaryRelationship beneRelationship, BeneAccountModel accountDetails) {
		log.info("In updateBeneAccount");
		BeneficaryAccount beneficaryAccount = beneficiaryService.getBeneAccountByAccountSeqId(beneRelationship.getBeneficaryAccountId());

		if (accountDetails.getBankAccountNumber() != null) {
			beneficaryAccount.setBankAccountNumber(accountDetails.getBankAccountNumber());
		}
		if (accountDetails.getBankId() != null) {
			beneficaryAccount.setBankId(accountDetails.getBankId());
		}
		if (accountDetails.getBankId() != null) {
			beneficaryAccount.setBankCode(bankService.getBankById(accountDetails.getBankId()).getBankCode());
		}
		if (accountDetails.getBeneficaryCountryId() != null) {
			beneficaryAccount.setBeneficaryCountryId(accountDetails.getBeneficaryCountryId());
		}
		if (accountDetails.getCurrencyId() != null) {
			beneficaryAccount.setCurrencyId(accountDetails.getCurrencyId());
		}
		if (accountDetails.getServiceGroupId() != null) {
			beneficaryAccount.setServiceGroupId(accountDetails.getServiceGroupId());
		}
		if (accountDetails.getServiceProviderBranchId() != null) {
			beneficaryAccount.setServiceProviderBranchId(accountDetails.getServiceProviderBranchId());
		}
		if (accountDetails.getServiceProviderId() != null) {
			beneficaryAccount.setServiceProviderId(accountDetails.getServiceProviderId());
		}
		if (accountDetails.getSwiftCode() != null) {
			beneficaryAccount.setSwiftCode(accountDetails.getSwiftCode());
		}

		// cash
		if (BigDecimal.ONE.equals(beneficaryAccount.getServiceGroupId())) {
			BigDecimal bankBranchId = accountDetails.getServiceProviderBranchId();
			if (bankBranchId != null) {
				beneficaryAccount.setBankBranchCode(beneficiaryTrnxManager.getBankBranchCode(accountDetails.getBankId(), bankBranchId));
				beneficaryAccount.setBankBranchId(bankBranchId);
			}
		} else {
			if (accountDetails.getBankBranchId() != null) {
				beneficaryAccount
						.setBankBranchCode(beneficiaryTrnxManager.getBankBranchCode(accountDetails.getBankId(), accountDetails.getBankBranchId()));
				beneficaryAccount.setBankBranchId(accountDetails.getBankBranchId());
			}
		}
		if (accountDetails.getBankAccountTypeId() != null) {
			beneficaryAccount.setBankAccountTypeId(accountDetails.getBankAccountTypeId());
		}
		if (accountDetails.getIbanNumber() != null) {
			beneficaryAccount.setIbanNumber(accountDetails.getIbanNumber());
		}
		try {
			boolean isModified = !JaxUtil.checkNull(accountDetails);
			if (isModified) {
				beneficaryAccount.setModifiedBy(jaxDBService.getCreatedOrUpdatedBy());
				beneficaryAccount.setModifiedDate(new Date());
				beneficiaryService.saveBeneAccount(beneficaryAccount);
			}
		} catch (IllegalAccessException e) {
			log.error("error in updateBeneAccount", e);
		}

	}

}
