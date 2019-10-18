package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.GroupingMaster;

@Transactional
public interface GroupingMasterRepository extends CrudRepository<GroupingMaster, BigDecimal> {

	@Query("select d from GroupingMaster d where groupType='CUR' and isActive='Y'")
	List<GroupingMaster> getGroupForCurrency();

	List<GroupingMaster> findByGroupType(String groupType);

	GroupingMaster findByGroupTypeAndGroupName(String groupType, String groupName);

}
