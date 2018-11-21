package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.BankBranchDto;
import com.amx.amxlib.meta.model.BankMasterDTO;
import com.amx.amxlib.meta.model.BranchDetailDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CurrencyMasterDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.request.GetBankBranchRequest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.model.response.BranchSystemDetailDto;
import com.amx.jax.model.response.CurrencyMasterDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=JaxServiceClientApplication.class)
public class MetaClientTest extends AbstractTestClient {

	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	// @Test
	public void testdefaultBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<OnlineConfigurationDto, Object> response = null;
		response = metaclient.getOnlineConfig("O");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void testgetBankListForCountry() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<BankMasterDTO, Object> response = null;
		response = metaclient.getBankListForCountry(new BigDecimal(91));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getBankId());
	}

	// @Test
	public void testgetBankBranchListByBankId() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<BankBranchDto, Object> response = null;
		GetBankBranchRequest request = new GetBankBranchRequest(new BigDecimal(73), new BigDecimal(95), "", "ab", "");
		response = metaclient.getBankBranchList(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getBankId());
	}

	// @Test
	public void testGetAllOnlineCurrency() {
		setDefaults();
		AmxApiResponse<CurrencyMasterDTO, Object> response = null;
		response = metaclient.getAllOnlineCurrency();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCurrencyName());
	}

	// @Test
	public void testgetCitytList() {
		setDefaults();
		AmxApiResponse<ViewCityDto, Object> response = null;
		response = metaclient.getCitytList(new BigDecimal(14186));
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCurrencyName());
	}

	// @Test
	public void testAllExchangeRateCurrencyList() {
		setDefaults();
		AmxApiResponse<CurrencyMasterDTO, Object> response = null;
		response = metaclient.getAllExchangeRateCurrencyList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCurrencyName());
	}

	// @Test
	public void testGetAllCountry() {
		setDefaults();
		AmxApiResponse<CountryMasterDTO, Object> response = null;
		response = metaclient.getAllCountry();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCountryName());
		// assertNotNull(response.getResult().getNationality());
	}

	// @Test
	public void testGetStateList() {
		setDefaults();
		AmxApiResponse<ViewStateDto, Object> response = null;
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
		AmxApiResponse<ViewDistrictDto, Object> response = null;
		response = metaclient.getDistrictList(stateCode);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getDistrictDesc());
	}

	// @Test
	public void testgetServiceGroupList() {
		setDefaults();
		AmxApiResponse<ServiceGroupMasterDescDto, Object> response = null;
		response = metaclient.getServiceGroupList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testgetBeneficiaryCurrency() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<CurrencyMasterDTO, Object> response = null;

		BigDecimal beneficiaryCountryId = new BigDecimal(94);
		BigDecimal serviceGroupId = new BigDecimal(1);
		BigDecimal routingBankId = new BigDecimal(2520);
		response = metaclient.getBeneficiaryCurrency(beneficiaryCountryId, serviceGroupId, routingBankId);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testOldGetBeneficiaryCurrency() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<CurrencyMasterDTO, Object> response = null;

		BigDecimal beneficiaryCountryId = new BigDecimal(94);
		BigDecimal serviceGroupId = new BigDecimal(1);
		BigDecimal routingBankId = new BigDecimal(2520);
		response = metaclient.getBeneficiaryCurrency(beneficiaryCountryId);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void getJaxMetaParameter() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		AmxApiResponse<JaxMetaParameter, Object> response = null;

		response = metaclient.getJaxMetaParameter();
	}

	// @Test
	public void testgetTermsAndCondition() {
		setDefaults();
		jaxMetaInfo.setLanguageId(new BigDecimal(2));
		AmxApiResponse<TermsAndConditionDTO, Object> response = null;
		response = metaclient.getTermsAndCondition();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testGetAllPrefix() {
		setDefaults();
		AmxApiResponse<PrefixDTO, Object> response = null;
		response = metaclient.getAllPrefix();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());
	}

	@Test
	public void testGetAllBranchDetail() {
		//setDefaults();
		setBahrainDefaults();
		AmxApiResponse<BranchDetailDTO, Object> response = null;
		response = metaclient.getAllBranchDetail();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());
	}

	@Test
	public void testlistBranchSystemInventory() {
		setDefaults();
		AmxApiResponse<BranchSystemDetailDto, Object> response = null;
		response = metaclient.listBranchSystemInventory();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResults());
	}
	
	//@Test
	public void testGetCountryListResponse() {
		setDefaults();
		AmxApiResponse<ApplicationSetupDTO, Object> response = null;
		response = metaclient.getApplicationCountry();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		// assertNotNull(response.getResult().getCurrencyName());
	}
}
