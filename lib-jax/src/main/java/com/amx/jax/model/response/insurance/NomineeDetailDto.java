package com.amx.jax.model.response.insurance;

import java.math.BigDecimal;

import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.ResourceDTO;

public class NomineeDetailDto extends ResourceDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BeneficiaryListDTO beneficiaryDetail;
	Integer percent;
	BigDecimal nomineeId;

	public BeneficiaryListDTO getBeneficiaryListDTO() {
		return beneficiaryDetail;
	}

	public void setBeneficiaryListDTO(BeneficiaryListDTO beneficiaryListDTO) {
		this.beneficiaryDetail = beneficiaryListDTO;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}

	@Override
	public BigDecimal getResourceId() {
		return getNomineeId();
	}

	@Override
	public String getResourceName() {
		return beneficiaryDetail.getBenificaryName();
	}

	public BeneficiaryListDTO getBeneficiaryDetail() {
		return beneficiaryDetail;
	}

	public void setBeneficiaryDetail(BeneficiaryListDTO beneficiaryDetail) {
		this.beneficiaryDetail = beneficiaryDetail;
	}

	public BigDecimal getNomineeId() {
		return nomineeId;
	}

	public void setNomineeId(BigDecimal nomineeId) {
		this.nomineeId = nomineeId;
	}
}
