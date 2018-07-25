package com.amx.jax.branch.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.amxlib.config.OtpSettings;

import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.util.JaxUtil;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmployeeValidationService 
{
	
	@Autowired
	OtpSettings otpSettings;
	
	@Autowired
	private EmployeeRepository repo;	
	
	@Autowired
	private JaxUtil jaxUtil;
	
	public void validateEmployeeLockCount(Employee employeeDetails) {
	final Integer MAX_OTP_ATTEMPTS = otpSettings.getMaxValidateOtpAttempts();
	if (employeeDetails.getLockCnt() != null) {
		int lockCnt = employeeDetails.getLockCnt().intValue();
		Date midnightTomorrow = getMidnightToday();

		if (lockCnt > 0 && employeeDetails.getLockDt() != null) {
			if (midnightTomorrow.compareTo(employeeDetails.getLockDt()) > 0) {
				employeeDetails.setLockCnt(new BigDecimal(0));
				repo.save(employeeDetails);
				lockCnt = 0;
			}
			if (lockCnt >= MAX_OTP_ATTEMPTS) {
				throw new GlobalException("Employee is locked. No of attempts:- " + lockCnt,
						JaxError.EMPLOYEE_OTP_ATTEMPT_EXCEEDED);
			}
		}
	}

	
}

public Date getMidnightToday() {
	Calendar date = new GregorianCalendar();
	date.set(Calendar.HOUR_OF_DAY, 0);
	date.set(Calendar.MINUTE, 0);
	date.set(Calendar.SECOND, 0);
	date.set(Calendar.MILLISECOND, 0);

	return date.getTime();
}

public void validateTokenDate(Employee employeeDetails) {

	long otpValidTimeInMins = otpSettings.getOtpValidityTime().longValue();
	Date tokenDate = employeeDetails.getTokenDate();
	if (tokenDate != null) {
		long diff = Calendar.getInstance().getTime().getTime() - tokenDate.getTime();
		long tokenTimeinMins = TimeUnit.MILLISECONDS.toMinutes(diff);
		if (tokenTimeinMins > otpValidTimeInMins) {
			throw new GlobalException("Otp has been expired", JaxError.OTP_EXPIRED.getCode());
		}
	}
}

public void validateTokenSentCount(Employee employeeDetails) {

	Integer limit = otpSettings.getMaxSendOtpAttempts();
	if (employeeDetails.getTokenSentCount() != null && employeeDetails.getTokenSentCount().intValue() >= limit) {
		throw new GlobalException("Limit to send otp exceeded", JaxError.SEND_OTP_LIMIT_EXCEEDED.getCode());
	}
}

/**
 * updates lock count by one due to wrong password/otp attempt
 */
public int incrementLockCount(Employee employeeDetails) {
	Integer lockCnt = 0;
	final Integer MAX_OTP_ATTEMPTS = otpSettings.getMaxValidateOtpAttempts();
	if (employeeDetails.getLockCnt() != null) {
		lockCnt = employeeDetails.getLockCnt().intValue();
	}
	lockCnt++;
	if (lockCnt >= MAX_OTP_ATTEMPTS) {
		employeeDetails.setLockDt(new Date());
	}
	employeeDetails.setLockCnt(new BigDecimal(lockCnt));
	repo.save(employeeDetails);
	if (lockCnt >= MAX_OTP_ATTEMPTS) {
		String errorExpression = JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.toString();
		errorExpression = jaxUtil.buildErrorExpression(JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.toString(), lockCnt);
		throw new GlobalException("Employee is locked. No of attempts:- " + lockCnt, errorExpression);
	}
	return MAX_OTP_ATTEMPTS - lockCnt;
}

}
