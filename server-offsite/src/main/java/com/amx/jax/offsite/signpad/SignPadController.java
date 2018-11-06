package com.amx.jax.offsite.signpad;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.api.FileSubmitRequestModel;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.device.ApiDeviceHeaders;
import com.amx.jax.offsite.device.DeviceConfigs.DeviceData;
import com.amx.jax.offsite.device.DeviceRequest;
import com.amx.jax.postman.model.File;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "SignPad APIs")
@ApiStatusService(IDeviceService.class)
public class SignPadController {

	@Autowired
	private DeviceClient deviceClient;

	@Autowired
	private DeviceRequest deviceRequestValidator;

	@ApiDeviceHeaders
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		deviceRequestValidator.validateRequest();
		return deviceClient.getStatus(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken()
		);
	}

	@ApiOperation("To update the status of Remitance")
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_REMIT }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo
	) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateRemittanceState(
				ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, employeeId
		);
	}

	@ApiOperation("To update the status of FC_PURCHASE")
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_FCPURCHASE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo
	) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateFcPurchase(
				ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, employeeId
		);
	}

	@ApiOperation("To update the status of FC_SALE")
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_FCSALE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo
	) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateFcSale(
				ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, employeeId
		);
	}

	@ApiOperation("To update the status of Customer Registration")
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_CUST_REG }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadRemittanceInfo
	) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateCustomerRegStateData(
				ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, employeeId
		);
	}

	@ApiDeviceHeaders
	@RequestMapping(value = SingPadConstants.Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateSignatureStateData(@RequestBody FileSubmitRequestModel file)
			throws ParseException {
		//DeviceData deviceData = deviceRequestValidator.getDeviceData();
		DeviceData deviceData = deviceRequestValidator.validateRequest();
		deviceData.setSignature(file);
		deviceRequestValidator.save();
		return deviceClient.updateSignatureStateData(
				ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()), file.getData()
		);
	}

	@ApiDeviceHeaders
	@RequestMapping(
			value = SingPadConstants.Path.SIGNPAD_STATUS_SIGNATURE, method = { RequestMethod.GET,
			}, produces = MediaType.IMAGE_JPEG_VALUE
	)
	public ResponseEntity<byte[]> getSignatureStateData(HttpServletResponse response)
			throws ParseException, IOException {
		DeviceData deviceData = deviceRequestValidator.getDeviceData();
		String sourceData = deviceData.getSignature().getData();
		File file = File.fromBase64(sourceData);
		file.setName(deviceData.getSignature().getName());
		return ResponseEntity.ok().contentLength(file.getBody().length)
				.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
	}

}
