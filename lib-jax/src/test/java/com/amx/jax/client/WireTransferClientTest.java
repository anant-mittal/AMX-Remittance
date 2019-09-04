package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WireTransferClientTest extends AbstractClientTest {
	private static final Logger LOGGER = Logger.getLogger(WireTransferClientTest.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	private WireTransferClient wireTransferClient;

	@Test
	public void getPaymentModes() {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<ResourceDTO, Object> response = null;

		response = wireTransferClient.getPaymentModes();
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}

	

	@Test
	public void getWtTrnxList() {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(287932));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.OMN);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<PayAtBranchTrnxListDTO, Object> response = null;

		response = wireTransferClient.getWtTrnxList();
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}

	@Test
	public void getWtTrnxListBranch() {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.OMN);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		AmxApiResponse<PayAtBranchTrnxListDTO, Object> response = null;

		response = wireTransferClient.getWtTrnxListBranch();
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}

}
