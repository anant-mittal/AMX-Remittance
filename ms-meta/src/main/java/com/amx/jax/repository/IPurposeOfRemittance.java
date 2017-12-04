package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;

public interface IPurposeOfRemittance extends JpaRepository<PurposeOfRemittanceViewModel, Serializable>{

	
	
	@Query("select pr from PurposeOfRemittanceViewModel pr where documentNumber =?1 and documentFinancialYear=?2")
	public List<PurposeOfRemittanceViewModel> getPurposeOfRemittance(BigDecimal documentNumber,BigDecimal documentFinancialYear);
}
