package com.amx.jax.trnx;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.JaxChannel;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.BeneficaryStatusRepository;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;

@Component
@SuppressWarnings("rawtypes")
public class BeneficiaryTrnxManager extends JaxTransactionManager<BeneficiaryTrnxModel> {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IBeneficiaryMasterDao beneficaryMasterRepository;

	@Autowired
	IBeneficaryContactDao beneficaryContactRepository;

	@Autowired
	IBeneficiaryRelationshipDao beneficiaryRelationshipDao;

	@Autowired
	IBeneficiaryAccountDao beneficiaryAccountDao;

	@Autowired
	BeneficaryStatusRepository beneficaryStatusRepository;

	@Autowired
	MetaData metaData;

	@Override
	public BeneficiaryTrnxModel init() {
		BeneficiaryTrnxModel model = new BeneficiaryTrnxModel();
		save(model);
		return model;
	}

	@Override
	@Transactional
	public BeneficiaryTrnxModel commit() {
		BeneficiaryTrnxModel beneficiaryTrnxModel = get();
		logger.info("commiting beneficary: " + beneficiaryTrnxModel.toString());
		BeneficaryMaster beneMaster = commitBeneMaster(beneficiaryTrnxModel);
		commitBeneContact(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		BeneficaryAccount beneAccount = commitBeneAccount(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		commitBeneRelationship(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
				beneAccount.getBeneficaryAccountSeqId());
		logger.info("commit done");

		return beneficiaryTrnxModel;
	}

	private BeneficaryAccount commitBeneAccount(BeneficiaryTrnxModel beneficiaryTrnxModel,
			BigDecimal beneficaryMasterId) {
		BeneAccountModel accountDetails = beneficiaryTrnxModel.getBeneAccountModel();
		BeneficaryAccount beneficaryAccount = new BeneficaryAccount();
		beneficaryAccount.setBankAccountNumber(accountDetails.getBankAccountNumber());
		beneficaryAccount.setBankAccountTypeId(accountDetails.getBankAccountTypeId());
		beneficaryAccount.setBankBranchId(accountDetails.getBankBranchId());
		beneficaryAccount.setBankId(accountDetails.getBankId());
		beneficaryAccount.setBeneApplicationCountryId(metaData.getCountryId());
		beneficaryAccount.setBeneficaryCountryId(accountDetails.getBeneficaryCountryId());
		beneficaryAccount.setBeneficaryMasterId(beneficaryMasterId);
		beneficaryAccount.setCreatedBy(metaData.getReferrer());
		beneficaryAccount.setCurrencyId(accountDetails.getCurrencyId());
		beneficaryAccount.setIsActive(ConstantDocument.Yes);
		beneficaryAccount.setServicegropupId(accountDetails.getServicegropupId());
		beneficaryAccount.setServiceProviderBranchId(accountDetails.getServiceProviderBranchId());
		beneficaryAccount.setServiceProviderId(accountDetails.getServiceProviderId());
		beneficiaryAccountDao.save(beneficaryAccount);
		return beneficaryAccount;
	}

	private void commitBeneRelationship(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneficaryMasterId,
			BigDecimal beneficaryAccountId) {
		BenePersonalDetailModel beneDetaisl = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryRelationship beneficaryRelationship = new BeneficaryRelationship();
		beneficaryRelationship.setApplicationCountry(metaData.getCountryId());
		beneficaryRelationship.setBeneficaryMasterId(beneficaryMasterId);
		beneficaryRelationship.setBeneficaryAccountId(beneficaryAccountId);
		beneficaryRelationship.setCreatedBy(metaData.getReferrer());
		beneficaryRelationship.setCreatedDate(new Date());
		beneficaryRelationship.setCustomerId(metaData.getCustomerId());
		beneficaryRelationship.setIsActive(ConstantDocument.Yes);
		beneficaryRelationship.setRelationsId(beneDetaisl.getRelationsId());
		beneficiaryRelationshipDao.save(beneficaryRelationship);

	}

	private void commitBeneContact(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneficaryMasterId) {
		BenePersonalDetailModel beneDetails = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryContact beneficaryContact = new BeneficaryContact();
		beneficaryContact.setApplicationCountryId(metaData.getCountryId());
		beneficaryContact.setBeneficaryMasterId(beneficaryMasterId);
		beneficaryContact.setCountryTelCode(beneDetails.getCountryTelCode());
		beneficaryContact.setCreatedBy(metaData.getReferrer());
		beneficaryContact.setCreatedDate(new Date());
		beneficaryContact.setIsActive(ConstantDocument.Yes);
		beneficaryContact.setMobileNumber(beneDetails.getMobileNumber());
		beneficaryContact.setTelephoneNumber(beneDetails.getTelephoneNumber());
		beneficaryContactRepository.save(beneficaryContact);

	}

	private BeneficaryMaster commitBeneMaster(BeneficiaryTrnxModel beneficiaryTrnxModel) {
		BenePersonalDetailModel benePersonalDetails = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryMaster beneMaster = new BeneficaryMaster();
		beneMaster.setApplicationCountryId(metaData.getCountryId());
		BeneficaryStatus beneStatus = getbeneStatus();
		beneMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
		beneMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());
		beneMaster.setCreatedBy(metaData.getReferrer());
		beneMaster.setCreatedDate(new Date());
		// names
		setNames(beneMaster, benePersonalDetails);
		beneMaster.setFsCityMaster(benePersonalDetails.getFsCityId());
		beneMaster.setFsCountryMaster(benePersonalDetails.getFsCountryId());
		beneMaster.setFsDistrictMaster(benePersonalDetails.getFsDistrictId());
		beneMaster.setFsStateMaster(benePersonalDetails.getFsStateId());
		beneMaster.setIsActive(ConstantDocument.Yes);
		beneMaster.setNationality(benePersonalDetails.getNationality());

		return beneficaryMasterRepository.save(beneMaster);

	}

	private void setNames(BeneficaryMaster beneMaster, BenePersonalDetailModel benePersonalDetails) {
		beneMaster.setFirstName(benePersonalDetails.getFirstName());
		beneMaster.setSecondName(benePersonalDetails.getSecondName());
		beneMaster.setThirdName(benePersonalDetails.getThirdName());
		beneMaster.setFourthName(benePersonalDetails.getFourthName());
		beneMaster.setFifthName(benePersonalDetails.getFifthName());
		beneMaster.setLocalFifthName(benePersonalDetails.getLocalFifthName());
		beneMaster.setLocalFirstName(benePersonalDetails.getLocalFirstName());
		beneMaster.setLocalFourthName(benePersonalDetails.getLocalFourthName());
		beneMaster.setLocalSecondName(benePersonalDetails.getLocalSecondName());
		beneMaster.setLocalThirdName(benePersonalDetails.getLocalThirdName());
	}

	private BeneficaryStatus getbeneStatus() {
		if (JaxChannel.ONLINE.equals(metaData.getChannel())) {
			return beneficaryStatusRepository.findByBeneficaryStatusName(ConstantDocument.INDIVIDUAL_STRING);
		} else {
			return beneficaryStatusRepository.findByBeneficaryStatusName(ConstantDocument.NON_INDIVIDUAL_STRING);
		}
	}

	public ApiResponse commitTransaction() {
		ApiResponse apiResponse = getJaxTransactionApiResponse();
		apiResponse.getData().getValues().add(get());

		return apiResponse;
	}

	public ApiResponse saveBeneAccountTrnx(BeneAccountModel beneAccountModel) {
		BeneficiaryTrnxModel trnxModel = getWithInit();
		trnxModel.setBeneAccountModel(beneAccountModel);
		save(trnxModel);
		ApiResponse apiResponse = getJaxTransactionApiResponse();

		return apiResponse;
	}

	public ApiResponse savePersonalDetailTrnx(BenePersonalDetailModel benePersonalDetailModel) {
		BeneficiaryTrnxModel trnxModel = getWithInit();
		trnxModel.setBenePersonalDetailModel(benePersonalDetailModel);
		save(trnxModel);
		ApiResponse apiResponse = getJaxTransactionApiResponse();

		return apiResponse;
	}

}
