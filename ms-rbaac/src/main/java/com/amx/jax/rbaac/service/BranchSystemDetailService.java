package com.amx.jax.rbaac.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.rbaac.dao.BranchDetailDao;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;

@Component
public class BranchSystemDetailService {

	@Autowired
	BranchDetailDao branchDetailDao;

	public BranchSystemDetail findBranchSystemByIp(String branchSystemIp) {
		BranchSystemDetail branchSystemDetail = branchDetailDao.getBranchSystemDetail(branchSystemIp);
		if (branchSystemDetail == null) {
			throw new AuthServiceException(RbaacServiceError.BRANCH_SYSTEM_NOT_FOUND,
					"No  branch system found for given IP ");
		}
		if (!"Y".equals(branchSystemDetail.getIsActive())) {
			throw new AuthServiceException(RbaacServiceError.BRANCH_SYSTEM_NOT_ACTIVE,
					"Given branch system is inactive ");
		}
		return branchSystemDetail;
	}
}
