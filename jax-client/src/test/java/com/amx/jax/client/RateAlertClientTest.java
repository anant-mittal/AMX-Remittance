package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.constant.RuleEnum;
import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ExchangeRateResponseModel;
import com.amx.amxlib.model.response.PurposeOfTransactionModel;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.amxlib.model.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RateAlertClientTest {

	@Autowired
	RateAlertClient client;
	
	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	//@Test
	public void saveRateAlert() throws ParseException{
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		RateAlertDTO rateAlertDTO = new RateAlertDTO();
		rateAlertDTO.setCustomerId(new BigDecimal(596142));
		rateAlertDTO.setAlertRate(new BigDecimal(253.15));
		rateAlertDTO.setBaseCurrencyId(new BigDecimal(1));
		rateAlertDTO.setForeignCurrencyId(new BigDecimal(5));
		rateAlertDTO.setRule(RuleEnum.EQUAL);
		rateAlertDTO.setFromDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-28"));
		rateAlertDTO.setToDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-28"));
		
    	 ApiResponse<RateAlertDTO> response = client.saveRateAlert(rateAlertDTO);
		
		assertNotNull("Response is null", response);
	}
	
	//@Test
	public void deleteRateAlert() throws ParseException{
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		
		RateAlertDTO rateAlertDTO = new RateAlertDTO();
		rateAlertDTO.setRateAlertId(new BigDecimal(2147));
		rateAlertDTO.setCustomerId(new BigDecimal(596142));
		rateAlertDTO.setAlertRate(new BigDecimal(253.15));
		rateAlertDTO.setBaseCurrencyId(new BigDecimal(1));
		rateAlertDTO.setForeignCurrencyId(new BigDecimal(5));
		rateAlertDTO.setRule(RuleEnum.EQUAL);
		rateAlertDTO.setFromDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-01-28"));
		rateAlertDTO.setToDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-28"));
		
    	 ApiResponse<RateAlertDTO> response = client.deleteRateAlert(rateAlertDTO);
		
		assertNotNull("Response is null", response);
	}
	
	@Test
	public void getRateAlert() throws ParseException{
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(596142));

		ApiResponse<RateAlertDTO> response = client.getRateAlertForCustomer();
		
		assertNotNull("Response is null", response);
	}
	


}
