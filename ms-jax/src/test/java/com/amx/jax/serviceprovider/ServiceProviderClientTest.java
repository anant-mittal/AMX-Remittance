package com.amx.jax.serviceprovider;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.ServiceProviderClient;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.response.serviceprovider.ServiceProviderDefaultDateDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderPartnerDTO;
import com.amx.jax.response.serviceprovider.ServiceProviderSummaryDTO;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.jax.serviceprovider.service.ServiceProviderService;

import ch.qos.logback.classic.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ServiceProviderClientTest {
	private static final Logger LOGGER = (Logger) LoggerService.getLogger(ServiceProviderClientTest.class);
	@Autowired
	ServiceProviderClient serviceProviderClient;

	@Autowired
	private MetaData jaxMetaInfo;
	
	@Autowired
	ServiceProviderService serviceProviderService;

	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		jaxMetaInfo.setTenant(Tenant.KWT);
		TenantContextHolder.setCurrent(Tenant.KWT);
		jaxMetaInfo.setReferrer("DEV-TESTING");
	}

	@Test
	public void testForServicePartner() {
		setDefaults();
		AmxApiResponse<ServiceProviderPartnerDTO, Object> response = null;
		LOGGER.debug("Response not set");
		response = serviceProviderClient.getServiceProviderPartner();
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	@Test
	public void testForUploadServiceProviderFile() throws Exception {
		setDefaults();
		List<ServiceProviderSummaryDTO> response = null;
		LOGGER.debug("Response not set");
		Date fileDate = Date.valueOf("2019-7-17");
		response = serviceProviderService.testUploadServiceProviderFile("D:\\income-jax\\amx-jax\\ms-jax\\UploadFile.xlsx", fileDate, "WU");
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.get(0));
	}

	@Test
	public void testForServiceProviderConfirmation() {
		setDefaults();
		AmxApiResponse<BoolRespModel, Object> response = null;
		LOGGER.debug("Response not set");
		Date fileDate = Date.valueOf("2019-7-17");
		response = serviceProviderClient.serviceProviderConfirmation(fileDate, "WU");
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	@Test
	public void testForGetServiceProviderDefaultDate() {
		setDefaults();
		AmxApiResponse<ServiceProviderDefaultDateDTO, Object> response = null;
		LOGGER.debug("Response not set");

		response = serviceProviderClient.getServiceProviderDefaultDate("WU");
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

}
