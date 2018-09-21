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

	@Value("${target_exchange_rate}")
	BigDecimal targetExchangeRate;


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

	public BigDecimal getTargetExchangeRate() {
		return targetExchangeRate;
	}

	public void setTargetExchangeRate(BigDecimal targetExchangeRate) {
		this.targetExchangeRate = targetExchangeRate;
	}

}
