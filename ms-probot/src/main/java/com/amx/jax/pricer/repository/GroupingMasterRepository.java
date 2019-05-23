package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.GroupingMaster;

@Transactional
public interface GroupingMasterRepository extends CrudRepository<GroupingMaster, BigDecimal> {

}
