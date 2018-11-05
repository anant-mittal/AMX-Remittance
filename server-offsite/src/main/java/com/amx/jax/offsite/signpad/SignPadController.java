package com.amx.jax.offsite.signpad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.DeviceClient;
import com.amx.jax.client.IDeviceService;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.model.response.DeviceStatusInfoDto;
import com.amx.jax.offsite.device.ApiDeviceHeaders;
import com.amx.jax.offsite.device.DeviceRequest;
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
	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_ACTIVITY }, method = { RequestMethod.GET })
	public AmxApiResponse<DeviceStatusInfoDto, Object> getStatus() {
		deviceRequestValidator.validateRequest();
		return deviceClient.getStatus(ArgUtil.parseAsInteger(deviceRequestValidator.getDeviceRegId()),
				deviceRequestValidator.getDeviceRegToken(), deviceRequestValidator.getDeviceSessionToken());
	}

	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_REMIT }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateRemittanceState(ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, null);
	}

	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_FCPURCHASE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(@RequestParam Integer countryBranchSystemInventoryId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateFcPurchase(ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, null);
	}

	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_FCSALE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(@RequestParam Integer countryBranchSystemInventoryId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateFcSale(ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, null);
	}

	@RequestMapping(value = { SingPadConstants.Path.SIGNPAD_STATUS_CUST_REG }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			@RequestParam Integer countryBranchSystemInventoryId,
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadRemittanceInfo) {
		deviceRequestValidator.validateRequest();
		return deviceClient.updateCustomerRegStateData(ClientType.SIGNATURE_PAD, countryBranchSystemInventoryId,
				signaturePadRemittanceInfo, null);
	}

}
