package com.amx.jax.complaince;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActionRepo extends JpaRepository<ActionParam, Serializable> {
	
	@Query(value = " select * from JAX_VW_COAC_PARAM  where RECORD_ID=?1", nativeQuery = true)
	public List<ActionParam> getComplainceActionData(String recordId);
	
	@Query(value = " select * from JAX_VW_COAC_PARAM  where ACTION_CODE=?1", nativeQuery = true)
	public ActionParam getComplainceActionDetails(String actionCode);

}
