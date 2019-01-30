package com.amx.jax.trnx;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import com.amx.amxlib.constant.AuthType;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dao.BeneficiaryDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.repository.BeneficaryStatusRepository;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.validation.BenePersonalDetailValidator;

/**
 * @author Prashant
 *
 */
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
	UserService userService;

	@Autowired
	BankService bankService;

	@Autowired
	BeneficiaryValidationService beneficiaryValidationService;

	@Autowired
	MetaService metaService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	BenePersonalDetailValidator benePersonalDetailValidator;

	@Autowired
	BeneficiaryDao beneficiaryDao;

	@Override
	public BeneficiaryTrnxModel init() {
		BeneficiaryTrnxModel model = new BeneficiaryTrnxModel();
		save(model);
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.cache.TransactionModel#commit()
	 */
	@Override
	@Transactional
	public BeneficiaryTrnxModel commit() {
		BeneficiaryTrnxModel beneficiaryTrnxModel = get();
		logger.info("commiting beneficary: " + beneficiaryTrnxModel.toString());
		BeneficaryMaster beneMaster = commitBeneMaster(beneficiaryTrnxModel);
		commitBeneContact(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		BeneficaryAccount beneAccount = commitBeneAccount(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		BeneficaryRelationship beneRelationship = commitBeneRelationship(beneficiaryTrnxModel,
				beneMaster.getBeneficaryMasterSeqId(), beneAccount.getBeneficaryAccountSeqId());
		logger.info("commit done");
		logger.info("Beneficiary Relationship Sequence Id : " +beneRelationship.getBeneficaryRelationshipId());
		populateOldEmosData(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
				beneAccount);
		beneRelationship = beneficiaryRelationshipDao.findOne(beneRelationship.getBeneficaryRelationshipId());
		if (beneRelationship.getMapSequenceId() == null) {
			logger.info("Map sequence is null for bene rel seq id: {}", beneRelationship.getBeneficaryRelationshipId());
			populateOldEmosData(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
					beneAccount);
		}else {
			logger.info("Map Sequence Id generated: {}", beneRelationship.getMapSequenceId());
		}
		return beneficiaryTrnxModel;
	}

	/**
	 * after bene is created steps
	 * 
	 * @param beneficiaryTrnxModel
	 * 
	 */
	private void populateOldEmosData(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneMasterSeqId,
			BeneficaryAccount beneAccount) {
		BeneAccountModel accModel = beneficiaryTrnxModel.getBeneAccountModel();
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_BENE_MASTER_ID", beneMasterSeqId);
		inputValues.put("P_BANK_ID", accModel.getBankId());
		inputValues.put("P_BANK_BRANCH_ID", beneAccount.getBankBranchId());
		inputValues.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneAccount.getBeneficaryAccountSeqId());
		inputValues.put("P_CURRENCY_ID", accModel.getCurrencyId());
		inputValues.put("P_CUSTOMER_ID", metaData.getCustomerId());
		beneficiaryDao.populateBeneDt(inputValues);
	}

	/**
	 * commits bene account in db
	 * 
	 * @param beneficiaryTrnxModel
	 * @param beneficaryMasterId
	 * @return bene account
	 * 
	 */
	private BeneficaryAccount commitBeneAccount(BeneficiaryTrnxModel beneficiaryTrnxModel,
			BigDecimal beneficaryMasterId) {
		if (beneficiaryTrnxModel.getBeneficaryAccountSeqId() != null) {
			logger.info("existing bene account id: " + beneficiaryTrnxModel.getBeneficaryAccountSeqId());
			return beneficiaryAccountDao.findOne(beneficiaryTrnxModel.getBeneficaryAccountSeqId());
		}
		BeneAccountModel accountDetails = beneficiaryTrnxModel.getBeneAccountModel();
		BeneficaryAccount beneficaryAccount = null;
		if (!BigDecimal.ONE.equals(accountDetails.getServiceGroupId())) {
			beneficaryAccount = beneficiaryValidationService.getBeneficaryAccount(accountDetails);
		}

		if (beneficaryAccount == null) {
			logger.info("creating new bene account");
			beneficaryAccount = new BeneficaryAccount();
			beneficaryAccount.setBankAccountNumber(accountDetails.getBankAccountNumber());
			beneficaryAccount.setBankId(accountDetails.getBankId());
			beneficaryAccount.setBankCode(bankService.getBankById(accountDetails.getBankId()).getBankCode());
			beneficaryAccount.setBeneApplicationCountryId(metaData.getCountryId());
			beneficaryAccount.setBeneficaryCountryId(accountDetails.getBeneficaryCountryId());
			beneficaryAccount.setBeneficaryMasterId(beneficaryMasterId);
			beneficaryAccount.setCreatedBy(getCreatedBy());
			beneficaryAccount.setCreatedDate(new Date());
			beneficaryAccount.setCurrencyId(accountDetails.getCurrencyId());
			beneficaryAccount.setIsActive(ConstantDocument.Yes);
			beneficaryAccount.setServiceGroupId(accountDetails.getServiceGroupId());
			beneficaryAccount.setServiceProviderBranchId(accountDetails.getServiceProviderBranchId());
			beneficaryAccount.setServiceProviderId(accountDetails.getServiceProviderId());
			beneficaryAccount.setSwiftCode(accountDetails.getSwiftCode());

			// cash
			if (BigDecimal.ONE.equals(beneficaryAccount.getServiceGroupId())) {
				BigDecimal bankBranchId = accountDetails.getServiceProviderBranchId();
				beneficaryAccount.setBankBranchCode(getBankBranchCode(accountDetails.getBankId(), bankBranchId));
				beneficaryAccount.setBankBranchId(bankBranchId);
			} else {
				beneficaryAccount.setBankBranchCode(
						getBankBranchCode(accountDetails.getBankId(), accountDetails.getBankBranchId()));
				beneficaryAccount.setBankBranchId(accountDetails.getBankBranchId());
			}
			beneficaryAccount.setBankAccountTypeId(accountDetails.getBankAccountTypeId());

			beneficiaryAccountDao.save(beneficaryAccount);
			logger.info("created new bene account id: " + beneficaryAccount.getBeneficaryAccountSeqId());
		} else {
			logger.info("existing bene account id: " + beneficaryAccount.getBeneficaryAccountSeqId());
		}

		return beneficaryAccount;
	}

	/**
	 * @param bankId
	 * @param bankBranchId
	 * @return bankbranch code
	 * 
	 */
	private BigDecimal getBankBranchCode(BigDecimal bankId, BigDecimal bankBranchId) {
		return bankService.getBankBranchView(bankId, bankBranchId).getBranchCode();
	}

	/**
	 * commits bene relationship in db
	 * 
	 * @param beneficiaryTrnxModel
	 * @param beneficaryMasterId
	 * @param beneficaryAccountId
	 * @return 
	 * 
	 */
	private BeneficaryRelationship commitBeneRelationship(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneficaryMasterId,
			BigDecimal beneficaryAccountId) {
		// TODO: set all 10 bene names in bene relationship
		BenePersonalDetailModel beneDetaisl = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryRelationship beneficaryRelationship = new BeneficaryRelationship();
		beneficaryRelationship.setApplicationCountry(metaData.getCountryId());
		beneficaryRelationship.setBeneficaryMasterId(beneficaryMasterId);
		beneficaryRelationship.setBeneficaryAccountId(beneficaryAccountId);
		beneficaryRelationship.setCreatedBy(getCreatedBy());
		beneficaryRelationship.setCreatedDate(new Date());
		beneficaryRelationship.setCustomerId(metaData.getCustomerId());
		beneficaryRelationship.setIsActive(ConstantDocument.Yes);
		beneficaryRelationship.setOrsSatus(BigDecimal.ONE); // for online
		beneficaryRelationship.setRelationsId(beneDetaisl.getRelationsId());

		beneficaryRelationship.setFirstName(beneDetaisl.getFirstName());
		beneficaryRelationship.setSecondName(beneDetaisl.getSecondName());
		beneficaryRelationship.setThirdName(beneDetaisl.getThirdName());
		beneficaryRelationship.setFourthName(beneDetaisl.getFourthName());
		beneficaryRelationship.setFifthName(beneDetaisl.getFifthName());
		beneficaryRelationship.setLocalFirstName(beneDetaisl.getLocalFirstName());
		beneficaryRelationship.setLocalSecondName(beneDetaisl.getLocalSecondName());
		beneficaryRelationship.setLocalThirdName(beneDetaisl.getLocalThirdName());
		beneficaryRelationship.setLocalFourthName(beneDetaisl.getLocalFourthName());
		beneficaryRelationship.setLocalFifthName(beneDetaisl.getLocalFifthName());
		
		beneficaryRelationship.setDeviceIp(metaData.getDeviceIp());
		beneficaryRelationship.setDeviceType(metaData.getDeviceType());
		
		beneficiaryRelationshipDao.save(beneficaryRelationship);
		return beneficaryRelationship;
	}

	/**
	 * commits bene contact in trnx
	 * 
	 * @param beneficiaryTrnxModel
	 * @param beneficaryMasterId
	 * 
	 */
	private void commitBeneContact(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneficaryMasterId) {
		BenePersonalDetailModel beneDetails = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneficaryContact beneficaryContact = new BeneficaryContact();
		beneficaryContact.setApplicationCountryId(metaData.getCountryId());
		beneficaryContact.setBeneficaryMasterId(beneficaryMasterId);
		if(beneDetails.getMobileNumber() != null) {
			beneficaryContact.setCountryTelCode(beneDetails.getCountryTelCode());
		}	
		beneficaryContact.setCreatedBy(getCreatedBy());
		beneficaryContact.setCreatedDate(new Date());
		beneficaryContact.setIsActive(ConstantDocument.Yes);
		beneficaryContact.setMobileNumber(beneDetails.getMobileNumber());
		beneficaryContact.setTelephoneNumber(
				(beneDetails.getTelephoneNumber() == null) ? beneDetails.getMobileNumber().toString()
						: beneDetails.getTelephoneNumber());
		beneficaryContactRepository.save(beneficaryContact);

	}

	/**
	 * commits bene master in db
	 * 
	 * @param beneficiaryTrnxModel
	 * @return bene master data
	 * 
	 */
	private BeneficaryMaster commitBeneMaster(BeneficiaryTrnxModel beneficiaryTrnxModel) {
		if (beneficiaryTrnxModel.getBeneficaryMasterSeqId() != null) {
			logger.info("existing new bene master id: " + beneficiaryTrnxModel.getBeneficaryMasterSeqId());
			return beneficaryMasterRepository.findOne(beneficiaryTrnxModel.getBeneficaryMasterSeqId());
		}
		BenePersonalDetailModel benePersonalDetails = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneAccountModel accountDetails = beneficiaryTrnxModel.getBeneAccountModel();
		BeneficaryAccount beneficaryAccount = null;
		if(!BigDecimal.ONE.equals(accountDetails.getServiceGroupId())) {
			beneficaryAccount = beneficiaryValidationService.getBeneficaryAccount(accountDetails);
		}
		BeneficaryMaster beneMaster = null;
		if (beneficaryAccount != null && !BigDecimal.ONE.equals(accountDetails.getServiceGroupId())) {
			beneMaster = beneficaryMasterRepository
					.findByBeneficaryMasterSeqId(beneficaryAccount.getBeneficaryMasterId());
		}

		if (beneMaster == null) {
			logger.info("creating new bene maseter");
			beneMaster = new BeneficaryMaster();
			beneMaster.setApplicationCountryId(metaData.getCountryId());
			BeneficaryStatus beneStatus = getbeneStatus();
			beneMaster.setBeneficaryStatus(beneStatus.getBeneficaryStatusId());
			beneMaster.setBeneficaryStatusName(beneStatus.getBeneficaryStatusName());
			beneMaster.setCreatedBy(getCreatedBy());
			beneMaster.setCreatedDate(new Date());
			// names
			setNames(beneMaster, benePersonalDetails);
			beneMaster.setFsCityMaster(benePersonalDetails.getCityId());
			beneMaster.setFsCountryMaster(benePersonalDetails.getCountryId());
			beneMaster.setFsDistrictMaster(benePersonalDetails.getDistrictId());
			String districtName = metaService.getDistrictMasterById(benePersonalDetails.getDistrictId())
					.getDistrictDesc();
			beneMaster.setDistrictName(districtName);
			beneMaster.setFsStateMaster(benePersonalDetails.getStateId());
			String stateName = metaService.getStateMasterById(benePersonalDetails.getStateId()).getStateName();
			beneMaster.setStateName(stateName);
			beneMaster.setIsActive(ConstantDocument.Yes);
			beneMaster.setNationality(benePersonalDetails.getNationality());

			beneficaryMasterRepository.save(beneMaster);
			logger.info("created new bene master id: " + beneMaster.getBeneficaryMasterSeqId());
		} else {
			logger.info("existing bene master: " + beneMaster.getBeneficaryMasterSeqId());
		}

		return beneMaster;
	}

	/**
	 * @param beneMaster
	 * @param benePersonalDetails
	 * 
	 */
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

	/**
	 * @return status of bene
	 * 
	 */
	private BeneficaryStatus getbeneStatus() {
		if (JaxChannel.ONLINE.equals(metaData.getChannel())) {
			return beneficaryStatusRepository.findByBeneficaryStatusName(ConstantDocument.INDIVIDUAL_STRING);
		} else {
			return beneficaryStatusRepository.findByBeneficaryStatusName(ConstantDocument.NON_INDIVIDUAL_STRING);
		}
	}

	/**
	 * commits trnx validating otps
	 * 
	 * @param mOtp
	 * @param eOtp
	 * @return apiresponse
	 * 
	 */
	public ApiResponse commitTransaction(String mOtp, String eOtp) {
		userService.validateOtp(null, mOtp, eOtp);
		commit();
		ApiResponse apiResponse = getBlankApiResponse();
		apiResponse.getData().setType("bene-trnx-model");
		BeneficiaryTrnxModel output = get();
		AuthenticationLimitCheckView authLimit = parameterService
				.getAuthenticationViewRepository(AuthType.NEW_BENE_TRANSACT_AMOUNT_LIMIT.getAuthType());
		if (authLimit != null) {
			output.setBeneTransactionAmountLimit(authLimit.getAuthLimit());
		}
		apiResponse.getData().getValues().add(output);

		return apiResponse;
	}

	/**
	 * save account in trnx
	 * 
	 * @param beneAccountModel
	 * @return
	 * 
	 */
	public ApiResponse saveBeneAccountTrnx(BeneAccountModel beneAccountModel) {
		beneficiaryValidationService.validateBeneAccount(beneAccountModel);
		BeneficiaryTrnxModel trnxModel = getWithInit();
		trnxModel.setBeneAccountModel(beneAccountModel);
		save(trnxModel);
		ApiResponse apiResponse = getJaxTransactionApiResponse();

		return apiResponse;
	}

	/**
	 * @param benePersonalDetailModel
	 * @return apiresponse
	 * 
	 */
	public ApiResponse savePersonalDetailTrnx(BenePersonalDetailModel benePersonalDetailModel) {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(benePersonalDetailModel,
				"benePersonalDetailModel");
		BeneficiaryTrnxModel trnxModel = getWithInit();
		trnxModel.setBenePersonalDetailModel(benePersonalDetailModel);

		// check for CASH channel only
		if (trnxModel.getBeneAccountModel() != null
				&& BigDecimal.ONE.equals(trnxModel.getBeneAccountModel().getServiceGroupId())) {
			beneficiaryValidationService.validateDuplicateCashBeneficiary(trnxModel);
		}
		benePersonalDetailValidator.validate(trnxModel, errors);
		save(trnxModel);
		ApiResponse apiResponse = getJaxTransactionApiResponse();

		return apiResponse;
	}

	@Override
	public String getJaxTransactionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
