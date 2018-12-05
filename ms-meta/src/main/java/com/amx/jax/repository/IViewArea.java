package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.ViewAreaModel;

public interface IViewArea extends CrudRepository<ViewAreaModel, Serializable>{

	@Query("select c from ViewAreaModel c")
	public List<ViewAreaModel> getAreaList();
	
	@Query("select c from ViewAreaModel c where c.areaCode=:areaCode")
	public ViewAreaModel getAreaList(@Param("areaCode") BigDecimal areaCode);
}
