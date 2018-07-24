package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
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
	
	//@Test
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
	
	//@Test
	public void deletePlaceOrder() throws ParseException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		

		PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO();
		placeOrderDTO.setPlaceOrderId(new BigDecimal(12));
		
		ApiResponse<PlaceOrderDTO> response = client.deletePlaceOrder(placeOrderDTO);
		assertNotNull("Response is null", response);
	}
	
	//@Test
	public void updatePlaceOrder() throws ParseException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO();
		placeOrderDTO.setPlaceOrderId(new BigDecimal(21));
		placeOrderDTO.setCustomerId(new BigDecimal(5218));
		placeOrderDTO.setBeneficiaryRelationshipSeqId(new BigDecimal(2));
		placeOrderDTO.setPayAmount(new BigDecimal(200));
		placeOrderDTO.setReceiveAmount(new BigDecimal(20000));
		placeOrderDTO.setTargetExchangeRate(new BigDecimal(222));
		placeOrderDTO.setBankRuleFieldId(new BigDecimal(2));
		placeOrderDTO.setSrlId(new BigDecimal(2));
		placeOrderDTO.setSourceOfIncomeId(new BigDecimal(2));
		placeOrderDTO.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-01"));
		placeOrderDTO.setValidFromDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-02"));
		placeOrderDTO.setValidToDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-06-11"));
		
		ApiResponse<PlaceOrderDTO> response = client.updatePlaceOrder(placeOrderDTO);
		assertNotNull("Response is null", response);
	}
	
	@Test
	public void rateAlertPlaceOrder() throws ParseException {
	/*	ApiResponse<PlaceOrderDTO> response = client.updatePlaceOrder(placeOrderDTO);
		assertNotNull("Response is null", response);*/
		
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		ApiResponse<PlaceOrderDTO> response = null;
		response = client.getPlaceOrderDetails(new BigDecimal(1), new BigDecimal(200), new BigDecimal(94), new BigDecimal(4), new BigDecimal(1256),new BigDecimal(300));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		/*assertNotNull(response.getResult().getModelType());*/
	}
}
