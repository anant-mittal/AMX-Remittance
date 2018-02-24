/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.model.url;

import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.payment.response.AResponseDataModel;

/**
 * @author Viki Sangani
 * 06-Nov-2017
 * SendTransactionEmailResponseData.java
 */
public class PaymentResponseData implements AResponseDataModel{

	/**
	 * 
	 */
	public PaymentResponseData() {
		// TODO Auto-generated constructor stub
	}

	private String msg;
	
	private PaymentResponseDto responseDTO;

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

	/**
	 * @return the responseDTO
	 */
	public PaymentResponseDto getResponseDTO() {
		return responseDTO;
	}

	/**
	 * @param responseDTO the responseDTO to set
	 */
	public void setResponseDTO(PaymentResponseDto responseDTO) {
		this.responseDTO = responseDTO;
	}
}
