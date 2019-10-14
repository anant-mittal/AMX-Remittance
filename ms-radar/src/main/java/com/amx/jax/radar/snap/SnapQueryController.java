package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.rest.RestService;
import com.amx.jax.tunnel.DBEvent;
import com.amx.jax.tunnel.TunnelEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.ArgUtil;
import com.axx.jax.table.PivotTable;

@Controller
public class SnapQueryController {

	@Autowired
	private SnapQueryService snapQueryTemplateService;

	@Autowired
	RestService restService;

	@Autowired
	OracleVarsCache oracleVarsCache;

	@Autowired
	private TunnelService tunnelService;

	/**
	 * List of place orders.
	 *
	 * @return the response wrapper
	 */
	@ResponseBody
	@RequestMapping(value = "/pub/task/{scheme}/{topic}", method = { RequestMethod.POST })
	public AmxApiResponse<TunnelEvent, ?> initTask(@RequestBody TunnelEvent event,
			@PathVariable String topic,
			@PathVariable TunnelEventXchange scheme) {
		event.setEventCode(topic);
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic, event);
		} else if (scheme == TunnelEventXchange.TASK_WORKER) {
			tunnelService.task(topic, event);
		} else {
			tunnelService.shout(topic, event);
		}
		return AmxApiResponse.build(event);
	}

	@ResponseBody
	@RequestMapping(value = "/pub/DBEvent/{scheme}/{topic}", method = { RequestMethod.POST })
	public AmxApiResponse<TunnelEvent, ?> initTask(@RequestBody DBEvent event,
			@PathVariable String topic,
			@PathVariable(required = true) TunnelEventXchange scheme) {
		event.setEventCode(topic);
		if (scheme == TunnelEventXchange.SEND_LISTNER) {
			tunnelService.send(topic, event);
		} else if (scheme == TunnelEventXchange.TASK_WORKER) {
			tunnelService.task(topic, event);
		} else {
			tunnelService.shout(topic, event);
		}
		return AmxApiResponse.build(event);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/query/{snapView}", method = RequestMethod.POST)
	public Map<String, Object> snapQuery(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {
		return snapQueryTemplateService.getQuery(snapView, params);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/view/{snapView}", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params,
			@RequestParam(defaultValue = "now-1m", required = false) String gte,
			@RequestParam(defaultValue = "now", required = false) String lte,
			@RequestParam(defaultValue = "100", required = false) Integer level)
			throws IOException {
		if (!ArgUtil.isEmpty(gte) && !params.containsKey("gte")) {
			params.put("gte", gte);
		}
		if (!ArgUtil.isEmpty(lte) && !params.containsKey("lte")) {
			params.put("lte", lte);
		}
		level = ArgUtil.parseAsInteger(params.getOrDefault("level", level));

		SnapModelWrapper x = snapQueryTemplateService.execute(snapView, params);

		if (level >= 0) {
			List<Map<String, List<String>>> p = x.getPivot();
			List<Map<String, Object>> inputBulk = x.getAggregations().toBulk();

			for (Map<String, List<String>> pivot : p) {
				level--;
				if (level < 0)
					break;
				PivotTable table = new PivotTable(
						pivot.get("rows"), pivot.get("cols"),
						pivot.get("vals"), pivot.get("aggs"), pivot.get("alias"),
						pivot.get("computed"), pivot.get("noncomputed"));
				for (Map<String, Object> map : inputBulk) {
					table.add(map);
				}
				table.calculate();
				inputBulk = table.toBulk();
			}
			x.toMap().put("bulk", inputBulk);
			x.removeAggregations();
		}

		return x;
	}

	@ResponseBody
	@RequestMapping(value = "/snap/view/{snapView}/bulk", method = RequestMethod.POST)
	public List<Map<String, Object>> snapBulkView(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params,
			@RequestParam(defaultValue = "now-1m", required = false) String gte,
			@RequestParam(defaultValue = "now", required = false) String lte,
			@RequestParam(defaultValue = "100", required = false) int level)
			throws IOException {
		if (!ArgUtil.isEmpty(gte) && !params.containsKey("gte")) {
			params.put("gte", gte);
		}
		if (!ArgUtil.isEmpty(lte) && !params.containsKey("lte")) {
			params.put("lte", lte);
		}
		return snapQueryTemplateService.execute(snapView, params).getAggregations().toBulk();
	}

	@ResponseBody
	@RequestMapping(value = "/snap/execute/query/{snapView}", method = RequestMethod.POST)
	public SnapModelWrapper executeQuery(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> query)
			throws IOException {
		return snapQueryTemplateService.executeQuery(snapView, query);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/reset/start/{dbSyncJobs}", method = RequestMethod.GET)
	public String snapResetStart(@PathVariable(value = "dbSyncJobs") DBSyncJobs dbSyncJobs) throws IOException {
		oracleVarsCache.clearStampStart(dbSyncJobs);
		return "CLEARED";
	}

	@ResponseBody
	@RequestMapping(value = "/snap/reset/end/{dbSyncJobs}", method = RequestMethod.GET)
	public String snapResetEnd(@PathVariable(value = "dbSyncJobs") DBSyncJobs dbSyncJobs) throws IOException {
		oracleVarsCache.clearStampEnd(dbSyncJobs);
		return "CLEARED";
	}

	@RequestMapping(value = "/snap/table/{snapView}", method = RequestMethod.GET)
	public String table(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestParam(defaultValue = "now-1m") String gte, @RequestParam(defaultValue = "now") String lte,
			@RequestParam(defaultValue = "100", required = false) int level,
			Model model) throws IOException {
		model.addAttribute("gte", gte);
		model.addAttribute("lte", lte);
		model.addAttribute("level", level);
		model.addAttribute("snapView", snapView.toString());
		model.addAttribute("snapViews", SnapQueryTemplate.values());
		return "table";
	}

}
