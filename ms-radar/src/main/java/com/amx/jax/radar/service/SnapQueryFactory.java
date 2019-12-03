package com.amx.jax.radar.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.def.AbstractQueryFactory;
import com.amx.jax.model.MapModel;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQuery;
import com.amx.jax.radar.service.SnapQueryFactory.SnapQueryParams;

@Component
public class SnapQueryFactory extends AbstractQueryFactory<SnapQueryTemplate, SnapQuery, SnapQueryParams> {

	private static final long serialVersionUID = 4887835524923451254L;
	private static final String LEVEL = "level";

	public static class SnapQueryParams extends MapModel {

		private List<Map<String, Object>> filters;

		public SnapQueryParams(Map<String, Object> params) {
			super(params);
		}

		public SnapQueryParams() {
			super();
		}

		public Integer getLevel() {
			return this.getInteger(LEVEL, 100);
		}

		public Integer getMinCount() {
			return this.getInteger("minCount", 0);
		}

		public void addFilter(String key, String value) {
			Map<String, Object> f = new HashMap<String, Object>();
			if (filters == null) {
				filters = new ArrayList<Map<String, Object>>();
				this.map.put("filters", filters);
			}
			filters.add(f);
		}
	}

	public SnapQueryFactory(@Autowired(required = false) List<QueryProcessor<?, SnapQueryParams>> libs) {
		super(libs);
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface SnapQuery {
		SnapQueryTemplate[] value();
	}

	@Override
	public Class<SnapQuery> getAnnotionClass() {
		return SnapQuery.class;
	}

	@Override
	public SnapQueryTemplate[] getValues(SnapQuery annotation) {
		return annotation.value();
	}
}
