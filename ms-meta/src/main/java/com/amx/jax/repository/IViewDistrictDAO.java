package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewDistrict;

public interface IViewDistrictDAO  extends JpaRepository<ViewDistrict, Serializable>{
	
	@Query("Select t from ViewDistrict t where t.stateId =?1 and t.districtId =?2 and t.languageId=?3 order by districtDesc")
	public List<ViewDistrict> getDistrict(BigDecimal stateId,BigDecimal districtId,BigDecimal languageId);
	

	@Query("Select t from ViewDistrict t where t.stateId =?1  and t.languageId=?2")
	public List<ViewDistrict> getAllDistrict(BigDecimal stateId,BigDecimal languageId);

}
