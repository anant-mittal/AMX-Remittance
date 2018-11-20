package com.amx.jax.model.request;
/**
 * @author	: Rabil
 * @date	: 18/11/2018
 * @purpose : Request model for Paynow. 
 */

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ShoppingCartDetailsDto;

public class FcSaleOrderPaynowRequestModel extends AbstractModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4929104237573922383L;
	private List<ShoppingCartDetailsDto> cartDetailList;
	private BigDecimal shippingAddressId;
	private String  deliveryDate;
	private String  timeSlot;
	private BigDecimal totalKnetAmount;
	

	

	
	public List<ShoppingCartDetailsDto> getCartDetailList() {
		return cartDetailList;
	}
	
	
	
	public void setCartDetailList(List<ShoppingCartDetailsDto> cartDetailList) {
		this.cartDetailList = cartDetailList;
	}
	public BigDecimal getShippingAddressId() {
		return shippingAddressId;
	}
	public void setShippingAddressId(BigDecimal shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public BigDecimal getTotalKnetAmount() {
		return totalKnetAmount;
	}
	public void setTotalKnetAmount(BigDecimal totalKnetAmount) {
		this.totalKnetAmount = totalKnetAmount;
	}



}
