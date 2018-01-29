/**  AlMulla Exchange
  *  
  */
package com.amx.jax.userservice.validation;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author Viki Sangani
 * 25-Jan-2018
 * KuwaitValidation.java
 */
@Component
public class BahrainValidationClient implements ValidationClient {
	
	Logger logger = Logger.getLogger(BahrainValidationClient.class);

	/* (non-Javadoc)
	 * @see com.amx.jax.validation.ValidationClient#getClientCode()
	 */
	@Override
	public ValidationServiceCode getClientCode() {
		// TODO Auto-generated method stub
		return ValidationServiceCode.BAH;
	}
	
	/* (non-Javadoc)
	 * @see com.amx.jax.validation.ValidationClient#validateMobileNumberLength()
	 */
	@Override
	public Boolean isValidMobileNumber(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.amx.jax.userservice.validation.ValidationClient#isMobileExist(java.lang.String)
	 */
	@Override
	public Boolean isMobileExist(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

}
