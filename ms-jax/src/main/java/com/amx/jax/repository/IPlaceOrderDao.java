package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.PlaceOrder;

public interface IPlaceOrderDao extends JpaRepository<PlaceOrder, Serializable>{
	

}
