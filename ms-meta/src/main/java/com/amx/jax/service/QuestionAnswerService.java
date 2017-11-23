package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IQuestionAnswerRepository;
import com.amx.jax.services.AbstractService;

@Service
public class QuestionAnswerService extends AbstractService{
	
	@Autowired
	IQuestionAnswerRepository questionAnswerRepository;
	
	
	public ApiResponse findAllQuestion(BigDecimal languageId,BigDecimal countryId){
		List<OnlineQuestModel> questList = questionAnswerRepository.findAllQuestion(languageId, countryId);
		ApiResponse response = getBlackApiResponse();
		if(questList.isEmpty()) {
			throw new GlobalException("Question not found");
		}else {
		response.getData().getValues().addAll(questList);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
		}
	}
	
	
	
	public ApiResponse getQuestionDescription(BigDecimal languageId,BigDecimal countryId,BigDecimal questId){
		List<OnlineQuestModel> questList = questionAnswerRepository.getQuestionDescription(languageId, countryId, questId);
		ApiResponse response = getBlackApiResponse();
		if(questList.isEmpty()) {
			throw new GlobalException("Question not found");
		}else {
		response.getData().getValues().addAll(questList);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
		}
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
