package com.amx.jax.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeviceStateClientTest extends AbstractClientTest {

	@Autowired
	DeviceStateClient client;

	@Test
	public void testRegisterDevice() {
		setDefaults();
		/*AmxApiResponse<DeviceDto, Object> response = null;
		DeviceRegistrationRequest request = new DeviceRegistrationRequest();
		request.setBranchSystemIp("192.178.0.1");
		request.setDeviceId("did");
		request.setDeviceType(ClientType.SIGNATURE_PAD);
		//response = client.registerNewDevice(request);
		assertNotNull("Response is null", response);
		assertNotNull(response.getResult());*/
	}
}
