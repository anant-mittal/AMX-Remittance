package com.amx.jax.client.snap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.json.JsonSerializerType;
import com.amx.utils.ArgUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	private static final String SUMMARY_KEY = "summary";
	private static final JsonPath SUMMARY = new JsonPath(SUMMARY_KEY);

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class MapModel implements JsonSerializerType<Object> {
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

		@SuppressWarnings("unchecked")
		public MapModel getMap(String key) {
			return new MapModel((Map<String, Object>) this.get(key));
		}

		public MapModel(Map<String, Object> map) {
			this.map = map;
		}

		@Override
		public Object toObject() {
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
			Object hitsObject = map.get(HITS_KEY);
			if (hitsObject instanceof Hits) {
				return (Hits) hitsObject;
			} else {
				HashMap<String, Object> hitsMap = HITS.load(map, new HashMap<String, Object>());
				Hits hits = new Hits(hitsMap);
				map.put(HITS_KEY, hits);
				return hits;
			}
		}

		Map<String, Object> summaryMap;

		public Map<String, Object> getSummary() {
			if (summaryMap == null) {
				summaryMap = SUMMARY.load(map, new HashMap<String, Object>());
				map.put(SUMMARY_KEY, summaryMap);
			}
			return summaryMap;
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

		public Long getTotal() {
			return this.getLong("total");
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Hit extends MapModel {

		public Hit(Map<String, Object> map) {
			super(map);
		}

		public Source getSource() {
			Object sourceObject = map.get(SOURCE_KEY);
			if (sourceObject instanceof Source) {
				return (Source) sourceObject;
			} else {
				HashMap<String, Object> sourceMap = SOURCE.load(map, new HashMap<String, Object>());
				Source source = new Source(sourceMap);
				map.put(SOURCE_KEY, source);
				return source;
			}
		}

		public <T> T getSource(Class<T> clazz) {
			Source source = this.getSource();
			return JsonUtil.getMapper().convertValue(source.toObject(), clazz);
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

		@JsonIgnore
		Map<String, Integer> keyIndex;

		public AggregationField(Map<String, Object> map) {
			super(map);
		}

		public List<Aggregations> getBuckets() {
			if (this.buckets == null) {
				this.buckets = new ArrayList<SnapModels.Aggregations>();
				List<Map<String, Object>> tempbuckets = BUCKETS_LIST.loadList(map, new HashMap<String, Object>());
				for (Map<String, Object> aggregationMap : tempbuckets) {
					Aggregations aggr = new Aggregations(aggregationMap);
					this.buckets.add(aggr);
				}
			}
			return buckets;
		}

		public void setBuckets(List<Aggregations> buckets) {
			this.buckets = buckets;
		}

		public Aggregations bucket(String key) {
			if (this.keyIndex == null) {
				int index = 0;
				this.keyIndex = new HashMap<String, Integer>();
				for (Aggregations aggregations : this.getBuckets()) {
					this.keyIndex.put(aggregations.getKey(), index++);
				}
			}
			Integer bucketIndex = this.keyIndex.get(key);
			if (bucketIndex == null) {
				return new Aggregations(new HashMap<String, Object>());
			}
			return this.getBuckets().get(bucketIndex);
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
