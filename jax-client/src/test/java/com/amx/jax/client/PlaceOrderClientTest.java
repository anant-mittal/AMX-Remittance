package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
		placeOrderDTO.setCustomerId(new BigDecimal(596142));
		placeOrderDTO.setBeneficiaryRelationshipSeqId(new BigDecimal(9));
		placeOrderDTO.setPayAmount(new BigDecimal(100));
		placeOrderDTO.setReceiveAmount(new BigDecimal(20000));
		placeOrderDTO.setTargetExchangeRate(new BigDecimal(218));
		placeOrderDTO.setBankRuleFieldId(new BigDecimal(4));
		placeOrderDTO.setSrlId(new BigDecimal(4));
		placeOrderDTO.setSourceOfIncomeId(new BigDecimal(3));
		placeOrderDTO.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-29"));
		placeOrderDTO.setValidFromDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-30"));
		placeOrderDTO.setValidToDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-05-31"));
		
		ApiResponse<PlaceOrderDTO> response = client.savePlaceOrder(placeOrderDTO);
		assertNotNull("Response is null", response);
	}
}
