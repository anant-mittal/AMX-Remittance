package com.amx.jax.model.request.remittance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.model.response.remittance.RemittanceCollectionDto;

public class BranchRemittanceRequestModel {

	List<BranchApplicationDto> remittanceApplicationId =new ArrayList<>();
	List<RemittanceCollectionDto> collctionModeDto = new ArrayList<>();
	UserStockDto currencyRefundDenomination = new UserStockDto();
	BigDecimal paidAmount = BigDecimal.ZERO;
	BigDecimal totalTrnxAmount = BigDecimal.ZERO;
	
	
	
	

	
		public UserStockDto getCurrencyRefundDenomination() {
			return currencyRefundDenomination;
		}
		public void setCurrencyRefundDenomination(UserStockDto currencyRefundDenomination) {
			this.currencyRefundDenomination = currencyRefundDenomination;
		}
		public List<RemittanceCollectionDto> getCollctionModeDto() {
			return collctionModeDto;
		}
		public void setCollctionModeDto(List<RemittanceCollectionDto> collctionModeDto) {
			this.collctionModeDto = collctionModeDto;
		}
		public List<BranchApplicationDto> getRemittanceApplicationId() {
			return remittanceApplicationId;
		}
		public void setRemittanceApplicationId(List<BranchApplicationDto> remittanceApplicationId) {
			this.remittanceApplicationId = remittanceApplicationId;
		}
		public BigDecimal getPaidAmount() {
			return paidAmount;
		}
		public BigDecimal getTotalTrnxAmount() {
			return totalTrnxAmount;
		}
		public void setTotalTrnxAmount(BigDecimal totalTrnxAmount) {
			this.totalTrnxAmount = totalTrnxAmount;
		}
	
		
		
}
