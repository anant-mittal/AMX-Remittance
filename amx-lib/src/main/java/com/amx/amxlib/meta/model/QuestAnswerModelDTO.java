package com.amx.amxlib.meta.model;

import java.util.List;

import com.amx.amxlib.model.AbstractAnswer;

public class QuestAnswerModelDTO {

	List<AbstractAnswer> possibleAnswers;
	
	private String answerKey;
	
	private String answerType;

	public List<AbstractAnswer> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<AbstractAnswer> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}

	public String getAnswerKey() {
		return answerKey;
	}

	public void setAnswerKey(String answerKey) {
		this.answerKey = answerKey;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
}
