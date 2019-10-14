package com.amx.jax.client.snap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amx.jax.model.MapModel;
import com.amx.utils.CollectionUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class SnapModels {

	private static final String BUCKETS = "buckets";
	private static final JsonPath BUCKETS_LIST = new JsonPath(BUCKETS);
	public static final String AGGREGATIONS_KEY = "aggregations";
	private static final JsonPath AGGREGATIONS = new JsonPath(AGGREGATIONS_KEY);
	private static final String HITS_KEY = "hits";
	private static final JsonPath HITS = new JsonPath(HITS_KEY);
	private static final String SOURCE_KEY = "_source";
	private static final JsonPath SOURCE = new JsonPath(SOURCE_KEY);
	private static final String FIELDS_KEY = "fields";
	private static final JsonPath FIELDS = new JsonPath(FIELDS_KEY);
	private static final String SUMMARY_KEY = "summary";
	private static final JsonPath SUMMARY = new JsonPath(SUMMARY_KEY);
	private static final Map<String, Boolean> KEYS = new HashMap<String, Boolean>();

	static {
		KEYS.put("key", true);
		KEYS.put("doc_count_error_upper_bound", true);
		KEYS.put("sum_other_doc_count", true);
		KEYS.put("doc_count", true);
		KEYS.put("to_as_string", true);
		KEYS.put("from_as_string", true);
		KEYS.put("from", true);
		KEYS.put("to", true);
	}

	public static class ASnapModel extends MapModel {
		public ASnapModel(String json) {
			super(json);
		}

		public ASnapModel(Map<String, Object> map) {
			super(map);
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
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SnapModelWrapper extends ASnapModel {

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

		Map<String, Object> summaryMap;

		public Map<String, Object> getSummary() {
			if (summaryMap == null) {
				summaryMap = SUMMARY.load(map, new HashMap<String, Object>());
				map.put(SUMMARY_KEY, summaryMap);
			}
			return summaryMap;
		}

		List<Map<String, List<String>>> pivot;

		public List<Map<String, List<String>>> getPivot() {
			if (this.pivot == null) {
				this.pivot = new ArrayList<Map<String, List<String>>>();
				List<HashMap<String, List<String>>> tempbuckets = new JsonPath("_pivot").loadList(map,
						new HashMap<String, List<String>>());
				for (HashMap<String, List<String>> aggregationMap : tempbuckets) {
					this.pivot.add(aggregationMap);
				}
			}
			return pivot;
		}

		public SnapModelWrapper removeAggregations() {
			map.remove(AGGREGATIONS_KEY);
			return this;
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
			return this.getLong("total", 0L);
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

		public Fields getFields() {
			Object sourceObject = map.get(FIELDS_KEY);
			if (sourceObject instanceof Source) {
				return (Fields) sourceObject;
			} else {
				HashMap<String, Object> sourceMap = FIELDS.load(map, new HashMap<String, Object>());
				Fields source = new Fields(sourceMap);
				map.put(FIELDS_KEY, source);
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
	public static class Fields extends MapModel {

		public Fields(Map<String, Object> map) {
			super(map);
		}

		public String getId() {
			return this.getString("id");
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class AggregationField extends ASnapModel {

		private String fieldName;

		List<Aggregations> buckets;

		@JsonIgnore
		Map<String, Integer> keyIndex;

		public AggregationField(Map<String, Object> map, String fieldName) {
			super(map);
			this.fieldName = fieldName;
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

		public String fieldName() {
			return fieldName;
		}

		public Map<String, Object> toBulkItem(Map<String, Object> bulkItem, String space) {
			if (map.containsKey("value")) {
				bulkItem.put(fieldName(), map.get("value"));
				bulkItem.put("_id", space);
			}
			if (map.containsKey("hits")) {
				bulkItem.put(fieldName(),
						CollectionUtil.getOne(this.getHits().getHits().get(0).getFields().first().asList("")));
				bulkItem.put("_id", space);
			}
			return bulkItem;
		}

		public List<Map<String, Object>> toBulk(Map<String, Object> bulkItem, String space,
				List<Map<String, Object>> list) {
			if (map.containsKey("value")) {
				bulkItem.put(fieldName(), map.get("value"));
			}
			if (map.containsKey("hits")) {
				bulkItem.put(fieldName(), this.getHits().getHits().get(0).getFields().getFirst());
			}
			return CollectionUtil.getList(bulkItem);
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
				try {
					HashMap<String, Object> fieldMap = new JsonPath(field).load(map, new HashMap<String, Object>());
					AggregationField aggregationField = new AggregationField(fieldMap, field);
					this.map.put(field, aggregationField);
					return aggregationField;
				} catch (Exception e) {
					System.out.println(map + "=== " + field);
					return new AggregationField(new HashMap<String, Object>(), field);
				}
			}
		}

		public List<AggregationField> fields() {
			List<AggregationField> fields = new ArrayList<AggregationField>();
			for (String aggregationField : map.keySet()) {
				if (!KEYS.containsKey(aggregationField)) {
					fields.add(this.field(aggregationField));
				}
			}
			return fields;
		}

		public List<Map<String, Object>> toBulk() {
			return this.toBulk(new HashMap<String, Object>(), "");
		}

		public static Map<String, Object> copy(Map<String, Object> map) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.putAll(map);
			return newMap;
		}

		public List<Map<String, Object>> toBulk(Map<String, Object> bulkItemBlank, String space) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			long afIndex = 0;
			for (AggregationField af : this.fields()) {
				if (af.toMap().containsKey("buckets")) {
					List<Aggregations> buckets = af.getBuckets();

					long bucketItemIndex = 0;
					for (Aggregations bucketItem : buckets) {
						// System.out.println(af.fieldName() + " " + bucketItem.getKey());
						Map<String, Object> _bulkItemBlank = copy(bulkItemBlank);
						_bulkItemBlank.put(af.fieldName(), bucketItem.getKey());
						List<Map<String, Object>> bulk = bucketItem.toBulk(_bulkItemBlank,
								space + afIndex + bucketItemIndex);
						for (Map<String, Object> bulkItem : bulk) {
							if (bulkItem.containsKey("_id")) {
								bulkItem.put("_docs", bucketItem.getDocCount());
								list.add(bulkItem);
							}
							// System.out.println("bulkItem " + JsonUtil.toJson(bulkItem));
						}
						bucketItemIndex++;
					}

				} else {
					af.toBulkItem(bulkItemBlank, space + afIndex);
				}
				afIndex++;
			}
			if (bulkItemBlank.containsKey("_id")) {
				list.add(bulkItemBlank);
			}
			return list;
		}

		public String getKey() {
			return this.getString("key");
		}

		public Long getDocCount() {
			return this.getLong("doc_count");
		}

	}

}
