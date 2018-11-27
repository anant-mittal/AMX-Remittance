package com.amx.jax.model.response.fx;

import java.util.ArrayList;
import java.util.List;

import com.amx.jax.payg.PaymentResponseDto;

public class FxOrderReportResponseModel {
	
	 List<FxOrderTransactionHistroyDto> fxOrderTrnxList = new ArrayList<>();
	 FxDeliveryDetailDto  deliveryDetailList = new FxDeliveryDetailDto();
	 ShippingAddressDto shippingAddressdto =new ShippingAddressDto();
	 PaygDetailsDto payg = new PaygDetailsDto();
	
	
	public List<FxOrderTransactionHistroyDto> getFxOrderTrnxList() {
		return fxOrderTrnxList;
	}
	public void setFxOrderTrnxList(
			List<FxOrderTransactionHistroyDto> fxOrderTrnxList) {
		this.fxOrderTrnxList = fxOrderTrnxList;
	}
	public PaygDetailsDto getPayg() {
		return payg;
	}
	public void setPayg(PaygDetailsDto payg) {
		this.payg = payg;
	}
	public FxDeliveryDetailDto getDeliveryDetailList() {
		return deliveryDetailList;
	}
	public void setDeliveryDetailList(FxDeliveryDetailDto deliveryDetailList) {
		this.deliveryDetailList = deliveryDetailList;
	}
	public ShippingAddressDto getShippingAddressdto() {
		return shippingAddressdto;
	}
	public void setShippingAddressdto(ShippingAddressDto shippingAddressdto) {
		this.shippingAddressdto = shippingAddressdto;
	}
	 
	

}
