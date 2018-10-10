package com.amx.jax.rbaac.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.rbaac.dbmodel.RoleMaster;

public interface OldIRoleMasterRepository extends JpaRepository<RoleMaster, Serializable>{

}
