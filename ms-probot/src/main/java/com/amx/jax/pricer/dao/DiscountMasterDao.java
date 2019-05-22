package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.DiscountMaster;
import com.amx.jax.pricer.repository.DiscountMasterRepository;

@Component
public class DiscountMasterDao {

	@Autowired
	DiscountMasterRepository repository;

	public DiscountMaster getByDiscountTypeAndDiscountTypeIdAndGroupId(String discountType, BigDecimal typeId,
			BigDecimal groupId) {
		return repository.getByDiscountTypeAndDiscountTypeIdAndGroupId(discountType, typeId, groupId);
	}

}
