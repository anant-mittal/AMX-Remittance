package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.repository.IQuestionAnswerRepository;
import com.amx.jax.services.AbstractService;

@Service
public class QuestionAnswerService extends AbstractService {

	@Autowired
	IQuestionAnswerRepository questionAnswerRepository;

	public AmxApiResponse<QuestModelDTO, Object> findAllQuestion(BigDecimal languageId, BigDecimal countryId) {
		List<OnlineQuestModel> questList = questionAnswerRepository.findAllQuestion(languageId, countryId);
		if (questList.isEmpty()) {
			throw new GlobalException("Question not found");
		} 		
		return AmxApiResponse.buildList(convert(questList));

	}

	
	public AmxApiResponse<QuestModelDTO, Object> getQuestionDescription(BigDecimal languageId, BigDecimal countryId, BigDecimal questId) {
		List<OnlineQuestModel> questList = questionAnswerRepository.getQuestionDescription(languageId, countryId,
				questId);
		if (questList.isEmpty()) {
			throw new GlobalException("Question not found");
		} 
		return AmxApiResponse.buildList(convert(questList));
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
