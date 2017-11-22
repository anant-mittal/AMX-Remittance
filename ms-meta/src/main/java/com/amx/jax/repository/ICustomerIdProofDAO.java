package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.model.CustomerIdProof;

public interface ICustomerIdProofDAO extends JpaRepository<CustomerIdProof, Serializable>{

	
	@Query(value="select * from FS_CUSTOMER_ID_PROOF where CUSTOMER_ID=?1 and NVL(ISACTIVE,'') <>'D' and  TRUNC(IDENTITY_EXPIRY_DATE) >=TRUNC(SYSDATE)"
			+ " ORDER BY NVL(LAST_UPDATED_DATE,CREATION_DATE) DESC",nativeQuery=true)
	public List<CustomerIdProof> getCustomerIdProofByCustomerId(BigDecimal customerId);
	
	
}