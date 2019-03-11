package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.List;

public class BranchRemittanceApplResponseDto {

	/*Total Gross amount */
	BigDecimal totalLocalAmount =BigDecimal.ZERO;
	/* total net amount with commission */
	BigDecimal totalNetAmount =BigDecimal.ZERO;
	BigDecimal totalTrnxFees =BigDecimal.ZERO;
	BigDecimal totalLyltyPointAmt =BigDecimal.ZERO;
	BigDecimal totalLoyaltyPointAvaliable =BigDecimal.ZERO;
	
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
	public BigDecimal getTotalNetAmount() {
		return totalNetAmount;
	}
	public void setTotalNetAmount(BigDecimal totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}
	public BigDecimal getTotalTrnxFees() {
		return totalTrnxFees;
	}
	public void setTotalTrnxFees(BigDecimal totalTrnxFees) {
		this.totalTrnxFees = totalTrnxFees;
	}
	public BigDecimal getTotalLyltyPointAmt() {
		return totalLyltyPointAmt;
	}
	public void setTotalLyltyPointAmt(BigDecimal totalLyltyPointAmt) {
		this.totalLyltyPointAmt = totalLyltyPointAmt;
	}
	public BigDecimal getTotalLoyaltyPointAvaliable() {
		return totalLoyaltyPointAvaliable;
	}
	public void setTotalLoyaltyPointAvaliable(BigDecimal totalLoyaltyPointAvaliable) {
		this.totalLoyaltyPointAvaliable = totalLoyaltyPointAvaliable;
	}
}
