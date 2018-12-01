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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.PrefixEnum;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.CustomerCredential;
import com.amx.jax.ICustRegService;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.customer.CustomerAuditEvent;
import com.amx.jax.customer.CustomerAuditEvent.Type;
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
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.dbmodel.EmploymentTypeMasterView;
import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.dbmodel.ProfessionMasterView;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.CardDetail;
import com.amx.jax.model.OtpData;
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
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.DOCBLOBRepository;
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
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.manager.CustomerRegistrationOtpManager;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.repository.CustomerRepository;
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
	CountryMasterRepository countryMasterRepository;

	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;

	@Autowired
	DOCBLOBRepository docblobRepository;

	@Autowired
	IUserFinancialYearRepo userFinancialYearRepo;

	@Autowired
	ImageCheckDao imageCheckDao;

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
			throw new GlobalException("Id Type List Is Not available ", JaxError.EMPTY_ID_TYPE_LIST);
		}
		return AmxApiResponse.buildList(list);
	}

	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {

		OtpData otpData = customerRegistrationManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(offsiteCustRegModel.getmOtp())) {
				auditService.excep(new CustomerAuditEvent(Type.VALIDATE_OTP, offsiteCustRegModel),
						new GlobalException("Otp field is required", JaxError.MISSING_OTP));
				throw new GlobalException("Otp field is required", JaxError.MISSING_OTP);
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				auditService.excep(new CustomerAuditEvent(Type.VALIDATE_OTP, offsiteCustRegModel),
						new GlobalException(
								"Sorry, you cannot proceed to register. Please try to register after 12 midnight",
								JaxError.VALIDATE_OTP_LIMIT_EXCEEDED));
				throw new GlobalException(
						"Sorry, you cannot proceed to register. Please try to register after 12 midnight",
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED);
			}
			
			// actual validation logic
			if (!otpData.getmOtp().equals(offsiteCustRegModel.getmOtp())) {
				otpMismatch(otpData);
			}
			
			if (!StringUtils.isBlank(offsiteCustRegModel.geteOtp())) {
				if (!otpData.geteOtp().equals(offsiteCustRegModel.geteOtp())) {
					otpMismatch(otpData);
				}
			}
				
			otpData.setOtpValidated(true);
			otpData.resetCounts();
		} finally {
			customerRegistrationManager.saveOtpData(otpData);
		}
		AmxApiResponse<String, Object> obj = AmxApiResponse.build("Customer Email And Mobile Validation Successfully");
		obj.setMessageKey("AUTH_SUCCESS");
		auditService.log(new CustomerAuditEvent(Type.VALIDATE_OTP, offsiteCustRegModel));
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
		throw new GlobalException("Invalid otp", JaxError.INVALID_OTP);

	}

	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse() {
		List<Map<String, Object>> articleList = articleDao.getArticles(metaData.getLanguageId());
		if (articleList == null || articleList.isEmpty()) {
			throw new GlobalException("Article List Is Empty ", JaxError.EMPTY_ARTICLE_LIST);
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
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(articleId, null, null);
		if (designationList == null || designationList.isEmpty()) {
			auditService.excep(new CustomerAuditEvent(Type.DESIGNATION_LIST, details),
					new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST));
			throw new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST);
		}
		List<ArticleDetailsDescDto> designationDataList = convertDesignation(designationList);
		auditService.log(new CustomerAuditEvent(Type.DESIGNATION_LIST, details));
		return AmxApiResponse.buildList(designationDataList);
	}

	private List<ArticleDetailsDescDto> convertDesignation(List<Map<String, Object>> designationList) {
		List<ArticleDetailsDescDto> output = new ArrayList<>();
		designationList.forEach(i -> {
			output.add(convertDesignation(i));
		});
		return output;
	}

	private ArticleDetailsDescDto convertDesignation(Map<String, Object> i) {
		ArticleDetailsDescDto dto = new ArticleDetailsDescDto();
		dto.setArticleDetailsDesc(
				i.get("ARTICLE_DETAIL_DESC") != null ? i.get("ARTICLE_DETAIL_DESC").toString() : null);
		dto.setArticleDetailsDescId(new BigDecimal(
				i.get("ARTICLE_DETAILS_DESC_ID") != null ? i.get("ARTICLE_DETAILS_DESC_ID").toString() : null));
		dto.setArticleDetailsId(
				new BigDecimal(i.get("ARTICLE_DETAILS_ID") != null ? i.get("ARTICLE_DETAILS_ID").toString() : null));
		dto.setLanguageId(new BigDecimal(i.get("LANGUAGE_ID") != null ? i.get("LANGUAGE_ID").toString() : null));
		return dto;
	}

	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal articleDetailsId = model.getArticleDetailsId();
		List<Map<String, Object>> incomeRangeList = articleDao.getIncomeRange(countryId, articleDetailsId);
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(null, articleDetailsId, countryId);
		if (incomeRangeList == null || incomeRangeList.isEmpty()) {
			auditService.excep(new CustomerAuditEvent(Type.INCOME_RANGE, details),
					new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE));
			throw new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE);
		}
		List<IncomeRangeDto> incomeRangeDataList = convertIncomeRange(incomeRangeList);
		auditService.log(new CustomerAuditEvent(Type.INCOME_RANGE, details));
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
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
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
			auditService.excep(new CustomerAuditEvent(Type.FIELD_LIST, model),
					new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION));
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
		}
		auditService.log(new CustomerAuditEvent(Type.FIELD_LIST, model));
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
			throw new GlobalException("Employment Type List Not Available", JaxError.EMPTY_EMPLOYMENT_TYPE);
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
			throw new GlobalException("Profession List Not Available", JaxError.EMPTY_PROFESSION_LIST);
		}
		List<ComponentDataDto> list = new ArrayList<>();
		for (ProfessionMasterView map : view) {
			list.add(new ComponentDataDto(map.getComponentDataId(), map.getDataDesc()));
		}
		return AmxApiResponse.buildList(list);
	}

	@Override
	public AmxApiResponse<CustomerInfo, Object> saveCustomerInfo(CustomerInfoRequest model) {
		// revalidateOtp(model.getOtpData());
		CustomerPersonalDetail customerDetails = new CustomerPersonalDetail();
		jaxUtil.convert(model.getCustomerPersonalDetail(), customerDetails);
		Customer customer = commitCustomer(customerDetails, model.getCustomerEmploymentDetails());
		commitCustomerLocalContact(model.getLocalAddressDetails(), customer, customerDetails);
		commitCustomerHomeContact(model.getHomeAddressDestails(), customer, customerDetails);
		commitOnlineCustomerIdProof(model, customer);
		commitEmploymentDetails(model.getCustomerEmploymentDetails(), customer, model.getLocalAddressDetails());
		auditService.log(new CustomerAuditEvent(Type.CUST_INFO, model));
		CustomerInfo info = new CustomerInfo();
		info.setCustomerId(customer.getCustomerId());
		return AmxApiResponse.build(info);
	}

	private void commitEmploymentDetails(CustomerEmploymentDetails customerEmploymentDetails, Customer customer, LocalAddressDetails localAddressDetails) {
		if (customerEmploymentDetails != null) {
			EmployeeDetails employeeModel = new EmployeeDetails();
			employeeModel.setFsBizComponentDataByEmploymentTypeId(bizcomponentDao
					.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getEmploymentTypeId()));
			employeeModel.setFsBizComponentDataByOccupationId(
					bizcomponentDao.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getProfessionId()));
			employeeModel.setEmployerName(customerEmploymentDetails.getEmployer());
			employeeModel.setBlock(localAddressDetails.getBlock());
			employeeModel.setStreet(localAddressDetails.getStreet());
			employeeModel.setArea(localAddressDetails.getStateId().toString());
			employeeModel.setPostal(customerEmploymentDetails.getPostal());
			employeeModel.setOfficeTelephone(customerEmploymentDetails.getOfficeTelephone());
			/*employeeModel.setFsCountryMaster(
					countryMasterRepository.getCountryMasterByCountryId(customerEmploymentDetails.getCountryId()));*/
			employeeModel.setFsCountryMaster(
					countryMasterRepository.getCountryMasterByCountryId(localAddressDetails.getCountryId()));
			employeeModel.setFsStateMaster(localAddressDetails.getStateId());
			employeeModel.setFsDistrictMaster(localAddressDetails.getDistrictId());
			employeeModel.setFsCityMaster(localAddressDetails.getCityId());
			// employeeModel.setFsCompanyMaster(customerEmploymentDetails.getCompanyId());
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
			ContactDetail contactDetail = new ContactDetail();
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
			// home type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(49));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
			contactDetailsRepository.save(contactDetail);
		}
	}

	private void commitCustomerHomeContact(HomeAddressDetails homeAddressDestails, Customer customer,
			com.amx.jax.model.request.CustomerPersonalDetail customerDetails) {
		if (homeAddressDestails != null) {
			ContactDetail contactDetail = new ContactDetail();
			contactDetail.setFsCountryMaster(new CountryMaster(homeAddressDestails.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(homeAddressDestails.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(homeAddressDestails.getStateId()));
			contactDetail.setFsCityMaster(new CityMaster(homeAddressDestails.getCityId()));
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
		Customer customer = new Customer();
		customer = customerRepository.getCustomerByCivilIdAndIsActive(customerDetails.getIdentityInt(),
				customerDetails.getCountryId(), customerDetails.getIdentityTypeId());
		if (customer != null) {
			if (customer.getIdentityTypeId().equals(new BigDecimal(198))) {
				throw new GlobalException("Customer Civil Id Already Exist", JaxError.EXISTING_CIVIL_ID);
			}
			if (customer.getIdentityTypeId().equals(new BigDecimal(204))) {
				throw new GlobalException("Passport Number Already Exist", JaxError.EXISTING_PASSPORT);
			}
			if (customer.getIdentityTypeId().equals(new BigDecimal(201))) {
				throw new GlobalException("GCC ID Already Exist", JaxError.EXISTING_GCC_ID);
			}
			if (customer.getIdentityTypeId().equals(new BigDecimal(197))) {
				throw new GlobalException("BEDOUIN ID Already Exist", JaxError.EXISTING_BEDOUIN_ID);
			}

		}
		customer = new Customer();
		if (customerDetails.getIdentityTypeId().equals(new BigDecimal(198))) {
			tenantContext.get().validateCivilId(customerDetails.getIdentityInt());
		}
		tenantContext.get().validateEmailId(customerDetails.getEmail());
		tenantContext.get().validateDuplicateMobile(customerDetails.getMobile());
		countryMetaValidation.validateMobileNumber(customerDetails.getCountryId(), customerDetails.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerDetails.getCountryId(), customerDetails.getMobile());
		jaxUtil.convert(customerDetails, customer);
		BigDecimal customerReference = customerDao.generateCustomerReference();
		PrefixEnum prefixEnum = PrefixEnum.getPrefixEnum(customerDetails.getTitle());
		customer.setCustomerReference(customerReference);
		customer.setIsActive(ConstantDocument.No);
		customer.setCountryId(customerDetails.getCountryId());
		customer.setCreatedBy(metaData.getAppType() != null ? metaData.getAppType() : customerDetails.getIdentityInt());
		customer.setCreationDate(new Date());
		customer.setIsOnlineUser(ConstantDocument.Yes);
		customer.setGender(prefixEnum.getGender());
		customer.setTitleLocal(getTitleLocal(prefixEnum.getTitleLocal()));
		customer.setLoyaltyPoints(BigDecimal.ZERO);
		customer.setCompanyId(metaData.getCompanyId());
		customer.setCustomerTypeId(bizcomponentDao.getBizComponentDataByComponmentCode(ConstantDocument.Individual).getComponentDataId());
		customer.setLanguageId(metaData.getLanguageId());
		customer.setBranchCode(metaData.getCountryBranchId());
		customer.setNationalityId(customerDetails.getNationalityId());
		
		customer.setPrefixCodeMobile(customerDetails.getTelPrefix());
		customer.setMobile(customerDetails.getMobile());
		customer.setMobileOther(customerDetails.getWatsAppMobileNo());
		customer.setPrefixCodeMobileOther(customerDetails.getWatsAppTelePrefix());
		customer.setIsMobileWhatsApp(customerDetails.getIsWatsApp());
		
		customer.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		customer.setIdentityTypeId(customerDetails.getIdentityTypeId());
		customer.setFirstNameLocal(customerDetails.getFirstNameLocal());
		customer.setLastNameLocal(customerDetails.getLastNameLocal());
		customer.setDateOfBirth(customerDetails.getDateOfBirth());
		customer.setMedicalInsuranceInd(customerDetails.getInsurance());
		if (customerDetails.getIdentityTypeId().toString().equals("204")) {
			customer.setIdentityExpiredDate(null);
			// commented by Prashant
			//customer.setExpiryDate(customerDetails.getExpiryDate());
			//customer.setIssueDate(customerDetails.getIssueDate());
		} else {
			customer.setIdentityExpiredDate(customerDetails.getExpiryDate());
			//customer.setExpiryDate(null);
			//customer.setIssueDate(null);
		}
		customer.setIdentityInt(customerDetails.getIdentityInt());
		
		customer.setCustomerRegistrationType(CustomerRegistrationType.OFF_CUSTOMER);
		if (customerEmploymentDetails != null) {
			customer.setFsArticleDetails(
					articleDao.getArticleDetailsByArticleDetailId(customerEmploymentDetails.getArticleDetailsId()));
			customer.setFsIncomeRangeMaster(
					articleDao.getIncomeRangeMasterByIncomeRangeId(customerEmploymentDetails.getIncomeRangeId()));
		}

		LOGGER.info("generated customer ref: {}", customerReference);
		LOGGER.info("Createing new customer record, civil id- {}", customerDetails.getIdentityInt());
		customerRepository.save(customer);
		return customer;
	}

	private String getTitleLocal(String titleLocal) {
		return bizcomponentDao.getBizComponentDataDescByComponmentId(titleLocal).getDataDesc();
	}

	private void commitOnlineCustomerIdProof(CustomerInfoRequest model, Customer customer) {
		CustomerIdProof custProof = new CustomerIdProof();

		Customer customerData = new Customer();
		customerData.setCustomerId(customer.getCustomerId());
		custProof.setFsCustomer(customerData);
		custProof.setLanguageId(metaData.getLanguageId());
		BizComponentData customerType = new BizComponentData();
		customerType.setComponentDataId(bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, metaData.getLanguageId()).getFsBizComponentData().getComponentDataId());
		custProof.setFsBizComponentDataByCustomerTypeId(customerType);
		custProof.setIdentityInt(customer.getIdentityInt());
		custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setCreatedBy(customer.getIdentityInt());
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(customer.getIdentityTypeId());
		
		if(customer.getIdentityExpiredDate() != null) {
			custProof.setIdentityExpiryDate(customer.getIdentityExpiredDate());
		}
		custProof.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		custProof.setScanSystem(Constants.CUST_DB_SCAN);
		customerIdProofRepository.save(custProof);
	}

	@Override
	public AmxApiResponse<String, Object> saveCustomeKycDocument(ImageSubmissionRequest model) throws ParseException {
		if (model != null) {
			if (metaData.getCustomerId() == null) {
				auditService.excep(new CustomerAuditEvent(Type.KYC_DOC, metaData.getCustomerId()),
						new GlobalException("Customer Id is not available", JaxError.NULL_CUSTOMER_ID));
				throw new GlobalException("Customer Id is not available", JaxError.NULL_CUSTOMER_ID);
			}
			Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(metaData.getCustomerId(),
					Constants.NO);
			if (customer == null) {
				auditService.excep(new CustomerAuditEvent(Type.KYC_DOC, metaData.getCustomerId()),
						new GlobalException("Customer is Invalid", JaxError.INVALID_CUSTOMER));
				throw new GlobalException("Customer is Invalid", JaxError.INVALID_CUSTOMER);
			}
			if (model.getImage() == null) {
				auditService.excep(new CustomerAuditEvent(Type.KYC_DOC, metaData.getCustomerId()),
						new GlobalException("Image is not available", JaxError.IMAGE_NOT_AVAILABLE));
				throw new GlobalException("Image is not available", JaxError.IMAGE_NOT_AVAILABLE);
			}

			for (String image : model.getImage()) {
				DmsApplMapping mappingData = new DmsApplMapping();
				mappingData = getDmsApplMappingData(customer);
				idmsAppMappingRepository.save(mappingData);
				DocBlobUpload documentDetails = new DocBlobUpload();
				documentDetails = getDocumentUploadDetails(image, mappingData);
				docblobRepository.save(documentDetails);
			}
		} else {
			throw new GlobalException("Image data is not available", JaxError.IMAGE_NOT_AVAILABLE);
		}
		return AmxApiResponse.build("Document Uploaded Successfully");
	}

	private DocBlobUpload getDocumentUploadDetails(String image, DmsApplMapping mappingData) {
		DocBlobUpload documentDetails = new DocBlobUpload();
		documentDetails.setCntryCd(mappingData.getApplicationCountryId());
		documentDetails.setDocBlobID(mappingData.getDocBlobId());
		documentDetails.setDocFinYear(mappingData.getFinancialYear());
		documentDetails.setSeqNo(new BigDecimal(1));
		//documentDetails.setDocContent(image.getBytes());
		
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
        //return null;
	}

	private DmsApplMapping getDmsApplMappingData(Customer model) throws ParseException {
		DmsApplMapping mappingData = new DmsApplMapping();
		BigDecimal financialYear = getDealYearbyDate();
		BigDecimal applCountryId = metaData.getCountryId();
		BigDecimal docBlobId = imageCheckDao.callTogenerateBlobID(financialYear);
		mappingData.setApplicationCountryId(applCountryId);
		mappingData.setCustomerId(metaData.getCustomerId());
		mappingData.setDocBlobId(docBlobId); // need to change value
		mappingData.setDocFormat("JPG");
		mappingData.setFinancialYear(financialYear);
		mappingData.setIdentityExpiryDate(model.getIdentityExpiredDate());
		mappingData.setIdentityInt(model.getIdentityInt());
		mappingData.setIdentityIntId(model.getIdentityTypeId());
		mappingData.setCreatedBy(metaData.getEmployeeId().toString());
		mappingData.setCreatedOn(new Date());
		return mappingData;
	}

	public BigDecimal getDealYearbyDate() throws ParseException {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		Date today = Calendar.getInstance().getTime();
		String date = formatter.format(today);
		UserFinancialYear list = userFinancialYearRepo.findAllByFinancialYearBegin(date, date);
		BigDecimal financialYear = list.getFinancialYear();
		return financialYear;
	}

	public AmxApiResponse<String, Object> saveCustomerSignature(ImageSubmissionRequest model) {
		if (model == null) {
			throw new GlobalException("Image data is not available", JaxError.SIGNATURE_NOT_AVAILABLE);
		}
		if (metaData.getCustomerId() == null) {
			auditService.excep(new CustomerAuditEvent(Type.SIGNATURE, metaData.getCustomerId()),
					new GlobalException("Customer Id is not available", JaxError.NULL_CUSTOMER_ID));
			throw new GlobalException("Customer Id is not available", JaxError.NULL_CUSTOMER_ID);
		}
		if (model.getImage() == null) {
			auditService.excep(new CustomerAuditEvent(Type.SIGNATURE, metaData.getCustomerId()),
					new GlobalException("Signature not available for this customer", JaxError.NULL_CUSTOMER_ID));
			throw new GlobalException("Signature not available", JaxError.SIGNATURE_NOT_AVAILABLE);
		}
		Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(metaData.getCustomerId(),
				Constants.NO);
		if (customer == null) {
			auditService.excep(new CustomerAuditEvent(Type.SIGNATURE, metaData.getCustomerId()),
					new GlobalException("Customer is Invalid", JaxError.INVALID_CUSTOMER));
			throw new GlobalException("Customer is Invalid", JaxError.INVALID_CUSTOMER);
		}
		customer.setSignatureSpecimenClob(model.getImage().get(0));
		customer.setPepsIndicator(model.getPoliticallyExposed());
		customerRepository.save(customer);
		return AmxApiResponse.build("Signature Uploaded Successfully");
	}

	@Override
	public AmxApiResponse<SendOtpModel, Object> sendOtp(CustomerPersonalDetail customerPersonalDetail) {
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
		tenantContext.get().validateEmailId(customerPersonalDetail.getEmail());
		tenantContext.get().validateDuplicateMobile(customerPersonalDetail.getMobile());
		countryMetaValidation.validateMobileNumber(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		countryMetaValidation.validateMobileNumberLength(customerPersonalDetail.getCountryId(),
				customerPersonalDetail.getMobile());
		userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(customerPersonalDetail.getIdentityInt(),
				JaxApiFlow.SIGNUP_DEFAULT);
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
			throw new GlobalException("Customer is black listed", JaxError.BLACK_LISTED_CUSTOMER.getStatusKey());
		}
	}

	private void validateOtpSendCount(OtpData otpData) {
		if (otpData.getSendOtpAttempts() >= otpSettings.getMaxSendOtpAttempts()) {
			throw new GlobalException("Sorry, you cannot proceed to register. Please try to register after 12 midnight",
					JaxError.VALIDATE_OTP_LIMIT_EXCEEDED);
		}
	}

	@Override
	public AmxApiResponse<CardDetail, Object> cardScan(CardDetail cardDetail) {
		return AmxApiResponse.build(cardDetail);
	}
 
	public AmxApiResponse<CustomerCredential, Object> saveLoginDetailOffsite(CustomerCredential customerCredential) {
		customerRegistrationManager.saveLoginDetail(customerCredential);
		customerCredentialValidator.validate(customerRegistrationManager.get(),  null);
		
		CustomerRegistrationTrnxModel model = customerRegistrationManager.get();
		Customer customer = customerDao.getCustomerByIdentityInt(model.getCustomerPersonalDetail().getIdentityInt()).get(0);
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
}
