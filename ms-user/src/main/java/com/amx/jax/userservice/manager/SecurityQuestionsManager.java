package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.QuestAnswerModelDTO;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractAnswer;
import com.amx.amxlib.model.OptionAnswer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.dbmodel.bene.RelationsDescription;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.auth.AbstractAnswer;
import com.amx.jax.model.auth.OptionAnswer;
import com.amx.jax.model.auth.QuestAnswerModelDTO;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.repository.IQuestionAnswerRepository;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.userservice.constant.CustomerDataVerificationQuestion;
import com.amx.jax.userservice.repository.RelationsRepository;
import com.amx.jax.util.JaxUtil;

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
	private RelationsRepository relationsRepository;

	@Autowired
	private JaxUtil util;

	public List<OnlineQuestModel> getAllQuestionsList() {
		return questionAnswerRepository.findAllQuestion(metaData.getLanguageId(), metaData.getCountryId());
	}

	public List<OnlineQuestModel> getQuestionsByIds(List<BigDecimal> ids) {
		return questionAnswerRepository.findAll(ids);
	}

	public List<QuestModelDTO> generateRandomQuestions(CustomerOnlineRegistration onlineCustomer, Integer size,
			Integer customerId) {
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		if (size > 4) {
			throw new GlobalException(JaxError.INVALID_RANDOM_QUEST_SIZE.getStatusKey(),
					"Random questions size can't be more than 4");
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

	public List<QuestModelDTO> getDataVerificationRandomQuestions(CustomerOnlineRegistration onlineCustomer, Integer size,
			BigDecimal customerId) {
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}

		QuestModelDTO q1 = CustomerDataVerificationQuestion.Q1.getQuestModelDTO();
		QuestModelDTO q2 = CustomerDataVerificationQuestion.Q2.getQuestModelDTO();
		QuestModelDTO q3 = CustomerDataVerificationQuestion.Q3.getQuestModelDTO();
		q1.setQuestAnswerModelDTO(getAnswerModelForQ1());
		q2.setQuestAnswerModelDTO(getExpiryDateForQ2());
		List<QuestModelDTO> result = new ArrayList<>();
		Map<Integer, QuestModelDTO> maps = new HashMap<>();
		maps.put(1, q1);
		maps.put(2, q2);
		int randQKey = ThreadLocalRandom.current().nextInt(1, 3);
		result.add(maps.get(randQKey));
		return result;

	}

	public List<QuestModelDTO> getDataVerificationRandomQuestions(Integer size) {

		QuestModelDTO q1 = CustomerDataVerificationQuestion.Q1.getQuestModelDTO();
		QuestModelDTO q2 = CustomerDataVerificationQuestion.Q2.getQuestModelDTO();
		
		q1.setQuestAnswerModelDTO(getAnswerModelForQ1());
		q2.setQuestAnswerModelDTO(getExpiryDateForQ2());
		List<QuestModelDTO> result = new ArrayList<>();
		Map<Integer, QuestModelDTO> maps = new HashMap<>();
		maps.put(1, q1);
		maps.put(2, q2);
		int randQKey = ThreadLocalRandom.current().nextInt(1, 3);
		result.add(maps.get(randQKey));
		return result;

	}

	private QuestAnswerModelDTO getListOfMonths() {
		QuestAnswerModelDTO dto = new QuestAnswerModelDTO();
		dto.setAnswerKey("select");
		List<AbstractAnswer> possibleAnswers = new ArrayList<>();
		dto.setPossibleAnswers(possibleAnswers);
		util.getMonthsList().forEach((k, v) -> {
			OptionAnswer answer = new OptionAnswer();
			answer.setOptionKey(k);
			answer.setOptionValue(v);
			possibleAnswers.add(answer);
		});
		return dto;
	}

	private QuestAnswerModelDTO getRelationShips() {
		QuestAnswerModelDTO dto = new QuestAnswerModelDTO();
		dto.setAnswerType("select");
		List<AbstractAnswer> possibleAnswers = new ArrayList<>();

		dto.setPossibleAnswers(possibleAnswers);
		List<RelationsDescription> allRelationsDesc = relationsRepository.findBylangId(metaData.getLanguageId());
		allRelationsDesc.forEach(i -> {
			OptionAnswer answer = new OptionAnswer();
			answer.setOptionKey(i.getRelationsId().toString());
			answer.setOptionValue(i.getLocalRelationsDesc());
			possibleAnswers.add(answer);
		});
		return dto;
	}
	
	private QuestAnswerModelDTO getAnswerModelForQ1() {
		QuestAnswerModelDTO dto = new QuestAnswerModelDTO();
		dto.setAnswerType("text");
		return dto;
	}

	private QuestAnswerModelDTO getExpiryDateForQ2() {
		QuestAnswerModelDTO dto = new QuestAnswerModelDTO();
		dto.setAnswerType("date");
		return dto;
	}
}
