package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.amxlib.meta.model.CustomerRatingDTO;
import com.amx.jax.dbmodel.CustomerRating;

public interface ICustomerRatingDao extends JpaRepository<CustomerRating, Serializable>{
	void save(CustomerRatingDTO dto);

}
