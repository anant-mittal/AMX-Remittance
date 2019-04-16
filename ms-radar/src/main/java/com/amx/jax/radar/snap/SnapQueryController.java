package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.radar.jobs.customer.OracleVarsCache.DBSyncJobs;
import com.amx.jax.rest.RestService;

@Controller
public class SnapQueryController {

	@Autowired
	private SnapQueryService snapQueryTemplateService;

	@Autowired
	RestService restService;

	@Autowired
	OracleVarsCache oracleVarsCache;

	@ResponseBody
	@RequestMapping(value = "/snap/query/{snapView}", method = RequestMethod.POST)
	public Map<String, Object> snapQuery(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {
		return snapQueryTemplateService.getQuery(snapView, params);
	}

	@ResponseBody
	@RequestMapping(value = "/snap/view/{snapView}", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {
		return snapQueryTemplateService.execute(snapView, params);
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
			@RequestParam String gte, @RequestParam String lte) throws IOException {
		
		return "table";
	}

}
