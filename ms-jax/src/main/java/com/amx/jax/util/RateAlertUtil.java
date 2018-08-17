package com.amx.jax.util;

import org.springframework.stereotype.Component;

import com.amx.amxlib.model.RateAlertDTO;
import com.amx.jax.dbmodel.RateAlert;

@Component
public class RateAlertUtil {

	public static RateAlert getRateAlertModel(RateAlertDTO dto) {
		
		RateAlert rateAlertModel = new  RateAlert();
		
		rateAlertModel.setCustomerId(dto.getCustomerId());
		rateAlertModel.setBaseCurrencyId(dto.getBaseCurrencyId());
		rateAlertModel.setBaseCurrencyCode(dto.getBaseCurrencyId().toString());
		rateAlertModel.setForeignCurrencyId(dto.getForeignCurrencyId());
		rateAlertModel.setForeignCurrencyCode(dto.getForeignCurrencyId().toString());
		rateAlertModel.setAlertRate(dto.getAlertRate());
		rateAlertModel.setRule(dto.getRule().name());
		rateAlertModel.setFromDate(dto.getFromDate());
		rateAlertModel.setToDate(dto.getToDate());
		rateAlertModel.setPayAmount(dto.getPayAmount());
		rateAlertModel.setReceiveAmount(dto.getReceiveAmount());
		
		return rateAlertModel;
	}
}
