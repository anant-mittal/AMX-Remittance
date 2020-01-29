package com.amx.amxlib.model;

public class DailyPromotionDTO {

	private String customerName;

	private String promotionCode;
	
	private String identityInt;
	
	private String promotionMsg;
	
	private String errorMsg;

	public String getPromotionMsg() {
		return promotionMsg;
	}

	public void setPromotionMsg(String promotionMsg) {
		this.promotionMsg = promotionMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

}
