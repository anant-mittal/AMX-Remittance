package com.amx.jax.complaince;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IndicatorRepo extends JpaRepository<IndicatorParam, Serializable> {
	
	@Query(value = " select * from JAX_VW_CSSN_PARAM  where RECORD_ID=?1", nativeQuery = true)
	public List<IndicatorParam> getComplainceIndicatorData(String recordId);

}
