package com.amx.jax.repository.customer;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.customer.DbScanRef;

public interface DbScanRefRepo extends CrudRepository<DbScanRef, Serializable> {

}
