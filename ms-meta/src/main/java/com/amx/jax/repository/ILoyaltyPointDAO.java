package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.LoyaltyPointModel;

public interface ILoyaltyPointDAO extends JpaRepository<LoyaltyPointModel, Serializable> {

	@Query("select lp from LoyaltyPointModel lp where cusRef=?1 and finYear=?2")	
	public List<LoyaltyPointModel> getLoyaltyPoints(BigDecimal cusref,BigDecimal finYear);
}
