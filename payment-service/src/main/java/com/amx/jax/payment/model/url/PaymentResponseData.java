/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.model.url;

import com.amx.jax.payment.response.AResponseDataModel;

/**
 * @author Viki Sangani
 * 06-Nov-2017
 * SendTransactionEmailResponseData.java
 */
public class PaymentResponseData extends AResponseDataModel{

	/**
	 * 
	 */
	public PaymentResponseData() {
		// TODO Auto-generated constructor stub
	}

	private String msg;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
