package com.amx.jax;

import java.io.Serializable;

import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JaxAuthMetaResp implements Serializable {
	private static final long serialVersionUID = 2852813652747499129L;

	@ApiMockModelProperty(example = "anx-sdff-dfdfdfd")
	String id;
	String otpPrefix;
	String mOtpPrefix;
	String eOtpPrefix;
	String wOtpPrefix;
	QuestModelDTO ques;

	public JaxAuthMetaResp() {

	}

	public JaxAuthMetaResp(String mOtpPrefix, String eOtpPrefix, QuestModelDTO ques) {
		super();
		this.mOtpPrefix = mOtpPrefix;
		this.eOtpPrefix = eOtpPrefix;
		this.ques = ques;
	}

	public JaxAuthMetaResp(String mOtpPrefix, String eOtpPrefix) {
		super();
		this.mOtpPrefix = mOtpPrefix;
		this.eOtpPrefix = eOtpPrefix;
	}

	public JaxAuthMetaResp(QuestModelDTO ques) {
		super();
		this.ques = ques;
	}

	public String getmOtpPrefix() {
		return mOtpPrefix;
	}

	public void setmOtpPrefix(String mOtpPrefix) {
		this.mOtpPrefix = mOtpPrefix;
	}

	public String geteOtpPrefix() {
		return eOtpPrefix;
	}

	public void seteOtpPrefix(String eOtpPrefix) {
		this.eOtpPrefix = eOtpPrefix;
	}

	public QuestModelDTO getQues() {
		return ques;
	}

	public void setQues(QuestModelDTO ques) {
		this.ques = ques;
	}

	public String getOtpPrefix() {
		return otpPrefix;
	}

	/**
	 * This is complimentary field, will always have duplicate value mOTP,eOTP or
	 * wOTP
	 * 
	 * @param otpPrefix
	 */
	public void setOtpPrefix(String otpPrefix) {
		this.otpPrefix = otpPrefix;
	}

	public String getwOtpPrefix() {
		return wOtpPrefix;
	}

	public void setwOtpPrefix(String wOtpPrefix) {
		this.wOtpPrefix = wOtpPrefix;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
