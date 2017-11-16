package com.amx.jax.userservice.model.onlinecustomer;

import java.math.BigDecimal;

public class SecurityQuestionModel {

	private BigDecimal questionSrNo;

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
