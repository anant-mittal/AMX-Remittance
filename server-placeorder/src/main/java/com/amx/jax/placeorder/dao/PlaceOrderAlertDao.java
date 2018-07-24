package com.amx.jax.placeorder.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.placeorder.repository.IPlaceoderAlertRate;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PlaceOrderAlertDao {
	
	@Autowired
	IPlaceoderAlertRate iPlaceoderAlertRate;

	public List<PlaceOrder> getPlaceOrderAlertRate(BigDecimal countryId,BigDecimal currencyId,BigDecimal bankId ,BigDecimal derivedSellRate) {
		return iPlaceoderAlertRate.getPlaceOrderAlertRate(countryId, currencyId, bankId ,derivedSellRate) ;

	}


}
