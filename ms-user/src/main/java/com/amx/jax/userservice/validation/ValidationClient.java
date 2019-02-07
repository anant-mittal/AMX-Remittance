/**  AlMulla Exchange
  *  
  */
package com.amx.jax.userservice.validation;

/**
 * @author Viki Sangani
 * 25-Jan-2018
 * ValidationClient.java
 */
public interface ValidationClient {
	
	/**
	 * This should return the Client Code identifier
	 * 
	 * @return
	 */
	public ValidationServiceCode getClientCode();
	
	/**
	 * This should return the status of mobile length validation.
	 * 
	 * @return
	 */
	public Boolean isValidMobileNumber(String mobile);
	
	/**
	 * This should return the status of whether mobile exist or not.
	 * 
	 * @return
	 */
	public Boolean isMobileExist(String mobile);
	
	public int mobileLength();

}
