package com.amx.jax.prop;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:/test/po/placeorderLoadTest.properties")
public class PlaceOrderProperties {

	@Value("${jax.placeorder.no_of_po}")
	String noOfPlaceOrders;

	@Value("${jax.placeorder.currency_id}")
	String currencyId;

	@Value("${jax.placeorder.rate_to_change}")
	String rateToChange;

	public String getNoOfPlaceOrders() {
		return noOfPlaceOrders;
	}

	public void setNoOfPlaceOrders(String noOfPlaceOrders) {
		this.noOfPlaceOrders = noOfPlaceOrders;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getRateToChange() {
		return rateToChange;
	}

	public void setRateToChange(String rateToChange) {
		this.rateToChange = rateToChange;
	}

}
