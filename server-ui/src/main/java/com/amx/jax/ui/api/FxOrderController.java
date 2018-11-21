
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.jax.client.FcSaleOrderClient;
import com.amx.jax.client.IFxOrderService;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.FxExchangeRateDto;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.response.ResponseWrapper;

import io.swagger.annotations.Api;

/**
 * The Class PlaceOrderController.
 */
@RestController
@Api(value = "FX Order Apis")
@ApiStatusService(IFxOrderService.class)
public class FxOrderController {

	@Autowired
	private FcSaleOrderClient fcSaleOrderClient;

	@RequestMapping(value = "/api/fco/purpose/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<PurposeOfTransactionDto>> getFcPurposeofTrnx() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcPurposeofTrnx());
	}

	@RequestMapping(value = "/api/fco/ccy/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> getFcCurrencyList() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcCurrencyList());
	}

	@RequestMapping(value = "/api/fco/xrate", method = { RequestMethod.GET })
	public ResponseWrapper<List<FxExchangeRateDto>> getFcXRate(@RequestParam BigDecimal forCur) {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcXRate(forCur));
	}

}
