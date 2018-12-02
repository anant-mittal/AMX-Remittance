package com.amx.jax.offsite.device;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.adapter.ICardService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.CardData;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Card Reader APIs")
@ApiStatusService(IDeviceStateService.class)
public class AuthAppController {

	private static final Logger LOGGER = LoggerService.getLogger(AuthAppController.class);

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private RbaacServiceClient rbaacServiceClient;

	public static class AuthAppData {
		private String sac;

		public String getSac() {
			return sac;
		}

		public void setSac(String sac) {
			this.sac = sac;
		}
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_AUTHAPP }, method = { RequestMethod.GET })
	public AmxApiResponse<AuthAppData, Object> getCardDetails()
			throws InterruptedException {
		deviceRequestValidator.validateDevice();
		String deviceRegId = deviceRequestValidator.getDeviceRegId();
		String deviceRegToken = deviceRequestValidator.getDeviceRegToken();

		DevicePairOtpResponse resp = rbaacServiceClient
				.createDeviceSession(ArgUtil.parseAsInteger(deviceRegId), deviceRegToken)
				.getResult();
		SessionPairingCreds creds = deviceRequestValidator.createSession(resp.getSessionPairToken(), resp.getOtp(),
				resp.getTermialId());

		return AmxApiResponse.build();
	}

}
