package com.amx.jax.repository.remittance;

/**
 * @author rabil
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewBnkFlexVal;

public interface IvBankFlxValRepository extends CrudRepository<ViewBnkFlexVal, Serializable>{

	public List<ViewBnkFlexVal> findByBnkcodeAndFileNameAndBeneBankCode(String bankCode,String fileName,String beneBankCode);
	
	public List<ViewBnkFlexVal> findByBnkcodeAndFileNameAndBeneBankCodeAndBeneBranchCode(String bankCode,String fileName,String beneBankCode,BigDecimal beneBranchCode);
	
	
 }
