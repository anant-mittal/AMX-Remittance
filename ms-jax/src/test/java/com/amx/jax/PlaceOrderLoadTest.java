package com.amx.jax;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dict.Tenant;
import com.amx.jax.multitenant.TenantContext;
import com.amx.jax.prop.PlaceOrderProperties;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.PlaceOrderService;
import com.amx.jax.util.PlaceOrderUtil;

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

	@Before
	public void contextLoads() {
		TenantContext.setCurrentTenant(Tenant.KWT.toString());
	}

	
	@Test
	public void loadTestPlaceOrder() {
		logger.info("in loadTestPlaceOrder with params: ");
		logger.info("currency id: " + placeOrderProperties.getCurrencyId());
		logger.info("No of place orders: " + placeOrderProperties.getNoOfPlaceOrders());
		logger.info("rate target: " + placeOrderProperties.getTargetExchangeRate());
		// find routing bank id to route place orders thr for given currency
		List<BenificiaryListView> beneList = beneficiaryService.listBeneficiaryForPOloadTest();
		List<PlaceOrder> placeOrders = new ArrayList<>();
		beneList.stream().limit(placeOrderProperties.getNoOfPlaceOrders().intValue()).forEach(bene -> {
			PlaceOrderDTO dto = new PlaceOrderDTO();
			dto.setBankRuleFieldId(new BigDecimal(101));
			dto.setBaseCurrencyId(new BigDecimal(1));
			dto.setBaseCurrencyQuote("KWD");
			dto.setBeneficiaryRelationshipSeqId(bene.getBeneficiaryRelationShipSeqId());
			dto.setCreatedDate(Calendar.getInstance().getTime());
			dto.setForeignCurrencyId(placeOrderProperties.getCurrencyId());
			dto.setForeignCurrencyQuote("INR");
			dto.setIsActive("Y");
			dto.setPayAmount(new BigDecimal(10));
			dto.setSourceOfIncomeId(new BigDecimal(2));
			dto.setSrlId(new BigDecimal(12));
			dto.setTargetExchangeRate(placeOrderProperties.getTargetExchangeRate());
			Calendar validToDate = Calendar.getInstance();
			validToDate.add(Calendar.HOUR, 10);
			dto.setValidFromDate(Calendar.getInstance().getTime());
			dto.setValidToDate(validToDate.getTime());
			dto.setCustomerId(bene.getCustomerId());
			dto.setBankId(bene.getBankId());
			dto.setCountryId(bene.getCountryId());
			dto.setCurrencyId(bene.getCurrencyId());
			placeOrders.add(PlaceOrderUtil.getPlaceOrderModel(dto));
		});
		placeOrderService.savePlaceOrder(placeOrders);
		// save or update place orders
		// fetch affected place order ids
		// record how many sent
		// assert

	}
}
