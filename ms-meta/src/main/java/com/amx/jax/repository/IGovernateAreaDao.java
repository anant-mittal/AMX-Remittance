package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.VwGovernateAreaModel;

public interface IGovernateAreaDao extends JpaRepository<VwGovernateAreaModel, Serializable>{
	@Query("select a from VwGovernateAreaModel a where a.governateId =?1")
	public List<VwGovernateAreaModel> getGovermenAreaList(BigDecimal governateId);
	
	@Query("select a from VwGovernateAreaModel a where a.goverAreaId =?1")
	public VwGovernateAreaModel getGovermenArea(BigDecimal goverAreaId);
}
