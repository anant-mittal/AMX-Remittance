package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.util.List;

public class FxOrderShoppingCartResponseModel {

	List<ShoppingCartDetailsDto> shoppingCartList;
	private BigDecimal deliveryCharges;
	private BigDecimal totalNetAmount;
	private BigDecimal totalAmount;
	public List<ShoppingCartDetailsDto> getShoppingCartList() {
		return shoppingCartList;
	}
	public void setShoppingCartList(List<ShoppingCartDetailsDto> shoppingCartList) {
		this.shoppingCartList = shoppingCartList;
	}
	public BigDecimal getDeliveryCharges() {
		return deliveryCharges;
	}
	public void setDeliveryCharges(BigDecimal deliveryCharges) {
		this.deliveryCharges = deliveryCharges;
	}
	public BigDecimal getTotalNetAmount() {
		return totalNetAmount;
	}
	public void setTotalNetAmount(BigDecimal totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}
