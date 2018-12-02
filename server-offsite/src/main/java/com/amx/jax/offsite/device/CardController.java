package com.amx.jax.offsite.device;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.adapter.IDeviceConnecter;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.CardData;
import com.amx.jax.device.CardReader;
import com.amx.jax.device.DeviceConstants;
import com.amx.jax.device.DeviceData;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Card Reader APIs")
@ApiStatusService(IDeviceStateService.class)
public class CardController {

	private static final Logger LOGGER = LoggerService.getLogger(CardController.class);

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private IDeviceConnecter iCardService;

	@ApiDeviceHeaders
	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.POST })
	public AmxApiResponse<CardData, Object> saveCardDetails(@RequestBody CardReader reader) {
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		iCardService.saveCardDetailsByTerminal(deviceData.getTerminalId(), reader.getData());
		return AmxApiResponse.build(reader.getData());
	}

	@RequestMapping(value = { DeviceConstants.Path.DEVICE_STATUS_CARD }, method = { RequestMethod.GET })
	public AmxApiResponse<CardData, Object> getCardDetails(
			@RequestParam(value = DeviceConstants.Params.PARAM_SYSTEM_ID) String systemid,
			@RequestParam(required = false) Boolean wait, @RequestParam(required = false) Boolean flush)
			throws InterruptedException {
		wait = ArgUtil.parseAsBoolean(wait, Boolean.FALSE);
		flush = ArgUtil.parseAsBoolean(flush, Boolean.FALSE);

		CardData data = iCardService.getCardDetailsByTerminal(systemid, wait, flush);

		return AmxApiResponse.build(data);
	}

}
