package com.amx.jax.repository.remittance;
/**
 @author rabil
 @date  19 mar 2019
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.CorporateMasterModel;

public interface ICorporateMasterRepository  extends CrudRepository<CorporateMasterModel, Serializable>{
	public List<CorporateMasterModel> findByCorporateMasterIdAndIsActive(BigDecimal corporateMasterId,String isActive);

}
