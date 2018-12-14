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
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceMetaInfo;
import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.device.DeviceRestModels.DevicePairingRequest;
import com.amx.jax.device.DeviceRestModels.NetAddress;
import com.amx.jax.device.DeviceRestModels.SessionPairingCreds;
import com.amx.jax.dict.UserClient;
import com.amx.jax.exception.AmxApiException;
import com.amx.jax.rest.RestService;

@Configuration
@PropertySource("classpath:application-adapter.properties")
public class DeviceConnectorClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeviceConnectorClient.class);

	@Value("${jax.offsite.url}")
	String offSiteUrl;

	public String getOffSiteUrl() {
		return offSiteUrl;
	}

	public void setOffSiteUrl(String offSiteUrl) {
		this.offSiteUrl = offSiteUrl;
	}

	@Autowired
	RestService restService;

	@Autowired(required = false)
	IDeviceConnecter iCardService;

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

	public Object saveCardDetailsByTerminal(String terminalId, CardReader reader, NetAddress address,
			DevicePairingCreds devicePairingCreds, SessionPairingCreds sessionPairingCreds) throws Exception {

		try {
			return restService.ajax(offSiteUrl)
					.meta(new DeviceMetaInfo())
					.path(DeviceConstants.Path.DEVICE_STATUS_CARD)
					.pathParam(DeviceConstants.Params.PARAM_SYSTEM_ID, terminalId)
					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
					.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
					.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())
					.header(
							DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY,
							sessionPairingCreds.getDeviceSessionToken())
					.header(
							DeviceConstants.Keys.CLIENT_REQ_TOKEN_XKEY,
							DeviceConstants.generateDeviceReqToken(sessionPairingCreds, devicePairingCreds))
					.post(reader).asObject();
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.DEVICE_STATUS_CARD, e);
			return AmxApiException.evaluate(e);
		}
	}

	public AmxApiResponse<DevicePairingCreds, Object> pairDevice(NetAddress address, DevicePairingRequest req)
			throws Exception {
		try {
			return restService.ajax(offSiteUrl)
					.meta(new DeviceMetaInfo())
					.path(DeviceConstants.Path.DEVICE_PAIR)
					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp()).post(req)
					.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairingCreds, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.DEVICE_PAIR, e);
			return AmxApiException.evaluate(e);
		}
	}

	public AmxApiResponse<DevicePairingCreds, Object> deActivateDevice(DevicePairingCreds req)
			throws Exception {
		try {
			return restService.ajax(offSiteUrl)
					.meta(new DeviceMetaInfo())
					.path(DeviceConstants.Path.DEVICE_DEACTIVATE)
					.queryParam(DeviceConstants.Params.PARAM_DEVICE_REG_ID, req.getDeviceRegId())
					.queryParam(DeviceConstants.Params.PARAM_DEVICE_TYPE, UserClient.ClientType.BRANCH_ADAPTER).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairingCreds, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.DEVICE_DEACTIVATE, e);
			return AmxApiException.evaluate(e);
		}
	}

	public AmxApiResponse<SessionPairingCreds, Object> createSession(NetAddress address,
			DevicePairingCreds devicePairingCreds)
			throws Exception {
		try {
			return restService.ajax(offSiteUrl)
					.meta(new DeviceMetaInfo())
					.path(DeviceConstants.Path.SESSION_CREATE)
					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())
					.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
					.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())
					.get().as(new ParameterizedTypeReference<AmxApiResponse<SessionPairingCreds, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.SESSION_CREATE, e);
			return AmxApiException.evaluate(e);
		}
	}

	public AmxApiResponse<Object, Object> pairTerminal(NetAddress address, DevicePairingCreds devicePairingCreds,
			SessionPairingCreds sessionPairingCreds, String tranxId) throws Exception {
		try {
			return restService.ajax(offSiteUrl).path(DeviceConstants.Path.SESSION_TERMINAL)
					.header(AppConstants.TRANX_ID_XKEY, tranxId)

					.header(AppConstants.DEVICE_ID_XKEY, address.getMac())
					.header(AppConstants.DEVICE_IP_LOCAL_XKEY, address.getLocalIp())

					.header(DeviceConstants.Keys.CLIENT_REG_KEY_XKEY, devicePairingCreds.getDeviceRegId())
					.header(DeviceConstants.Keys.CLIENT_REG_TOKEN_XKEY, devicePairingCreds.getDeviceRegToken())

					.header(DeviceConstants.Keys.CLIENT_SESSION_TOKEN_XKEY, sessionPairingCreds.getDeviceSessionToken())
					.header(DeviceConstants.Keys.DEVICE_REQ_TIME_XKEY, System.currentTimeMillis())
					.header(DeviceConstants.Keys.CLIENT_REQ_TOKEN_XKEY,
							DeviceConstants.generateDeviceReqToken(sessionPairingCreds, devicePairingCreds))
					.get().as(new ParameterizedTypeReference<AmxApiResponse<Object, Object>>() {
					});
		} catch (Exception e) {
			LOGGER.error(DeviceConstants.Path.SESSION_TERMINAL, e);
			return AmxApiException.evaluate(e);
		}
	}

	public void sendSACtoEmployee(String employeeId, String sac) {
		if (iCardService != null) {
			iCardService.sendSACtoEmployee(employeeId, sac);
		}
	}
}
