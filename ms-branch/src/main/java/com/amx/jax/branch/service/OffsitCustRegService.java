package com.amx.jax.branch.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.amxlib.exception.jax.InvalidJsonInputException;
import com.amx.amxlib.exception.jax.InvalidOtpException;
import com.amx.amxlib.meta.model.ArticleDetailsDescDto;
import com.amx.amxlib.meta.model.ArticleMasterDescDto;
import com.amx.amxlib.meta.model.IncomeRangeDto;
import com.amx.amxlib.model.BizComponentDataDescDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.ComponentDataDto;
import com.amx.amxlib.model.request.CommonRequest;
import com.amx.amxlib.model.request.DynamicFieldRequest;
import com.amx.amxlib.model.request.EmploymentDetailsRequest;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.auditlogs.ArticleListAuditEvent;
import com.amx.jax.auditlogs.DesignationListAuditEvent;
import com.amx.jax.auditlogs.FieldListAuditEvent;
import com.amx.jax.auditlogs.IncomeRangeAuditEvent;
import com.amx.jax.auditlogs.ValidateOTPAuditEvent;
import com.amx.jax.branch.dao.EmployeeDao;
import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.constants.JaxEvent;
import com.amx.jax.dal.ArticleDao;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.FieldListDao;
import com.amx.jax.dbmodel.BizComponentDataDesc;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.FieldList;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.dbmodel.JaxConditionalFieldRuleDto;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.OtpData;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.service.PrefixService;
import com.amx.jax.userservice.manager.CustomerRegistrationManager;
import com.amx.jax.userservice.service.CheckListManager;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Constants;
import com.amx.utils.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsitCustRegService /*implements ICustRegService*/ {

	private static final Logger LOGGER = LoggerService.getLogger(OffsitCustRegService.class);
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private EmployeeValidationService employeeValidationService;
	
	@Autowired
	private JaxUtil util;
	
	@Autowired
	private CryptoUtil cryptoUtil;
	
	@Autowired
	private EmployeeRepository repo;
	
	@Autowired
	private CheckListManager checkListManager;

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
	
	/*@Override
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel regModeModel) {
		return null;
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return null;
	}*/

	public AmxApiResponse<CivilIdOtpModel, Object> validateEmployeeDetails(
			OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		if(offsiteCustRegModel.getCivilId() == null)
			throw new GlobalException("Null civil id passed ", JaxError.BLANK_CIVIL_ID);
		
		if(offsiteCustRegModel.getEcNumber() == null)
			throw new GlobalException("Null civil id passed ", JaxError.BLANK_EMPLOYEE_ID);
		
		Employee employeeDetails = employeeDao.getEmployeeDetails(offsiteCustRegModel.getCivilId(),offsiteCustRegModel.getEcNumber());
		
		if(employeeDetails ==null)
			throw new GlobalException("Invalid Employee", JaxError.INVALID_EMPLOYEE);
		
		if(employeeDetails.getEmail() == null)
			throw new GlobalException("Your Email Id Not Present",
					JaxError.EMPLOYEE_EMAIL_ID_NOT_AVAILABLE);
		
		employeeValidationService.validateEmployeeLockCount(employeeDetails);
		try {
			employeeValidationService.validateTokenDate(employeeDetails);
		} catch (GlobalException e) {
			// reset sent token count
			employeeDetails.setTokenSentCount(BigDecimal.ZERO);
		}
		employeeValidationService.validateTokenSentCount(employeeDetails);
		CivilIdOtpModel model = new CivilIdOtpModel();
		List<CommunicationChannel> channels = null;
		generateToken(employeeDetails.getCivilId(), model, channels);
		employeeDetails.setEmailToken(model.getHashedeOtp());
		employeeDetails.setSmsToken(model.getHashedmOtp());
		employeeDetails.setTokenDate(new Date());
		
		BigDecimal tokenSentCount = (employeeDetails.getTokenSentCount() == null) ? BigDecimal.ZERO
				: employeeDetails.getTokenSentCount().add(new BigDecimal(1));
		employeeDetails.setTokenSentCount(tokenSentCount);
		repo.save(employeeDetails);		
		
		return AmxApiResponse.build(model);		
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
			LOGGER.info("Generated otp for civilid email- " + userId + " is " + randeOtp);
		}
		LOGGER.info("Generated otp for civilid mobile- " + userId + " is " + randmOtp);
	}

	/*@Override
	public AmxApiResponse<BigDecimal, Object> getModes() {
		// TODO Auto-generated method stub
		return null;
	}
*/
	
	public AmxApiResponse<List<JaxConditionalFieldRuleDto>, Object> getIdDetailsFields(GetJaxFieldRequest request) {
		List<JaxConditionalFieldRule> fieldList = null;
		if (request.getEntity() == null)
		{
			auditService.excep(new FieldListAuditEvent(request), new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION));
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
		}			
		fieldList = jaxConditionalFieldRuleRepository.findByEntityName(request.getEntity());
		if(fieldList.isEmpty())
		{
			auditService.excep(new FieldListAuditEvent(request), new NullPointerException());
			throw new GlobalException("Wrong Field Condition. No Field List Found", JaxError.WRONG_FIELD_CONDITION);
		}			
		List<JaxConditionalFieldRuleDto> dtoList = convertData(fieldList);
		//auditService.log(new FieldListAuditEvent(request));
		return AmxApiResponse.build(dtoList);
	}

	private List<JaxConditionalFieldRuleDto> convertData(List<JaxConditionalFieldRule> fieldList) {
		List<JaxConditionalFieldRuleDto> output = new ArrayList<>();
		fieldList.forEach(i-> {
			output.add(convertInDto(i));
		});
		return output;
	}
	

	private JaxConditionalFieldRuleDto convertInDto(JaxConditionalFieldRule i) {
		JaxConditionalFieldRuleDto dto = new JaxConditionalFieldRuleDto();
		dto.setConditionKey(i.getConditionKey());
		dto.setConditionValue(i.getConditionValue());
		dto.setEntityName(i.getEntityName());
		dto.setField(i.getField());
		if(i.getField().getName().equalsIgnoreCase("OFFSITE_CUST_FIRST_NAME_PREFIX"))
		{
			dto.setPossibleValues(prefixService.getPrefixListOffsite());			
		}		
		return dto;
	}

	@SuppressWarnings("null")
	public AmxApiResponse<String, Object> validateOTP(OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		LOGGER.info("In validateopt of civilid: " + offsiteCustRegModel.getCivilId());
		String civilId = offsiteCustRegModel.getCivilId();
		String mOtp = offsiteCustRegModel.getmOtp();
		String eOtp = offsiteCustRegModel.geteOtp();
		Employee employee = null;
		if (civilId != null) {
			employee = employeeDao.getEmployeeByCivilId(civilId);
		}
		if (employee == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		if (offsiteCustRegModel.getmOtp() == null) {
			throw new InvalidJsonInputException("Otp is empty for civil-id: " + civilId);
		}		 
		employeeValidationService.validateEmployeeLockCount(employee);
		employeeValidationService.validateTokenDate(employee);
		String etokenHash = employee.getEmailToken();
		String mtokenHash = employee.getSmsToken();
		String mOtpHash = cryptoUtil.getHash(civilId, mOtp);
		String eOtpHash = null;
		if (StringUtils.isNotBlank(eOtp)) {
			eOtpHash = cryptoUtil.getHash(civilId, eOtp);
		}
		if (!mOtpHash.equals(mtokenHash)) {
			employeeValidationService.incrementLockCount(employee);
			throw new InvalidOtpException("Sms Otp is incorrect for civil-id: " + civilId);
		}
		if (eOtpHash != null && !eOtpHash.equals(etokenHash)) {
			employeeValidationService.incrementLockCount(employee);
			throw new InvalidOtpException("Email Otp is incorrect for civil-id: " + civilId);
		}
		checkListManager.updateMobileAndEmailCheck(employee, employeeDao.getCheckListForUserId(civilId));
		this.unlockCustomer(employee);				
		LOGGER.info("end of validateopt for civilid: " + civilId);
		//repo.save(employee);			
		AmxApiResponse<String, Object> obj = AmxApiResponse.build("Employee Authentication Successfull");		
		obj.setMessageKey("AUTH_SUCCESS");
		return obj;
	}
	
	/**
	 * reset lock
	 */
	protected void unlockCustomer(Employee employee) {
		if (employee.getLockCnt() != null || employee.getLockDt() != null) {
			employee.setLockCnt(null);
			employee.setLockDt(null);
			repo.save(employee);
		}
		employee.setTokenSentCount(BigDecimal.ZERO);
	}
	
	public AmxApiResponse<List<ComponentDataDto>, Object> sendIdTypes() {
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

		// List<BizComponentDataDesc> bizComponentDataDescs =
		// bizcomponentDao.getBizComponentDataDescListByComponmentId();
		if (tempList.isEmpty())
		{			
			throw new GlobalException("Id Type List Is Not available ", JaxError.EMPTY_ID_TYPE_LIST);
		}
			
		// List<BizComponentDataDescDto> dtoList = convert(bizComponentDataDescs);		
		return AmxApiResponse.build(list);
	}

	/*private List<BizComponentDataDescDto> convert(List<BizComponentDataDesc> bizComponentDataDescs) {
		List<BizComponentDataDescDto> output = new ArrayList<>();
		bizComponentDataDescs.forEach(i -> {
			output.add(convert(i));
		});
		return output;
	}*/

	private BizComponentDataDescDto convert(BizComponentDataDesc i) {
		BizComponentDataDescDto dto =  new BizComponentDataDescDto();
		dto.setComponentDataDescId(i.getComponentDataDescId());
		dto.setDataDesc(i.getDataDesc());
		dto.setFsBizComponentData(i.getFsBizComponentData().getComponentDataId());
		dto.setFsLanguageType(i.getFsLanguageType().getLanguageId());
		return dto;
	}

	public AmxApiResponse<String, Object> validateOtp(OffsiteCustomerRegistrationRequest offsiteCustRegModel) {
		
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(offsiteCustRegModel.geteOtp()) || StringUtils.isBlank(offsiteCustRegModel.getmOtp())) {
				auditService.excep(new ValidateOTPAuditEvent(offsiteCustRegModel), 
						new GlobalException("Otp field is required", JaxError.MISSING_OTP));
				throw new GlobalException("Otp field is required", JaxError.MISSING_OTP);
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				auditService.excep(new ValidateOTPAuditEvent(offsiteCustRegModel), 
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
		auditService.log(new ValidateOTPAuditEvent(offsiteCustRegModel));
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

	public AmxApiResponse<ArticleMasterDescDto, Object> getArticleListResponse(CommonRequest model) {
		List<Map<String, Object>> articleList = articleDao.getArtilces(model.getCountryId(), metaData.getLanguageId());
		if(articleList == null || articleList.isEmpty())
		{
			auditService.excep(new ArticleListAuditEvent(model), 
					new GlobalException("Article List Is Empty ", JaxError.EMPTY_ARTICLE_LIST));
			throw new GlobalException("Article List Is Empty ", JaxError.EMPTY_ARTICLE_LIST);
		}
		List<ArticleMasterDescDto> articleDtoList = convertArticle(articleList);
		auditService.log(new ArticleListAuditEvent(model));
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

	public AmxApiResponse<ArticleDetailsDescDto, Object> getDesignationListResponse(BigDecimal articleId,
			BigDecimal languageId) {
		List<Map<String, Object>> designationList = articleDao.getDesignationData(articleId, languageId);
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(articleId,null,null);
		if(designationList == null || designationList.isEmpty())
		{
			auditService.excep(new DesignationListAuditEvent(details), 
					new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST));
			throw new GlobalException("Designation List Is Empty ", JaxError.EMPTY_DESIGNATION_LIST);
		}
		List<ArticleDetailsDescDto> designationDataList = convertDesignation(designationList);
		auditService.log(new DesignationListAuditEvent(details));
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

	public AmxApiResponse<IncomeRangeDto, Object> getIncomeRangeResponse(BigDecimal countryId,
			BigDecimal articleDetailsId) {
		List<Map<String, Object>> incomeRangeList = articleDao.getIncomeRange(countryId, articleDetailsId);
		EmploymentDetailsRequest details = new EmploymentDetailsRequest(null,articleDetailsId,countryId);
		if(incomeRangeList == null || incomeRangeList.isEmpty())
		{
			auditService.excep(new IncomeRangeAuditEvent(details), 
					new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE));
			throw new GlobalException("Income Range List Is Empty ", JaxError.EMPTY_INCOME_RANGE);
		}
		List<IncomeRangeDto> incomeRangeDataList = convertIncomeRange(incomeRangeList);		
		auditService.log(new IncomeRangeAuditEvent(details));
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

	public AmxApiResponse<Map<String, FieldList>, Object> getFieldList(DynamicFieldRequest model) {
		List<FieldList> fieldList = null;
		fieldList = fieldListDao.getFieldList(model.getTenant(),Constants.COMMON_NATIONALITY,model.getComponent());
		Map<String,FieldList> map = new HashMap<>();
		if(fieldList != null)
		{
			map = fieldList.stream().collect(Collectors.toMap(FieldList:: getKey, Function.identity()));	
		}
		
		if(model.getNationality()!= null && !model.getNationality().equalsIgnoreCase("ALL"))
		{
			//fieldList = null;
			fieldList = fieldListDao.getFieldList(model.getTenant(),model.getNationality(),model.getComponent());
			if(fieldList != null)
			{				
				Map<String,FieldList> map1 = fieldList.stream().collect(Collectors.toMap(FieldList:: getKey, Function.identity()));
				map.putAll(map1);
			}
		}	
		if(map == null || map.isEmpty())
		{
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
		}		
		return AmxApiResponse.build(map);
	}
	
	/*private List<JaxConditionalFieldDto> convert(List<JaxConditionalFieldRule> fieldList) {
		List<JaxConditionalFieldDto> list = new ArrayList<>();
		fieldList.forEach(i -> {
			list.add(convert(i));
		});
		return list;
	}
	
	private JaxConditionalFieldDto convert(JaxConditionalFieldRule i) {
		JaxConditionalFieldDto dto = new JaxConditionalFieldDto();
		dto.setEntityName(i.getEntityName());
		JaxFieldDto fieldDto = convert(i.getField());
		dto.setField(fieldDto);
		dto.setId(i.getId());
		return dto;
	}
	
	private JaxFieldDto convert(JaxField field) {
		JaxFieldDto dto = new JaxFieldDto();
		jaxUtil.convert(field, dto);
		dto.setRequired(ConstantDocument.Yes.equals(field.getRequired()) ? true : false);
		List<ValidationRegexDto> validationdtos = new ArrayList<>();
		if (field.getValidationRegex() != null) {
			field.getValidationRegex().forEach(validation -> {
				ValidationRegexDto regexdto = new ValidationRegexDto();
				jaxUtil.convert(validation, regexdto);
				validationdtos.add(regexdto);
			});
		}
		dto.setValidationRegex(validationdtos);
		return dto;
	}*/
}
