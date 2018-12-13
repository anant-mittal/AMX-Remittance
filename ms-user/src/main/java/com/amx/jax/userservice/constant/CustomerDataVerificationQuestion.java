package com.amx.jax.userservice.constant;

import java.math.BigDecimal;

import com.amx.amxlib.meta.model.QuestModelDTO;

public enum CustomerDataVerificationQuestion {

	Q1(1, "The last 4 digits of your primary mobile number") {
		@Override
		public QuestModelDTO getQuestModelDTO() {
			QuestModelDTO dto = new QuestModelDTO(Q1.getId(), Q1.getId(), Q1.getQuestion());
			return dto;

		}
	},
	Q2(2, "Identity Id Expiry Date") {
		@Override
		public QuestModelDTO getQuestModelDTO() {
			QuestModelDTO dto = new QuestModelDTO(Q2.getId(), Q2.getId(), Q2.getQuestion());
			return dto;
		}
	};
	
	BigDecimal id;
	String question;

	public abstract QuestModelDTO getQuestModelDTO();

	CustomerDataVerificationQuestion(int id, String question) {
		this.id = new BigDecimal(id);
		this.question = question;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}
