package com.amx.jax.model.request.fx;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class ForexOutLookRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2083497040621075500L;

	@NotNull(message="pairId may not be null")
	private BigDecimal pairId;
	
	@NotNull(message="message may not be null")
	private String message;
	
	
	public BigDecimal getPairId() {
		return pairId;
	}
	public void setPairId(BigDecimal pairId) {
		this.pairId = pairId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
