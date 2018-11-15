package com.amx.jax;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.controller.PlaceOrderController;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.PipsMaster;
import com.amx.jax.dbmodel.PlaceOrder;
import com.amx.jax.dict.Tenant;
import com.amx.jax.exrateservice.dao.PipsMasterDao;
import com.amx.jax.prop.PlaceOrderProperties;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.PlaceOrderService;
import com.amx.jax.util.PlaceOrderUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=JaxServiceApplication.class)
public class PlaceOrderLoadTest {

	Logger logger = LoggerFactory.getLogger(PlaceOrderLoadTest.class);

	@Autowired
	BeneficiaryService beneficiaryService;
	@Autowired
	PlaceOrderService placeOrderService;
	@Autowired
	PlaceOrderProperties placeOrderProperties;
	@Autowired
	PipsMasterDao pipsMasterDao;
	@Autowired
	PlaceOrderController placeOrderController;

	@Before
	public void contextLoads() {
		TenantContextHolder.setCurrent(Tenant.KWT);
		//TenantContext.setCurrentTenant(Tenant.KWT.toString());
	}

	@Test
	public void loadTestPlaceOrder() {
		TenantContextHolder.setCurrent(Tenant.KWT);
		//TenantContext.setCurrentTenant(Tenant.KWT.toString());
		logger.info("in loadTestPlaceOrder with params: ");
		logger.info("currency id: " + placeOrderProperties.getCurrencyId());
		logger.info("No of place orders: " + placeOrderProperties.getNoOfPlaceOrders());
		logger.info("rate target: " + placeOrderProperties.getTargetExchangeRate());
		// find routing bank id to route place orders thr for given currency
		List<BenificiaryListView> beneList = beneficiaryService.listBeneficiaryForPOloadTest(
				placeOrderProperties.getNoOfPlaceOrders().intValue(), placeOrderProperties.getCurrencyId());
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
			dto.setReceiveAmount(new BigDecimal(2260));
			dto.setSourceOfIncomeId(new BigDecimal(2));
			dto.setSrlId(new BigDecimal(12));
			dto.setTargetExchangeRate(placeOrderProperties.getTargetExchangeRate());
			Calendar validToDate = Calendar.getInstance();
			validToDate.add(Calendar.HOUR, 2);
			dto.setValidFromDate(Calendar.getInstance().getTime());
			dto.setValidToDate(validToDate.getTime());
			dto.setCustomerId(bene.getCustomerId());
			dto.setBankId(bene.getBankId());
			dto.setCountryId(bene.getCountryId());
			dto.setCurrencyId(bene.getCurrencyId());
			PlaceOrder placeOrderModel = PlaceOrderUtil.getPlaceOrderModel(dto);
			placeOrderModel.setCreatedDate(new Date());
			placeOrderModel.setIsActive("Y");

			placeOrderModel.setBankId(dto.getBankId());
			placeOrderModel.setCountryId(dto.getCountryId());
			placeOrderModel.setCurrencyId(dto.getCurrencyId());
			placeOrders.add(placeOrderModel);
		});
		placeOrderService.savePlaceOrder(placeOrders);
		// fetch pips master id
		List<PipsMaster> pips = pipsMasterDao.getPipsMasterForForeignAmount(placeOrderProperties.getCurrencyId(),
				new BigDecimal(2260), new BigDecimal(78), placeOrderProperties.getRoutingBankId());
		BigDecimal pipsMasterId = pips.get(0).getPipsMasterId();
		logger.info("pips masterid: " + pipsMasterId);
		StopWatch watch = new StopWatch();
		watch.start();
		ApiResponse apiResponse = placeOrderController.handleUrlPlaceOrderOnTrigger(pipsMasterId);
		watch.stop();
		int size = apiResponse.getResults().size();
		logger.info("Total size of matched place order= " + size);
		long timetaken = watch.getLastTaskTimeMillis();
		logger.info("Total time taken to fetch placeorders from db: " + timetaken / 1000 + " seconds");

	}
}
