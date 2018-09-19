package com.amx.jax.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.UserFinancialYear;

public interface IUserFinancialYearRepo extends JpaRepository<UserFinancialYear, Serializable>{

	@Query("select u from UserFinancialYear u where financialYearBegin <= to_date(?1,'MM/dd/yyyy') "
			+ "and financialYearEnd >= to_date(?2,'MM/dd/yyyy')")
	public UserFinancialYear findAllByFinancialYearBegin(String startDate,String endDate);

}
