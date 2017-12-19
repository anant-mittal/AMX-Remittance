package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.SourceOfIncomeView;

public interface ISourceOfIncomeDao extends JpaRepository<SourceOfIncomeView, Serializable>{
	
	@Query("select si from SourceOfIncomeView si where si.languageId=?1")
	public List<SourceOfIncomeView> getSourceofIncome(BigDecimal languageId);

}
