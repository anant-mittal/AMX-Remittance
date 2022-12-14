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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.AbtractUpdateBeneDetailDto;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.repository.BeneficaryStatusRepository;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.service.MetaService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.JaxDBService;
import com.amx.jax.trnx.BeneficiaryTrnxManager;
import com.amx.jax.util.JaxUtil;

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
	@Autowired
	BeneAccountManager beneAccountManager;
	@Autowired
	BeneficaryStatusRepository beneficaryStatusRepository;

	public void updateBeneStatus(UpdateBeneStatusRequest request) {
		BeneficaryRelationship beneRel = iBeneficiaryRelationshipDao.findOne(BigDecimal.valueOf(request.getBeneRelationshipSeqId()));
		if (beneRel == null) {
			throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Bene not found");
		}
		//
		beneRel.setIsActive(request.getStatusCode().getDbFlag());
		beneRel.setReasonCodeId(request.getReasonCodeId());
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
		if (benePersonalDetail.getNationality() != null) {
			beneMaster.setNationality(benePersonalDetail.getNationality());
		}
		if (benePersonalDetail.getStateId() != null) {
			beneMaster.setFsStateMaster(benePersonalDetail.getStateId());
			String stateName = metaService.getStateMasterById(benePersonalDetail.getStateId()).getStateName();
			beneMaster.setStateName(stateName);
		}

		if (request.getAge() != null) {
			beneMaster.setAge(BigDecimal.valueOf(request.getAge()));
		}
		if (request.getBeneficaryTypeId() != null) {
			BeneficaryStatus beneStatus = beneficaryStatusRepository.findOne(request.getBeneficaryTypeId());
			beneMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
			beneMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());
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

	public void updateBeneRelationship(BeneficaryRelationship beneRelationship, BenePersonalDetailModel benePersonalDetail,
			AbtractUpdateBeneDetailDto request) {
		boolean isModified = false;

		if (benePersonalDetail.getFifthName() != null) {
			beneRelationship.setFifthName(benePersonalDetail.getFifthName());
			isModified = true;
		}
		if (benePersonalDetail.getFirstName() != null) {
			beneRelationship.setFirstName(benePersonalDetail.getFirstName());
			isModified = true;
		}

		if (benePersonalDetail.getFourthName() != null) {
			beneRelationship.setFourthName(benePersonalDetail.getFourthName());
			isModified = true;
		}
		if (benePersonalDetail.getLocalFifthName() != null) {
			beneRelationship.setLocalFifthName(benePersonalDetail.getLocalFifthName());
			isModified = true;
		}
		if (benePersonalDetail.getLocalFirstName() != null) {
			beneRelationship.setLocalFirstName(benePersonalDetail.getLocalFirstName());
			isModified = true;
		}
		if (benePersonalDetail.getLocalFourthName() != null) {
			beneRelationship.setLocalFourthName(benePersonalDetail.getLocalFourthName());
			isModified = true;
		}
		if (benePersonalDetail.getLocalSecondName() != null) {
			beneRelationship.setLocalSecondName(benePersonalDetail.getLocalSecondName());
			isModified = true;
		}
		if (benePersonalDetail.getLocalThirdName() != null) {
			beneRelationship.setLocalThirdName(benePersonalDetail.getLocalThirdName());
			isModified = true;
		}
		if (benePersonalDetail.getSecondName() != null) {
			beneRelationship.setSecondName(benePersonalDetail.getSecondName());
			isModified = true;
		}
		if (benePersonalDetail.getThirdName() != null) {
			beneRelationship.setThirdName(benePersonalDetail.getThirdName());
			isModified = true;
		}

		// update institute names
		if (benePersonalDetail.getInstitutionName() != null) {
			beneRelationship.setFirstName(beneficiaryTrnxManager.getInstitutionFirstName(benePersonalDetail));
			beneRelationship.setSecondName(beneficiaryTrnxManager.getInstitutionSecondName(benePersonalDetail));
		}
		if (benePersonalDetail.getInstitutionNameLocal() != null) {
			beneRelationship.setLocalFirstName(beneficiaryTrnxManager.getInstitutionFirstNameLocal(benePersonalDetail));
			beneRelationship.setLocalSecondName(beneficiaryTrnxManager.getInstitutionSecondNameLocal(benePersonalDetail));
		}
		beneRelationship.setModifiedBy(jaxDBService.getCreatedOrUpdatedBy());
		beneRelationship.setModifiedDate(new Date());
		iBeneficiaryRelationshipDao.save(beneRelationship);
	}

	public void updateBeneContact(BeneficaryRelationship beneRelationship, BenePersonalDetailModel beneDetails) {
		boolean isModified = false;
		BeneficaryContact beneficaryContact = beneficiaryService.getBeneficiaryContactByMasterId(beneRelationship.getBeneficaryMasterId());
		if (beneDetails.getCountryTelCode() != null) {
			beneficaryContact.setCountryTelCode(beneDetails.getCountryTelCode());
			isModified = true;
		}
		if (beneDetails.getMobileNumber() != null) {
			beneficaryContact.setTelephoneNumber(beneDetails.getMobileNumber());
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
			beneficaryAccount.setServiceProviderBranchId(beneAccountManager.getRoutingBankBranchId(accountDetails));
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
