package com.amx.jax.repository.remittance;

/**
 * @author rabil
 */
import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewBnkFlexVal;

public interface IvBankFlxValRepository extends CrudRepository<ViewBnkFlexVal, Serializable>{

	public List<ViewBnkFlexVal> findByBnkcodeAndFileNameAndBeneBankCode(String bankCode,String fileName,String beneBankCode);
 }
