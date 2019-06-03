package com.amx.jax.customer.service;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.PrefixEnum;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.CustomerCredential;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.customer.ICustRegService;
import com.amx.jax.customer.document.manager.CustomerKycManager;
import com.amx.jax.customer.manager.CustomerEmployementManager;
import com.amx.jax.customer.manager.OffsiteCustomerRegManager;
import com.amx.jax.customer.manager.OffsiteCustomerRegValidator;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.FieldListDao;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.CityMaster;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.DmsApplMapping;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.dbmodel.EmploymentTypeMasterView;
import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.dbmodel.ProfessionMasterView;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.request.customer.GetOffsiteCustomerDetailRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.EmployeeRespository;
import com.amx.jax.repository.EmploymentTypeRepository;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.repository.IDMSAppMappingRepository;
import com.amx.jax.repository.IUserFinancialYearRepo;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.repository.ProfessionRepository;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.service.PrefixService;
import com.amx.jax.services.AbstractService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerIdProofManager;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.manager.CustomerRegistrationOtpManager;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.ContactDetailService;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.userservice.validation.CustomerCredentialValidator;
import com.amx.jax.userservice.validation.CustomerPersonalDetailValidator;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.CountryMetaValidation;
import com.amx.utils.Constants;
import com.amx.utils.JsonUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsitCustRegService extends AbstractService implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(OffsitCustRegService.class);

	@Autowired
	JaxConditionalFieldRuleRepository jaxConditionalFieldRuleRepository;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	BizcomponentDao bizcomponentDao;

	@Autowired
	PrefixService prefixService;

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;

	@Autowired
	OtpSettings otpSettings;

	@Autowired
	DateUtil dateUtil;

	@Autowired
	MetaData metaData;

	@Autowired
	ArticleDao articleDao;

	@Autowired
	FieldListDao fieldListDao;

	@Autowired
	AuditService auditService;

	@Autowired
	EmploymentTypeRepository employmentTypeRepo;

	@Autowired
	ProfessionRepository professionRepository;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	CustomerIdProofRepository customerIdProofRepository;

	@Autowired
	private ContactDetailsRepository contactDetailsRepository;

	@Autowired
	ContactDetailService contactDetailService;

	@Autowired
	CountryMasterRepository countryMasterRepository;

	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;

	@Autowired
	DOCBLOBRepository docblobRepository;

	@Autowired
	IDMSAppMappingRepository idmsAppMappingRepository;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	CountryMetaValidation countryMetaValidation;

	@Autowired
	UserValidationService userValidationService;

	@Autowired
	BlackListDao blackListDao;

	@Autowired
	CustomerPersonalDetailValidator customerPersonalDetailValidator;

	@Autowired
	CustomerRegistrationOtpManager customerRegistrationOtpManager;

	@Autowired
	CustomerCredentialValidator customerCredentialValidator;

	@Autowired
	CustomerService customerService;

	@Autowired
	IApplicationCountryRepository applicationSetup;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	UserService userService;

	@Autowired
	private CryptoUtil cryptoUtil;
	@Autowired
	OffsiteCustomerRegValidator offsiteCustomerRegValidator;
	@Autowired
	OffsiteCustomerRegManager offsiteCustomerRegManager;
	@Autowired
	CustomerIdProofManager customerIdProofManager;
	@Autowired
	EmployeeRespository employeeRespository;
	@Autowired
	CustomerEmployementManager customerEmployementManager;
	@Autowired
	CustomerKycManager customerKycManager;

	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		List<Map<String, Object>> tempList = bizcomponentDao
				.getAllComponentComboDataForCustomer(metaData.getLanguageId());
		List<ComponentDataDto> list = new ArrayList<>();
		for (Map row : tempList) {
			String idType = bizcomponentDao.getIdentityTypeMaster((BigDecimal) row.get("COMPONENT_DATA_ID"));
			if (idType.equalsIgnoreCase("I")) {
				list.add(new ComponentDataDto((BigDecimal) row.get("COMPONENT_DATA_ID"), (String) row.get("DATA_DESC"),
						(String) row.get("SHORT_CODE")));
			}
		}
		if (tempList.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_ID_TYPE_LIST, "Id Type List Is Not available ");
		}
		return AmxApiResponse.buildList(list);
	}

	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {

		OtpData otpData = customerRegistrationManager.get().getOtpData();

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.VALIDATION)
				.target(offsiteCustRegModel.getMobile()).target(offsiteCustRegModel.getEmail())
				.customer(offsiteCustRegModel.getIdentityInt()); // Audit

		boolean otpMisMatch = false;
		try {
			if (StringUtils.isBlank(offsiteCustRegModel.getmOtp())) {
				throw new GlobalException(JaxError.MISSING_OTP, "Otp field is required");
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.VALIDATE_OTP_LIMIT_EXCEEDED));
				throw new GlobalException(
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED,
						"Sorry, you cannot proceed to register. Please try to register after 12 midnight");
			}

			// actual validation logic
			if (!otpData.getmOtp().equals(offsiteCustRegModel.getmOtp())) {
				otpMisMatch = true;
				otpMismatch(otpData);
			}

			if (!StringUtils.isBlank(offsiteCustRegModel.geteOtp())) {
				if (!otpData.geteOtp().equals(offsiteCustRegModel.geteOtp())) {
					otpMisMatch = true;
					otpMismatch(otpData);
				}
			}

			otpData.setOtpValidated(true);
			otpData.resetCounts();
		} finally {
			if (otpMisMatch) {
				auditService.log(auditEvent.result(Result.FAIL).message(JaxError.INVALID_OTP));
			}
			customerRegistrationManager.saveOtpData(otpData);
		}
		AmxApiResponse<String, Object> obj = AmxApiResponse.build("Customer Email And Mobile Validation Successfully");
		obj.setMessageKey("AUTH_SUCCESS");
		auditService.log(auditEvent.result(Result.DONE)); // Audit
		return obj;

	}

	private void resetAttempts(OtpData otpData) {
		Date midnightToday = dateUtil.getMidnightToday();

		if (otpData.getLockDate() != null && midnightToday.compareTo(otpData.getLockDate()) > 0) {
			otpData.resetCounts();
		}
	}

	private void otpMismatch(OtpData otpData) {
		otpData.setValidateOtpAttempts(otpData.getValidateOtpAttempts() + 1);
		if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
			otpData.setLockDate(new Date());
		}
		throw new GlobalException(JaxError.INVALID_OTP, "Invalid otp");

	}

	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		List<Map<String, Object>> articleList = articleDao.getArticles(metaData.getLanguageId());
		if (articleList == null || articleList.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_ARTICLE_LIST, "Article List Is Empty ");
		}
		List<ArticleMasterDescDto> articleDtoList = convertArticle(articleList);
		return AmxApiResponse.buildList(articleDtoList);
	}

	private List<ArticleMasterDescDto> convertArticle(List<Map<String, Object>> articleList) {
		List<ArticleMasterDescDto> output = new ArrayList<>();
		articleList.forEach(i -> {
			output.add(convert(i));
		});
		return output;
	}

	private ArticleMasterDescDto convert(Map<String, Object> i) {
		ArticleMasterDescDto dto = new ArticleMasterDescDto();
		dto.setArticleDescId(
				new BigDecimal(i.get("ARTICLE_DESC_ID") != null ? i.get("ARTICLE_DESC_ID").toString() : null));
		dto.setArticleDescription(i.get("ARTICLE_DESC") != null ? i.get("ARTICLE_DESC").toString() : null);
		dto.setArticleId(new BigDecimal(i.get("ARTICLE_ID") != null ? i.get("ARTICLE_ID").toString() : null));
		dto.setLanguageType(new BigDecimal(i.get("LANGUAGE_ID").toString()));
		return dto;
	}

	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		BigDecimal articleId = model.getArticleId();
		List<Map<String, Object>> designationList = articleDao.getDesignationData(articleId, metaData.getLanguageId());
		if (designationList == null || designationList.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_DESIGNATION_LIST, "Designation List Is Empty ");
		}
		List<ArticleDetailsDescDto> designationDataList = convertDesignation(designationList);
		return AmxApiResponse.buildList(designationDataList);
	}

	private List<ArticleDetailsDescDto> convertDesignation(List<Map<String, Object>> designationList) {
		List<ArticleDetailsDescDto> output = new ArrayList<>();
		designationList.forEach(i -> {
			output.add(convertDesignation(i));
		});
		LOGGER.debug("List Output is :", output);
		return output;
	}

	private ArticleDetailsDescDto convertDesignation(Map<String, Object> designationMap) {
		ArticleDetailsDescDto dto = new ArticleDetailsDescDto();
		LOGGER.debug("Dto is declared and  is empty");
		dto.setResourceName(
				designationMap.get("ARTICLE_DETAIL_DESC") != null ? designationMap.get("ARTICLE_DETAIL_DESC").toString()
						: null);
		LOGGER.debug("Dto DescId is set");
		dto.setResourceId(designationMap.get("ARTICLE_DETAILS_ID") != null
				? new BigDecimal(designationMap.get("ARTICLE_DETAILS_ID").toString())
				: null);
		dto.setArticleDetailsDesc(dto.getResourceName());
		dto.setArticleDetailsId(dto.getResourceId());
		LOGGER.debug("Dto ArticleDetailId is set");
		return dto;
	}

	public AmxApiResponse<ResourceDTO, Object> getDesignationList() {

		List<Map<String, Object>> designationList = articleDao.getDesignationsByCustomer(metaData.getLanguageId(),
				metaData.getCustomerId());
		LOGGER.debug("The list is returned from dao");
		if (designationList == null || designationList.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_DESIGNATION_LIST, "Designation List Is Empty ");
		}
		LOGGER.debug("The list is not empty");
		List<ResourceDTO> designationDataList = convertDesignationIncome(designationList);
		LOGGER.debug("The list is ", designationDataList);
		return AmxApiResponse.buildList(designationDataList);
	}
	

	private List<ResourceDTO> convertDesignationIncome(List<Map<String, Object>> designationList) {
		List<ResourceDTO> output = new ArrayList<>();
		designationList.forEach(i -> {
			output.add(convertDesignationIncome(i));
		});
		LOGGER.debug("List Output is :", output);
		return output;
	}

	private ResourceDTO convertDesignationIncome(Map<String, Object> designationMap) {
		ResourceDTO dto = new ResourceDTO();
		LOGGER.debug("Dto is declared and  is empty");
		dto.setResourceName(
				designationMap.get("ARTICLE_DETAIL_DESC") != null ? designationMap.get("ARTICLE_DETAIL_DESC").toString()
						: null);
		LOGGER.debug("Dto DescId is set");
		dto.setResourceId(designationMap.get("ARTICLE_DETAIL_ID") != null
				? new BigDecimal(designationMap.get("ARTICLE_DETAIL_ID").toString())
				: null);
		LOGGER.debug("Dto ArticleDetailId is set");
		return dto;
	}

	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal articleDetailsId = model.getArticleDetailsId();
		List<Map<String, Object>> incomeRangeList = articleDao.getIncomeRange(countryId, articleDetailsId);
		if (incomeRangeList == null || incomeRangeList.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_INCOME_RANGE, "Income Range List Is Empty ");
		}
		List<IncomeRangeDto> incomeRangeDataList = convertIncomeRange(incomeRangeList);
		return AmxApiResponse.buildList(incomeRangeDataList);
	}

	private List<IncomeRangeDto> convertIncomeRange(List<Map<String, Object>> incomeRangeList) {
		List<IncomeRangeDto> output = new ArrayList<>();
		incomeRangeList.forEach(i -> {
			output.add(convertIncomeRange(i));
		});
		return output;
	}

	private IncomeRangeDto convertIncomeRange(Map<String, Object> i) {
		IncomeRangeDto dto = new IncomeRangeDto();
		dto.setArticleDetailsId(
				new BigDecimal(i.get("ARTICLE_DETAIL_ID") != null ? i.get("ARTICLE_DETAIL_ID").toString() : null));
		dto.setIncomeFrom(new BigDecimal(i.get("INCOME_FROM") != null ? i.get("INCOME_FROM").toString() : null));
		dto.setIncomeRangeId(
				new BigDecimal(i.get("INCOME_RANGE_ID") != null ? i.get("INCOME_RANGE_ID").toString() : null));
		dto.setIncomeTo(new BigDecimal(i.get("INCOME_TO") != null ? i.get("INCOME_TO").toString() : null));
		return dto;
	}

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		List<FieldList> fieldList = null;
		fieldList = fieldListDao.getFieldList(model.getTenant(), Constants.COMMON_NATIONALITY, model.getComponent());
		if (fieldList == null) {
			throw new GlobalException(JaxError.EMPTY_FIELD_CONDITION, "Field Condition is Empty ");
		}
		List<FieldListDto> listDto = convertFieldList(fieldList);
		Map<String, FieldListDto> map = new HashMap<>();
		if (listDto != null) {
			map = listDto.stream().collect(Collectors.toMap(FieldListDto::getKey, Function.identity()));
		}

		if (model.getNationality() != null && !model.getNationality().equalsIgnoreCase("ALL")) {
			fieldList = fieldListDao.getFieldList(model.getTenant(), model.getNationality(), model.getComponent());
			if (fieldList != null) {
				listDto = new ArrayList<>();
				listDto = convertFieldList(fieldList);
				Map<String, FieldListDto> map1 = listDto.stream()
						.collect(Collectors.toMap(FieldListDto::getKey, Function.identity()));
				map.putAll(map1);
			}
		}
		if (map == null || map.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_FIELD_CONDITION, "Field Condition is Empty ");
		}
		return AmxApiResponse.build(map);
	}

	private List<FieldListDto> convertFieldList(List<FieldList> fieldList) {
		List<FieldListDto> output = new ArrayList<>();
		fieldList.forEach(i -> {
			output.add(convertFieldList(i));
		});
		return output;
	}

	private FieldListDto convertFieldList(FieldList i) {
		FieldListDto dto = new FieldListDto();
		dto.setFieldId(i.getFieldId());
		dto.setComponent(i.getComponent());
		dto.setDetails(i.getDetails());
		dto.setKey(i.getKey());
		dto.setNationality(i.getNationality());
		dto.setRemitCountry(i.getRemitCountry());
		dto.setTenant(i.getTenant());
		dto.setValue(i.getValue());
		return dto;
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendEmploymentTypeList() {
		List<EmploymentTypeMasterView> view = employmentTypeRepo.findAll();
		if (view.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_EMPLOYMENT_TYPE, "Employment Type List Not Available");
		}
		List<ComponentDataDto> list = new ArrayList<>();
		for (EmploymentTypeMasterView map : view) {
			list.add(new ComponentDataDto(map.getComponentDataId(), map.getDataDesc()));
		}
		return AmxApiResponse.buildList(list);
	}

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		List<ProfessionMasterView> view = professionRepository.findAll();
		if (view.isEmpty()) {
			throw new GlobalException(JaxError.EMPTY_PROFESSION_LIST, "Profession List Not Available");
		}
		List<ComponentDataDto> list = new ArrayList<>();
		for (ProfessionMasterView map : view) {
			list.add(new ComponentDataDto(map.getComponentDataId(), map.getDataDesc()));
		}
		return AmxApiResponse.buildList(list);
	}

	@Override
	@Transactional
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(CustomerInfoRequest model) {
		LOGGER.debug("in saveCustomerInfo with request model: {}", JsonUtil.toJson(model));
		CustomerPersonalDetail customerDetails = new CustomerPersonalDetail();
		jaxUtil.convert(model.getCustomerPersonalDetail(), customerDetails);
		Customer customer = commitCustomer(customerDetails, model.getCustomerEmploymentDetails());
		commitCustomerLocalContact(model.getLocalAddressDetails(), customer, customerDetails);
		commitCustomerHomeContact(model.getHomeAddressDestails(), customer, customerDetails);
		customerIdProofManager.commitOnlineCustomerIdProof(customer);
		commitEmploymentDetails(model.getCustomerEmploymentDetails(), customer, model.getLocalAddressDetails());
		auditService.log(new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).result(Result.DONE));
		CustomerInfo info = new CustomerInfo();
		info.setCustomerId(customer.getCustomerId());
		return AmxApiResponse.build(info);
	}

	private void commitEmploymentDetails(CustomerEmploymentDetails customerEmploymentDetails, Customer customer,
			LocalAddressDetails localAddressDetails) {
		if (customerEmploymentDetails != null) {

			EmployeeDetails employeeModel = customerEmployeeDetailsRepository.getCustomerEmploymentData(customer);
			if (employeeModel == null) {
				employeeModel = new EmployeeDetails();
			}

			employeeModel.setFsBizComponentDataByEmploymentTypeId(bizcomponentDao
					.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getEmploymentTypeId()));

			if (customerEmploymentDetails.getEmploymentTypeId().compareTo(new BigDecimal(222)) != 0) {
				employeeModel.setFsBizComponentDataByOccupationId(
						bizcomponentDao
								.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getProfessionId()));
				employeeModel.setEmployerName(customerEmploymentDetails.getEmployer());

			}

			employeeModel.setFsCountryMaster(
					countryMasterRepository.getCountryMasterByCountryId(customerEmploymentDetails.getCountryId()));
			employeeModel.setFsStateMaster(localAddressDetails.getStateId());
			employeeModel.setFsDistrictMaster(localAddressDetails.getDistrictId());

			employeeModel.setIsActive(ConstantDocument.Yes);
			employeeModel.setCreatedBy(metaData.getEmployeeId().toString());
			employeeModel.setCreationDate(new Date());
			employeeModel.setFsCustomer(customer);
			customerEmployeeDetailsRepository.save(employeeModel);
		}

	}

	private void commitCustomerLocalContact(LocalAddressDetails localAddressDetails, Customer customer,
			com.amx.jax.model.request.CustomerPersonalDetail customerDetails) {
		if (localAddressDetails != null) {
			ContactDetail contactDetail = contactDetailService.getContactsForLocal(customer);

			if (contactDetail == null) {
				contactDetail = new ContactDetail();
			}

			contactDetail.setFsCountryMaster(new CountryMaster(localAddressDetails.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(localAddressDetails.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(localAddressDetails.getStateId()));
			contactDetail.setFsCityMaster(new CityMaster(localAddressDetails.getCityId()));
			contactDetail.setBuildingNo(localAddressDetails.getHouse());
			contactDetail.setFlat(localAddressDetails.getFlat());
			contactDetail.setBlock(localAddressDetails.getBlock());
			contactDetail.setStreet(localAddressDetails.getStreet());
			contactDetail.setFsCustomer(customer);
			contactDetail.setActiveStatus(ConstantDocument.Yes);
			contactDetail.setLanguageId(customer.getLanguageId());
			contactDetail.setCreatedBy(metaData.getEmployeeId().toString());
			contactDetail.setCreationDate(customer.getCreationDate());

			contactDetail.setMobile(customerDetails.getMobile());
			contactDetail.setTelephoneCode(customerDetails.getTelPrefix());
			contactDetail.setIsWatsApp(customerDetails.getIsWatsApp());

			BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
			// local type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(49));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
			contactDetailsRepository.save(contactDetail);
		}
	}

	private void commitCustomerHomeContact(HomeAddressDetails homeAddressDestails, Customer customer,
			com.amx.jax.model.request.CustomerPersonalDetail customerDetails) {
		if (homeAddressDestails != null) {
			ContactDetail contactDetail = contactDetailService.getContactsForHome(customer);

			if (contactDetail == null) {
				contactDetail = new ContactDetail();
			}

			contactDetail.setFsCountryMaster(new CountryMaster(homeAddressDestails.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(homeAddressDestails.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(homeAddressDestails.getStateId()));
			if(null != homeAddressDestails.getCityId()) {
				contactDetail.setFsCityMaster(new CityMaster(homeAddressDestails.getCityId()));
			}
			contactDetail.setBuildingNo(homeAddressDestails.getHouse());
			contactDetail.setFlat(homeAddressDestails.getFlat());
			contactDetail.setStreet(homeAddressDestails.getStreet());
			contactDetail.setBlock(homeAddressDestails.getBlock());
			contactDetail.setFsCustomer(customer);
			contactDetail.setActiveStatus(ConstantDocument.Yes);
			contactDetail.setLanguageId(customer.getLanguageId());
			contactDetail.setCreatedBy(metaData.getEmployeeId().toString());
			contactDetail.setCreationDate(customer.getCreationDate());

			BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
			// home type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(50));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
			contactDetailsRepository.save(contactDetail);
		}
	}

	/*
	 * private void revalidateOtp(OtpData otpData) { if (!otpData.isOtpValidated())
	 * { throw new GlobalException("otp is not validated",
	 * JaxError.OTP_NOT_VALIDATED); } }
	 */

	private Customer commitCustomer(com.amx.jax.model.request.CustomerPersonalDetail customerDetails,
			CustomerEmploymentDetails customerEmploymentDetails) {
		Customer customer = offsiteCustomerRegManager.getCustomerForRegistration(customerDetails.getIdentityInt(),
				customerDetails.getIdentityTypeId());
		
		BigDecimal employeeId = metaData.getEmployeeId();
		Employee employeeDetails = employeeRespository.findEmployeeById(employeeId);
		
		if (customer == null) {
			LOGGER.info("creating new customer for offiste registration. idint {} idtype {}",
					customerDetails.getIdentityInt(), customerDetails.getIdentityTypeId());
			customer = new Customer();
		} else {
			LOGGER.info("editing existing customer for offiste registration. idint {} idtype {}",
					customerDetails.getIdentityInt(), customerDetails.getIdentityTypeId());
		}

		if (customerDetails.getIdentityTypeId().equals(new BigDecimal(198))) {
			tenantContext.get().validateCivilId(customerDetails.getIdentityInt());
		}

		if (customer.getEmail() != null) {
			if (!customer.getEmail().equals(customerDetails.getEmail())) {
				tenantContext.get().validateEmailId(customerDetails.getEmail());
			}
		} else {
			tenantContext.get().validateEmailId(customerDetails.getEmail());
		}

		if (customer.getMobile() != null) {
			if (!customer.getMobile().equals(customerDetails.getMobile())) {
				tenantContext.get().validateDuplicateMobile(customerDetails.getMobile());
			}
		} else {
			tenantContext.get().validateDuplicateMobile(customerDetails.getMobile());
		}
		countryMetaValidation.validateMobileNumber(customerDetails.getCountryId(), customerDetails.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerDetails.getCountryId(), customerDetails.getMobile());
		jaxUtil.convertNotNull(customerDetails, customer);
		if(customer.getCustomerReference() == null) {
			BigDecimal customerReference = customerDao.generateCustomerReference();
			customer.setCustomerReference(customerReference);
			LOGGER.info("generated customer ref: {}", customerReference);
		}
		PrefixEnum prefixEnum = PrefixEnum.getPrefixEnum(customerDetails.getTitle());
		customer.setIsActive(ConstantDocument.No);
		customer.setCountryId(customerDetails.getCountryId());
		//customer.setCreatedBy(metaData.getAppType() != null ? metaData.getAppType() : customerDetails.getIdentityInt());
		customer.setCreatedBy(employeeDetails.getUserName() != null ? employeeDetails.getUserName() : customerDetails.getIdentityInt());
		customer.setCreationDate(new Date());
		customer.setIsOnlineUser(ConstantDocument.Yes);
		customer.setGender(prefixEnum.getGender());
		customer.setTitleLocal(getTitleLocal(prefixEnum.getTitleLocal()));
		customer.setLoyaltyPoints(BigDecimal.ZERO);
		customer.setCompanyId(metaData.getCompanyId());
		customer.setCustomerTypeId(
				bizcomponentDao.getBizComponentDataByComponmentCode(ConstantDocument.Individual).getComponentDataId());
		customer.setLanguageId(metaData.getLanguageId());
		customer.setBranchCode(metaData.getCountryBranchId());
		customer.setNationalityId(customerDetails.getNationalityId());

		customer.setPrefixCodeMobile(customerDetails.getTelPrefix());
		customer.setMobile(customerDetails.getMobile());
		customer.setMobileOther(customerDetails.getWatsAppMobileNo());
		customer.setPrefixCodeMobileOther(customerDetails.getWatsAppTelePrefix());
		customer.setIsMobileWhatsApp(customerDetails.getIsWatsApp());
		if (null != customerDetails.getWatsAppMobileNo()) {
			customer.setIsMobileOtherWhatsApp(ConstantDocument.Yes);
		}
		customer.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		customer.setIdentityTypeId(customerDetails.getIdentityTypeId());
		customer.setFirstNameLocal(customerDetails.getFirstNameLocal());
		customer.setLastNameLocal(customerDetails.getLastNameLocal());
		customer.setDateOfBirth(customerDetails.getDateOfBirth());
		customer.setMedicalInsuranceInd(customerDetails.getInsurance());
		if (customerDetails.getIdentityTypeId().toString().equals("204")) {
			customer.setIdentityExpiredDate(customerDetails.getExpiryDate());
		} else {
			customer.setIdentityExpiredDate(customerDetails.getExpiryDate());
		}
		customer.setIdentityInt(customerDetails.getIdentityInt());
		customer.setShortName(customerDetails.getFirstName()+customerDetails.getLastName());

		customer.setCustomerRegistrationType(CustomerRegistrationType.OFF_CUSTOMER);
		if (customerEmploymentDetails != null) {
			customer.setFsArticleDetails(
					articleDao.getArticleDetailsByArticleDetailId(customerEmploymentDetails.getArticleDetailsId()));
			customer.setFsIncomeRangeMaster(
					articleDao.getIncomeRangeMasterByIncomeRangeId(customerEmploymentDetails.getIncomeRangeId()));
		}
		userValidationService.validateBlackListedCustomerForLogin(customer);
		LOGGER.info("Createing new customer record, civil id- {}", customerDetails.getIdentityInt());
		customer = customerRepository.save(customer);
		return customer;
	}

	private String getTitleLocal(String titleLocal) {
		return bizcomponentDao.getBizComponentDataDescByComponmentId(titleLocal).getDataDesc();
	}

	public AmxApiResponse<String, Object> saveCustomeKycDocumentAndPopulateCusmas(ImageSubmissionRequest model) throws ParseException {
		AmxApiResponse<String, Object> result = saveCustomeKycDocument(model);
		/*if (metaData.getCustomerId() != null) {
			customerDao.callProcedurePopulateCusmas(metaData.getCustomerId());
		}*/
		return result;
	}

	private void commitOnlineCustomerIdProof(CustomerInfoRequest model, Customer customer) {

		CustomerIdProof custProof = null;
		List<CustomerIdProof> customerIdProofs = customerIdProofRepository
				.getCustomerIdProofByCustomerId(customer.getCustomerId());
		if (!customerIdProofs.isEmpty()) {
			custProof = customerIdProofs.get(0);
		}
		if (custProof == null) {
			custProof = new CustomerIdProof();
		}
		Customer customerData = new Customer();
		customerData.setCustomerId(customer.getCustomerId());
		custProof.setFsCustomer(customerData);
		custProof.setLanguageId(metaData.getLanguageId());
		BizComponentData customerType = new BizComponentData();
		customerType.setComponentDataId(
				bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, metaData.getLanguageId())
						.getFsBizComponentData().getComponentDataId());
		custProof.setFsBizComponentDataByCustomerTypeId(customerType);
		custProof.setIdentityInt(customer.getIdentityInt());
		custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setCreatedBy(customer.getIdentityInt());
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(customer.getIdentityTypeId());

		if (customer.getIdentityExpiredDate() != null) {
			custProof.setIdentityExpiryDate(customer.getIdentityExpiredDate());
		}
		custProof.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		custProof.setScanSystem(Constants.CUST_DB_SCAN);
		customerIdProofRepository.save(custProof);
	}

	@Override
	@Transactional
	public AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest model) throws ParseException {

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE)
				.field("KYC_DOC");

		if (model != null) {
			if (metaData.getCustomerId() == null) {
				auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.NULL_CUSTOMER_ID));
				throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Customer Id is not available");
			}
			Customer customer = customerRepository.findOne(metaData.getCustomerId());
			if (customer == null) {
				auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.INVALID_CUSTOMER));
				throw new GlobalException(JaxError.INVALID_CUSTOMER, "Customer is Invalid");
			}
			if (model.getImage() == null) {
				auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.IMAGE_NOT_AVAILABLE));
				throw new GlobalException(JaxError.IMAGE_NOT_AVAILABLE, "Image is not available");
			}
			if (model.getIdentityExpiredDate() != null) {
				customerIdProofManager.createIdProofForExpiredCivilId(model, customer);
			}
			for (String image : model.getImage()) {
				DmsApplMapping mappingData = new DmsApplMapping();
				mappingData = customerKycManager.getDmsApplMappingData(customer, model);
				idmsAppMappingRepository.save(mappingData);
				DocBlobUpload documentDetails = new DocBlobUpload();
				documentDetails = getDocumentUploadDetails(image, mappingData);
				docblobRepository.save(documentDetails);
			}
		} else {
			auditService.log(auditEvent.result(Result.FAIL).message(JaxError.IMAGE_NOT_AVAILABLE));
			throw new GlobalException(JaxError.IMAGE_NOT_AVAILABLE, "Image data is not available");
		}
		auditService.log(auditEvent.result(Result.DONE));
		return AmxApiResponse.build("Document Uploaded Successfully");
	}

	private DocBlobUpload getDocumentUploadDetails(String image, DmsApplMapping mappingData) {
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails.setCntryCd(mappingData.getApplicationCountryId());
		documentDetails.setDocBlobID(mappingData.getDocBlobId());
		documentDetails.setDocFinYear(mappingData.getFinancialYear());
		documentDetails.setSeqNo(new BigDecimal(1));
		// documentDetails.setDocContent(image.getBytes());

		try {
			Blob documentContent = new javax.sql.rowset.serial.SerialBlob(decodeImage(image));
			documentDetails.setDocContent(documentContent);
		} catch (SerialException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		documentDetails.setCreatedOn(new Date());
		documentDetails.setCreatedBy(metaData.getCustomerId().toString());
		return documentDetails;
	}

	public static byte[] decodeImage(String imageDataString) {
		return Base64.decodeBase64(imageDataString);
		// return null;
	}

	public AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model) {
		if (model == null) {
			throw new GlobalException(JaxError.SIGNATURE_NOT_AVAILABLE, "Image data is not available");
		}

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE)
				.field("SIGNATURE");

		if (metaData.getCustomerId() == null) {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.NULL_CUSTOMER_ID));
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "Customer Id is not available");
		}
		if (model.getImage() == null) {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.SIGNATURE_NOT_AVAILABLE));
			throw new GlobalException(JaxError.SIGNATURE_NOT_AVAILABLE, "Signature not available");
		}
		Customer customer = customerRepository.findOne((metaData.getCustomerId()));
		if (customer == null) {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.INVALID_CUSTOMER));
			throw new GlobalException(JaxError.INVALID_CUSTOMER, "Customer is Invalid");
		}
		customer.setSignatureSpecimenClob(model.getImage().get(0));
		customer.setPepsIndicator(model.getPoliticallyExposed());
		customerRepository.save(customer);
		auditService.log(auditEvent.result(Result.DONE));
		return AmxApiResponse.build("Signature Uploaded Successfully");
	}

	@Override
	public AmxApiResponse<SendOtpModel, Object> sendOtp(CustomerPersonalDetail customerPersonalDetail) {
		LOGGER.debug("in sendOtp customerPersonalDetail:{} ",customerPersonalDetail);
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(customerPersonalDetail,
				"customerPersonalDetail");
		customerRegistrationManager.setIdentityInt(customerPersonalDetail.getIdentityInt());
		// initiate transaction
		CustomerRegistrationTrnxModel trnxModel = customerRegistrationManager.init(customerPersonalDetail);
		validate(trnxModel, errors);
		SendOtpModel output = customerRegistrationOtpManager.generateOtpTokens(customerPersonalDetail.getIdentityInt());
		customerRegistrationOtpManager.sendOtp();
		return AmxApiResponse.build(output);
	}

	public void validate(Object target, Errors e) {
		CustomerRegistrationTrnxModel beneficiaryTrnxModel = (CustomerRegistrationTrnxModel) target;
		CustomerPersonalDetail customerPersonalDetail = beneficiaryTrnxModel.getCustomerPersonalDetail();
		if (customerPersonalDetail.getIdentityTypeId().equals(new BigDecimal(198))) {
			tenantContext.get().validateCivilId(customerPersonalDetail.getIdentityInt());
		}

		Customer customer = null;
		List<Customer> customerList = customerDao.getCustomerByIdentityInt(customerPersonalDetail.getIdentityInt());
		if (!customerList.isEmpty()) {
			customer = customerList.get(0);
		}

		if (null != customer) {
			if (ConstantDocument.Yes.equals(customer.getIsActive())) {
				throw new GlobalException(JaxError.CUSTOMER_ACTIVE_BRANCH, "Customer active in branch");
			}
		}

		if (null != customer) {
			if (customer.getEmail() != null) {
				if (!customer.getEmail().equals(customerPersonalDetail.getEmail())) {
					tenantContext.get().validateEmailId(customerPersonalDetail.getEmail());
				}
			}
		} else {
			tenantContext.get().validateEmailId(customerPersonalDetail.getEmail());
		}

		if (null != customer) {
			if (customer.getMobile() != null) {
				if (!customer.getMobile().equals(customerPersonalDetail.getMobile())) {
					tenantContext.get().validateDuplicateMobile(customerPersonalDetail.getMobile());
				}
			}
		} else {
			tenantContext.get().validateDuplicateMobile(customerPersonalDetail.getMobile());
		}

		countryMetaValidation.validateMobileNumber(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());

		validateCustomerBlackList(customerPersonalDetail);
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		resetAttempts(otpData);
		customerRegistrationManager.saveOtpData(otpData);
		validateOtpSendCount(beneficiaryTrnxModel.getOtpData());
	}

	private void validateCustomerBlackList(CustomerPersonalDetail customerPersonalDetail) {
		StringBuilder customerName = new StringBuilder();
		if (StringUtils.isNotBlank(customerPersonalDetail.getFirstName())) {
			customerName.append(customerPersonalDetail.getFirstName().trim().toUpperCase());
		}
		if (StringUtils.isNotBlank(customerPersonalDetail.getLastName())) {
			customerName.append(customerPersonalDetail.getLastName().trim().toUpperCase());
		}
		List<BlackListModel> blist = blackListDao.getBlackByName(customerName.toString());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException(JaxError.BLACK_LISTED_CUSTOMER.getStatusKey(), "Customer is black listed");
		}
	}

	private void validateOtpSendCount(OtpData otpData) {
		if (otpData.getSendOtpAttempts() >= otpSettings.getMaxSendOtpAttempts()) {
			throw new GlobalException(JaxError.VALIDATE_OTP_LIMIT_EXCEEDED,
					"Sorry, you cannot proceed to register. Please try to register after 12 midnight");
		}
	}

	@Override
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		return AmxApiResponse.build(cardDetail);
	}

	public AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(CustomerCredential customerCredential) {
		customerRegistrationManager.saveLoginDetail(customerCredential);
		customerCredentialValidator.validate(customerRegistrationManager.get(), null);

		CustomerRegistrationTrnxModel model = customerRegistrationManager.get();
		Customer customer = customerDao.getCustomerByIdentityInt(model.getCustomerPersonalDetail().getIdentityInt())
				.get(0);
		commitOnlineCustomer(model, customer);

		Customer customerDetails = customerService.getCustomerDetails(customerCredential.getLoginId());
		ApplicationSetup applicationSetupData = applicationSetup.getApplicationSetupDetails();
		PersonInfo personinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(personinfo, customerDetails);
		} catch (Exception e) {
		}
		jaxNotificationService.sendPartialRegistraionMail(personinfo, applicationSetupData);
		return AmxApiResponse.build(customerCredential);
	}

	private void commitOnlineCustomer(CustomerRegistrationTrnxModel model, Customer customer) {
		CustomerOnlineRegistration customerOnlineRegistration = new CustomerOnlineRegistration(customer);
		String userName = customerOnlineRegistration.getUserName();
		List<SecurityQuestionModel> secQuestions = model.getSecurityquestions();
		userService.simplifyAnswers(secQuestions);
		customerDao.setSecurityQuestions(secQuestions, customerOnlineRegistration);
		customerOnlineRegistration.setCaption(cryptoUtil.encrypt(userName, model.getCaption()));
		customerOnlineRegistration.setImageUrl(model.getImageUrl());
		customerOnlineRegistration.setLoginId(model.getCustomerCredential().getLoginId());
		customerOnlineRegistration
				.setPassword(cryptoUtil.getHash(userName, model.getCustomerCredential().getPassword()));
		customerOnlineRegistration.setStatus(ConstantDocument.Yes);
		customerDao.saveOnlineCustomer(customerOnlineRegistration);
	}

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(String identityInt,
			BigDecimal identityTypeId) {
		LOGGER.debug("in getOffsiteCustomerData: identityInt {}, identityTypeId {}", identityInt, identityTypeId);
		Customer customer = offsiteCustomerRegManager.getCustomerForRegistration(identityInt, identityTypeId);
		offsiteCustomerRegValidator.validateGetOffsiteCustomerDetailRequest(new GetOffsiteCustomerDetailRequest(identityInt, identityTypeId));
		
		OffsiteCustomerDataDTO offsiteCustomer = new OffsiteCustomerDataDTO();
		offsiteCustomer.setIdentityInt(identityInt);
		offsiteCustomer.setIdentityTypeId(identityTypeId);
		CustomerPersonalDetail customerDetails = new CustomerPersonalDetail();

		if (customer != null) {
			customerDetails.setCustomerId(customer.getCustomerId());
			customerDetails.setCountryId(customer.getCountryId());
			customerDetails.setNationalityId(customer.getNationalityId());
			customerDetails.setIdentityInt(customer.getIdentityInt());
			customerDetails.setTitle(customer.getTitle());
			customerDetails.setFirstName(customer.getFirstName());
			customerDetails.setLastName(customer.getLastName());
			customerDetails.setEmail(customer.getEmail());
			customerDetails.setMobile(customer.getMobile());
			customerDetails.setTelPrefix(customer.getPrefixCodeMobile());
			customerDetails.setFirstNameLocal(customer.getFirstNameLocal());
			customerDetails.setLastNameLocal(customer.getLastNameLocal());
			customerDetails.setExpiryDate(customer.getIdentityExpiredDate());
			customerDetails.setDateOfBirth(customer.getDateOfBirth());
			customerDetails.setIdentityTypeId(customer.getIdentityTypeId());
			customerDetails.setInsurance(customer.getMedicalInsuranceInd());
			customerDetails.setWatsAppMobileNo(customer.getMobileOther());
			customerDetails.setWatsAppTelePrefix(customer.getPrefixCodeMobileOther());
			customerDetails.setIsWatsApp(customer.getIsMobileWhatsApp());
			customerDetails.setRegistrationType(customer.getCustomerRegistrationType());

			offsiteCustomer.setCustomerPersonalDetail(customerDetails);

			// --- Local Address Data
			LocalAddressDetails localAddress = new LocalAddressDetails();
			ContactDetail localData = contactDetailService.getContactsForLocal(customer);
			if (localData != null) {
				localAddress.setContactTypeId(localData.getFsBizComponentDataByContactTypeId().getComponentDataId());
				localAddress.setBlock(localData.getBlock());
				localAddress.setStreet(localData.getStreet());
				localAddress.setHouse(localData.getBuildingNo());
				localAddress.setFlat(localData.getFlat());
				if (null != localData.getFsCountryMaster()) {
					localAddress.setCountryId(localData.getFsCountryMaster().getCountryId());
				}
				if (null != localData.getFsStateMaster()) {
					localAddress.setStateId(localData.getFsStateMaster().getStateId());
				}
				if (null != localData.getFsDistrictMaster()) {
					localAddress.setDistrictId(localData.getFsDistrictMaster().getDistrictId());
				}
				if (null != localData.getFsCityMaster()) {
					localAddress.setCityId(localData.getFsCityMaster().getCityId());
				}
				offsiteCustomer.setLocalAddressDetails(localAddress);
			}
			// --- Home Address Data
			HomeAddressDetails homeAddress = new HomeAddressDetails();
			ContactDetail homeData = contactDetailService.getContactsForHome(customer);
			if (homeData != null) {
				homeAddress.setContactTypeId(homeData.getFsBizComponentDataByContactTypeId().getComponentDataId());
				homeAddress.setBlock(homeData.getBlock());
				homeAddress.setStreet(homeData.getStreet());
				homeAddress.setHouse(homeData.getBuildingNo());
				homeAddress.setFlat(homeData.getFlat());
				if (null != homeData.getFsCountryMaster()) {
					homeAddress.setCountryId(homeData.getFsCountryMaster().getCountryId());
				}
				if (null != homeData.getFsStateMaster()) {
					homeAddress.setStateId(homeData.getFsStateMaster().getStateId());
				}
				if (null != homeData.getFsDistrictMaster()) {
					homeAddress.setDistrictId(homeData.getFsDistrictMaster().getDistrictId());
				}
				if (null != homeData.getFsCityMaster()) {
					homeAddress.setCityId(homeData.getFsCityMaster().getCityId());
				}
				offsiteCustomer.setHomeAddressDestails(homeAddress);
				offsiteCustomer.setCustomerEmploymentDetails(
						customerEmployementManager.createCustomerEmploymentDetail(customer));
			}
			
		} else {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		offsiteCustomer.setCustomerFlags(userService.getCustomerFlags(customer.getCustomerId()));
		return AmxApiResponse.build(offsiteCustomer);
	}
	
	/**
	 * To fetch customer details
	 * auth    : MRU
	 * purpose : to fethc custoemr deatails 
	 */
	
	@Override
	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerDetails(String identityInt,BigDecimal identityTypeId) {
		LOGGER.debug("in getOffsiteCustomerData: identityInt {}, identityTypeId {}", identityInt, identityTypeId);
		OffsiteCustomerDataDTO offsiteCustomer =customerRegistrationManager.getCustomerDeatils(identityInt, identityTypeId);
		return AmxApiResponse.build(offsiteCustomer); 
	}

	public AmxApiResponse<OffsiteCustomerDataDTO, Object> getOffsiteCustomerData(
			GetOffsiteCustomerDetailRequest request) {
		offsiteCustomerRegValidator.validateGetOffsiteCustomerDetailRequest(request);
		return getOffsiteCustomerData(request.getIdentityInt(), request.getIdentityType());
	}
	
}
