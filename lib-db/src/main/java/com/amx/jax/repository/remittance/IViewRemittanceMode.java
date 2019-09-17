package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.ViewRemittanceMode;

public interface IViewRemittanceMode  extends CrudRepository<ViewRemittanceMode, Serializable>{
	
	List<ViewRemittanceMode> findByRemittanceModeIdAndLanguageId(BigDecimal remittanceModeId,BigDecimal languageId);
	
	ViewRemittanceMode findByRemittancCode(String remittancCode); 	

}
