/*package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;


public interface IGeneralDAO<T> extends JpaRepository<T, BigDecimal>{
	
	@Query("Select t from ViewDistrict t where stateId =?1 and districtId =?2 and languageId=?3")
	public List<ViewDistrict> getDistrict(BigDecimal stateId,BigDecimal districtId,BigDecimal languageId);
	
	@Query("Select t from ViewState t where countryId=?1 and stateId =?2 and languageId=?3")
	public List<ViewState> getState(BigDecimal countryId,BigDecimal stateId,BigDecimal languageId);
	
}
*/