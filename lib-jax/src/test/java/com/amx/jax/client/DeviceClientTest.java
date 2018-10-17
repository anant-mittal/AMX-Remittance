package com.amx.jax.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.model.request.DeviceRegistrationRequest;
import com.amx.jax.model.response.DeviceDto;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceClientTest extends AbstractClientTest {

	@Autowired
	DeviceClient client;

	@Test
	public void testRegisterDevice() {
		setDefaults();
		AmxApiResponse<DeviceDto, Object> response = null;
		DeviceRegistrationRequest request = new DeviceRegistrationRequest();
		request.setBranchSystemIp("192.178.0.1");
		request.setCountryBranchId(78);
		request.setDeviceId("did");
		request.setDeviceType("tab");
		response = client.registerNewDevice(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());
	}
}
