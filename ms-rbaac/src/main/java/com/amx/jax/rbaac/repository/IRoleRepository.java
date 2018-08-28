package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.Role;

public interface IRoleRepository extends JpaRepository<Role, Serializable> {
	
	
	public List<Role> findById(BigDecimal id);
	
	public List<Role> findByRole(String role);
	
 	
}
