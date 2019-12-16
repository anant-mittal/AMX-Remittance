package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// reason repository rename
public interface ReasonsRepository extends JpaRepository<ReasonCodeMaster, Serializable>  {
	
	@Query("select a from ReasonCodeMaster a where a.reasonCodeCategory =?1 AND a.isActive='Y' ORDER BY reasonCodeId")
	public List<ReasonCodeMaster> getReasonsList(String reasonCodeCategory);
	
	@Query("select a from ReasonCodeMaster a where a.reasonCodeCategory =?1 AND a.isActive='Y'")
	public ReasonCodeMaster getReason(String reasonCodeCategory);
}
