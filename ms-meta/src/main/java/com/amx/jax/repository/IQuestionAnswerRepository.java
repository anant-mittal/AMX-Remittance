package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.OnlineQuestModel;

public interface IQuestionAnswerRepository extends JpaRepository<OnlineQuestModel, BigDecimal>{
	
	@Query("Select qa from OnlineQuestModel qa where  languageId=?1 and countryId=?2")
	List<OnlineQuestModel> findAllQuestion(BigDecimal languageId,BigDecimal countryId);
	
	@Query("Select qa from OnlineQuestModel qa where  languageId=?1 and countryId=?2 and questId=?3")
	List<OnlineQuestModel> getQuestionDescription(BigDecimal languageId,BigDecimal countryId,BigDecimal questId);
	
	@Query("Select qa from OnlineQuestModel qa where  languageId=?1 and countryId=?2 and questId=?3")
	OnlineQuestModel getEngQuestionDescription(BigDecimal languageId,BigDecimal countryId,BigDecimal questId);
	
}
