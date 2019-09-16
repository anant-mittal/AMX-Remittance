package com.amx.jax.radar.snap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.client.snap.ISnapService;
import com.amx.jax.client.snap.SnapConstants.SnapIndexName;
import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.dict.Currency;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;

@Controller
public class SnapApiController implements ISnapService {

	@Autowired
	private SnapQueryService snapQueryService;

	@Autowired
	RestService restService;

	@Autowired
	OracleVarsCache oracleVarsCache;

	@Override
	@ResponseBody
	@RequestMapping(value = Path.SNAP_API_CUSTOMER_JOINED, method = RequestMethod.GET)
	public SnapModelWrapper getCustomerStats() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-20y");
		SnapModelWrapper resp = snapQueryService.execute(SnapQueryTemplate.CUSTOMERS_JOINED, params);
		Map<String, Object> summary = resp.getSummary();
		summary.put("all_time_customer",
				resp.getAggregations().field("join_inteval").bucket("all_time").getDocCount());
		summary.put("all_time_online_customer",
				resp.getAggregations().field("join_inteval").bucket("all_time").field("isOnlineUser").bucket("Y")
						.getDocCount());
		return resp;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = Path.SNAP_API_TRANX_DONE, method = RequestMethod.GET)
	public SnapModelWrapper getTranxStats() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");
		params.put("gte", "now-5y");
		params.put("_type", SnapIndexName.TRANX);
		SnapModelWrapper resp = snapQueryService.execute(SnapQueryTemplate.TRANX_DONE, params);
		Map<String, Object> summary = resp.getSummary();
		long tranxLastYear = ArgUtil
				.parseAsLong(resp.getAggregations().field("tranx").bucket("last_year").getDocCount(), 0L);
		long tranxLastYearBranch = ArgUtil.parseAsLong(resp.getAggregations().field("tranx").bucket("last_year")
				.field("channel").bucket("BRANCH").getDocCount(), 0L);
		long tranxLastYearKiosk = ArgUtil.parseAsLong(resp.getAggregations().field("tranx").bucket("last_year")
				.field("channel").bucket("KIOSK").getDocCount(), 0L);
		summary.put("tranx_last_year", tranxLastYear);
		summary.put("tranx_last_year_online", tranxLastYear - tranxLastYearBranch - tranxLastYearKiosk);
		return resp;
	}

	@Override
	@ResponseBody
	@RequestMapping(value = Path.SNAP_API_XRATE_SELL_TRANSFER, method = RequestMethod.GET)
	public SnapModelWrapper getXRateStats(@RequestParam StatsSpan graph,
			@RequestParam(defaultValue = "INR") Currency forCur,
			@RequestParam(defaultValue = "KWD") Currency domCur) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("lte", "now");

		switch (graph) {
		case YEAR5:
			params.put("gte", "now-5y");
			params.put("interval", "1d");
			break;
		case YEAR2:
			params.put("gte", "now-2y");
			params.put("interval", "1d");
			break;
		case YEAR1:
			params.put("gte", "now-1y");
			params.put("interval", "1d");
			break;
		case MONTH1:
			params.put("gte", "now-1M");
			params.put("interval", "1d");
			break;
		case WEEK1:
			params.put("gte", "now-1w");
			params.put("interval", "1d");
			break;
		default:
			params.put("gte", "now-1w");
			params.put("interval", "1d");
			break;
		}
		params.put("xrate_src", RateSource.AMX.toString());
		params.put("xrate_rateType", RateType.SELL_TRNSFR.toString());
		params.put("xrate_forCur", forCur);
		params.put("xrate_domCur", domCur);
		return snapQueryService.execute(SnapQueryTemplate.XRATE_SELL_TRANSFER, params);
	}

}
