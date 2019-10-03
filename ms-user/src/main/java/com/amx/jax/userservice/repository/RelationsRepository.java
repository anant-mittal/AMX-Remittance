package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.bene.RelationsDescription;

public interface RelationsRepository extends JpaRepository<RelationsDescription, Serializable> {

	List<RelationsDescription> findBylangId(BigDecimal langId);
	
	List<RelationsDescription> findByRelationsCodeAndLangId(String relationCode,BigDecimal langId);
}
