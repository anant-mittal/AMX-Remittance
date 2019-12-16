package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// reason repository rename
public interface ReasonsRepository extends JpaRepository<ReasonsModel, Serializable>  {
	
	@Query("select a from ReasonsModel a where a.reasonCodeCategory =?1 AND a.isActive='Y' ORDER BY reasonCodeId")
	public List<ReasonsModel> getReasonsList(String reasonCodeCategory);
	
	@Query("select a from ReasonsModel a where a.reasonCodeCategory =?1 AND a.isActive='Y'")
	public ReasonsModel getReason(String reasonCodeCategory);
}
