package com.amx.jax.signpad;

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
import com.amx.jax.client.DeviceStateClient;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.constant.DeviceState;
import com.amx.jax.device.DeviceData;
import com.amx.jax.device.DeviceRequest;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.http.RequestType;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.terminal.TerminalBox;
import com.amx.jax.terminal.TerminalService;
import com.amx.jax.terminal.TerminalConstants.Path;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.rbaac.RbaacServiceClient;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceHeaders;
import com.amx.jax.sso.server.ApiHeaderAnnotations.ApiDeviceSessionHeaders;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.TimeUtils;

import io.swagger.annotations.Api;

@RestController
@Api(value = "SignPad APIs")
@ApiStatusService(IDeviceStateService.class)
public class SignPadController {

	@Autowired
	private RbaacServiceClient rbaacServiceClient;

	@Autowired
	private DeviceStateClient deviceStateClient;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@Autowired
	private SignPadBox signPadBox;

	@Autowired
	private TerminalBox terminalBox;

	@Autowired
	TerminalService terminalService;

	@Autowired(required = false)
	private SSOUser sSOUser;

	@ApiRequest(type = RequestType.POLL)
	@ApiDeviceSessionHeaders
	@RequestMapping(value = { Path.SIGNPAD_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		DeviceData deviceData = deviceRequestValidator.validateRequest();

		AmxApiResponse<DeviceStatusInfoDto, Object> defaultRespo = AmxApiResponse.build(new DeviceStatusInfoDto());

		TerminalData terminalData = terminalBox.getOrDefault(deviceData.getTerminalId());
		SignPadData signPadData = signPadBox.getOrDefault(deviceData.getTerminalId());

		boolean isTerminalUpdated = signPadData.getUpdatestamp() < terminalData.getUpdatestamp();
		boolean isSignPadDataStaled = signPadData.getChangeStamp() < terminalData.getStartStamp();

		if (ArgUtil.isEmpty(signPadData)
				|| ArgUtil.isEmpty(signPadData.getDeviceState())
				|| isTerminalUpdated
				|| deviceData.getUpdatestamp() > signPadData.getUpdatestamp()) {

			DevicePairOtpResponse devAuthResp = rbaacServiceClient.validateDeviceSessionToken(
					ArgUtil.parseAsBigDecimal(deviceRequestValidator.getDeviceRegId()),
					deviceRequestValidator.getDeviceSessionToken()).getResult();

			if (!ArgUtil.isEmpty(devAuthResp)) {
				signPadData.setDeviceState(devAuthResp.getDeviceState());
				if (devAuthResp.getDeviceState() == DeviceState.SESSION_PAIRED) {
					AmxApiResponse<DeviceStatusInfoDto, Object> devResp = deviceStateClient.getStatus(
							ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
							deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken());
					if (!ArgUtil.isEmpty(devResp) && !ArgUtil.isEmpty(devResp.getResult())) {
						signPadData.setStateData(devResp.getResult());
						signPadData.setUpdatestamp(terminalData.getUpdatestamp());
					}
				}
				signPadBox.fastPut(deviceData.getTerminalId(), signPadData);
			}
		}

		boolean isSuccessTimeout = (Constants.Common.SUCCESS.equalsIgnoreCase(terminalData.getStatus())
				&& TimeUtils.isDead(terminalData.getChangestamp(), 5000));

		if (signPadData.getDeviceState() != DeviceState.SESSION_PAIRED

				|| (!"SIGNATURE".equalsIgnoreCase(terminalData.getStatus())
						&& !Constants.Common.SUCCESS.equalsIgnoreCase(terminalData.getStatus()))

				|| TimeUtils.isDead(terminalData.getLivestamp(), 15000)

				|| isSuccessTimeout

				|| terminalData.getUpdatestamp() < terminalData.getStartStamp()

				|| ArgUtil.isEmpty(signPadData.getStateData())
				|| ArgUtil.isEmpty(signPadData.getStateData().getStateDataType())

		// || TimeUtils.isDead(terminalData.getChangestamp(), 180000)

//				|| (!ArgUtil.isEmpty(signPadData.getStateData())
//						&& TimeUtils.isDead(signPadData.getStateData().getLastUpdatedTime().getTime(), 180000))

		) {

			defaultRespo.setStatusKey(
					ArgUtil.parseAsString(signPadData.getDeviceState()));
			// defaultRespo.getResult().setStateDataType(null);

		} else { // DeviceState : SESSION_PAIRED

			String actualStatus = signPadData.getStateData().getStateDataType().toString()
					+ (ArgUtil.isEmpty(terminalData.getStatus()) ? Constants.BLANK
							: ("_" + terminalData.getStatus()));
			defaultRespo.setStatusKey(actualStatus);
			defaultRespo.setResult(signPadData.getStateData());

		}

		// System.out.println("TerminalStatus"+terminalData.getStatus());

		if ((isSuccessTimeout || isSignPadDataStaled)
				&& !ArgUtil.isEmpty(signPadData.getStateData())
				&& !ArgUtil.isEmpty(signPadData.getStateData().getStateDataType())) {
			deviceStateClient.clearDeviceState(ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
					deviceRequestValidator.getDeviceRegToken(),
					deviceRequestValidator.getDeviceSessionToken());
			signPadData.setSignature(null);
			signPadData.setStateData(new DeviceStatusInfoDto());
			signPadBox.fastPut(deviceData.getTerminalId(), signPadData);
		} else if (isSignPadDataStaled && !ArgUtil.isEmpty(signPadData.getStateData())) {
			signPadData.setSignature(null);
			signPadBox.fastPut(deviceData.getTerminalId(), signPadData);
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

		return deviceStateClient.updateSignatureStateData(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				file.getData());
	}

	@ApiRequest(type = RequestType.POLL)
	@ApiDeviceHeaders
	@RequestMapping(value = Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.GET },
			produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getSignatureStateData(HttpServletResponse response)
			throws ParseException, IOException {

		String terminalId = null;

		if (!ArgUtil.isEmpty(sSOUser) && sSOUser.isAuthDone() && !ArgUtil.isEmpty(sSOUser.getUserClient())) {
			terminalId = ArgUtil.parseAsString(sSOUser.getUserClient().getTerminalId());
		} else {
			DeviceData deviceData = deviceRequestValidator.getDeviceData();
			terminalId = deviceData.getTerminalId();
		}

		SignPadData signPadData = signPadBox.getOrDefault(terminalId);
		if (ArgUtil.isEmpty(signPadData.getSignature())) {
			return ResponseEntity.noContent().build();
		}

		String sourceData = signPadData.getSignature().getData();

		File file = File.fromBase64(sourceData, Type.PNG);
		file.setName(signPadData.getSignature().getName());
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

}
