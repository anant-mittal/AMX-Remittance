package com.amx.jax.ui.model;

import java.util.List;

import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.ui.response.ResponseDataInterface;

public class UserUpdateData implements ResponseDataInterface {

	private List<QuestModelDTO> secQuesMeta = null;

	public List<QuestModelDTO> getSecQuesMeta() {
		return secQuesMeta;
	}

	public void setSecQuesMeta(List<QuestModelDTO> secQuesMeta) {
		this.secQuesMeta = secQuesMeta;
	}

	private List<SecurityQuestionModel> secQuesAns = null;

	public List<SecurityQuestionModel> getSecQuesAns() {
		return secQuesAns;
	}

	public void setSecQuesAns(List<SecurityQuestionModel> secQuesAns) {
		this.secQuesAns = secQuesAns;
	}

}
