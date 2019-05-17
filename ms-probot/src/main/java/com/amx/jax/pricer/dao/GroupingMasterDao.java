package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.GroupingMaster;
import com.amx.jax.pricer.repository.GroupingMasterRepository;

@Component
public class GroupingMasterDao {

	@Autowired
	GroupingMasterRepository repository;

	public GroupingMaster getGroupById(BigDecimal groupId) {
		return repository.findOne(groupId);
	}

}
