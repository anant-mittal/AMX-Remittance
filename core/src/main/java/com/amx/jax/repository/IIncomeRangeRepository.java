package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.IncomeRangeMaster;

public interface IIncomeRangeRepository extends JpaRepository<IncomeRangeMaster,Serializable>{
public IncomeRangeMaster getIncomeRangeMasterByIncomeRangeId(BigDecimal id);
}
