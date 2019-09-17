package com.amx.jax.model.response.remittance;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;



public class FlexFieldDto extends ResourceDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal additionalBankRuleFiledId;
	private BigDecimal srlId;
	private String amieceDescription;
	private String localName;
	private String amieceCode;
	private String indic;

	public FlexFieldDto(BigDecimal additionalBankRuleFiledId, BigDecimal srlId, String amieceDescription,String amieceCode) {
		super();
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
		this.srlId = srlId;
		this.amieceDescription = amieceDescription;
		this.amieceCode = amieceCode;
	}
	
	
	public FlexFieldDto(BigDecimal additionalBankRuleFiledId, BigDecimal srlId, String amieceDescription,String amieceCode,String indic) {
		super();
		this.additionalBankRuleFiledId = additionalBankRuleFiledId;
		this.srlId = srlId;
		this.amieceDescription = amieceDescription;
		this.amieceCode =amieceCode;
		this.indic=indic;
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
	
	

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public FlexFieldDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getResourceLocalName() {
		return this.localName;

	}
	
	@Override
	public String getResourceName() {
		return this.amieceDescription;
	}

	public String getAmieceCode() {
		return amieceCode;
	}

	public void setAmieceCode(String amieceCode) {
		this.amieceCode = amieceCode;
	}

	public String getIndic() {
		return indic;
	}

	public void setIndic(String indic) {
		this.indic = indic;
	}


}
