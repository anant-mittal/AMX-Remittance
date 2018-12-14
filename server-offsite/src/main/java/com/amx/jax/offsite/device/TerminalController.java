package com.amx.jax.offsite.device;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.DeviceStateClient;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.TerminalBox;
import com.amx.jax.device.TerminalData;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.offsite.device.TerminalConstants.Path;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;
import com.amx.utils.Urly;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "Terminal APIs")
@ApiStatusService(IDeviceStateService.class)
public class TerminalController {

	@Autowired
	private DeviceStateClient deviceClient;

	@Autowired
	private TerminalBox terminalBox;

	@RequestMapping(value = { Path.TERMINAL_STATUS_PING }, method = { RequestMethod.GET })
	public String getPing(@RequestParam String state, @RequestParam String terminalId,
			@RequestParam(required = false) String status, @RequestParam(required = false) Long pageStamp,
			@RequestParam(required = false) Long startStamp,
			Model model,
			HttpServletResponse response, HttpServletRequest request) throws MalformedURLException, URISyntaxException {

		TerminalData terminalData = terminalBox.getOrDefault(terminalId);

		if (ArgUtil.isEmpty(pageStamp)) {
			pageStamp = System.currentTimeMillis();
		}
		if (pageStamp >= terminalData.getPagestamp()) {
			startStamp = ArgUtil.ifNotEmpty(startStamp, terminalData.getStartStamp());
			if (!ArgUtil.areEqual(terminalData.getStatus(), status)
					|| !ArgUtil.areEqual(terminalData.getState(), state)) {
				terminalData.setChangestamp(System.currentTimeMillis());
				if ("START".equalsIgnoreCase(status)) {
					startStamp = System.currentTimeMillis();
				}
			}

			terminalData.setState(state);
			terminalData.setStatus(status);
			terminalData.setLivestamp(System.currentTimeMillis());
			terminalData.setPagestamp(pageStamp);
			terminalData.setStartStamp(startStamp);
			terminalBox.fastPut(terminalId, terminalData);
		}

		model.addAttribute("url",
				Urly.parse(HttpUtils.getServerName(request)).setPath(Path.TERMINAL_STATUS_PING)
						.addParameter("terminalId", terminalId).addParameter("state", state)
						.addParameter("status", status).addParameter("pageStamp", pageStamp)
						.addParameter("startStamp", startStamp)
						.getURL());
		return "js/signpad";
	}

	@ResponseBody
	@ApiOperation("To update the status of Remitance")
	@RequestMapping(value = { Path.TERMINAL_STATUS_REMIT }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			@RequestParam Integer terminalId, @RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		terminalBox.updateStamp(terminalId);

		return deviceClient.updateRemittanceState(ClientType.SIGNATURE_PAD, terminalId,
				signaturePadRemittanceInfo, employeeId);
	}

	@ResponseBody
	@ApiOperation("To update the status of FC_PURCHASE")
	@RequestMapping(value = { Path.TERMINAL_STATUS_FCPURCHASE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(@RequestParam Integer terminalId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		terminalBox.updateStamp(terminalId);

		return deviceClient.updateFcPurchase(ClientType.SIGNATURE_PAD, terminalId,
				signaturePadRemittanceInfo, employeeId);
	}

	@ResponseBody
	@ApiOperation("To update the status of FC_SALE")
	@RequestMapping(value = { Path.TERMINAL_STATUS_FCSALE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(@RequestParam Integer terminalId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		terminalBox.updateStamp(terminalId);

		return deviceClient.updateFcSale(ClientType.SIGNATURE_PAD, terminalId,
				signaturePadRemittanceInfo, employeeId);
	}

	@ResponseBody
	@ApiOperation("To update the status of Customer Registration")
	@RequestMapping(value = { Path.TERMINAL_STATUS_CUST_REG }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			@RequestParam Integer terminalId, @RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadRemittanceInfo) {
		terminalBox.updateStamp(terminalId);

		return deviceClient.updateCustomerRegStateData(ClientType.SIGNATURE_PAD, terminalId,
				signaturePadRemittanceInfo, employeeId);
	}

}