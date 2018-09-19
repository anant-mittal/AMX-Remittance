package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.response.ArticleDetailsDescDto;
import com.amx.jax.model.response.ArticleMasterDescDto;
import com.amx.jax.model.response.ComponentDataDto;
import com.amx.jax.model.response.IncomeRangeDto;
import com.amx.utils.JsonUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OffsiteCustRegClientTest extends AbstractClientTest {

	@Autowired
	OffsiteCustRegClient offsiteClient;

	// @Test
	public void testEmploymentTypeList() {
		setDefaults();
		AmxApiResponse<ComponentDataDto, Object> response = null;
		response = offsiteClient.sendEmploymentTypeList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	@Test
	public void testIdTypeList() {
		setDefaults();
		AmxApiResponse<ComponentDataDto, Object> response = null;
		response = offsiteClient.sendIdTypes();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	// @Test
	public void testProfessionList() {
		setDefaults();
		AmxApiResponse<ComponentDataDto, Object> response = null;
		response = offsiteClient.sendProfessionList();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}

	//@Test
	public void testArticleList() {
		setDefaults();
		AmxApiResponse<ArticleMasterDescDto, Object> response = null;
		response = offsiteClient.getArticleListResponse();
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void testDesignationList() {
		setDefaults();
		EmploymentDetailsRequest model = new EmploymentDetailsRequest();
		model.setArticleId(new BigDecimal(1));
		AmxApiResponse<ArticleDetailsDescDto, Object> response = null;
		response = offsiteClient.getDesignationListResponse(model);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void testIncomeRangeList() {
		setDefaults();
		EmploymentDetailsRequest model = new EmploymentDetailsRequest();
		model.setArticleDetailsId(new BigDecimal(2));
		AmxApiResponse<IncomeRangeDto, Object> response = null;
		response = offsiteClient.getIncomeRangeResponse(model);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
	
	//@Test
	public void testSendOTPForMobileAndEmail()  throws URISyntaxException, IOException {
			setDefaults();		
			/*String json = new String(
					Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("customer/person-detail.json").toURI())));
			CustomerPersonalDetail model = JsonUtil.fromJson(json, CustomerPersonalDetail.class);*/
			CustomerPersonalDetail model = new CustomerPersonalDetail();
			model.setCountryId(new BigDecimal(91));
			model.setEmail("efewdf@gmail.com");
			model.setFirstName("fdsgd");
			model.setIdentityInt("287052707076");
			model.setLastName("gdssedf");
			model.setMobile("98456123");
			model.setNationalityId(new BigDecimal(91));
			model.setTelPrefix("965");
			model.setTitle("181");
			AmxApiResponse<List, Object> response = null;
			response = offsiteClient.sendOtpForEmailAndMobile(model);
			assertNotNull("Response is null", response);
			assertNotNull(response.getResult());
		}

}
