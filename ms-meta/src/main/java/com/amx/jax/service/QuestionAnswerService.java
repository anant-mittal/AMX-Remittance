package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.OnlineQuestModel;
import com.amx.jax.repository.IQuestionAnswerRepository;

@Service
public class QuestionAnswerService {
	
	@Autowired
	IQuestionAnswerRepository questionAnswerRepository;
	
	public List<OnlineQuestModel> findAllQuestion(BigDecimal languageId,BigDecimal countryId){
		return questionAnswerRepository.findAllQuestion(languageId, countryId);
	}
	
	public List<OnlineQuestModel> getQuestionDescription(BigDecimal languageId,BigDecimal countryId,BigDecimal questId){
		return questionAnswerRepository.getQuestionDescription(languageId, countryId, questId);
	}

}
