package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.rest.RestService;

@Controller
public class SnapApiController {

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	RestService restService;

	@ResponseBody
	@RequestMapping(value = "/snap/api/customer/joined", method = RequestMethod.GET)
	public Map<String, Object> customerJoined()
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-20y");
		return snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_JOINED, params);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/api/tranx/done", method = RequestMethod.GET)
	public Map<String, Object> tranxDone()
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-5y");
		return snapQueryService.execute(SnapQueryTemplate.TRANX_DONE, params);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/api/xrate/sell_transfer", method = RequestMethod.GET)
	public Map<String, Object> xrateOnline()
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-5y");
		return snapQueryService.execute(SnapQueryTemplate.XRATE_SELL_TRANSFER, params);
	}

}
