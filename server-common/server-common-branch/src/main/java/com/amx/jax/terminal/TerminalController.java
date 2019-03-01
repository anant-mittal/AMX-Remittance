package com.amx.jax.terminal;

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

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.DeviceStateClient;
import com.amx.jax.client.IDeviceStateService;
import com.amx.jax.device.TerminalBox;
import com.amx.jax.device.TerminalData;
import com.amx.jax.model.request.device.SignaturePadCustomerRegStateMetaInfo;
import com.amx.jax.model.request.device.SignaturePadFCPurchaseSaleInfo;
import com.amx.jax.model.request.device.SignaturePadRemittanceInfo;
import com.amx.jax.sso.SSOUser;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.terminal.TerminalConstants.Path;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;
import com.amx.utils.Urly;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @deprecated Should be used for JavaBranch Application
 * 
 */
@Deprecated
@Controller
@Api(value = "Terminal APIs")
@ApiStatusService(IDeviceStateService.class)
public class TerminalController {

	@Autowired
	private TerminalBox terminalBox;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	TerminalService terminalService;

	@Autowired(required = false)
	private SSOUser sSOUser;

	public static class PingStatus {
		public String state;
		public String status;
		public Long pageStamp;
		public Long startStamp;
	}

	private PingStatus getPingStatus(String terminalId,
			String state, String status,
			Long pageStamp, Long startStamp) {
		PingStatus map = new PingStatus();

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

		map.state = state;
		map.status = status;
		map.pageStamp = pageStamp;
		map.startStamp = startStamp;

		return map;
	}

	@ResponseBody
	@RequestMapping(value = { Path.TERMINAL_STATUS_PING }, method = { RequestMethod.POST })
	public AmxApiResponse<PingStatus, Object> postPing(@RequestParam String state,
			@RequestParam(required = false) String status, @RequestParam(required = false) Long pageStamp,
			@RequestParam(required = false) Long startStamp,
			Model model,
			HttpServletResponse response, HttpServletRequest request) {
		PingStatus map = getPingStatus(ArgUtil.parseAsString(sSOUser.getUserClient().getTerminalId()), state, status,
				pageStamp, startStamp);
		return AmxApiResponse.build(map);
	}

	@RequestMapping(value = { Path.TERMINAL_STATUS_PING }, method = { RequestMethod.GET })
	public String getPing(@RequestParam String state, @RequestParam String terminalId,
			@RequestParam(required = false) String status, @RequestParam(required = false) Long pageStamp,
			@RequestParam(required = false) Long startStamp,
			Model model,
			HttpServletResponse response, HttpServletRequest request) throws MalformedURLException, URISyntaxException {

		PingStatus map = getPingStatus(ArgUtil.parseAsString(sSOUser.getUserClient().getTerminalId()), state, status,
				pageStamp, startStamp);

		model.addAttribute("url",
				Urly.parse(HttpUtils.getServerName(request)).path(appConfig.getAppPrefix())
						.path(Path.TERMINAL_STATUS_PING)
						.queryParam("terminalId", terminalId).queryParam("state", map.state)
						.queryParam("status", map.status).queryParam("pageStamp", map.pageStamp)
						.queryParam("startStamp", map.startStamp)
						.getURL());
		return "js/signpad";
	}

	@ResponseBody
	@ApiOperation("To update the status of Remitance")
	@RequestMapping(value = { Path.TERMINAL_STATUS_REMIT }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRemittanceState(
			@RequestParam Integer terminalId, @RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadRemittanceInfo signaturePadRemittanceInfo) {
		return terminalService.updateRemittanceState(terminalId, employeeId, signaturePadRemittanceInfo);
	}

	@ResponseBody
	@ApiOperation("To update the status of FC_PURCHASE")
	@RequestMapping(value = { Path.TERMINAL_STATUS_FCPURCHASE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcPurchase(@RequestParam Integer terminalId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {

		return terminalService.updateFcPurchaseState(terminalId, employeeId, signaturePadRemittanceInfo);
	}

	@ResponseBody
	@ApiOperation("To update the status of FC_SALE")
	@RequestMapping(value = { Path.TERMINAL_STATUS_FCSALE }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateFcSale(@RequestParam Integer terminalId,
			@RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadFCPurchaseSaleInfo signaturePadRemittanceInfo) {
		return terminalService.updateFcSaleState(terminalId, employeeId, signaturePadRemittanceInfo);
	}

	@ResponseBody
	@ApiOperation("To update the status of Customer Registration")
	@RequestMapping(value = { Path.TERMINAL_STATUS_CUST_REG }, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateCustomerRegStateData(
			@RequestParam Integer terminalId, @RequestParam BigDecimal employeeId,
			@RequestBody SignaturePadCustomerRegStateMetaInfo signaturePadRemittanceInfo) {
		return terminalService.updateCustomerRegStateData(terminalId, employeeId, signaturePadRemittanceInfo);
	}

}
