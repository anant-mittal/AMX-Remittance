package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.TransactionLimitCheckView;

public interface TransactionLimitCheckDAO extends JpaRepository<TransactionLimitCheckView, Serializable> {

	public List<TransactionLimitCheckView> findAll();
	
}
