package com.amx.jax;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.prop.PlaceOrderProperties;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.PlaceOrderService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceOrderLoadTest {

	Logger logger = LoggerFactory.getLogger(PlaceOrderLoadTest.class);

	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	PlaceOrderService placeOrderService;
	@Autowired
	PlaceOrderProperties placeOrderProperties;

	@Test
	public void loadTestPlaceOrder() {
		logger.debug("in loadTestPlaceOrder with params: ");
		logger.debug("currency id: " + placeOrderProperties.getCurrencyId());
		logger.debug("No of place orders: " + placeOrderProperties.getNoOfPlaceOrders());
		logger.debug("rate to change: " + placeOrderProperties.getRateToChange());
		logger.debug("customerid: " + placeOrderProperties.getCurrencyId());
		// find routing bank id to route place orders thr for given currency
		PlaceOrderDTO dto = new PlaceOrderDTO();
		dto.setBankRuleFieldId(new BigDecimal(101));
		dto.setBaseCurrencyId(new BigDecimal(1));
		dto.setBaseCurrencyQuote("KWD");
		dto.setBeneficiaryRelationshipSeqId(beneficiaryRelationshipSeqId);
		dto.setCreatedDate(createdDate);
		dto.setForeignCurrencyId(new BigDecimal(4));
		dto.setForeignCurrencyQuote("INR");
		// save or update place orders
		// fetch affected place order ids
		// record how many sent
		// assert

	}
}
