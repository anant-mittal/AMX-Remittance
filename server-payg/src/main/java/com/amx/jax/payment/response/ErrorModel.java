/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.response;

/**
 * @author Viki Sangani 01-Nov-2017 ErrorModel.java
 */
public class ErrorModel {
	
	//a unique identifier for this particular occurrence of the problem
	private String id;
	
	//the HTTP status code applicable to this problem
	private String status;
	
	//an application-specific error code
	private String code;
	
	// a short, human-readable summary of the problem
	private String title;
	
	//a human-readable explanation specific to this occurrence of the problem
	private String detail;
	
	//containing non-standard meta-information about the error
	private String meta;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the meta
	 */
	public String getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(String meta) {
		this.meta = meta;
	}

}
