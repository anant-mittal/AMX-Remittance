package com.amx.amxlib.meta.model;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class AnnualIncomeRangeDTO extends ResourceDTO {
	public BigDecimal incomeRangeFrom;
	public BigDecimal incomeRangeTo;
	
		
	public BigDecimal getIncomeRangeFrom() {
		return incomeRangeFrom;
	}
	public void setIncomeRangeFrom(BigDecimal incomeRangeFrom) {
		this.incomeRangeFrom = incomeRangeFrom;
	}
	public BigDecimal getIncomeRangeTo() {
		return incomeRangeTo;
	}
	public void setIncomeRangeTo(BigDecimal incomeRangeTo) {
		this.incomeRangeTo = incomeRangeTo;
		
	}
	
	
}
