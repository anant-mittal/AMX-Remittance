package com.amx.jax.userservice.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.Customer;



@Transactional
public interface CustomerRepository extends CrudRepository<Customer, Long> {

}
