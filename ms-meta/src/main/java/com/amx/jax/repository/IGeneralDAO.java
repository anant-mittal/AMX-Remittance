/*package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewDistrict;



public interface IGeneralDAO<T> extends JpaRepository<T, BigDecimal>{
	
	@Query("Select t from ViewDistrict t where stateId =?1 and districtId =?2 and languageId=?3")
	public List<ViewDistrict> getDistrict(BigDecimal stateId,BigDecimal districtId,BigDecimal languageId);
}
*/