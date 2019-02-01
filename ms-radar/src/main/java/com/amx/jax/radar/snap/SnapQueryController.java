package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.rest.RestService;
import com.amx.utils.JsonUtil;

@Controller
public class SnapQueryController {

	@Autowired
	private SnapQueryTemplateService snapQueryTemplateService;

	@Autowired
	RestService restService;

	@ResponseBody
	@RequestMapping(value = "/snap/query/{snapView}", method = RequestMethod.POST)
	public Map<String, Object> snapQuery(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {
		return JsonUtil.getMapFromJsonString(snapQueryTemplateService.process(snapView, params));
	}

	@ResponseBody
	@RequestMapping(value = "/snap/view/{snapView}", method = RequestMethod.POST)
	public Map<String, Object> snapView(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params) throws IOException {

		Map<String, Object> queryStr = JsonUtil
				.getMapFromJsonString(snapQueryTemplateService.process(snapView, params));

		return restService.ajax("http://10.28.42.21:9200/oracle-v3-tranx-v4/_search").post(queryStr).asMap();
	}

}
