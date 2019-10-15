package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.LanguageType;

public interface ILanguageTypeRepository  extends CrudRepository<LanguageType, Serializable>{
	
	public LanguageType findBylanguageId(BigDecimal languageId);

}
