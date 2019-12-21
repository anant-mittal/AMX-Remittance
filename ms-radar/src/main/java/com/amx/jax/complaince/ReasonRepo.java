package com.amx.jax.complaince;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReasonRepo extends JpaRepository<ReasonParam, Serializable> {
	
	@Query(value = " select * from JAX_VW_CORE_PARAM  where RECORD_ID=?1", nativeQuery = true)
	public List<ReasonParam> getComplainceReasonData(String recordId);
}
