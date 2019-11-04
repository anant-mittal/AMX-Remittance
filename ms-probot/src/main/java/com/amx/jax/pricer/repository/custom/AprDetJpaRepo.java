package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;

@Transactional
public interface AprDetJpaRepo extends PagingAndSortingRepository<ExchangeRateMasterApprovalDet, BigDecimal>, AprDetJpaRepoCustom {

}
