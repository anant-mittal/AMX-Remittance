package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceOrderClientTest {

	@Autowired
	PlaceOrderClient client;
	
	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;
	
	@Test
	public void savePlaceOrder() throws ParseException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5246));
		
		PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO();
		placeOrderDTO.setCustomerId(new BigDecimal(2406));
		placeOrderDTO.setBeneficiaryRelationshipSeqId(new BigDecimal(4));
		placeOrderDTO.setPayAmount(new BigDecimal(400));
		placeOrderDTO.setReceiveAmount(new BigDecimal(80000));
		placeOrderDTO.setTargetExchangeRate(new BigDecimal(222));
		placeOrderDTO.setBankRuleFieldId(new BigDecimal(6));
		placeOrderDTO.setSrlId(new BigDecimal(6));
		placeOrderDTO.setSourceOfIncomeId(new BigDecimal(2));
		placeOrderDTO.setCreatedDate(new Date());
		placeOrderDTO.setValidFromDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-01"));
		placeOrderDTO.setValidToDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-09"));
		
		ApiResponse<PlaceOrderDTO> response = client.savePlaceOrder(placeOrderDTO);
		assertNotNull("Response is null", response);
	}
	
	//@Test
	public void getPlaceOrderForCustomer() throws ParseException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		ApiResponse<PlaceOrderDTO> response = client.getPlaceOrderForCustomer();
		assertNotNull("Response is null", response);
	}
	
	//@Test
	public void getAllPlaceOrder() throws ParseException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		ApiResponse<PlaceOrderDTO> response = client.getAllPlaceOrder();
		assertNotNull("Response is null", response);
	}
}
