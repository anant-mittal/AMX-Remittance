package com.amx.jax.branch.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.PrefixEnum;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.ICustRegService;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.auditlogs.JaxAuditEvent.Type;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.FieldListDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.CityMaster;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.DocBlobUpload;
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.dbmodel.EmploymentTypeMasterView;
import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.dbmodel.ProfessionMasterView;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.FieldListDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.jax.repository.CountryMasterRepository;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.DOCBLOBRepository;
import com.amx.jax.repository.EmploymentTypeRepository;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.repository.ProfessionRepository;
import com.amx.jax.service.PrefixService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Constants;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsitCustRegService implements ICustRegService {

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
	
	public AmxApiResponse<ComponentDataDto, Object> sendIdTypes() {
		List<Map<String, Object>> tempList = bizcomponentDao
				.getAllComponentComboDataForCustomer(metaData.getLanguageId());
		List<ComponentDataDto> list = new ArrayList<>();
		for (Map row : tempList) {
			String idType = bizcomponentDao.getIdentityTypeMaster((BigDecimal) row.get("COMPONENT_DATA_ID"));
			if (idType.equalsIgnoreCase("I")) {
				list.add(
						new ComponentDataDto((BigDecimal) row.get("COMPONENT_DATA_ID"), (String) row.get("DATA_DESC")));
			}
		}		
		if (tempList.isEmpty())
		{
			throw new GlobalException("Id Type List Is Not available ", JaxError.EMPTY_ID_TYPE_LIST);
		}		
		return AmxApiResponse.buildList(list);
	}	

	public AmxApiResponse<String, Object> validateOtpForEmailAndMobile(OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(offsiteCustRegModel.geteOtp()) || StringUtils.isBlank(offsiteCustRegModel.getmOtp())) {
				auditService.excep(new JaxAuditEvent(Type.VALIDATE_OTP,offsiteCustRegModel), 
						new GlobalException("Otp field is required", JaxError.MISSING_OTP));
				throw new GlobalException("Otp field is required", JaxError.MISSING_OTP);
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				auditService.excep(new JaxAuditEvent(Type.VALIDATE_OTP,offsiteCustRegModel), 
						new GlobalException(
								"Sorry, you cannot proceed to register. Please try to register after 12 midnight",
								JaxError.VALIDATE_OTP_LIMIT_EXCEEDED));
				throw new GlobalException(
						"Sorry, you cannot proceed to register. Please try to register after 12 midnight",
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED);
			}
			// actual validation logic
			if (!otpData.geteOtp().equals(offsiteCustRegModel.geteOtp()) || !otpData.getmOtp().equals(offsiteCustRegModel.getmOtp())) {
				otpMismatch(otpData);
			}
			otpData.setOtpValidated(true);
			otpData.resetCounts();
		} finally {
			customerRegistrationManager.saveOtpData(otpData);
		}
		AmxApiResponse<String, Object> obj = AmxApiResponse.build("Customer Email And Mobile Validation Successfull");		
		obj.setMessageKey("AUTH_SUCCESS");
		auditService.log(new JaxAuditEvent(Type.VALIDATE_OTP,offsiteCustRegModel));
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
		if(articleList == null || articleList.isEmpty())
		{				
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
		dto.setArticleDescId(new BigDecimal(i.get("ARTICLE_DESC_ID").toString()));
		dto.setArticleDescription(i.get("ARTICLE_DESC").toString());
		dto.setArticleId(new BigDecimal(i.get("ARTICLE_ID").toString()));
		dto.setLanguageType(new BigDecimal(i.get("LANGUAGE_ID").toString()));
		return dto;
	}

	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(EmploymentDetailsRequest model) {
		BigDecimal articleId = model.getArticleId();
		List<Map<String, Object>> designationList = articleDao.getDesignationData(articleId, metaData.getLanguageId());
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(articleId,null,null);
		if(designationList == null || designationList.isEmpty())
		{
			auditService.excep(new JaxAuditEvent(Type.DESIGNATION_LIST,details), 
					new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST));
			throw new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST);
		}
		List<ArticleDetailsDescDto> designationDataList = convertDesignation(designationList);
		auditService.log(new JaxAuditEvent(Type.DESIGNATION_LIST,details));
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
		dto.setArticleDetailsDesc(i.get("ARTICLE_DETAIL_DESC") != null ? i.get("ARTICLE_DETAIL_DESC").toString():"");
		dto.setArticleDetailsDescId(new BigDecimal(i.get("ARTICLE_DETAILS_DESC_ID") != null ? i.get("ARTICLE_DETAILS_DESC_ID").toString():""));
		dto.setArticleDetailsId(new BigDecimal(i.get("ARTICLE_DETAILS_ID")!= null ? i.get("ARTICLE_DETAILS_ID").toString():""));
		dto.setLanguageId(new BigDecimal(i.get("LANGUAGE_ID") != null ? i.get("LANGUAGE_ID").toString():""));
		return dto;
	}

	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(EmploymentDetailsRequest model) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal articleDetailsId = model.getArticleDetailsId(); 
		List<Map<String, Object>> incomeRangeList = articleDao.getIncomeRange(countryId,articleDetailsId);
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(null,articleDetailsId,countryId);
		if(incomeRangeList == null || incomeRangeList.isEmpty())
		{
			auditService.excep(new JaxAuditEvent(Type.INCOME_RANGE,details), 
					new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE));
			throw new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE);
		}
		List<IncomeRangeDto> incomeRangeDataList = convertIncomeRange(incomeRangeList);
		auditService.log(new JaxAuditEvent(Type.INCOME_RANGE,details));
		return AmxApiResponse.buildList(incomeRangeDataList);
	}

	private List<IncomeRangeDto> convertIncomeRange(List<Map<String, Object>> incomeRangeList) {
		List<IncomeRangeDto> output = new ArrayList<>();
		incomeRangeList.forEach(i-> {
			output.add(convertIncomeRange(i));
		});
		return output;
	}

	private IncomeRangeDto convertIncomeRange(Map<String, Object> i) {
		IncomeRangeDto dto = new IncomeRangeDto();
		dto.setArticleDetailsId(new BigDecimal(i.get("ARTICLE_DETAIL_ID") != null ? i.get("ARTICLE_DETAIL_ID").toString():""));
		dto.setIncomeFrom(new BigDecimal(i.get("INCOME_FROM") != null ? i.get("INCOME_FROM").toString():""));
		dto.setIncomeRangeId(new BigDecimal(i.get("INCOME_RANGE_ID") != null ? i.get("INCOME_RANGE_ID").toString():""));
		dto.setIncomeTo(new BigDecimal(i.get("INCOME_TO") != null ? i.get("INCOME_TO").toString():""));
		return dto;
	}

	public AmxApiResponse<Map<String, FieldListDto>, Object> getFieldList(DynamicFieldRequest model) {
		List<FieldList> fieldList = null;
		fieldList = fieldListDao.getFieldList(model.getTenant(),Constants.COMMON_NATIONALITY,model.getComponent());
		if(fieldList == null)
		{
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
		}
		List<FieldListDto> listDto = convertFieldList(fieldList) ;
		Map<String,FieldListDto> map = new HashMap<>();
		if(listDto != null)
		{
			map = listDto.stream().collect(Collectors.toMap(FieldListDto:: getKey, Function.identity()));	
		}
		
		if(model.getNationality()!= null && !model.getNationality().equalsIgnoreCase("ALL"))
		{
			fieldList = fieldListDao.getFieldList(model.getTenant(),model.getNationality(),model.getComponent());			
			if(fieldList != null)
			{				
				listDto = new ArrayList<>(); 
				listDto = convertFieldList(fieldList) ;
				Map<String,FieldListDto> map1 = listDto.stream().collect(Collectors.toMap(FieldListDto:: getKey, Function.identity()));
				map.putAll(map1);
			}
		}	
		if(map == null || map.isEmpty())
		{
			auditService.excep(new JaxAuditEvent(Type.FIELD_LIST,model),
					new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION));
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
		}		
		auditService.log(new JaxAuditEvent(Type.FIELD_LIST,model));
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
		if(view.isEmpty())
		{
			throw new GlobalException("Employment Type List Not Available",JaxError.EMPTY_EMPLOYMENT_TYPE);
		}
		List<ComponentDataDto> list = new ArrayList<>();
		for(EmploymentTypeMasterView map : view)
		{
			list.add(new ComponentDataDto(map.getComponentDataId(),map.getDataDesc()));
		}	
		return AmxApiResponse.buildList(list);
	}	

	@Override
	public AmxApiResponse<ComponentDataDto, Object> sendProfessionList() {
		List<ProfessionMasterView> view = professionRepository.findAll();
		if(view.isEmpty())
		{
			throw new GlobalException("Profession List Not Available",JaxError.EMPTY_PROFESSION_LIST);
		}
		List<ComponentDataDto> list = new ArrayList<>();
		for(ProfessionMasterView map : view)
		{
			list.add(new ComponentDataDto(map.getComponentDataId(),map.getDataDesc()));
		}
		return AmxApiResponse.buildList(list);
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> saveCustomerInfo(CustomerInfoRequest model) {		
		//revalidateOtp(model.getOtpData());
		auditService.log(new JaxAuditEvent(Type.CUST_INFO,model));
		Customer customer = commitCustomer(model.getCustomerPersonalDetail(),model.getCustomerEmploymentDetails());
		commitCustomerLocalContact(model.getLocalAddressDetails(), customer);
		commitCustomerHomeContact(model.getHomeAddressDestails(), customer);				
		commitOnlineCustomerIdProof(model, customer);
		commitEmploymentDetails(model.getCustomerEmploymentDetails(),customer);
		return AmxApiResponse.build(customer.getCustomerId());
	}
	
	private void commitEmploymentDetails(CustomerEmploymentDetails customerEmploymentDetails, Customer customer) {
		if(customerEmploymentDetails != null)
		{
			EmployeeDetails employeeModel = new EmployeeDetails();
			employeeModel.setFsBizComponentDataByEmploymentTypeId(
					bizcomponentDao.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getEmploymentTypeId()));
			employeeModel.setFsBizComponentDataByOccupationId(
					bizcomponentDao.getBizComponentDataByComponmentDataId(customerEmploymentDetails.getProfessionId()));
			employeeModel.setEmployerName(customerEmploymentDetails.getEmployer());
			employeeModel.setBlock(customerEmploymentDetails.getBlock());
			employeeModel.setStreet(customerEmploymentDetails.getStreet());
			employeeModel.setArea(customerEmploymentDetails.getArea());
			employeeModel.setPostal(customerEmploymentDetails.getPostal());
			employeeModel.setOfficeTelephone(customerEmploymentDetails.getOfficeTelephone());
			employeeModel.setFsCountryMaster(
					countryMasterRepository.getCountryMasterByCountryId(customerEmploymentDetails.getCountryId()));
			employeeModel.setFsStateMaster(customerEmploymentDetails.getStateId());
			employeeModel.setFsDistrictMaster(customerEmploymentDetails.getDistrictId());
			employeeModel.setFsCityMaster(customerEmploymentDetails.getCityId());
			//employeeModel.setFsCompanyMaster(customerEmploymentDetails.getCompanyId());
			employeeModel.setIsActive(ConstantDocument.Yes);
			employeeModel.setCreatedBy(metaData.getCustomerId().toString());
			employeeModel.setCreationDate(new Date());
			employeeModel.setFsCustomer(customer);
			customerEmployeeDetailsRepository.save(employeeModel);
		}
		
	}

	private void commitCustomerLocalContact(LocalAddressDetails localAddressDetails, Customer customer) {
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
			contactDetail.setCreatedBy(metaData.getCustomerId().toString());
			contactDetail.setCreationDate(customer.getCreationDate());
			BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
			// home type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(49));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);			
			contactDetailsRepository.save(contactDetail);
		}
	}

	private void commitCustomerHomeContact(HomeAddressDetails homeAddressDestails, Customer customer) {
		if (homeAddressDestails != null) {
			ContactDetail contactDetail = new ContactDetail();
			contactDetail.setFsCountryMaster(new CountryMaster(homeAddressDestails.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(homeAddressDestails.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(homeAddressDestails.getStateId()));
			contactDetail.setFsCityMaster(new CityMaster(homeAddressDestails.getCityId()));
			contactDetail.setBuildingNo(homeAddressDestails.getHouse());
			contactDetail.setFlat(homeAddressDestails.getFlat());
			contactDetail.setFsCustomer(customer);
			contactDetail.setActiveStatus(ConstantDocument.Yes);
			contactDetail.setLanguageId(customer.getLanguageId());
			contactDetail.setCreatedBy(metaData.getCustomerId().toString());
			contactDetail.setCreationDate(customer.getCreationDate());
			BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
			// home type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(50));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
			contactDetailsRepository.save(contactDetail);
		}
	}

	/*private void revalidateOtp(OtpData otpData) {
		if (!otpData.isOtpValidated()) {
			throw new GlobalException("otp is not validated", JaxError.OTP_NOT_VALIDATED);
		}
	}*/

	private Customer commitCustomer(CustomerPersonalDetail customerPersonalDetail, CustomerEmploymentDetails customerEmploymentDetails) {
		Customer customer = new Customer();		
		customer = customerRepository.getCustomerByCivilIdAndIsActive(customerPersonalDetail.getIdentityInt(),customerPersonalDetail.getCountryId());
		if(customer != null)
		{			
			throw new GlobalException("Customer Civil Id Already Exist", JaxError.EXISTING_CIVIL_ID);
		}
		customer = new Customer();
		jaxUtil.convert(customerPersonalDetail, customer);
		BigDecimal customerReference = customerDao.generateCustomerReference();
		PrefixEnum prefixEnum = PrefixEnum.getPrefixEnum(customerPersonalDetail.getTitle());
		customer.setCustomerReference(customerReference);
		customer.setIsActive(ConstantDocument.No);
		customer.setCountryId(metaData.getCountryId());
		customer.setCreatedBy(metaData.getAppType() != null ? metaData.getAppType()
				: customerPersonalDetail.getIdentityInt());
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
		customer.setNationalityId(customerPersonalDetail.getNationalityId());
		customer.setMobile(customerPersonalDetail.getMobile());
		customer.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		customer.setIdentityTypeId(customerPersonalDetail.getIdentityTypeId());
		customer.setFirstNameLocal(customerPersonalDetail.getFirstNameLocal());
		customer.setLastNameLocal(customerPersonalDetail.getLastNameLocal());
		customer.setDateOfBirth(customerPersonalDetail.getDateOfBirth());
		if(customerPersonalDetail.getIdentityTypeId().toString().equals("204"))
		{
			customer.setIdentityExpiredDate(null);
			customer.setExpiryDate(customerPersonalDetail.getExpiryDate());
			customer.setIssueDate(customerPersonalDetail.getIssueDate());
		}
		else
		{
			customer.setIdentityExpiredDate(customerPersonalDetail.getExpiryDate());
			customer.setExpiryDate(null);
			customer.setIssueDate(null);
		}		
		customer.setIdentityInt(customerPersonalDetail.getIdentityInt());
		if(customerEmploymentDetails != null)
		{
			customer.setFsArticleDetails(
					articleDao.getArticleDetailsByArticleDetailId(customerEmploymentDetails.getArticleDetailsId()));
			customer.setFsIncomeRangeMaster(
					articleDao.getIncomeRangeMasterByIncomeRangeId(customerEmploymentDetails.getIncomeRangeId()));
		}
		
		LOGGER.info("generated customer ref: {}", customerReference);
		LOGGER.info("Createing new customer record, civil id- {}", customerPersonalDetail.getIdentityInt());
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
		customerType.setComponentDataId(
				bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, metaData.getLanguageId())
						.getFsBizComponentData().getComponentDataId());
		custProof.setFsBizComponentDataByCustomerTypeId(customerType);
		custProof.setIdentityInt(customer.getIdentityInt());
		custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setCreatedBy(customer.getIdentityInt());
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(customer.getIdentityTypeId());
		customerIdProofRepository.save(custProof);
	}

	public AmxApiResponse<BigDecimal, Object> saveCustomeKycDocument() throws IOException {		
		File f = new File("C:\\Users\\Chetan Pawar\\Desktop\\ganpati.jpg");
		byte[] fileContent = FileUtils.readFileToByteArray(f);
		DocBlobUpload kycDocument = new DocBlobUpload();
		kycDocument.setCntryCd(new BigDecimal(1));
		kycDocument.setDocBlobID(new BigDecimal(1));
		kycDocument.setSeqNo(new BigDecimal(1));
		kycDocument.setDocFinYear(new BigDecimal(2018));
		kycDocument.setDocContent(fileContent);
		kycDocument.setUpdatedDate(new Date());
		docblobRepository.save(kycDocument);
		
		
		/*List<DocBlobUpload> doc = docblobRepository.findAll();
		DocBlobUpload kycDocument = doc.get(0);
		byte[] f = kycDocument.getDocContent();		
		FileUtils.writeByteArrayToFile(new File("D:\\chetan\\ganpati123.jpg"), f);*/
		
		
		return AmxApiResponse.build(new BigDecimal(1));
	}

}
