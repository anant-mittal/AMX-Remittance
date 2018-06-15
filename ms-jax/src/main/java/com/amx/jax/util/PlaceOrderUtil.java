package com.amx.jax.util;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.dbmodel.PlaceOrder;

public class PlaceOrderUtil {
	
	public static PlaceOrder getPlaceOrderModel(PlaceOrderDTO dto) {
		PlaceOrder placeOrdermodel = new PlaceOrder();
		
		placeOrdermodel.setCustomerId(dto.getCustomerId());
		placeOrdermodel.setBeneficiaryRelationshipSeqId(dto.getBeneficiaryRelationshipSeqId());
		placeOrdermodel.setBankRuleFieldId(dto.getBankRuleFieldId());
		placeOrdermodel.setSrlId(dto.getSrlId());
		placeOrdermodel.setTargetExchangeRate(dto.getTargetExchangeRate());
		placeOrdermodel.setSourceOfIncomeId(dto.getSourceOfIncomeId());
		placeOrdermodel.setValidFromDate(dto.getValidFromDate());
		placeOrdermodel.setValidToDate(dto.getValidToDate());
		placeOrdermodel.setPayAmount(dto.getPayAmount());
		placeOrdermodel.setReceiveAmount(dto.getReceiveAmount());
		placeOrdermodel.setCreatedDate(dto.getCreatedDate());
		
		return placeOrdermodel;
	}
}
