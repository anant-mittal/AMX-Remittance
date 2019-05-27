package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

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

	public DiscountMaster getByDiscountTypeAndDiscountTypeId(String discType, BigDecimal discTypeId) {
		// TODO Auto-generated method stub
		return repository.getByDiscountTypeAndDiscountTypeId(discType, discTypeId);
	}

	public List<DiscountMaster> getByDiscountTypeAndGroupId(String discType, BigDecimal groupId) {
		// TODO Auto-generated method stub
		return repository.getByDiscountTypeAndGroupId(discType, groupId);
	}

	public DiscountMaster getByDiscountIdAndGroupId(BigDecimal disountId, BigDecimal groupId, String discountType) {
		// TODO Auto-generated method stub
		return repository.getByDiscountIdAndGroupId(disountId, groupId, discountType);
	}

	public void saveDiscountForChannel(List<DiscountMaster> channelGroupingDiscounts) {
		repository.save(channelGroupingDiscounts);
	}

	public void saveDiscountForCustomerCategory(List<DiscountMaster> custCatGroupingDiscounts) {
		repository.save(custCatGroupingDiscounts);
	}

}
