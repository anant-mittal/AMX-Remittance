package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.EmploymentTypeMasterView;

public interface EmploymentTypeRepository extends JpaRepository<EmploymentTypeMasterView, BigDecimal>{
	List<EmploymentTypeMasterView> findAll();
}
