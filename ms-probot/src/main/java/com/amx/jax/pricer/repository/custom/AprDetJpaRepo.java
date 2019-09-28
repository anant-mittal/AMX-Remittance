package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;

@Transactional
public interface AprDetJpaRepo extends JpaRepository<ExchangeRateMasterApprovalDet, BigDecimal>, AprDetJpaRepoCustom {

}
