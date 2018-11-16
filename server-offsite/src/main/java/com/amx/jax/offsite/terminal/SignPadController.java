package com.amx.jax.offsite.terminal;

import java.io.IOException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.exception.ApiHttpExceptions.ApiStatusCodes;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.device.ApiDeviceHeaders;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.jax.offsite.device.DeviceConfigs.SignPadBox;
import com.amx.jax.offsite.device.DeviceConfigs.SignPadData;
import com.amx.jax.offsite.device.DeviceConfigs.TerminalBox;
import com.amx.jax.offsite.device.DeviceConfigs.TerminalData;
import com.amx.jax.offsite.device.DeviceRequest;
import com.amx.jax.offsite.terminal.TerminalConstants.Path;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

import io.swagger.annotations.Api;

@RestController
@Api(value = "SignPad APIs")
@ApiStatusService(IDeviceService.class)
public class SignPadController {

	@Autowired
	private DeviceClient deviceClient;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private SignPadBox signPadBox;

	@Autowired
	private TerminalBox terminalBox;

	@ApiRequest(type = RequestType.POLL)
	@ApiDeviceHeaders
	@RequestMapping(value = { Path.SIGNPAD_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		DeviceData deviceData = deviceRequestValidator.validateRequest();

		AmxApiResponse<DeviceStatusInfoDto, Object> defaultRespo = AmxApiResponse.build(new DeviceStatusInfoDto());

		TerminalData terminalData = terminalBox.getOrDefault(deviceData.getTerminalId());
		SignPadData signPadData = signPadBox.getOrDefault(deviceData.getTerminalId());
		defaultRespo.setStatusEnum(ApiStatusCodes.NO_STATUS);

		if (!ArgUtil.isEmpty(signPadData)) {
			defaultRespo.getResult().setDeviceState(signPadData.getDeviceState());
		}

		if (TimeUtils.isDead(terminalData.getLivestamp(), 15000)) {
			return defaultRespo;
		} else if (Constants.Common.SUCCESS.equalsIgnoreCase(terminalData.getStatus())
				&& TimeUtils.isDead(terminalData.getChangestamp(), 5000)) {
			return defaultRespo;
		}

		AmxApiResponse<DeviceStatusInfoDto, Object> devResp = deviceClient.getStatus(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken());

		if (!ArgUtil.isEmpty(devResp) && !ArgUtil.isEmpty(devResp.getResult())
				&& !ArgUtil.isEmpty(devResp.getResult().getStateDataType())) {
			String actualStatus = devResp.getResult().getStateDataType().toString()
					+ (ArgUtil.isEmpty(terminalData.getStatus()) ? Constants.BLANK : terminalData.getStatus());

			defaultRespo.setStatusKey(actualStatus);
			defaultRespo.addResult(devResp.getResult());
			/// data.getBranchPcLastLogoutTime()
		}

		return defaultRespo;
	}

	@ApiDeviceHeaders
	@RequestMapping(value = Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(@RequestBody FileSubmitRequestModel file)
			throws ParseException {
		// DeviceData deviceData = deviceRequestValidator.getDeviceData();
		DeviceData deviceData = deviceRequestValidator.validateRequest();

		SignPadData signPadData = signPadBox.getOrDefault(deviceData.getTerminalId());
		signPadData.setSignature(file);
		signPadBox.fastPut(deviceData.getTerminalId(), signPadData);

		return deviceClient.updateSignatureStateData(ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				file.getData());
	}

	@ApiDeviceHeaders
	@RequestMapping(value = Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.GET, },
			produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getSignatureStateData(HttpServletResponse response)
			throws ParseException, IOException {
		DeviceData deviceData = deviceRequestValidator.getDeviceData();

		SignPadData signPadData = signPadBox.getOrDefault(deviceData.getTerminalId());
		String sourceData = signPadData.getSignature().getData();

		File file = File.fromBase64(sourceData, Type.PNG);
		file.setName(signPadData.getSignature().getName());
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

}
