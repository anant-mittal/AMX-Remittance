package com.amx.jax.auth.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.auth.dbmodel.RoleMaster;

public interface IRoleMasterRepository extends JpaRepository<RoleMaster, Serializable>{

}
