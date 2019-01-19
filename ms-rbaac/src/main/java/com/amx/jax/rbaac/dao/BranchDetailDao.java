package com.amx.jax.rbaac.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.rbaac.repository.BranchSystemDetailRepository;

@Component
public class BranchDetailDao {

	@Autowired
	private BranchSystemDetailRepository branchSystemDetailRepository;

	public List<BranchSystemDetail> getBranchSystemDetail(BigDecimal countryBranchId) {
		return branchSystemDetailRepository.findByIsActiveAndCountryBranchId("Y", countryBranchId);
	}

	public BranchSystemDetail getBranchSystemDetail(String ipAddress) {
		return branchSystemDetailRepository.findByIpAddress(ipAddress);
	}

	public BranchSystemDetail getBranchSystemDetailByInventoryId(BigDecimal countryBranchSystemInventoryId) {
		return branchSystemDetailRepository.findOne(countryBranchSystemInventoryId);
	}

	public List<BranchSystemDetail> listBranchSystemDetail() {
		return branchSystemDetailRepository.findByIsActive("Y");
	}
}
