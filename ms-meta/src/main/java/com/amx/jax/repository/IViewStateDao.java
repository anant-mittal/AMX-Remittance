package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.ViewState;

public interface IViewStateDao extends JpaRepository<ViewState, Serializable>{
	
	
	@Query("Select t from ViewState t where countryId=?1 and stateId =?2 and languageId=?3")
	public List<ViewState> getState(BigDecimal countryId,BigDecimal stateId,BigDecimal languageId);
	
	
	
	@Query("Select t from ViewState t where countryId=?1 and languageId=?2 order by stateName")
	public List<ViewState> getStateForCountry(BigDecimal countryId,BigDecimal languageId);


}
