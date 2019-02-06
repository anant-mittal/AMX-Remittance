/**  AlMulla Exchange
  *  
  */
package com.amx.jax.userservice.validation;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.userservice.dao.CustomerDao;

/**
 * @author Viki Sangani
 * 25-Jan-2018
 * KuwaitValidation.java
 */
@Component
public class KuwaitValidationClient implements ValidationClient {
	
	Logger logger = Logger.getLogger(KuwaitValidationClient.class);
	
	@Autowired
	private CustomerDao custDao;
	
	private static Integer MOBILE_LENGTH = new Integer(8);

	/* (non-Javadoc)
	 * @see com.amx.jax.validation.ValidationClient#getClientCode()
	 */
	@Override
	public ValidationServiceCode getClientCode() {
		// TODO Auto-generated method stub
		return ValidationServiceCode.KWT;
	}
	
	/* (non-Javadoc)
	 * @see com.amx.jax.validation.ValidationClient#validateMobileNumberLength()
	 */
	@Override
	public Boolean isValidMobileNumber(String mobile) {
		
		if (mobile.length()== MOBILE_LENGTH) {
			return true;
		}
		
		return false;
	}
	
	public Boolean isMobileExist(String mobile) {
		
		Customer cust = custDao.getCustomerByMobile(mobile);
		if (cust!=null) {
			return true;
		}
		return false;
	}

	@Override
	public int mobileLength() {
		return MOBILE_LENGTH.intValue();
	}

}
