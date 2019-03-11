package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;



public class FlexFieldDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;
	private String amieceDescription;

	public FlexFieldDto(BigDecimal additionalBankRuleFiledId, BigDecimal srlId, String amieceDescription) {
		super();
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
		this.srlId = srlId;
		this.amieceDescription = amieceDescription;
	}

	public FlexFieldDto(String amieceDescription) {
		this.amieceDescription = amieceDescription;
	}

	public BigDecimal getAdditionalBankRuleFiledId() {
		return additionalBankRuleFiledId;
	}

	public void setAdditionalBankRuleFiledId(BigDecimal additionalBankRuleFiledId) {
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
	}

	public BigDecimal getSrlId() {
		return srlId;
	}

	public void setSrlId(BigDecimal srlId) {
		this.srlId = srlId;
	}

	public String getAmieceDescription() {
		return amieceDescription;
	}

	public void setAmieceDescription(String amieceDescription) {
		this.amieceDescription = amieceDescription;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalBankRuleFiledId == null) ? 0 : additionalBankRuleFiledId.hashCode());
		result = prime * result + ((srlId == null) ? 0 : srlId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FlexFieldDto other = (FlexFieldDto) obj;
		if (additionalBankRuleFiledId == null) {
			if (other.additionalBankRuleFiledId != null)
				return false;
		} else if (!additionalBankRuleFiledId.equals(other.additionalBankRuleFiledId))
			return false;
		if (srlId == null) {
			if (other.srlId != null)
				return false;
		} else if (!srlId.equals(other.srlId))
			return false;
		return true;
	}

	public FlexFieldDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
