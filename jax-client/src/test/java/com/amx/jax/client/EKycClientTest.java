package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.amxlib.exception.InvalidInputException;
import com.amx.amxlib.exception.LimitExeededException;
import com.amx.amxlib.exception.RemittanceTransactionValidationException;
import com.amx.amxlib.exception.ResourceNotFoundException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.constants.JaxChannel;
import com.amx.jax.dict.Tenant;
import com.amx.jax.model.request.ImageSubmissionRequest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EKycClientTest extends AbstractTestClient{
	private static final Logger LOGGER = Logger.getLogger(EKycClientTest.class);

	@Autowired
	private JaxMetaInfo jaxMetaInfo;

	@Autowired
	EKycClient client;
	
	@Test
	public void eKycSaveDetails() throws IOException, ResourceNotFoundException,
	InvalidInputException, RemittanceTransactionValidationException, LimitExeededException {
		jaxMetaInfo.setDeviceId("301019967");
		jaxMetaInfo.setCountryId(new BigDecimal(91));
		jaxMetaInfo.setCompanyId(new BigDecimal(1));
		jaxMetaInfo.setCountryBranchId(new BigDecimal(78));
		jaxMetaInfo.setCustomerId(new BigDecimal(5218));
		jaxMetaInfo.setChannel(JaxChannel.ONLINE);
		jaxMetaInfo.setTenant(Tenant.KWT);
		jaxMetaInfo.setLanguageId(new BigDecimal(1));
		jaxMetaInfo.setEmployeeId(new BigDecimal(265));
		BoolRespModel response = null;
		ImageSubmissionRequest imageSubmissionRequest = new ImageSubmissionRequest();
		ArrayList<String> imageList=new ArrayList<String>();
		imageList.add("abcde");
		imageSubmissionRequest.setImage(imageList);
		Date date = new Date();
		imageSubmissionRequest.setIdentityExpiredDate(date);
		imageSubmissionRequest.setPoliticallyExposed("Y");
		response = client.eKycsaveDetails(imageSubmissionRequest);
		LOGGER.debug("Response is "+response);
		assertNotNull("Response is null", response);
		assertNotNull(response);
	}
}
