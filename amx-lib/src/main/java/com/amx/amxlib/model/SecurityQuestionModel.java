package com.amx.amxlib.model;

import java.math.BigDecimal;

public class SecurityQuestionModel {

	private BigDecimal questionSrNo;

	public SecurityQuestionModel(BigDecimal questionSrNo, String answer) {
		super();
		this.questionSrNo = questionSrNo;
		this.answer = answer;
	}

	private String answer;

	public BigDecimal getQuestionSrNo() {
		return questionSrNo;
	}

	public void setQuestionSrNo(BigDecimal questionSrNo) {
		this.questionSrNo = questionSrNo;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
