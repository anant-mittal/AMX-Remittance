package com.amx.jax.offsite.device;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceData;
import com.amx.jax.device.NotipyBox;
import com.amx.jax.device.NotipyData;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Card Reader APIs")
@ApiStatusService(IDeviceStateService.class)
public class NotipyController {

	private static final Logger LOGGER = LoggerService.getLogger(NotipyController.class);

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private RbaacServiceClient rbaacServiceClient;

	@Autowired
	private NotipyBox notipyBox;

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_NOTIPY }, method = { RequestMethod.GET })
	public AmxApiResponse<NotipyData, Object> getCardDetails()
			throws InterruptedException {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		NotipyData notipyData = notipyBox.getOrDefault(deviceData.getEmpId());

		if (notipyData.getUpdatestamp() > deviceData.getCheckstamp()) {
			DevicePairOtpResponse devAuthResp = rbaacServiceClient.validateDeviceSessionToken(
					ArgUtil.parseAsBigDecimal(deviceRequestValidator.getDeviceRegId()),
					deviceRequestValidator.getDeviceSessionToken()).getResult();
			deviceRequestValidator.checkStamp(deviceRequestValidator.getDeviceRegId());
		}
		return AmxApiResponse.build(notipyData);
	}

}
