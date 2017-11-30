package com.amx.jax.userservice.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.CusmasModel;
import com.amx.jax.userservice.repository.CusmasRepository;

@Component
public class CusmosDao {

	@Autowired
	CusmasRepository repo;

	public CusmasModel getOldCusMasDetails(BigDecimal customerRefernce) {
		return repo.findOne(customerRefernce);
	}
}
