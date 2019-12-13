package com.amx.jax.userservice.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.amx.jax.dbmodel.bene.RelationsDescription;

public interface RelationsRepository extends JpaRepository<RelationsDescription, Serializable> {

	List<RelationsDescription> findBylangId(BigDecimal langId);

	@Query(value = "SELECT * FROM JAX_VW_RELATIONSHIP jvr WHERE RELATIONS_CODE='30'", nativeQuery=true)
	List<RelationsDescription> getOthersRelations();
}
