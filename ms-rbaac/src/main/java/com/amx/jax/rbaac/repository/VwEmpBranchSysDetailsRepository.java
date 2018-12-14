package com.amx.jax.rbaac.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.rbaac.dbmodel.ViewExEmpBranchSysDetails;

public interface VwEmpBranchSysDetailsRepository extends CrudRepository<ViewExEmpBranchSysDetails, Serializable> {

	public List<ViewExEmpBranchSysDetails> findByEmployeeIdAndIpAddress(BigDecimal employeeId, String ipAddress);

	public List<ViewExEmpBranchSysDetails> findByEmployeeIdAndBranchSysInventoryId(BigDecimal employeeId,
			BigDecimal branchSysInvId);

}
