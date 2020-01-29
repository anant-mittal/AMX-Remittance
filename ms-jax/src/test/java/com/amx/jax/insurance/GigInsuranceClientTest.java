package com.amx.jax.insurance;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.AbstractTest;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.insurance.GigInsuranceClient;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GigInsuranceClientTest extends AbstractTest {

	@Autowired
	GigInsuranceClient client;

	@Test
	public void testFetchInsuranceDetail() {
		setDefaults();
		AmxApiResponse<GigInsuranceDetail, Object> resp = client.fetchInsuranceDetail();
		assertNotNull(resp.getResult());
	}
}
