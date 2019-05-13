package com.amx.jax.offsite;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.model.response.customer.AddressProofDTO;
import com.amx.jax.scope.TenantContextHolder;

import ch.qos.logback.classic.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffsiteCustRegClientTest {
	private static final Logger LOGGER = (Logger) LoggerService.getLogger(OffsiteCustRegClientTest.class);
	@Autowired
	OffsiteCustRegClient offsiteClient;

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	
	//@Test
	public void testForDesignationList() {
		setDefaults();
		AmxApiResponse<ResourceDTO, Object> response = null;
		LOGGER.debug("Response not set");
		response = offsiteClient.getDesignationList();
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	protected void setDefaults() {
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(589));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		jaxMetaInfo.setTenant(Tenant.BHR);
		TenantContextHolder.setCurrent(Tenant.BHR);
		jaxMetaInfo.setReferrer("DEV-TESTING");
	}
	//@Test
	public void testForAddressProof() {
		setDefaults();
		AmxApiResponse<AddressProofDTO, Object> response = null;
		LOGGER.debug("Response not set");
		response = offsiteClient.getAddressProof();
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	@Test
	public void testForsaveUploadReference() throws Exception {
		//setDefaults();
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(589));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		jaxMetaInfo.setTenant(Tenant.BHR);
		TenantContextHolder.setCurrent(Tenant.BHR);
		jaxMetaInfo.setReferrer("DEV-TESTING");
		BoolRespModel response = null;
		LOGGER.debug("Response not set");
		ImageSubmissionRequest imageSubmissionRequest = new ImageSubmissionRequest();
		List<String> image = new ArrayList<String>();
		image.add("abc");
		imageSubmissionRequest.setDocumentUploadReferenceId(new BigDecimal(1));
		imageSubmissionRequest.setIdentityExpiredDate(new Date());
		imageSubmissionRequest.setImage(image);
		imageSubmissionRequest.setPoliticallyExposed("Y");
		imageSubmissionRequest.setUploadType("ADDR");
		
		response = offsiteClient.saveDocumentUploadReference(imageSubmissionRequest);
		LOGGER.debug("Response is set");
		assertNotNull("Response is null", response);
		
	}
}
