package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.io.JSONable;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;

public class ExchangeRateAndRoutingRequest extends PricingRequestDTO implements Serializable, JSONable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3112085486390742452L;

	@NotNull(message = "Beneficiary Id Can not be Null or Empty")
	private BigDecimal beneficiaryId;

	@NotNull(message = "Beneficiary Bank Id Can not be Null or Empty")
	private BigDecimal beneficiaryBankId;

	@NotNull(message = "Beneficiary Branch Id Can not be Null or Empty")
	private BigDecimal beneficiaryBranchId;

	@NotNull(message = "Service Group Can not be Null or Empty")
	private SERVICE_GROUP serviceGroup;

	private List<BigDecimal> excludeCorBanks;

	private BigDecimal employeeId;

	private BigDecimal companyId;

	public SERVICE_GROUP getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(SERVICE_GROUP serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	/* O */

	public BigDecimal getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(BigDecimal beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public BigDecimal getBeneficiaryBankId() {
		return beneficiaryBankId;
	}

	public void setBeneficiaryBankId(BigDecimal beneficiaryBankId) {
		this.beneficiaryBankId = beneficiaryBankId;
	}

	public BigDecimal getBeneficiaryBranchId() {
		return beneficiaryBranchId;
	}

	public void setBeneficiaryBranchId(BigDecimal beneficiaryBranchId) {
		this.beneficiaryBranchId = beneficiaryBranchId;
	}

	public List<BigDecimal> getExcludeCorBanks() {
		return excludeCorBanks;
	}

	public void setExcludeCorBanks(List<BigDecimal> excludeCorBanks) {
		this.excludeCorBanks = excludeCorBanks;
	}

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public BigDecimal getCompanyId() {
		return companyId;
	}

	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

}
