package com.amx.jax.util;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.dbmodel.PlaceOrder;

public class PlaceOrderUtil {
	
	public static PlaceOrder getPlaceOrderModel(PlaceOrderDTO dto) {
		PlaceOrder placeOrdermodel = new PlaceOrder();
		
		placeOrdermodel.setCustomerId(dto.getCustomerId());
		placeOrdermodel.setBaseCurrencyId(dto.getBaseCurrencyId());
		placeOrdermodel.setBaseCurrencyCode(dto.getBaseCurrencyId().toString());
		placeOrdermodel.setForeignCurrencyId(dto.getForeignCurrencyId());
		placeOrdermodel.setForeignCurrencyCode(dto.getForeignCurrencyId().toString());
		placeOrdermodel.setTargetRate(dto.getTargetRate());
		placeOrdermodel.setPurposeId(dto.getPurposeId());
		placeOrdermodel.setSourceOfIncomeId(dto.getSourceOfIncomeId());
		placeOrdermodel.setValidFromDate(dto.getValidFromDate());
		placeOrdermodel.setValidToDate(dto.getValidToDate());
		placeOrdermodel.setPayAmount(dto.getPayAmount());
		placeOrdermodel.setReceiveAmount(dto.getReceiveAmount());
		
		return placeOrdermodel;
	}
}
