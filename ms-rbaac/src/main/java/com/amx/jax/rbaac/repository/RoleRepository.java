package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.Role;

public interface RoleRepository extends JpaRepository<Role, Serializable> {
	
	
	public Role findById(BigDecimal id);
	
	public Role findByRole(String role);
	
 	
}
