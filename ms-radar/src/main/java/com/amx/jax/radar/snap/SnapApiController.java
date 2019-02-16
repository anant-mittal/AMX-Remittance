package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.radar.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.rates.AmxCurConstants.RCur;
import com.amx.jax.rest.RestService;

@Controller
public class SnapApiController {

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	RestService restService;

	@ResponseBody
	@RequestMapping(value = "/snap/api/customer/joined", method = RequestMethod.GET)
	public SnapModelWrapper customerJoined()
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-20y");
		return snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_JOINED, params);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/api/tranx/done", method = RequestMethod.GET)
	public SnapModelWrapper tranxDone()
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-5y");
		return snapQueryService.execute(SnapQueryTemplate.TRANX_DONE, params);
	}

	public static enum XRateGraph {
		WEEK1, MONTH1, YEAR1, YEAR2, YEAR5
	}

	@ResponseBody
	@RequestMapping(value = "/snap/api/xrate/sell_transfer", method = RequestMethod.GET)
	public SnapModelWrapper xrateOnline(@RequestParam XRateGraph graph, @RequestParam RCur forCur,
			@RequestParam RCur domCur)
			throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");

		switch (graph) {
		case YEAR5:
			params.put("gte", "now-5y");
			params.put("interval", "1M");
			break;
		case YEAR2:
			params.put("gte", "now-2y");
			params.put("interval", "1M");
			break;
		case YEAR1:
			params.put("gte", "now-1y");
			params.put("interval", "1M");
			break;
		case MONTH1:
			params.put("gte", "now-1M");
			params.put("interval", "1d");
			break;
		default:
			params.put("gte", "now-1w");
			params.put("interval", "1d");
			break;
		}
		params.put("rSrc", "AMX");
		params.put("rType", "SELL_TRNSFR");
		params.put("rForCur", forCur);
		params.put("rDomCur", domCur);
		return snapQueryService.execute(SnapQueryTemplate.XRATE_SELL_TRANSFER, params);
	}

}
