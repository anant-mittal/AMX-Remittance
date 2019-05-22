package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.DiscountMaster;

@Transactional
public interface DiscountMasterRepository extends CrudRepository<DiscountMaster, BigDecimal> {

	public DiscountMaster getByDiscountTypeAndDiscountTypeIdAndGroupId(String discountType, BigDecimal typeId,
			BigDecimal groupId);

}
