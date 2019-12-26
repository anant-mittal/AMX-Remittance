package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CardTypeViewModel;

@Transactional
public interface ICardTypeRepository extends CrudRepository<CardTypeViewModel, BigDecimal> {

	@Query("select c from CardTypeViewModel c where languageId=?1")
	List<CardTypeViewModel> getCardTypeList(BigDecimal languageId);

	
}
