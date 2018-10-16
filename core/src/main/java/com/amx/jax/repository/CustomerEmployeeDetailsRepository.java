package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.EmployeeDetails;

public interface CustomerEmployeeDetailsRepository extends JpaRepository< EmployeeDetails, Serializable>{

}
