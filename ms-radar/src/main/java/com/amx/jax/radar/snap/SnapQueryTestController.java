package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.radar.jobs.customer.OracleVarsCache;
import com.amx.jax.rest.RestService;
import com.amx.jax.tunnel.TunnelService;
import com.amx.utils.FileUtil;
import com.amx.utils.JsonUtil;

@Controller
public class SnapQueryTestController {

	@Autowired
	private SnapQueryService snapQueryTemplateService;

	@Autowired
	RestService restService;

	@Autowired
	OracleVarsCache oracleVarsCache;

	@Autowired
	private TunnelService tunnelService;

	@ResponseBody
	@RequestMapping(value = "/test/snap/query/{snapView}", method = RequestMethod.POST)
	public List<Map<String, Object>> snapQuery(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {
		String json = FileUtil
				.readFile(FileUtil.normalize(
						"file://" + System.getProperty("user.dir") + "/src/test/java/com/amx/test/es-output.json"));
		SnapModelWrapper wrapper = new SnapModelWrapper(json);
		List<Map<String, Object>> x = wrapper.getAggregations().toBulk(null);
		
		for (Map<String, Object> map : x) {
			System.out.println(JsonUtil.toJson(map));
		}
		return x;
	}

}
