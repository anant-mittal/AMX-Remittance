package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.ViewOnlineCustomerCheck;



public interface IViewOnlineCustomerCheck  extends JpaRepository<ViewOnlineCustomerCheck, Serializable>{

	
	@Query("select oc from ViewOnlineCustomerCheck oc where companyId=?1 and countryId=?2 and civilId=?3")
	public List<ViewOnlineCustomerCheck> civilIdCheckForOnlineUser(BigDecimal companyId,BigDecimal countryId,String civilId);
}
