package com.amx.jax.customer.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.customer.repository.EmployeeRepository;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.error.JaxError;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class EmployeeValidationService 
{
	
	@Autowired
	OtpSettings otpSettings;
	
	@Autowired
	private EmployeeRepository repo;	
	
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
				throw new GlobalException(JaxError.EMPLOYEE_OTP_ATTEMPT_EXCEEDED,
						"Employee is locked. No of attempts:- " + lockCnt);
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

public void validateTokenDate(Employee employeeDetails) {/*

	long otpValidTimeInMins = otpSettings.getOtpValidityTime().longValue();
	Date tokenDate = employeeDetails.getTokenDate();
	if (tokenDate != null) {
		long diff = Calendar.getInstance().getTime().getTime() - tokenDate.getTime();
		long tokenTimeinMins = TimeUnit.MILLISECONDS.toMinutes(diff);
		if (tokenTimeinMins > otpValidTimeInMins) {
			throw new GlobalException("Otp has been expired", JaxError.OTP_EXPIRED.getStatusKey());
		}
	}
*/}

public void validateTokenSentCount(Employee employeeDetails) {/*

	Integer limit = otpSettings.getMaxSendOtpAttempts();
	if (employeeDetails.getTokenSentCount() != null && employeeDetails.getTokenSentCount().intValue() >= limit) {
		throw new GlobalException("Limit to send otp exceeded", JaxError.SEND_OTP_LIMIT_EXCEEDED.getStatusKey());
	}
*/}}
