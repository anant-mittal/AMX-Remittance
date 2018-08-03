package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.repository.IQuestionAnswerRepository;
import com.amx.jax.services.AbstractService;

@Service
public class QuestionAnswerService extends AbstractService {

	@Autowired
	IQuestionAnswerRepository questionAnswerRepository;

	public ApiResponse findAllQuestion(BigDecimal languageId, BigDecimal countryId) {
		List<OnlineQuestModel> questList = questionAnswerRepository.findAllQuestion(languageId, countryId);
		ApiResponse response = getBlackApiResponse();
		if (questList.isEmpty()) {
			throw new GlobalException("Question not found");
		} else {
			response.getData().getValues().addAll(convert(questList));
			response.setResponseStatus(ResponseStatus.OK);

		}
		response.getData().setType("quest");
		return response;

	}

	
	public ApiResponse getQuestionDescription(BigDecimal languageId, BigDecimal countryId, BigDecimal questId) {
		List<OnlineQuestModel> questList = questionAnswerRepository.getQuestionDescription(languageId, countryId,
				questId);
		ApiResponse response = getBlackApiResponse();
		if (questList.isEmpty()) {
			throw new GlobalException("Question not found");
		} else {
			response.getData().getValues().addAll(convert(questList));
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("quest");
		return response;
	}

	
	public List<QuestModelDTO> convert(List<OnlineQuestModel> questList) {
		List<QuestModelDTO> list = new ArrayList<QuestModelDTO>();
		for (OnlineQuestModel quest : questList) {
			QuestModelDTO model = new QuestModelDTO();
			model.setCompanyId(quest.getCompanyId());
			model.setCountryId(quest.getCountryId());
			model.setDescription(quest.getDescription());
			model.setLanguageId(quest.getLanguageId());
			model.setQuestId(quest.getQuestId());
			model.setQuestNumber(quest.getQuestNumber());
			model.setStatus(quest.getStatus());
			list.add(model);
		}
		return list;
	}

	
	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
