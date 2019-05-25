package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.DiscountMaster;

@Transactional
public interface DiscountMasterRepository extends CrudRepository<DiscountMaster, BigDecimal> {

	public DiscountMaster getByDiscountTypeAndDiscountTypeIdAndGroupId(String discountType, BigDecimal typeId,
			BigDecimal groupId);

	@Query("select d from DiscountMaster d where discountType=?1 and  discountTypeId =?2")
	public DiscountMaster getByDiscountTypeAndDiscountTypeId(String discountType, BigDecimal discountTypeId);

	@Query("select d from DiscountMaster d where discountType=?1 and  groupId =?2")
	public List<DiscountMaster> getByDiscountTypeAndGroupId(String discountType, BigDecimal groupId);

}
