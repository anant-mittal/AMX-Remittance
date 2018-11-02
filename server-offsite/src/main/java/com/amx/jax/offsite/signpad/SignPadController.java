package com.amx.jax.offsite.signpad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.device.ApiDeviceHeaders;
import com.amx.jax.offsite.device.DeviceRequest;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "SignPad APIs")
@ApiStatusService(IDeviceService.class)
public class SignPadController {

	@Autowired
	private DeviceClient deviceClient;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@ApiDeviceHeaders
	@RequestMapping(value = { SingPadConstants.Path.DEVICE_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		deviceRequestValidator.validateRequest();
		return deviceClient.getStatus(ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken());
	}

}
