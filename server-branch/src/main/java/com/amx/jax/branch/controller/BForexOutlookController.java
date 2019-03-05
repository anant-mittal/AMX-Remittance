package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.meta.ForexOutlookClient;
import com.amx.jax.client.meta.IForexOutlookService;
import com.amx.jax.model.request.fx.ForexOutLookRequest;
import com.amx.jax.model.response.fx.CurrencyPairDTO;
import com.amx.jax.model.response.fx.ForexOutLookResponseDTO;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

import io.swagger.annotations.Api;

//@PreAuthorize("hasPermission('CUSTOMER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Forex Outlook APIs")
@ApiStatusService(IForexOutlookService.class)
public class BForexOutlookController implements IForexOutlookService {
	@Autowired
	private ForexOutlookClient forexOutlookClient;
	
	@RequestMapping(value = "/api/outlook/currencypair/list", method = { RequestMethod.GET })
	public AmxApiResponse<CurrencyPairDTO, Object> getCurrencyPairList() {
		// TODO Auto-generated method stub
		return forexOutlookClient.getCurrencyPairList();
	}

	@RequestMapping(value = "/api/outlook/history/list", method = { RequestMethod.GET })
	public AmxApiResponse<ForexOutLookResponseDTO, Object> getCurpairHistory() {
		// TODO Auto-generated method stub
		return forexOutlookClient.getCurpairHistory();
	}

	
	@RequestMapping(value = "/api/outlook/currencypair/saveorupdate", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> saveUpdateCurrencyPair(@RequestBody ForexOutLookRequest forexOutlookRequest) {
		// TODO Auto-generated method stub
			return forexOutlookClient.saveUpdateCurrencyPair(forexOutlookRequest);
	}

	@RequestMapping(value = "/api/outlook/currencypair/delete", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> deleteCurrencyPair(@RequestParam(value = "pairId", required = true)BigDecimal pairId) {
		// TODO Auto-generated method stub
		return forexOutlookClient.deleteCurrencyPair(pairId);
	}
	
}
