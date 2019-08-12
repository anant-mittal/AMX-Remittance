package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.amx.jax.dbmodel.PurposeTrnxAmicDesc;

public interface IPurposeTrnxAmicDescRepository extends CrudRepository<PurposeTrnxAmicDesc, Serializable>{
	
	@Query(value = "SELECT * FROM JAX_PURPOSE_TRNX_AMIEC_DESC WHERE AMIEC_CODE in ?1 AND LANGUAGE_ID = ?2 ", nativeQuery = true)
	public List<PurposeTrnxAmicDesc> fetchAllAmiecDataByLanguageId(List<String> amiecCode, BigDecimal languageId);

	@Query(value = "SELECT * FROM JAX_PURPOSE_TRNX_AMIEC_DESC WHERE AMIEC_CODE in ?1 AND LANGUAGE_ID = ?2 ", nativeQuery = true)
	public PurposeTrnxAmicDesc fetchAllAmicDataByLanguageId(String amiecCode, BigDecimal languageId);
}
