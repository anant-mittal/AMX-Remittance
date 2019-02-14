package com.amx.jax.offsite.device;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.adapter.DeviceConnectorClient;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceData;
import com.amx.jax.device.DeviceRequest;
import com.amx.jax.device.NotipyBox;
import com.amx.jax.device.NotipyData;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.NotpDTO;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceSessionHeaders;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Random;

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

	@Autowired
	DeviceConnectorClient adapterServiceClient;

	@ApiDeviceSessionHeaders
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_NOTIPY }, method = { RequestMethod.GET })
	public AmxApiResponse<NotipyData, Object> getNotipyStatus()
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

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_SEND_NOTIPY }, method = { RequestMethod.GET })
	public AmxApiResponse<NotipyData, Object> sendNotipy(@RequestParam String empId)
			throws InterruptedException {
		adapterServiceClient.sendSACtoEmployee(empId, Random.randomAlphaNumeric(6));
		NotipyData notipyData = notipyBox.getOrDefault(empId);
		return AmxApiResponse.build(notipyData);
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_VERIFY_NOTIPY }, method = { RequestMethod.GET })
	public AmxApiResponse<NotpDTO, Object> verifyNotipy(@RequestParam BigDecimal empId, @RequestParam String sac,
			@RequestParam String otp)
			throws InterruptedException {
		NotpDTO notpDto = new NotpDTO();
		notpDto.setEmployeeId(empId);
		notpDto.setSac(sac);
		notpDto.setOtp(otp);
		return rbaacServiceClient.verifyOTP(notpDto);
	}

}
