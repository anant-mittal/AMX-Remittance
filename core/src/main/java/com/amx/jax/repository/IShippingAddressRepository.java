package com.amx.jax.repository;

import java.io.Serializable;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ShippingAddressDetail;


public interface IShippingAddressRepository extends CrudRepository<ShippingAddressDetail, Serializable>{
	
	public List<ShippingAddressDetail>  findByFsCustomerAndActiveStatus(Customer fsCustomer,String activeStatus);

}