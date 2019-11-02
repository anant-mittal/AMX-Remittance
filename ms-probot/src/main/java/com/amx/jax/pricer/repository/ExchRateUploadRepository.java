package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ExchRateUpload;

@Transactional
public interface ExchRateUploadRepository extends CrudRepository<ExchRateUpload, BigDecimal> {

	List<ExchRateUpload> findByRuleId(String ruleId);
	
	

}
