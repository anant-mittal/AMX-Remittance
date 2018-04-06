package com.amx.amxlib.model.response;

import com.amx.amxlib.model.AbstractModel;

public class JaxTransactionResponse extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success;
	
	private String tranxId;

	public JaxTransactionResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public JaxTransactionResponse(boolean success, String tranxId) {
		super();
		this.success = success;
		this.tranxId = tranxId;
	}



	@Override
	public String getModelType() {
		return "jax-trnx-response";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}
}
