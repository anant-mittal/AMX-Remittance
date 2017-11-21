package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.ViewOnlineEmailMobileCheck;

public interface ICustomerEmailMobileCheck extends JpaRepository<ViewOnlineEmailMobileCheck, Serializable>{
	
	@Query("Select t from ViewOnlineEmailMobileCheck t where languageId=?1 and countryId=?2 and email=?3")
	public List<ViewOnlineEmailMobileCheck> getEmailCheck(BigDecimal languageId,BigDecimal countryId,String email);
	
	@Query("Select t from ViewOnlineEmailMobileCheck t where languageId=?1 and countryId=?2 and mobile=?3")
	public List<ViewOnlineEmailMobileCheck> getMobileCheck(BigDecimal languageId,BigDecimal countryId,String mobile);
	

	

	
}
