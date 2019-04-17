package com.amx.jax;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JaxAuthMeta implements Serializable {

	private static final long serialVersionUID = 4710795819445446940L;

	public JaxAuthMeta() {
		this.otp = "";
		this.mOtp = "";
		this.eOtp = "";
		this.wOtp = "";
		this.secAns = "";
	}

	public JaxAuthMeta(String mOtp, String eOtp, String secAns) {
		this();
		this.mOtp = mOtp;
		this.eOtp = eOtp;
		this.secAns = secAns;
	}

	@ApiMockModelProperty(example = "123456")
	String otp;

	@ApiMockModelProperty(example = "123456")
	String mOtp;
	@ApiMockModelProperty(example = "234567")
	String eOtp;
	@ApiMockModelProperty(example = "123456")
	String wOtp;
	@ApiMockModelProperty(example = "Q1")
	BigDecimal questId;
	@ApiMockModelProperty(example = "black")
	String secAns;

	/**
	 * @return the questId
	 */
	public BigDecimal getQuestId() {
		return questId;
	}

	/**
	 * @param questId the questId to set
	 */
	public void setQuestId(BigDecimal questId) {
		this.questId = questId;
	}

	public String getSecAns() {
		return secAns;
	}

	public void setSecAns(String secAns) {
		this.secAns = secAns;
	}

	public String getmOtp() {
		return mOtp;
	}

	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	public String geteOtp() {
		return eOtp;
	}

	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getwOtp() {
		return wOtp;
	}

	public void setwOtp(String wOtp) {
		this.wOtp = wOtp;
	}
}