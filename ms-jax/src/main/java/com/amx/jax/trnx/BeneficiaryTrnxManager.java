package com.amx.jax.trnx;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;

import com.amx.amxlib.constant.AuthType;
import com.amx.amxlib.constant.NotificationConstants;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.BeneCreateDetailsDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.AppContextUtil;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.branchbene.BeneAccountManager;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dao.BeneficiaryDao;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.bene.BeneficaryAccount;
import com.amx.jax.dbmodel.bene.BeneficaryContact;
import com.amx.jax.dbmodel.bene.BeneficaryMaster;
import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.dbmodel.bene.BeneficaryStatus;
import com.amx.jax.dbmodel.bene.RelationsDescription;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.benebranch.BeneAccountModel;
import com.amx.jax.model.request.benebranch.BenePersonalDetailModel;
import com.amx.jax.model.request.benebranch.BeneficiaryTrnxModel;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.BeneficaryStatusRepository;
import com.amx.jax.repository.IBeneficaryContactDao;
import com.amx.jax.repository.IBeneficiaryAccountDao;
import com.amx.jax.repository.IBeneficiaryMasterDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.ParameterService;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.BeneficiaryValidationService;
import com.amx.jax.services.JaxEmailNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerDBAuthManager;
import com.amx.jax.userservice.repository.RelationsRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.jax.validation.BenePersonalDetailValidator;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonUtil;

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

	@Autowired
	JaxEmailNotificationService jaxEmailNotificationService;

	@Autowired
	PostManService postManService;

	@Autowired
	private PushNotifyClient pushNotifyClient;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;
	@Autowired
	BeneAccountManager beneAccountManager;
	@Autowired
	BeneficiaryService beneficiaryService ; 
	@Autowired
	RelationsRepository relationsRepository;
	@Autowired
	CustomerDBAuthManager customerDBAuthManager;
	@Autowired	
	CustomerDao custDao;
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	@Autowired
	WhatsAppClient whatsAppClient;


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
		return commit(beneficiaryTrnxModel);
	}


	public BeneficiaryTrnxModel commit(BeneficiaryTrnxModel beneficiaryTrnxModel) {
		
		logger.info("commiting beneficary: {}", JsonUtil.toJson(beneficiaryTrnxModel));
		BeneficaryMaster beneMaster = commitBeneMaster(beneficiaryTrnxModel);
		commitBeneContact(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		BeneficaryAccount beneAccount = commitBeneAccount(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId());
		BeneficaryRelationship beneRelationship = commitBeneRelationship(beneficiaryTrnxModel,
				beneMaster.getBeneficaryMasterSeqId(), beneAccount.getBeneficaryAccountSeqId());
		logger.info("commit done");
		logger.info("Beneficiary Relationship Sequence Id : " +beneRelationship.getBeneficaryRelationshipId());
		beneficiaryTrnxModel.setBeneficaryRelationSeqId(beneRelationship.getBeneficaryRelationshipId());
		populateOldEmosData(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
				beneAccount);
		beneRelationship = beneficiaryRelationshipDao.findOne(beneRelationship.getBeneficaryRelationshipId());
		if (beneRelationship.getMapSequenceId() == null) {
			logger.info("Map sequence is null for bene rel seq id: {}", beneRelationship.getBeneficaryRelationshipId());
			populateOldEmosData(beneficiaryTrnxModel, beneMaster.getBeneficaryMasterSeqId(),
					beneAccount);
			if (beneRelationship.getMapSequenceId() == null) {
				sendAlertEmailForCreationError(beneficiaryTrnxModel, beneRelationship);
			}
		}else {
			logger.info("Map Sequence Id generated: {}", beneRelationship.getMapSequenceId());
		}
		BenificiaryListView beneListView = beneficiaryOnlineDao
				.findBybeneficiaryRelationShipSeqId(beneRelationship.getBeneficaryRelationshipId());

		BigDecimal custId = beneListView.getCustomerId();
		PersonInfo personInfo = userService.getPersonInfo(custId);

		BeneCreateDetailsDTO beneDetails = new BeneCreateDetailsDTO();
		beneDetails.setBeneBankName(beneListView.getBankShortNames());
		beneDetails.setBeneCountry(beneListView.getBenificaryBankCountryName());
		beneDetails.setBeneName(beneListView.getBenificaryName());
		if(!ArgUtil.isEmpty(personInfo.getLastName())) {
			beneDetails.setCustomerName(personInfo.getFirstName() +" "+personInfo.getLastName());
		}
		else {
			beneDetails.setCustomerName(personInfo.getFirstName());
		}

		sendNotificationTemplate(beneDetails, personInfo, custId);
		return beneficiaryTrnxModel;
	}

	private void sendAlertEmailForCreationError(BeneficiaryTrnxModel beneficiaryTrnxModel, BeneficaryRelationship beneRelationship) {
		String[] recieverIds = jaxEmailNotificationService.getBeneCreationErrorEmailList();
		if (recieverIds != null) {
			Email email = new Email();
			StringBuffer message = new StringBuffer();
			message.append("BeneRelationship id: ").append(beneRelationship.getBeneficaryRelationshipId());
			message.append("\n ").append(beneficiaryTrnxModel.toString());
			email.addTo(recieverIds);
			email.setMessage(message.toString());
			email.setSubject("Bene creation error");
		}
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
			beneficaryAccount.setServiceProviderBranchId(beneAccountManager.getRoutingBankBranchId(accountDetails));
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
			beneficaryAccount.setIbanNumber(accountDetails.getIbanNumber());

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
	public BigDecimal getBankBranchCode(BigDecimal bankId, BigDecimal bankBranchId) {
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
		beneficaryRelationship.setRelationsId(getRelationsId(beneficiaryTrnxModel, beneDetaisl.getRelationsId()));

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
		if (beneDetaisl.getInstitutionName() != null) {
			beneficaryRelationship.setFirstName(getInstitutionFirstName(beneDetaisl));
			beneficaryRelationship.setSecondName(getInstitutionSecondName(beneDetaisl));
		}
		if (beneDetaisl.getInstitutionNameLocal() != null) {
			beneficaryRelationship.setLocalFirstName(getInstitutionFirstNameLocal(beneDetaisl));
			beneficaryRelationship.setLocalSecondName(getInstitutionSecondNameLocal(beneDetaisl));
		}
		
		
		beneficaryRelationship.setDeviceIp(metaData.getDeviceIp());
		beneficaryRelationship.setDeviceType(metaData.getDeviceType());
		
		beneficiaryRelationshipDao.save(beneficaryRelationship);
		return beneficaryRelationship;
	}

	public String getInstitutionSecondName(BenePersonalDetailModel beneDetaisl) {
		String institutionName = beneDetaisl.getInstitutionName().trim();
		int splitIndex = institutionName.indexOf(" ");
		return institutionName.substring(splitIndex+1);
	}

	public String getInstitutionFirstName(BenePersonalDetailModel beneDetaisl) {
		return beneDetaisl.getInstitutionName().trim().split(" ")[0];
	}
	
	public String getInstitutionSecondNameLocal(BenePersonalDetailModel beneDetaisl) {
		String institutionNameLocal = beneDetaisl.getInstitutionNameLocal().trim();
		int splitIndex = institutionNameLocal.indexOf(" ");
		return institutionNameLocal.substring(splitIndex + 1);
	}

	public String getInstitutionFirstNameLocal(BenePersonalDetailModel beneDetaisl) {
		return beneDetaisl.getInstitutionNameLocal().trim().split(" ")[0];
	}

	private BigDecimal getRelationsId(BeneficiaryTrnxModel beneficiaryTrnxModel, BigDecimal relationsId) {
		if (relationsId == null) {
			BigDecimal beneficaryTypeId = beneficiaryTrnxModel.getBenePersonalDetailModel().getBeneficaryTypeId();
			if (beneficiaryService.isNonIndividualBene(beneficaryTypeId)) {
				List<RelationsDescription> othersRelationship = relationsRepository.getOthersRelations();
				if (CollectionUtils.isEmpty(othersRelationship)) {
					throw new GlobalException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "other relationship not defined in relation master");
				}
				relationsId = othersRelationship.get(0).getRelationsId();
			}
		}
		return relationsId;
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
		String beneMobileNo = null;
		if(beneDetails.getMobileNumber()!=null) {
			beneMobileNo = beneDetails.getMobileNumber().toString();
		}
		beneficaryContact.setTelephoneNumber(
				(beneDetails.getTelephoneNumber() == null) ? beneMobileNo
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
			BeneficaryStatus beneStatus = getbeneStatus(benePersonalDetails.getBeneficaryTypeId());
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
			BigDecimal beneficaryTypeId = beneficiaryTrnxModel.getBenePersonalDetailModel().getBeneficaryTypeId();
			if (beneficiaryService.isNonIndividualBene(beneficaryTypeId)) {
				// in case of non individiual bene set country id as nationallity as per
				// Jabarullah
				beneMaster.setNationality(benePersonalDetails.getCountryId().toString());
			}
			beneMaster.setInstitutionCategoryId(benePersonalDetails.getInstitutionCategoryId());
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
		if (benePersonalDetails.getInstitutionName() != null) {
			beneMaster.setFirstName(getInstitutionFirstName(benePersonalDetails));
			beneMaster.setSecondName(getInstitutionSecondName(benePersonalDetails));
		}
		if (benePersonalDetails.getInstitutionNameLocal() != null) {
			beneMaster.setLocalFirstName(getInstitutionFirstNameLocal(benePersonalDetails));
			beneMaster.setLocalSecondName(getInstitutionSecondNameLocal(benePersonalDetails));
		}
	}

	/**
	 * @param bigDecimal 
	 * @return status of bene
	 * 
	 */
	private BeneficaryStatus getbeneStatus(BigDecimal beneStatusId) {
		if (beneStatusId == null) {
			return beneficaryStatusRepository.findByBeneficaryStatusName(ConstantDocument.INDIVIDUAL_STRING);
		}
		return beneficaryStatusRepository.findOne(beneStatusId);
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
		BigDecimal custId = metaData.getCustomerId();
		JaxAuthContext.contactType(ContactType.SMS_EMAIL);
		if(eOtp != null) {
			JaxAuthContext.eOtp(eOtp);
		}
		if(mOtp != null) {
			JaxAuthContext.mOtp(mOtp);
		}
		logger.info("Flow is "+AppContextUtil.getFlow());
		if(custId != null) {
			customerDBAuthManager.validateAndSendOtp(custId);
		}
		//userService.validateOtp(null, mOtp, eOtp);
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
	public void sendNotificationTemplate(BeneCreateDetailsDTO wrapper, PersonInfo personInfo, BigDecimal custId) {
		try {
			Customer c = custDao.getActiveCustomerDetailsByCustomerId(custId);
			CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.BENE_CREAT_SUCC, c);
			logger.debug("Sending beneCreationEmail  to customer : ");
			// Send Email
			if(communicationPrefsResult.isEmail()) {
				Email beneCreationEmail = new Email();
				beneCreationEmail.setSubject("New Beneficiary Addition Success");
				if (personInfo.getEmail() != null) {
					beneCreationEmail.addTo(personInfo.getEmail());
				}
				beneCreationEmail.setITemplate(TemplatesMX.BENE_SUCC);
				beneCreationEmail.setHtml(true);

				beneCreationEmail.getModel().put(NotificationConstants.RESP_DATA_KEY, wrapper);
				postManService.sendEmailAsync(beneCreationEmail);
			}
			
			if(communicationPrefsResult.isSms()) {
				SMS sms = new SMS();
				sms.addTo(personInfo.getPrefixCodeMobile()+personInfo.getMobile());
				sms.setITemplate(TemplatesMX.BENE_SUCC);
				sms.getModel().put(NotificationConstants.RESP_DATA_KEY, wrapper);
				postManService.sendSMSAsync(sms);
			}
			
			if(communicationPrefsResult.isWhatsApp()) {
				WAMessage waMessage = new WAMessage();
				waMessage.addTo(personInfo.getWhatsappPrefixCode()+personInfo.getWhatsAppNumber());
				waMessage.setITemplate(TemplatesMX.BENE_SUCC);
				waMessage.getModel().put(NotificationConstants.RESP_DATA_KEY, wrapper);
				logger.info("Wa for bene succ "+JsonUtil.toJson(waMessage));
				whatsAppClient.send(waMessage);
			}
			
			if(communicationPrefsResult.isPushNotify()) {
				// Send Push Message
				PushMessage pushMessage = new PushMessage();

				pushMessage.setITemplate(TemplatesMX.BENE_SUCC);
				pushMessage.getModel().put(NotificationConstants.RESP_DATA_KEY, wrapper);
				pushMessage.addToUser(custId);
				pushNotifyClient.send(pushMessage);
			}
			

		} catch (Exception e) {
			logger.error("Error while sending mail beneCreationEmail : " , e);
		}
	}

}
