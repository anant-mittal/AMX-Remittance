package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.amxlib.model.response.ApiResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetaClientTest extends AbstractTestClient {

	@Autowired
	MetaClient metaclient;

	// @Test
	public void testdefaultBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<OnlineConfigurationDto> response = null;
		response = metaclient.getOnlineConfig("O");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void testgetBankListForCountry() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<BankMasterDTO> response = null;
		response = metaclient.getBankListForCountry(new BigDecimal(91));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getBankId());
	}

	// @Test
	public void testgetBankBranchListByBankId() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<BankBranchDto> response = null;
		GetBankBranchRequest request = new GetBankBranchRequest(new BigDecimal(2721), new BigDecimal(91), null, null,
				null);
		response = metaclient.getBankBranchList(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getBankId());
	}

	// @Test
	public void testGetAllOnlineCurrency() {
		setDefaults();
		ApiResponse<CurrencyMasterDTO> response = null;
		response = metaclient.getAllOnlineCurrency();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCurrencyName());
	}

	// @Test
	public void testGetAllCountry() {
		setDefaults();
		ApiResponse<CountryMasterDTO> response = null;
		response = metaclient.getAllCountry();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCountryName());
		// assertNotNull(response.getResult().getNationality());
	}

	// @Test
	public void testGetStateList() {
		setDefaults();
		ApiResponse<ViewStateDto> response = null;
		response = metaclient.getStateList(new BigDecimal(94));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getStateName());
	}

	// @Test
	public void testGetDistrictList() {

		// 1 is for english
		BigDecimal langCode = new BigDecimal(1);
		BigDecimal stateCode = new BigDecimal(495);

		setDefaults();
		ApiResponse<ViewDistrictDto> response = null;
		response = metaclient.getDistrictList(stateCode);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getDistrictDesc());
	}
}
