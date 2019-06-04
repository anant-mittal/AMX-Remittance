package com.amx.jax;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JaxAuthMeta implements Serializable {

	private static final long serialVersionUID = 4710795819445446940L;

	public JaxAuthMeta() {
		this.id = null;
		this.otpHash = "";
		this.mOtpHash = "";
		this.eOtpHash = "";
		this.wOtpHash = "";
		this.secAnsHash = "";
	}
	
	public JaxAuthMeta(String id) {
		this.id = id;
		this.otpHash = "";
		this.mOtpHash = "";
		this.eOtpHash = "";
		this.wOtpHash = "";
		this.secAnsHash = "";
	}

	public JaxAuthMeta(String mOtp, String eOtp, String secAns) {
		this();
		this.mOtpHash = mOtp;
		this.eOtpHash = eOtp;
		this.secAnsHash = secAns;
	}

	@ApiMockModelProperty(example = "anx-sdff-dfdfdfd")
	String id;

	@ApiMockModelProperty(example = "123456")
	String otpHash;

	@ApiMockModelProperty(example = "123456")
	String mOtpHash;
	@ApiMockModelProperty(example = "234567")
	String eOtpHash;
	@ApiMockModelProperty(example = "123456")
	String wOtpHash;
	@ApiMockModelProperty(example = "Q1")
	BigDecimal questId;
	@ApiMockModelProperty(example = "black")
	String secAnsHash;

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

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOtpHash() {
		return otpHash;
	}

	public void setOtpHash(String otpHash) {
		this.otpHash = otpHash;
	}

	public String getmOtpHash() {
		return mOtpHash;
	}

	public void setmOtpHash(String mOtpHash) {
		this.mOtpHash = mOtpHash;
	}

	public String geteOtpHash() {
		return eOtpHash;
	}

	public void seteOtpHash(String eOtpHash) {
		this.eOtpHash = eOtpHash;
	}

	public String getwOtpHash() {
		return wOtpHash;
	}

	public void setwOtpHash(String wOtpHash) {
		this.wOtpHash = wOtpHash;
	}

	public String getSecAnsHash() {
		return secAnsHash;
	}

	public void setSecAnsHash(String secAnsHash) {
		this.secAnsHash = secAnsHash;
	}
}