package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Author : Rabil
 */
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.PurposeOfTransaction;



public interface IPurposeOfTrnxDao  extends JpaRepository<PurposeOfTransaction, BigDecimal>{
	
	@Query("select pt from PurposeOfTransaction pt where pt.isActive='Y' order by pt.purposeFullDesc asc")	
	public List<PurposeOfTransaction> getPurposeOfTrnx();
}


