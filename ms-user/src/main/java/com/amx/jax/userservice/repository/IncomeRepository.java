package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.IncomeModel;

public interface IncomeRepository extends JpaRepository<IncomeModel, Serializable>{
	
	/*
	 * @Query("select c from IncomeModel c where c.activeStatus ='Y'") public
	 * List<IncomeModel> getAnnualIncome();
	 */
	
	@Query("select c from IncomeModel c where c.activeStatus ='Y' order by c.incomeRangeMasterId")
	public List<IncomeModel> getAnnualIncome();
	
	@Query("select c from IncomeModel c where c.activeStatus ='Y' and c.rangeType = ?1 order by c.incomeRangeMasterId")
	public List<IncomeModel> getAnnualTransactionLimitRange(String rangeType);
	
	@Query("select c from IncomeModel c where c.incomeRangeFrom = ?1 and c.incomeRangeTo = ?2 and c.activeStatus ='Y'")
	public IncomeModel getAnnualIncomeRangeId(BigDecimal incomeRangeFrom, BigDecimal incomeRangeTo);
	
	

}
