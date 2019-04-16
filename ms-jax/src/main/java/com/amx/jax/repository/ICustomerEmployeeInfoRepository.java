package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CustomerEmployeeInfoView;



public interface ICustomerEmployeeInfoRepository extends CrudRepository<CustomerEmployeeInfoView, Serializable>{

	public List<CustomerEmployeeInfoView> findByCustomerId(BigDecimal customerId);
}
