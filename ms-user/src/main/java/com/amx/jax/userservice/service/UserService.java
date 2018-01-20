package com.amx.jax.userservice.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exception.InvalidCivilIdException;
import com.amx.jax.exception.InvalidJsonInputException;
import com.amx.jax.exception.InvalidOtpException;
import com.amx.jax.exception.UserNotFoundException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.SecurityQuestionsManager;
import com.amx.jax.userservice.repository.LoginLogoutHistoryRepository;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.StringUtil;
import com.amx.jax.util.Util;
import com.amx.jax.util.WebUtils;
import com.bootloaderjs.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class UserService extends AbstractUserService {

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private Util util;

	@Autowired
	private WebUtils webutil;

	@Autowired
	private CheckListManager checkListManager;

	@Autowired
	private UserValidationService userValidationService;

	@Autowired
	private SecurityQuestionsManager secQmanager;

	@Autowired
	private StringUtil stringUtil;

	@Autowired
	private LoginLogoutHistoryRepository loginLogoutHistoryRepositoryRepo;

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	ITransactionHistroyDAO tranxHistDao;

	@Autowired
	MetaData metaData;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Override
	public ApiResponse registerUser(AbstractUserModel userModel) {
		UserModel kwUserModel = (UserModel) userModel;
		Customer cust = getDao().saveOrUpdateUser(kwUserModel);
		AbstractModel model = convert(cust);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	@Override
	public AbstractUserDao getDao() {
		return null;
	}

	@Override
	public AbstractModel convert(Customer cust) {
		UserModel model = new UserModel();
		return model;
	}

	public CustomerModel convert(CustomerOnlineRegistration cust) {
		CustomerModel model = new CustomerModel();
		model.setIdentityId(cust.getUserName());
		if (cust.getCaption() != null) {
			model.setCaption(cryptoUtil.decrypt(cust.getUserName(), cust.getCaption()));
		}
		model.setEmail(cust.getEmail());
		model.setImageUrl(cust.getImageUrl());
		model.setMobile(cust.getMobileNumber());
		model.setCustomerId(cust.getCustomerId());
		model.setIsActive(ConstantDocument.Yes.equals(cust.getStatus()));
		List<SecurityQuestionModel> securityquestions = new ArrayList<>();
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion1(), cust.getSecurityAnswer1()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion2(), cust.getSecurityAnswer2()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion3(), cust.getSecurityAnswer3()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion4(), cust.getSecurityAnswer4()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion5(), cust.getSecurityAnswer5()));
		model.setSecurityquestions(securityquestions);
		try {
			PersonInfo personinfo = new PersonInfo();
			Customer customer = custDao.getCustById(cust.getCustomerId());
			LoginLogoutHistory history = this.getLoginLogoutHistoryByUserName(cust.getUserName());
			if (history != null) {
				personinfo.setLastLoginTime(history.getLoginTime());
			}
			BeanUtils.copyProperties(personinfo, customer);
			model.setPersoninfo(personinfo);
		} catch (Exception e) {
		}
		return model;
	}

	public ApiResponse saveCustomer(CustomerModel model) {
		BigDecimal customerId = (model.getCustomerId() == null) ? metaData.getCustomerId() : model.getCustomerId();
		if (customerId == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustomerByCustomerId(customerId);
		if (onlineCust == null) {
			throw new UserNotFoundException("Customer is not registered for online flow");
		}
		if (model.getLoginId() != null) {
			userValidationService.validateLoginId(model.getLoginId());
		}
		userValidationService.validateOtpFlow(model);
		simplifyAnswers(model.getSecurityquestions());
		onlineCust = custDao.saveOrUpdateOnlineCustomer(onlineCust, model);
		checkListManager.updateCustomerChecks(onlineCust, model);
		ApiResponse response = getBlackApiResponse();
		CustomerModel outputModel = convert(onlineCust);
		if (model.getLoginId() != null || model.getPassword() != null) { // after this step flow is going to login
			Map<String, Object> output = afterLoginSteps(onlineCust);
			if (output.get("PERSON_INFO") != null) {
				outputModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
			}
		}
		response.getData().getValues().add(outputModel);
		response.getData().setType(outputModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		if (isNewUserRegistrationSuccess(model, onlineCust)) {
			jaxNotificationService.sendNewRegistrationSuccessEmailNotification(outputModel.getPersoninfo(),
					onlineCust.getEmail());
		} else {
			jaxNotificationService.sendProfileChangeNotificationEmail(model, outputModel.getPersoninfo());
		}

		return response;
	}

	private boolean isNewUserRegistrationSuccess(CustomerModel model, CustomerOnlineRegistration onlineCust) {

		if (model.getPassword() != null && ConstantDocument.Yes.equals(onlineCust.getStatus())) {
			return true;
		}
		return false;
	}

	private void simplifyAnswers(List<SecurityQuestionModel> securityquestions) {
		if (securityquestions != null && !securityquestions.isEmpty()) {
			securityquestions.forEach(qa -> qa.setAnswer(stringUtil.simplifyString(qa.getAnswer())));
		}

	}

	@Override
	public Class<UserModel> getModelClass() {
		return UserModel.class;
	}

	public CustomerOnlineRegistration verifyCivilId(String civilId, CivilIdOtpModel model) {
		Customer cust = userValidationService.validateCustomerForOnlineFlow(civilId);

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(cust.getCustomerId());
		if (onlineCust == null) {
			onlineCust = new CustomerOnlineRegistration(cust);
			onlineCust.setHresetBy(cust.getIdentityInt());
			onlineCust.setHresetIp(webutil.getClientIp());
			onlineCust.setHresetkDt(new Date());
			onlineCust.setResetIp(webutil.getClientIp());
		}
		model.setEmail(cust.getEmail());
		model.setMobile(cust.getMobile());
		model.setIsActiveCustomer(ConstantDocument.Yes.equals(onlineCust.getStatus()) ? true : false);
		return onlineCust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		return sendOtpForCivilId(civilId, null);
	}

	public ApiResponse sendOtpForCivilId(String civilId, List<CommunicationChannel> channels) {
		BigDecimal customerId = metaData.getCustomerId();
		if (customerId != null) {
			civilId = custDao.getCustById(customerId).getIdentityInt();
		}
		userValidationService.validateCivilId(civilId);
		CivilIdOtpModel model = new CivilIdOtpModel();
		CustomerOnlineRegistration onlineCust = verifyCivilId(civilId, model);
		try {
			userValidationService.validateTokenDate(onlineCust);
		} catch (GlobalException e) {
			// reset sent token count
			onlineCust.setTokenSentCount(BigDecimal.ZERO);
		}
		userValidationService.validateCustomerLockCount(onlineCust);
		userValidationService.validateTokenSentCount(onlineCust);
		generateToken(civilId, model, channels);
		onlineCust.setEmailToken(model.getHashedeOtp());
		onlineCust.setSmsToken(model.getHashedmOtp());
		onlineCust.setTokenDate(new Date());
		BigDecimal tokenSentCount = (onlineCust.getTokenSentCount() == null) ? BigDecimal.ZERO
				: onlineCust.getTokenSentCount().add(new BigDecimal(1));
		onlineCust.setTokenSentCount(tokenSentCount);
		custDao.saveOnlineCustomer(onlineCust);
		Customer customer = custDao.getCustById(onlineCust.getCustomerId());
		model.setFirstName(customer.getFirstName());
		model.setLastName(customer.getLastName());
		model.setCustomerId(onlineCust.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		PersonInfo personinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(personinfo, customer);
		} catch (Exception e) {
		}
		jaxNotificationService.sendOtpSms(personinfo, model);
		if (channels != null && channels.contains(CommunicationChannel.EMAIL)) {
			jaxNotificationService.sendOtpEmail(customer, model.geteOtp());
		}
		return response;
	}

	private void generateToken(String userId, CivilIdOtpModel model, List<CommunicationChannel> channels) {
		String randmOtp = util.createRandomPassword(6);
		String hashedmOtp = cryptoUtil.getHash(userId, randmOtp);
		String randeOtp = util.createRandomPassword(6);
		String hashedeOtp = cryptoUtil.getHash(userId, randeOtp);
		model.setHashedmOtp(hashedmOtp);
		model.setmOtp(randmOtp);
		model.setmOtpPrefix(Random.randomAlpha(3));
		if (channels != null && channels.contains(CommunicationChannel.EMAIL)) {
			model.setHashedeOtp(hashedeOtp);
			model.seteOtp(randeOtp);
			model.seteOtpPrefix(Random.randomAlpha(3));
			logger.info("Generated otp for civilid email- " + userId + " is " + randeOtp);
		}
		logger.info("Generated otp for civilid mobile- " + userId + " is " + randmOtp);
	}

	public ApiResponse validateOtp(String civilId, String mOtp) {
		return validateOtp(civilId, mOtp, null);
	}

	public ApiResponse validateOtp(String civilId, String mOtp, String eOtp) {
		logger.debug("in validateopt of civilid: " + civilId);
		Customer customer = null;
		if (civilId != null) {
			customer = custDao.getCustomerByCivilId(civilId);
		}
		if (metaData.getCustomerId() != null) {
			customer = custDao.getCustById(metaData.getCustomerId());
			civilId = customer.getIdentityInt();
		}
		if (customer == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCust == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		if (StringUtils.isEmpty(mOtp)) {
			throw new InvalidJsonInputException("Otp is empty for civil-id: " + civilId);
		}
		userValidationService.validateCustomerLockCount(onlineCust);
		userValidationService.validateTokenDate(onlineCust);
		String etokenHash = onlineCust.getEmailToken();
		String mtokenHash = onlineCust.getSmsToken();
		String mOtpHash = cryptoUtil.getHash(civilId, mOtp);
		String eOtpHash = null;
		if (eOtp != null) {
			eOtpHash = cryptoUtil.getHash(civilId, eOtp);
		}
		if (!mOtpHash.equals(mtokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new InvalidOtpException("Sms Otp is incorrect for civil-id: " + civilId);
		}
		if (eOtpHash != null && !eOtpHash.equals(etokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new InvalidOtpException("Email Otp is incorrect for civil-id: " + civilId);
		}
		checkListManager.updateMobileAndEmailCheck(onlineCust, custDao.getCheckListForUserId(civilId));
		this.unlockCustomer(onlineCust);
		onlineCust.setEmailToken(null);
		onlineCust.setMobileNumber(null);
		custDao.saveOnlineCustomer(onlineCust);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCust);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.debug("end of validateopt for civilid: " + civilId);
		return response;
	}

	public ApiResponse loginUser(String userId, String password) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustomerByLoginIdOrUserName(userId);
		if (onlineCustomer == null) {
			throw new GlobalException("User with userId: " + userId + " is not registered or not active",
					JaxError.USER_NOT_REGISTERED);
		}
		Customer customer = custDao.getCustById(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerLockCount(onlineCustomer);
		userValidationService.validatePassword(onlineCustomer, password);
		userValidationService.validateCustIdProofs(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerData(onlineCustomer, customer);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCustomer);
		Map<String, Object> output = afterLoginSteps(onlineCustomer);
		if (output.get("PERSON_INFO") != null) {
			customerModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
		}
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * call this method to perform tasks after login
	 */
	private Map<String, Object> afterLoginSteps(CustomerOnlineRegistration onlineCustomer) {
		custDao.updatetLoyaltyPoint(onlineCustomer.getCustomerId());
		this.unlockCustomer(onlineCustomer);
		this.saveLoginLogoutHistoryByUserName(onlineCustomer.getUserName());
		Map<String, Object> output = new HashMap<>();
		return output;
	}

	public ApiResponse getUserCheckList(String loginId) {
		UserVerificationCheckListDTO model = checkListManager.getUserCheckList(loginId);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.debug("end of getUserCheckList for loginId: " + loginId);
		return response;

	}

	public ApiResponse generateRandomQuestions(Integer size, Integer customerId) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(new BigDecimal(customerId));
		ApiResponse response = getBlackApiResponse();
		List<QuestModelDTO> result = secQmanager.generateRandomQuestions(onlineCustomer, size, customerId);
		response.getData().getValues().addAll(result);
		response.getData().setType("quest");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	public ApiResponse validateCustomerData(CustomerModel model) {
		if (model.getCustomerId() == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(model.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		userValidationService.validateCustomerLockCount(onlineCustomer);
		simplifyAnswers(model.getSecurityquestions());
		userValidationService.validateCustomerSecurityQuestions(model.getSecurityquestions(), onlineCustomer);
		this.unlockCustomer(onlineCustomer);
		CustomerModel responseModel = convert(onlineCustomer);
		Map<String, Object> output = afterLoginSteps(onlineCustomer);
		if (output.get("PERSON_INFO") != null) {
			responseModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
		}

		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	public ApiResponse updatePassword(CustomerModel model) {
		BigDecimal custId = (model.getCustomerId() == null) ? metaData.getCustomerId() : null;
		if (custId == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		userValidationService.validateOtpFlow(model);
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(custId);
		onlineCustomer.setPassword(cryptoUtil.getHash(onlineCustomer.getUserName(), model.getPassword()));
		custDao.saveOnlineCustomer(onlineCustomer);
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * reset lock
	 */
	protected void unlockCustomer(CustomerOnlineRegistration onlineCustomer) {
		if (onlineCustomer.getLockCnt() != null || onlineCustomer.getLockDt() != null) {
			onlineCustomer.setLockCnt(null);
			onlineCustomer.setLockDt(null);
			custDao.saveOnlineCustomer(onlineCustomer);
		}
		onlineCustomer.setTokenSentCount(BigDecimal.ZERO);
	}

	protected LoginLogoutHistory getLoginLogoutHistoryByUserName(String userName) {

		Sort sort = new Sort(Direction.DESC, "loginLogoutId");
		LoginLogoutHistory output = loginLogoutHistoryRepositoryRepo.findFirst1ByuserName(userName, sort);

		return output;
	}

	protected void saveLoginLogoutHistoryByUserName(String userName) {
		LoginLogoutHistory output = getLoginLogoutHistoryByUserName(userName);
		if (output == null) {
			output = new LoginLogoutHistory();
			output.setLoginType("C");
			output.setUserName(userName);
		}
		output.setLoginTime(new Timestamp(new Date().getTime()));
		loginLogoutHistoryRepositoryRepo.save(output);
	}

	/**
	 * My Profile Info
	 */

	public ApiResponse getCustomerInfo(BigDecimal countryId, BigDecimal companyId, BigDecimal customerId) {
		CustomerDto customerInfo = new CustomerDto();
		ApiResponse response = getBlackApiResponse();
		BeneficiaryListDTO beneDto = null;
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		BenificiaryListView defaultBene = beneficiaryOnlineDao.getDefaultBeneficiary(customerId, countryId);
		List<CustomerRemittanceTransactionView> noOfTrnxList = tranxHistDao.getCustomerTotalTrnx(customerId);
		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));

		if (customerList.isEmpty()) {
			throw new GlobalException("Customer is not avaliable");
		} else {
			customerInfo = convertCustomerDto(customerList);
			if (defaultBene != null) {
				beneDto = convertBeneModelToDto(defaultBene);
			}
			if (!noOfTrnxList.isEmpty()) {
				customerInfo.setTotalTrnxCount(new BigDecimal(noOfTrnxList.size()));
			}
			customerInfo.setDefaultBeneDto(beneDto);

			if (!contactList.isEmpty()) {
				customerInfo.setLocalContactBuilding(contactList.get(0).getBuildingNo());
				customerInfo.setStreet(contactList.get(0).getStreet());
				customerInfo.setBlockNo(contactList.get(0).getBlock());
				customerInfo.setHouse(contactList.get(0).getFlat());
				List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),
						contactList.get(0).getFsCountryMaster().getCountryId());
				if (!countryMasterView.isEmpty()) {
					customerInfo.setLocalContactCountry(countryMasterView.get(0).getCountryName());
					List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),
							contactList.get(0).getFsStateMaster().getStateId(), new BigDecimal(1));
					if (!stateMasterView.isEmpty()) {
						customerInfo.setLocalContactState(stateMasterView.get(0).getStateName());
						List<ViewDistrict> districtMas = districtDao.getDistrict(stateMasterView.get(0).getStateId(),
								contactList.get(0).getFsDistrictMaster().getDistrictId(), new BigDecimal(1));
						if (!districtMas.isEmpty()) {
							customerInfo.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
							List<ViewCity> cityDetails = cityDao.getCityDescription(districtMas.get(0).getDistrictId(),
									contactList.get(0).getFsCityMaster().getCityId(), new BigDecimal(1));
							if (!cityDetails.isEmpty()) {
								customerInfo.setLocalContactCity(cityDetails.get(0).getCityName());
							}
						}
					}

				}
				response.getData().getValues().add(customerInfo);
				response.setResponseStatus(ResponseStatus.OK);
			}
		}
		response.getData().setType("customer-dto");
		return response;
	}

	public CustomerDto convertCustomerDto(List<Customer> customerList) {
		CustomerDto dto = null;
		for (Customer model : customerList) {
			dto = new CustomerDto();
			dto.setTitle(model.getTitle());
			dto.setFirstName(model.getFirstName());
			dto.setMiddleName(model.getMiddleName());
			dto.setLastName(model.getLastName());
			dto.setIdentityInt(model.getIdentityInt());
			dto.setIdentityExpiredDate(model.getIdentityExpiredDate());
			dto.setEmail(model.getEmail() == null ? "" : model.getEmail());
			dto.setMobile(model.getMobile() == null ? "" : model.getMobile());
			dto.setLoyaltyPoints(model.getLoyaltyPoints());
			dto.setDateOfBirth(model.getDateOfBirth());
			dto.setCustomerId(model.getCustomerId());
			dto.setCustomerReference(model.getCustomerReference());
			dto.setIsActive(model.getIsActive());
			dto.setFirstNameLocal(model.getFirstNameLocal());
			dto.setMiddleNameLocal(model.getMiddleNameLocal());
			dto.setLastNameLocal(model.getLastNameLocal());
			dto.setShortName(model.getShortName());
			dto.setShortNameLocal(model.getShortNameLocal());
		}

		return dto;
	}

	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}

	/**
	 * Unlocks the customer account
	 */
	public ApiResponse unlockCustomer() {
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse();
		BigDecimal customerId = metaData.getCustomerId();
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);
		if (onlineCustomer == null) {
			throw new GlobalException("User with userId: " + customerId + " is not registered or not active",
					JaxError.USER_NOT_REGISTERED);
		}
		this.unlockCustomer(onlineCustomer);
		responseModel.setSuccess(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;

	}

	/**
	 * Deactivates the customer
	 */
	public ApiResponse deactivateCustomer() {
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse();
		BigDecimal customerId = metaData.getCustomerId();
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);
		if (onlineCustomer != null) {
			onlineCustomer.setStatus(ConstantDocument.No);
			custDao.saveOnlineCustomer(onlineCustomer);
		}
		responseModel.setSuccess(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;

	}

}
