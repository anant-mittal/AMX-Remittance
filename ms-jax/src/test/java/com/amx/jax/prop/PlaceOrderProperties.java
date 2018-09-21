package com.amx.jax.prop;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:/test/po/placeorderLoadTest.properties")
public class PlaceOrderProperties {

	@Value("${no_of_po}")
	BigDecimal noOfPlaceOrders;

	@Value("${currency_id}")
	BigDecimal currencyId;

	@Value("${rate_to_change}")
	BigDecimal rateToChange;

	@Value("${customer_id}")
	BigDecimal customerId;

	public BigDecimal getNoOfPlaceOrders() {
		return noOfPlaceOrders;
	}

	public void setNoOfPlaceOrders(BigDecimal noOfPlaceOrders) {
		this.noOfPlaceOrders = noOfPlaceOrders;
	}

	public BigDecimal getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getRateToChange() {
		return rateToChange;
	}

	public void setRateToChange(BigDecimal rateToChange) {
		this.rateToChange = rateToChange;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

}
