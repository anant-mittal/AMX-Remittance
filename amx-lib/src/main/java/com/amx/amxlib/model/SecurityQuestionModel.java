package com.amx.amxlib.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.amx.jax.model.AbstractModel;

import io.swagger.annotations.ApiModelProperty;

public class SecurityQuestionModel extends AbstractModel {

	private static final long serialVersionUID = -1144780796090568414L;

	@NotNull
	private BigDecimal questionSrNo;

	private String answerKey = null;

	public SecurityQuestionModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SecurityQuestionModel(BigDecimal questionSrNo, String answer) {
		super();
		this.questionSrNo = questionSrNo;
		this.answer = answer;
	}

	@NotNull
	@Size(min=1)
	private String answer;

	@ApiModelProperty(example = "6")
	public BigDecimal getQuestionSrNo() {
		return questionSrNo;
	}

	public void setQuestionSrNo(BigDecimal questionSrNo) {
		this.questionSrNo = questionSrNo;
	}

	@ApiModelProperty(example = "test")
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@ApiModelProperty(example = "null")
	public String getAnswerKey() {
		return answerKey;
	}

	public void setAnswerKey(String answerKey) {
		this.answerKey = answerKey;
	}

}
