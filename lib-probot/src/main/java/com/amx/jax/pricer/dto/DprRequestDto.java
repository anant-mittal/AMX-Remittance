package com.amx.jax.pricer.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_GROUP;

public class DprRequestDto extends PricingRequestDTO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3112085486390742452L;

/*	private BigDecimal customerId;

	@NotNull(message = "Local Country Id Can not be Null or Empty")
	private BigDecimal localCountryId;

	@NotNull(message = "Foreign Country Id Can not be Null or Empty")
	private BigDecimal foreignCountryId;

	private BigDecimal localAmount;
	
	private BigDecimal foreignAmount;

	@NotNull(message = "Local Currency Id Can not be Null or Empty")
	private BigDecimal localCurrencyId;

	@NotNull(message = "Foreign Currency Id Can not be Null or Empty")
	private BigDecimal foreignCurrencyId;

	@NotNull(message = "Country Branch Id Can not be Null or Empty")
	private BigDecimal countryBranchId;

	@NotNull(message = "Channel Can not be Null or Empty")
	private Channel channel;
*/
	
	@NotNull(message = "Beneficiary Id Can not be Null or Empty")
	private BigDecimal beneficiaryId;

	@NotNull(message = "Beneficiary Bank Id Can not be Null or Empty")
	private BigDecimal beneficiaryBankId;

	@NotNull(message = "Beneficiary Branch Id Can not be Null or Empty")
	private BigDecimal beneficiaryBranchId;

	@NotNull(message = "Service Group Can not be Null or Empty")
	private SERVICE_GROUP serviceGroup;

	public SERVICE_GROUP getServiceGroup() {
		return serviceGroup;
	}

	public void setServiceGroup(SERVICE_GROUP serviceGroup) {
		this.serviceGroup = serviceGroup;
	}

	/*O*/

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

}
