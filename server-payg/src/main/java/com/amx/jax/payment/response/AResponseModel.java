/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.response;

/**
 * @author Viki Sangani
 * 06-Nov-2017
 * AResponseModel.java
 */
public abstract class AResponseModel {

	/**
	 * 
	 */
	public AResponseModel() {
		// TODO Auto-generated constructor stub
	}
	
	//the document’s “primary data
	private AResponseDataModel data;
	
	//error object provide additional information about problems encountered while performing an operation
	private ErrorModel error;
	
	//a meta member can be used to include non-standard meta-information
	private MetaModel meta;
	
	private String responseCode;
	
	private String responseMessage;

	/**
	 * @return the data
	 */
	public AResponseDataModel getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(AResponseDataModel data) {
		this.data = data;
	}

	/**
	 * @return the error
	 */
	public ErrorModel getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorModel error) {
		this.error = error;
	}

	/**
	 * @return the meta
	 */
	public MetaModel getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(MetaModel meta) {
		this.meta = meta;
	}

	/**
	 * @return the responseCode
	 */
	public String getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * @param responseMessage the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

}
