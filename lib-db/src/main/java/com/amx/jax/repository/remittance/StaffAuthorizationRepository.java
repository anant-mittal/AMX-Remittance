package com.amx.jax.repository.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.remittance.StaffAuthorizationView;

public interface StaffAuthorizationRepository extends CrudRepository<StaffAuthorizationView, Serializable>{
	
	@Query(value = "Select * from VW_EX_DEBIT_AUTH", nativeQuery = true)
	public List<StaffAuthorizationView> fetchStaffAuthorization();
	
	@Query(value = "Select * from VW_EX_DEBIT_AUTH", nativeQuery = true)
	public List<StaffAuthorizationView> fetchStaffAuthorization(BigDecimal countryBranchCode);
	
	@Query(value = "Select * from VW_EX_DEBIT_AUTH where User_Name = ?1 and Password = ?2", nativeQuery = true)
	public BigDecimal validationStaffCredentials(String userName,String password);
	
	@Query(value = "Select * from VW_EX_DEBIT_AUTH where User_Name = ?1 and Password = ?2", nativeQuery = true)
	public BigDecimal validationStaffCredentials(String userName,String password,BigDecimal countryBranchCode);
	
	public List<StaffAuthorizationView> findByLocCode(BigDecimal locCode);

}
