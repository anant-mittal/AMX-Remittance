package com.amx.jax.branch.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.JaxFieldDto;
import com.amx.amxlib.model.ValidationRegexDto;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.ICustRegService;
import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.dao.EmployeeDao;
import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BizComponentDataDesc;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.JaxConditionalFieldRule;
import com.amx.jax.dbmodel.JaxField;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.repository.JaxConditionalFieldRuleRepository;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.service.AbstractUserService;
import com.amx.jax.userservice.service.CheckListManager;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
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
	
	public AmxApiResponse<List<JaxConditionalFieldRule>, Object> getIdDetailsFields(GetJaxFieldRequest request) {
		List<JaxConditionalFieldRule> fieldList = null;
		if (request.getEntity() == null)
			throw new GlobalException("Field Condition is Empty ", JaxError.EMPTY_FIELD_CONDITION);
			
		fieldList = jaxConditionalFieldRuleRepository.findByEntityName(request.getEntity());
		if(fieldList.isEmpty())
			throw new GlobalException("Wrong Field Condition. No Field List Found", JaxError.WRONG_FIELD_CONDITION);
		return AmxApiResponse.build(fieldList);
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
	
	public AmxApiResponse<List<BizComponentDataDesc>, Object> sendIdTypes() {
		List<BizComponentDataDesc> bizComponentDataDescs = bizcomponentDao.getBizComponentDataDescListByComponmentId();
		return AmxApiResponse.build(bizComponentDataDescs);
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
