package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BranchSystemDetail;

public interface BranchSystemDetailRepository extends CrudRepository<BranchSystemDetail, Serializable> {

	public List<BranchSystemDetail> findByIsActiveAndCountryBranchId(String isActive, BigDecimal countryBranchId);

	public BranchSystemDetail findByIpAddress(String ip);

	public List<BranchSystemDetail> findByIsActive(String isActive);
}
