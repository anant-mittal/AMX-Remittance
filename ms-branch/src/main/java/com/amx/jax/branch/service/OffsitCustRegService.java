package com.amx.jax.branch.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.branch.dao.EmployeeDao;
import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.service.AbstractUserService;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class OffsitCustRegService extends AbstractUserService {

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
	
	/*@Override
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(RegModeModel regModeModel) {
		return null;
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return null;
	}*/

	public ApiResponse validateEmployeeDetails(
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
		
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType("otp");
		response.setResponseStatus(ResponseStatus.OK);
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
			LOGGER.info("Generated otp for civilid email- " + userId + " is " + randeOtp);
		}
		LOGGER.info("Generated otp for civilid mobile- " + userId + " is " + randmOtp);
	}

	@Override
	public ApiResponse registerUser(AbstractUserModel userModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractUserDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractModel convert(Customer cust) {
		// TODO Auto-generated method stub
		return null;
	}	

}
