package com.amx.jax.radar.snap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.utils.ArgUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonSerializerType;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class SnapModels {

	private static final String BUCKETS = "buckets";
	private static final JsonPath BUCKETS_LIST = new JsonPath(BUCKETS);
	private static final String AGGREGATIONS_KEY = "aggregations";
	private static final JsonPath AGGREGATIONS = new JsonPath(AGGREGATIONS_KEY);
	private static final String HITS_KEY = "hits";
	private static final JsonPath HITS = new JsonPath(HITS_KEY);
	private static final String SOURCE_KEY = "_source";
	private static final JsonPath SOURCE = new JsonPath(SOURCE_KEY);

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MapModel implements JsonSerializerType<Map<String, Object>> {
		protected Map<String, Object> map;

		@SuppressWarnings("unchecked")
		public MapModel(String json) {
			this.map = JsonUtil.fromJson(json, Map.class);
		}

		public Object get(String key) {
			return this.map.get(key);
		}

		public String getString(String key) {
			return ArgUtil.parseAsString(this.get(key));
		}

		public Long getLong(String key) {
			return ArgUtil.parseAsLong(this.get(key));
		}

		public BigDecimal getBigDecimal(String key) {
			return ArgUtil.parseAsBigDecimal(this.get(key));
		}

		public MapModel(Map<String, Object> map) {
			this.map = map;
		}

		@Override
		public Map<String, Object> toObject() {
			return this.map;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SnapModelWrapper extends MapModel {

		public SnapModelWrapper(String json) {
			super(json);
		}

		public SnapModelWrapper(Map<String, Object> map) {
			super(map);
		}

		public Aggregations getAggregations() {
			Object aggregationsObject = map.get(AGGREGATIONS_KEY);
			if (aggregationsObject instanceof Aggregations) {
				return (Aggregations) aggregationsObject;
			} else {
				HashMap<String, Object> aggregationsMap = AGGREGATIONS.load(map, new HashMap<String, Object>());
				Aggregations aggregations = new Aggregations(aggregationsMap);
				map.put(AGGREGATIONS_KEY, aggregations);
				return aggregations;
			}
		}

		public Hits getHits() {
			Object hitsObject = map.get("hits");
			if (hitsObject instanceof Hits) {
				return (Hits) hitsObject;
			} else {
				HashMap<String, Object> hitsMap = HITS.load(map, new HashMap<String, Object>());
				Hits hits = new Hits(hitsMap);
				map.put(HITS_KEY, hits);
				return hits;
			}
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Hits extends MapModel {
		List<Hit> hits;

		public Hits(Map<String, Object> map) {
			super(map);
		}

		public List<Hit> getHits() {
			if (this.hits == null) {
				this.hits = new ArrayList<Hit>();
				List<Map<String, Object>> tempbuckets = HITS.loadList(map, new HashMap<String, Object>());
				for (Map<String, Object> aggregationMap : tempbuckets) {
					this.hits.add(new Hit(aggregationMap));
				}
			}
			return hits;
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Hit extends MapModel {

		public Hit(Map<String, Object> map) {
			super(map);
		}

		public Source getSource() {
			Object sourceObject = map.get("hits");
			if (sourceObject instanceof Source) {
				return (Source) sourceObject;
			} else {
				HashMap<String, Object> sourceMap = SOURCE.load(map, new HashMap<String, Object>());
				Source source = new Source(sourceMap);
				map.put(SOURCE_KEY, source);
				return source;
			}
		}

		public String getId() {
			return this.getString("_id");
		}

		public String getType() {
			return this.getString("_type");
		}

		public BigDecimal getScore() {
			return this.getBigDecimal("_score");
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Source extends MapModel {

		public Source(Map<String, Object> map) {
			super(map);
		}

		public String getId() {
			return this.getString("id");
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AggregationField extends MapModel {

		List<Aggregations> buckets;

		public AggregationField(Map<String, Object> map) {
			super(map);
		}

		public List<Aggregations> getBuckets() {
			if (this.buckets == null) {
				this.buckets = new ArrayList<SnapModels.Aggregations>();
				List<Map<String, Object>> tempbuckets = BUCKETS_LIST.loadList(map, new HashMap<String, Object>());
				for (Map<String, Object> aggregationMap : tempbuckets) {
					this.buckets.add(new Aggregations(aggregationMap));
				}
			}
			return buckets;
		}

		public void setBuckets(List<Aggregations> buckets) {
			this.buckets = buckets;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Aggregations extends MapModel {
		public Aggregations(Map<String, Object> map) {
			super(map);
		}

		public AggregationField field(String field) {
			Object fieldObject = map.get(field);
			if (fieldObject instanceof AggregationField) {
				return (AggregationField) fieldObject;
			} else {
				HashMap<String, Object> fieldMap = new JsonPath(field).load(map, new HashMap<String, Object>());
				AggregationField aggregationField = new AggregationField(fieldMap);
				this.map.put(field, aggregationField);
				return aggregationField;
			}
		}

		public String getKey() {
			return this.getString("key");
		}

		public Long getDocCount() {
			return this.getLong("doc_count");
		}

	}

}
