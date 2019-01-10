package com.amx.jax.model.auth;

public class CustomerRequestAuthMeta {
	String mOtpPrefix;
	String eOtpPrefix;
	QuestModelDTO ques;

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
}
