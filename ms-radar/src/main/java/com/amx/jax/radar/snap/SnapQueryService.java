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

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.client.snap.SnapQueryException;
import com.amx.jax.radar.AESDocument;
import com.amx.jax.radar.AESRepository.BulkRequestBuilder;
import com.amx.jax.radar.ESRepository;
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

	@Autowired
	public ESRepository esRepository;

	public String processJson(SnapQueryTemplate template, Context context) {
		return templateEngine.process("json/" + template.getFile(), context);
	}

	public String buildQueryString(SnapQueryTemplate template, Map<String, Object> map) {
		Locale locale = new Locale("en");
		Context context = new Context(locale);
		if (!map.containsKey("_type")) {
			map.put("_type", template.getIndexName());
		}
		context.setVariables(map);
		return this.processJson(template, context);
	}

	public Map<String, Object> getQuery(SnapQueryTemplate template, Map<String, Object> params) throws IOException {
		return JsonUtil.getMapFromJsonString(this.buildQueryString(template, params));
	}

	public SnapModelWrapper executeQuery(Map<String, Object> query, String index) {
		Map<String, Object> x = restService.ajax(ssConfig.getClusterUrl())
				.header(ssConfig.getBasicAuthHeader()).path(
						EsConfig.indexName(index) + "/_search")
				.post(query)
				.asMap();
		// x.put("aggs", query.get("aggs"));
		// System.out.println(JsonUtil.toJson(query));
		return new SnapModelWrapper(x);
	}

	public SnapModelWrapper executeQuery(Map<String, Object> query) throws IOException {
		return executeQuery(query, "oracle-v3-*-v4");
	}

	public SnapModelWrapper execute(SnapQueryTemplate template, String index, Map<String, Object> params) {
		Map<String, Object> query = null;
		try {
			query = getQuery(template, params);
		} catch (IOException e) {
			throw new SnapQueryException(SnapQueryException.SnapServiceCodes.INVALID_QUERY);
		}
		return executeQuery(query, index);
	}

	public SnapModelWrapper execute(SnapQueryTemplate template, Map<String, Object> params) {
		return this.execute(template, template.getIndex(), params);
	}

	public static class BulkRequestSnapBuilder extends BulkRequestBuilder {
		@Override
		public BulkRequestBuilder updateById(String index, String type, String id, AESDocument vote) {
			return super.updateById(EsConfig.indexName(index), type, id, vote);
		}
	}

	public Map<String, Object> save(BulkRequestSnapBuilder builder) {
		return esRepository.bulk(builder.build());
	}

	public Map<String, Object> save(String index, AESDocument vote) {
		return esRepository.update(EsConfig.indexName(index), vote.getType(), vote);
	}
}
