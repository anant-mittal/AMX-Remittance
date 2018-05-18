package com.amx.jax.trnx;

import java.math.BigDecimal;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import com.amx.amxlib.constant.AuthType;
import com.amx.amxlib.constant.JaxChannel;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BenePersonalDetailModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.constant.ConstantDocument;
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
		commitBeneRelationship(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
				beneAccount.getBeneficaryAccountSeqId());
		logger.info("commit done");

		return beneficiaryTrnxModel;
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
			return beneficiaryAccountDao.findOne(beneficiaryTrnxModel.getBeneficaryAccountSeqId());
		}
		BeneAccountModel accountDetails = beneficiaryTrnxModel.getBeneAccountModel();
		BeneficaryAccount beneficaryAccount = beneficiaryValidationService.getBeneficaryAccount(accountDetails);

		if (beneficaryAccount == null) {

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
	 * 
	 */
	private void commitBeneRelationship(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal beneficaryMasterId,
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

		beneficiaryRelationshipDao.save(beneficaryRelationship);

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
		beneficaryContact.setCountryTelCode(beneDetails.getCountryTelCode());
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
			return beneficaryMasterRepository.findOne(beneficiaryTrnxModel.getBeneficaryMasterSeqId());
		}
		BenePersonalDetailModel benePersonalDetails = beneficiaryTrnxModel.getBenePersonalDetailModel();
		BeneAccountModel accountDetails = beneficiaryTrnxModel.getBeneAccountModel();
		BeneficaryAccount beneficaryAccount = beneficiaryValidationService.getBeneficaryAccount(accountDetails);
		BeneficaryMaster beneMaster = null;
		if (beneficaryAccount != null) {
			beneMaster = beneficaryMasterRepository
					.findByBeneficaryMasterSeqId(beneficaryAccount.getBeneficaryMasterId());
		}

		if (beneMaster == null) {

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

}
