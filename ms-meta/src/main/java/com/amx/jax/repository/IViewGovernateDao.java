package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.VwGovernateModel;

public interface IViewGovernateDao extends CrudRepository<VwGovernateModel, Serializable>{
	@Query("select l from VwGovernateModel l where applicationCountryId=?1")
	public List<VwGovernateModel> getGovermentList(BigDecimal applicationCountryId);
	
	
	@Query("select l from VwGovernateModel l where l.governateId=?1")
	public VwGovernateModel getGovermentDetails(BigDecimal governateId);
}
