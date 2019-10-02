package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.customer.CivilIdOtpModel;

public class BranchRemittanceApplResponseDto extends AbstractModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*Total Gross amount */
	BigDecimal totalLocalAmount =BigDecimal.ZERO;
	/* total net amount with commission */
	BigDecimal totalNetAmount =BigDecimal.ZERO;
	BigDecimal totalTrnxFees =BigDecimal.ZERO;
	BigDecimal totalLyltyPointAmt =BigDecimal.ZERO;
	BigDecimal totalLoyaltyPointAvaliable =BigDecimal.ZERO;
	BigDecimal totalNetCollectionAmount =BigDecimal.ZERO;
	String warnigMsg;
	ConfigDto configDto = new ConfigDto();
	
	
	private CivilIdOtpModel civilIdOtpModel;
	private PayGServiceCode pgCode = PayGServiceCode.DEFAULT;
	
	
	public ConfigDto getConfigDto() {
		return configDto;
	}
	public void setConfigDto(ConfigDto configDto) {
		this.configDto = configDto;
	}
	/* application records */
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
	public BigDecimal getTotalNetCollectionAmount() {
		return totalNetCollectionAmount;
	}
	public void setTotalNetCollectionAmount(BigDecimal totalNetCollectionAmount) {
		this.totalNetCollectionAmount = totalNetCollectionAmount;
	}
	public String getWarnigMsg() {
		return warnigMsg;
	}
	public void setWarnigMsg(String warnigMsg) {
		this.warnigMsg = warnigMsg;
	}
	public CivilIdOtpModel getCivilIdOtpModel() {
		return civilIdOtpModel;
	}
	public void setCivilIdOtpModel(CivilIdOtpModel civilIdOtpModel) {
		this.civilIdOtpModel = civilIdOtpModel;
	}
}
