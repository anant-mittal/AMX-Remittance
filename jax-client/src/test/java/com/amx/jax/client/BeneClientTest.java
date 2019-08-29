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
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentBranchParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterAgentParam;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceImpl;
import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.client.configs.JaxMetaInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeneClientTest extends AbstractTestClient {

	@Autowired
	BeneClient client;

	@Autowired
	MetaClient metaclient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	// @Test
	public void testdefaultBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		BigDecimal beneId = new BigDecimal(88041);
		ApiResponse<RemittancePageDto> response = null;
		response = client.defaultBeneficiary(beneId, null);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void testgetBeneficiaryRelations() throws IOException, ResourceNotFoundException, InvalidInputException {
		setDefaults();
		ApiResponse<BeneRelationsDescriptionDto> response = null;
		response = client.getBeneficiaryRelations();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testSendOtp() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		ApiResponse<CivilIdOtpModel> response = null;
		response = client.sendOtp();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}

	// @Test
	public void testValidateOtp() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		String mOtp = "570605";
		String eOtp = "573782";

		ApiResponse<CustomerModel> response = null;
		response = client.validateOtp(mOtp, eOtp);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
		assertNotNull(response.getResult().getModelType());
	}



	// @Test
	@SuppressWarnings("rawtypes")
	public void testServiceProvider() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		ApiResponse response = null;
		RoutingBankMasterServiceProviderParam param = new RoutingBankMasterServiceImpl();
		param.setRoutingCountryId(new BigDecimal(107));
		param.setServiceGroupId(new BigDecimal(1));

		response = client.getServiceProvider(param);

		assertNotNull("Response is null", response);
	}

	// @Test
	@SuppressWarnings("rawtypes")
	public void testAgentMaster() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		ApiResponse response = null;
		RoutingBankMasterAgentParam param = new RoutingBankMasterServiceImpl();
		param.setRoutingCountryId(new BigDecimal(107));
		param.setCurrencyId(new BigDecimal(8));
		param.setRoutingBankId(new BigDecimal(237));
		param.setServiceGroupId(new BigDecimal(1));

		response = client.getAgentMaster(param);

		assertNotNull("Response is null", response);
	}

	// @Test
	@SuppressWarnings("rawtypes")
	public void testAgentBranch() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		ApiResponse response = null;
		RoutingBankMasterAgentBranchParam param = new RoutingBankMasterServiceImpl();
		param.setRoutingCountryId(new BigDecimal(107));
		param.setCurrencyId(new BigDecimal(8));
		param.setRoutingBankId(new BigDecimal(237));
		param.setServiceGroupId(new BigDecimal(1));
		param.setAgentBankId(new BigDecimal(237));

		response = client.getAgentBranch(param);

		assertNotNull("Response is null", response);
	}
	
	// @Test
	@SuppressWarnings("rawtypes")
	public void testGetBeneficiaryAccountType() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));

		ApiResponse response = null;
		BigDecimal beneCountryId = new BigDecimal(94);
		response = client.getBeneficiaryAccountType(beneCountryId);
		assertNotNull("Response is null", response);
	}
	
	/**
	 * @author Chetan Pawar
	 * @return remove parameter beneCountryId which is not in use 11-05-2018	 
	 */
	//@Test
	//@SuppressWarnings("rawtypes")	
	public void testGetBeneficiaryCountry() {
		setDefaults();
		ApiResponse response = null;
		BigDecimal beneCountryId = new BigDecimal(91);
		response = client.getBeneficiaryCountryList(beneCountryId);
		assertNotNull("Response is null", response);
		assertNotNull("result is null", response.getResult());
	}
	
	//@Test
	@SuppressWarnings("rawtypes")
	public void testGetBeneficiary() {
		setDefaults();
		ApiResponse response = null;
		BigDecimal beneCountryId = new BigDecimal(94);
		response = client.getBeneficiaryList(beneCountryId);
		assertNotNull("Response is null", response);
		assertNotNull("result is null", response.getResult());
	}

	// @Test
    public void testPOBeneficiary() throws IOException, ResourceNotFoundException, InvalidInputException {
        jaxMetaInfo.setCountryId(new BigDecimal(91));
        jaxMetaInfo.setCompanyId(new BigDecimal(1));
        jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
        jaxMetaInfo.setCustomerId(new BigDecimal(5218));
        BigDecimal placeOrderId = new BigDecimal(88041);
        ApiResponse<RemittancePageDto> response = null;
        response = client.poBeneficiary(placeOrderId);
        assertNotNull("Response is null", response);
        assertNotNull(response.getResult());
        assertNotNull(response.getResult().getModelType());
    }
    
    @Test
  	@SuppressWarnings("rawtypes")	
  	public void testGetBenCountryList() {
  		setDefaults();
  		ApiResponse response = null;
  		response = client.getBeneficiaryCountryList();
  		assertNotNull("Response is null", response);
  		assertNotNull("result is null", response.getResult());
  	}
}
