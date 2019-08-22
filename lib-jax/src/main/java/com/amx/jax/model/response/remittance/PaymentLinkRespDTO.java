package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentLinkRespDTO implements Serializable {

	private static final long serialVersionUID = 2315791709068216697L;

	private BigDecimal linkId;
	private String verificationCode;
	private String applicationIds;

	public BigDecimal getLinkId() {
		return linkId;
	}

	public void setLinkId(BigDecimal linkId) {
		this.linkId = linkId;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getApplicationIds() {
		return applicationIds;
	}

	public void setApplicationIds(String applicationIds) {
		this.applicationIds = applicationIds;
	}

}
