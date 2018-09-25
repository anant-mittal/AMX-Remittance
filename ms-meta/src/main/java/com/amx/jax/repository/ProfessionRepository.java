package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.dbmodel.ProfessionMasterView;

public interface ProfessionRepository extends JpaRepository<ProfessionMasterView, BigDecimal>{
	List<ProfessionMasterView> findAll();
}
