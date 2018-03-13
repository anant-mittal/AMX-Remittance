package com.amx.amxlib.meta.model;

import java.util.List;

import com.amx.amxlib.model.AbstractAnswer;

public class QuestAnswerModelDTO {

	List<AbstractAnswer> possibleAnswers;

	public List<AbstractAnswer> getPossibleAnswers() {
		return possibleAnswers;
	}

	public void setPossibleAnswers(List<AbstractAnswer> possibleAnswers) {
		this.possibleAnswers = possibleAnswers;
	}
}
