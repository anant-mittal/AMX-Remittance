package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.LoyaltyPointModel;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPointModel, Serializable> {

	@Query("select lp from LoyaltyPointModel lp where cusRef=?1")
	public BigDecimal getLoyaltyPoints(BigDecimal cusref);
}
