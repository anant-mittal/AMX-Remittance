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
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.device.ApiDeviceHeaders;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.jax.offsite.device.DeviceRequest;
import com.amx.jax.offsite.terminal.TerminalConstants.Path;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
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
	@RequestMapping(value = { Path.SIGNPAD_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		deviceRequestValidator.validateRequest();
		AmxApiResponse<DeviceStatusInfoDto, Object> devResp = deviceClient.getStatus(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken()
		);

		if (!ArgUtil.isEmpty(devResp) && !ArgUtil.isEmpty(devResp.getResult())) {
			DeviceStatusInfoDto data = devResp.getResult();
			/// data.getBranchPcLastLogoutTime()

		}
		return devResp;
	}

	@ApiDeviceHeaders
	@RequestMapping(value = Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(@RequestBody FileSubmitRequestModel file)
			throws ParseException {
		// DeviceData deviceData = deviceRequestValidator.getDeviceData();
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		deviceData.setSignature(file);
		deviceRequestValidator.save();
		return deviceClient.updateSignatureStateData(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()), file.getData()
		);
	}

	@ApiDeviceHeaders
	@RequestMapping(
			value = Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.GET,
			}, produces = MediaType.IMAGE_PNG_VALUE
	)
	public ResponseEntity<byte[]> getSignatureStateData(HttpServletResponse response)
			throws ParseException, IOException {
		DeviceData deviceData = deviceRequestValidator.getDeviceData();
		String sourceData = deviceData.getSignature().getData();
		File file = File.fromBase64(sourceData, Type.PNG);
		file.setName(deviceData.getSignature().getName());
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

}
