package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.AccountTypeFromViewModel;


public interface IAccountTypeFromViewDao extends JpaRepository<AccountTypeFromViewModel, Serializable>{
	
	@Query("select av from AccountTypeFromViewModel av where av.countryId=?1")
	public List<AccountTypeFromViewModel> getAccountTypeByCountryId(BigDecimal countryId);
	
	public AccountTypeFromViewModel findByAdditionalAmiecId(BigDecimal additionalAmiecId);

}
