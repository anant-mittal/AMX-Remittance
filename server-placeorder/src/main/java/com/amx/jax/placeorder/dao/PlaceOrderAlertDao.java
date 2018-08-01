package com.amx.jax.placeorder.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;


@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PlaceOrderAlertDao {
	
/*	@Autowired
	IPlaceoderAlertRate iPlaceoderAlertRate;

	public List<PlaceOrder> getPlaceOrderAlertRate(BigDecimal countryId,BigDecimal currencyId,BigDecimal bankId ,BigDecimal derivedSellRate) {
		derivedSellRate  = BigDecimal.ONE.divide(derivedSellRate, 5, RoundingMode.HALF_UP);
		return iPlaceoderAlertRate.getPlaceOrderAlertRate(countryId, currencyId, bankId ,derivedSellRate) ;
	}
*/

}
