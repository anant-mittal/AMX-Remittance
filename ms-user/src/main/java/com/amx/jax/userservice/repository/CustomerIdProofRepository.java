package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CustomerIdProof;

public interface CustomerIdProofRepository extends JpaRepository<CustomerIdProof, Serializable> {

	@Query(value = "select * from FS_CUSTOMER_ID_PROOF where CUSTOMER_ID=?1 and NVL(ISACTIVE,'') ='Y' and TRUNC(IDENTITY_EXPIRY_DATE) >= TRUNC(SYSDATE)"
			+ " ORDER BY NVL(LAST_UPDATED_DATE,CREATION_DATE) DESC", nativeQuery = true)
	public List<CustomerIdProof> getCustomerIdProofByCustomerId(BigDecimal customerId);

	@Query(value = "select * from FS_CUSTOMER_ID_PROOF where CUSTOMER_ID=?1 and IDENTITY_TYPE_ID =?2 and NVL(ISACTIVE,'') ='Y' and  TRUNC(IDENTITY_EXPIRY_DATE) >=TRUNC(SYSDATE)"
			+ " ORDER BY NVL(LAST_UPDATED_DATE,CREATION_DATE) DESC", nativeQuery = true)
	public List<CustomerIdProof> getCustomerImageValidation(BigDecimal customerId, BigDecimal identityTypeId);

}
