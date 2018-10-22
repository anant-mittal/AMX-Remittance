package com.amx.jax.offsite.device;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.offsite.OffsiteMvcConfig.CardBox;
import com.amx.jax.offsite.device.DevicePairingModels.DeviceReqResp;
import com.amx.jax.offsite.device.DevicePairingModels.PairingRequest;
import com.amx.jax.offsite.device.DevicePairingModels.PairingResponse;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Device APIs")
public class DeviceController {

	private static final Logger LOGGER = LoggerService.getLogger(DeviceController.class);

	@Autowired
	CardBox cardBox;

	@RequestMapping(value = { DeviceConstants.DEVICE_INFO_URL }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader,
			@PathVariable(value = DeviceConstants.PARAM_SYSTEM_ID) String systemid) {
		if (ArgUtil.isEmpty(reader.getData())) {
			cardBox.fastRemove(systemid);
		} else {
			cardBox.put(systemid, reader.getData());
		}
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.DEVICE_INFO_URL }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@PathVariable(value = DeviceConstants.PARAM_SYSTEM_ID) String systemid,
			@RequestParam(required = false) Boolean wait, @RequestParam(required = false) Boolean flush)
			throws InterruptedException {
		wait = ArgUtil.parseAsBoolean(wait, Boolean.FALSE);
		flush = ArgUtil.parseAsBoolean(flush, Boolean.FALSE);
		CardData data = null;
		if (wait) {
			data = cardBox.take(systemid, 15, TimeUnit.SECONDS);
		} else {
			data = cardBox.get(systemid);
		}

		if (flush) {
			cardBox.fastRemove(systemid);
		}
		return AmxApiResponse.build(data);
	}

	@RequestMapping(value = { DeviceConstants.DEVICE_PAIR }, method = { RequestMethod.POST })
	public AmxApiResponse<PairingResponse, Object> pairDevice(@RequestBody PairingRequest req) {
		return AmxApiResponse.build(new DeviceReqResp());
	}

}
