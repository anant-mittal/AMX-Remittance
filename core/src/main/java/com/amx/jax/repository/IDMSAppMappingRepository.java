package com.amx.jax.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.DmsApplMapping;

public interface IDMSAppMappingRepository  extends JpaRepository<DmsApplMapping, Serializable>{

}
