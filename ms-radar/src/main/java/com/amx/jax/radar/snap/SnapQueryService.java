package com.amx.jax.radar.snap;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.amx.jax.radar.EsConfig;
import com.amx.jax.rest.RestService;
import com.amx.utils.JsonUtil;

/**
 * The Class QueryTemplateService.
 */
@Component
public class SnapQueryService {

	private Logger log = Logger.getLogger(getClass());

	@Autowired
	@Qualifier("messageTemplateEngine")
	private SpringTemplateEngine templateEngine;

	@Autowired
	RestService restService;

	@Autowired
	EsConfig ssConfig;

	public String processJson(SnapQueryTemplate template, Context context) {
		return templateEngine.process("json/" + template.getFile(), context);
	}

	public String buildQueryString(SnapQueryTemplate template, Map<String, Object> map) {
		Locale locale = new Locale("en");
		Context context = new Context(locale);
		context.setVariables(map);
		return this.processJson(template, context);
	}

	public Map<String, Object> buildQuery(SnapQueryTemplate template, Map<String, Object> map) throws IOException {
		return JsonUtil.getMapFromJsonString(this.buildQueryString(template, map));
	}

	public Map<String, Object> getQuery(Map<String, Object> query) throws IOException {
		return restService.ajax(ssConfig.getClusterUrl()).path("oracle-v3-tranx-v4/_search").post(query).asMap();
	}

}
