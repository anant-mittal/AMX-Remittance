package com.amx.jax.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;

import com.amx.jax.AppConstants;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.NetAddress;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.rest.RestService;

@Configuration
@PropertySource("classpath:application-adapter.properties")
public class AdapterServiceClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdapterServiceClient.class);

	@Value("${jax.offsite.url}")
	String offSiteUrl;

	@Autowired
	RestService restService;

	@Autowired(required = false)
	ICardService iCardService;

	public AmxApiResponse<CardData, Object> getCardDetailsByTerminal(String terminalId) throws InterruptedException {
		if (iCardService == null) {
			return restService.ajax(offSiteUrl).path(DeviceConstants.Path.DEVICE_STATUS_CARD)
					.queryParam(DeviceConstants.Params.PARAM_SYSTEM_ID, terminalId).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CardData, Object>>() {
					});
		} else {
			return AmxApiResponse.build(iCardService.getCardDetailsByTerminal(terminalId, false, false));
		}
	}

	public AmxApiResponse<CardData, Object> pollCardDetailsByTerminal(String terminalId) throws InterruptedException {
		if (iCardService == null) {
			return restService.ajax(offSiteUrl).path(DeviceConstants.Path.DEVICE_STATUS_CARD)
					.queryParam(DeviceConstants.Params.PARAM_SYSTEM_ID, terminalId).queryParam("wait", true).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CardData, Object>>() {
					});
		} else {
			return AmxApiResponse.build(iCardService.getCardDetailsByTerminal(terminalId, true, false));
		}
	}

	public AmxApiResponse<CardData, Object> pairTerminal(NetAddress address, DevicePairingCreds devicePairingCreds,
			SessionPairingCreds sessionPairingCreds, String tranxId) throws Exception {
		try {
			return restService.ajax(offSiteUrl).path(DeviceConstants.Path.SESSION_TERMINAL)
					.header(AppConstants.TRANX_ID_XKEY, tranxId)

					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())

					.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
					.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())

					.header(DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY, sessionPairingCreds.getDeviceSessionToken())
					.header(DeviceConstants.Keys.CLIENT_REQ_TOKEN_XKEY,
							DeviceConstants.generateDeviceReqToken(sessionPairingCreds, devicePairingCreds))
					.get().as(new ParameterizedTypeReference<AmxApiResponse<CardData, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.SESSION_TERMINAL, e);
			return AmxApiException.evaluate(e);
		}
	}

}
