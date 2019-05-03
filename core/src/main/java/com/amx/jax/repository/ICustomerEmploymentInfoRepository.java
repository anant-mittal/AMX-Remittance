package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerEmploymentInfo;



public interface ICustomerEmploymentInfoRepository extends  CrudRepository<CustomerEmploymentInfo, Serializable>{
	
	public List<CustomerEmploymentInfo> findByFsCustomerAndIsActive(Customer fsCustomer , String isActive);
}
