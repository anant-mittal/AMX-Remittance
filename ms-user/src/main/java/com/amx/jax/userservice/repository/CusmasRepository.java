package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.CusmasModel;

public interface CusmasRepository extends JpaRepository<CusmasModel, Serializable>{

	@Query("select cd from CusmasModel cd where cd.customerReference=?1")
	public List<CusmasModel> getEmosCustomerDetails(BigDecimal customerRefernce);
}
