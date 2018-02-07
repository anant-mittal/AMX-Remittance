package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IQuestionAnswerRepository;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.util.Util;

@Component
public class SecurityQuestionsManager {

	@Autowired
	IQuestionAnswerRepository questionAnswerRepository;

	@Autowired
	MetaData metaData;

	@Autowired
	private SecurityQuestionsManager secQManager;

	@Autowired
	private QuestionAnswerService qaService;

	@Autowired
	private Util util;

	public List<OnlineQuestModel> getAllQuestionsList() {
		return questionAnswerRepository.findAllQuestion(metaData.getLanguageId(), metaData.getCountryId());
	}

	public List<OnlineQuestModel> getQuestionsByIds(List<BigDecimal> ids) {
		return questionAnswerRepository.findAll(ids);
	}

	public List<QuestModelDTO> generateRandomQuestions(CustomerOnlineRegistration onlineCustomer, Integer size,
			Integer customerId) {
		if (onlineCustomer == null) {
			throw new GlobalException("Online Customer id not found", JaxError.CUSTOMER_NOT_FOUND.getCode());
		}
		if (size > 4) {
			throw new GlobalException("Random questions size can't be more than 4",
					JaxError.INVALID_RANDOM_QUEST_SIZE.getCode());
		}
		List<BigDecimal> questions = new ArrayList<>();
		questions.add(onlineCustomer.getSecurityQuestion1());
		questions.add(onlineCustomer.getSecurityQuestion2());
		questions.add(onlineCustomer.getSecurityQuestion3());
		questions.add(onlineCustomer.getSecurityQuestion4());
		questions.add(onlineCustomer.getSecurityQuestion5());
		List<BigDecimal> randomQuestoids = util.getRandomIntegersFromList(questions, size);
		List<OnlineQuestModel> questList = secQManager.getQuestionsByIds(randomQuestoids);
		List<QuestModelDTO> result = qaService.convert(questList);
		return result;
	}
}
