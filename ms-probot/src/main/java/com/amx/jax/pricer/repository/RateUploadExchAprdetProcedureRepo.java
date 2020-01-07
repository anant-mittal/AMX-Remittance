package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.ExchRateUpload;

import oracle.sql.ARRAY;

public interface RateUploadExchAprdetProcedureRepo extends CrudRepository<ExchRateUpload, Serializable> {

	// @Procedure(procedureName = "EX_P_POPULATE_EXRATE_APRDET")

	@Query(nativeQuery = true, value = "call EX_P_POPULATE_EXRATE_APRDET(:P_APPLICATION_COUNTRY_ID, :P_RULE_ID)")
	@Transactional
	public void uploadApprovedRateRules(@Param("P_APPLICATION_COUNTRY_ID") BigDecimal applicationCountryId,
			@Param("P_RULE_ID") ARRAY ruleIds);

}
