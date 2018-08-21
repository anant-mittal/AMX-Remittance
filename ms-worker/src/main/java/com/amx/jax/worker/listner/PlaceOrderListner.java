package com.amx.jax.worker.listner;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.event.AmxTunnelEvents;
import com.amx.jax.event.Event;
import com.amx.jax.tunnel.ITunnelSubscriber;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.worker.service.PlaceOrderRateAlertService;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;

@TunnelEvent(topic = AmxTunnelEvents.Names.XRATE_BEST_RATE_CHANGE, queued = true)
public class PlaceOrderListner implements ITunnelSubscriber<Event> {

	@Autowired
	PlaceOrderRateAlertService rateAlertService;
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String BANK_ID = "BANKID";
	public static final String TOAMOUNT = "TOAMOUNT";
	public static final String CNTRYID = "CNTRYID";
	public static final String CURRID = "CURRID";
	public static final String FROMAMOUNT = "FROMAMOUNT";
	public static final String DRVSELLRATE = "DRVSELLRATE";

	// {"event_code":"XRATE_BEST_RATE_CHANGE","priority":"H",
	// "description":"Exchange Rate change","data":
	// {"FROMAMOUNT":"1","TOAMOUNT":"300000","CNTRYID":"94","CURRID":"4","BANKID":"1256","DRVSELLRATE":".004524"}}

	@Override
	public void onMessage(String channel, Event event) {
		LOGGER.info("======onMessage1==={} ====  {}", channel, JsonUtil.toJson(event));
		BigDecimal bankId = ArgUtil.parseAsBigDecimal(event.getData().get(BANK_ID));
		BigDecimal toAmount = ArgUtil.parseAsBigDecimal(event.getData().get(TOAMOUNT));
		BigDecimal countryId = ArgUtil.parseAsBigDecimal(event.getData().get(CNTRYID));
		BigDecimal currencyId = ArgUtil.parseAsBigDecimal(event.getData().get(CURRID));
		BigDecimal fromAmount = ArgUtil.parseAsBigDecimal(event.getData().get(FROMAMOUNT));
		BigDecimal derivedSellRate = ArgUtil.parseAsBigDecimal(event.getData().get(DRVSELLRATE));
		rateAlertService.rateAlertPlaceOrder(fromAmount,toAmount,countryId,currencyId,bankId,derivedSellRate);
	}

}