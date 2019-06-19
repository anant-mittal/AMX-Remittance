package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.AddressProofModel;

public interface AddressProofRepository extends JpaRepository<AddressProofModel, Serializable>{
	
	@Query("select a from AddressProofModel a where a.isActive='Y'")
	public List<AddressProofModel> getAddressProof();

}
