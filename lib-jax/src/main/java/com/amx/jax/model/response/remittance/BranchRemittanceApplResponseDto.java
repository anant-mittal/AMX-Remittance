package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.List;

public class BranchRemittanceApplResponseDto {

	BigDecimal totalLocalAmount =BigDecimal.ZERO;
	List<CustomerShoppingCartDto> shoppingCartDetails;
	public BigDecimal getTotalLocalAmount() {
		return totalLocalAmount;
	}
	public void setTotalLocalAmount(BigDecimal totalLocalAmount) {
		this.totalLocalAmount = totalLocalAmount;
	}
	public List<CustomerShoppingCartDto> getShoppingCartDetails() {
		return shoppingCartDetails;
	}
	public void setShoppingCartDetails(List<CustomerShoppingCartDto> shoppingCartDetails) {
		this.shoppingCartDetails = shoppingCartDetails;
	}
}
