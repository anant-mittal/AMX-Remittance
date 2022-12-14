package com.amx.jax.client.snap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amx.jax.api.AmxResponseSchemes.ApiWrapperResponse;
import com.amx.jax.model.MapModel;
import com.amx.utils.CollectionUtil;
import com.amx.utils.JsonPath;
import com.amx.utils.JsonUtil;
import com.amx.utils.StringUtils;
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
		KEYS.put("key_as_string", true);
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

		@SuppressWarnings("unchecked")
		public List<Map<String, Object>> getBulk() {
			return (List<Map<String, Object>>) map.get("bulk");
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
				try {
					if (map.containsKey(BUCKETS)) {
						Object tempbucketsmapCheck = BUCKETS_LIST.load(map, new HashMap<String, Map<String, Object>>());
						if (tempbucketsmapCheck instanceof ArrayList) {
							List<Map<String, Object>> tempbuckets = BUCKETS_LIST.loadList(map,
									new HashMap<String, Object>());
							for (Map<String, Object> aggregationMap : tempbuckets) {
								Aggregations aggr = new Aggregations(aggregationMap);
								this.buckets.add(aggr);
							}
						} else {
							HashMap<String, Map<String, Object>> tempbucketsmap = BUCKETS_LIST.load(map,
									new HashMap<String, Map<String, Object>>());

							for (Entry<String, Map<String, Object>> aggregationMap : tempbucketsmap.entrySet()) {
								Aggregations aggr = new Aggregations(aggregationMap.getValue());
								aggr.toMap().put("key", aggregationMap.getKey());
								this.buckets.add(aggr);
							}
						}

					}
				} catch (Exception e) {
					System.out.println("===  " + JsonUtil.toJson(map));
					// e.printStackTrace();
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
					System.out.println("ERROR : " + map + "=== " + field);
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
			return this.toBulk(new HashMap<String, Object>(), "", 0);
		}

		public List<Map<String, Object>> toBulk(int minCount) {
			return this.toBulk(new HashMap<String, Object>(), "", minCount);
		}

		public static Map<String, Object> copy(Map<String, Object> map) {
			Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.putAll(map);
			return newMap;
		}

		public List<Map<String, Object>> toBulk(Map<String, Object> bulkItemBlank, String space, int minCount) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			long afIndex = 0;
			for (AggregationField af : this.fields()) {
				String afIndexStr = StringUtils.alpha62(afIndex);
				// String afIndexStr = "-";//StringUtils.alpha62(afIndex);
				if (af.toMap().containsKey("buckets")) {
					List<Aggregations> buckets = af.getBuckets();

					long bucketItemIndex = 0;
					for (Aggregations bucketItem : buckets) {
						String bucketItemIndexStr = StringUtils.alpha62(afIndex + bucketItemIndex);
						// String bucketItemIndexStr = "-";
						// System.out.println(af.fieldName() + " " + bucketItem.getKey());
						Map<String, Object> _bulkItemBlank = copy(bulkItemBlank);
						_bulkItemBlank.put(af.fieldName(), bucketItem.getKey());
						List<Map<String, Object>> bulk = bucketItem.toBulk(_bulkItemBlank, space + bucketItemIndexStr,
								minCount);
						for (Map<String, Object> bulkItem : bulk) {
							if (bulkItem.containsKey("_id")) {
								// bulkItem.put("_docs", bucketItem.getDocCount());
								list.add(bulkItem);
							}
							// System.out.println("bulkItem " + JsonUtil.toJson(bulkItem));
						}
						bucketItemIndex++;
					}
				} else {
					bulkItemBlank.put("_docs", this.getDocCount());
					af.toBulkItem(bulkItemBlank, space + afIndexStr);
				}
				afIndex++;
			}
			if (afIndex == 0) {
				bulkItemBlank.put("_id", space);
				bulkItemBlank.put("_docs", this.getDocCount());
				// System.out.println("This is end of "+ this.getKey() + " " + space);
			}
			if (bulkItemBlank.containsKey("_id") && (this.getDocCount() >= minCount)) {
				list.add(bulkItemBlank);
			}
			return list;
		}

		public String getKey() {
			return this.getString("key");
		}

		public Long getDocCount() {
			return this.getLong("doc_count", 0L);
		}

	}

	public static class SnapQueryParams extends MapModel {

		private static final String LEVEL = "level";

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
			f.put("key", key);
			f.put("value", value);
			if (filters == null) {
				filters = new ArrayList<Map<String, Object>>();
			}
			this.map.put("filters", filters);
			filters.add(f);
		}

		public void addValue(String key, String value) {
			this.map.put(key, value);
		}

	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class SnapModelResponse extends SnapModelWrapper implements ApiWrapperResponse {

		public SnapModelResponse(Map<String, Object> map) {
			super(map);
			this.setStatus("200");
			this.setStatusKey("SUCCESS");
			this.setTimestamp(System.currentTimeMillis());
		}

		public SnapModelResponse(String string) {
			super(string);
			this.setStatus("200");
			this.setStatusKey("SUCCESS");
			this.setTimestamp(System.currentTimeMillis());
		}

		@Override
		public void setTimestamp(Long timestamp) {
			this.map.put("timestamp", timestamp);
		}

		@Override
		public Long getTimestamp() {
			return this.getLong("timestamp");
		}

		@Override
		public String getStatus() {
			return this.getString("status", "200");
		}

		@Override
		public void setStatus(String status) {
			this.map.put("status", status);

		}

		@Override
		public String getStatusKey() {
			return this.getString("statusKey");
		}

		@Override
		public void setStatusKey(String statusKey) {
			this.map.put("statusKey", statusKey);
		}

		@Override
		public String getMessage() {
			return this.getString("message");
		}

		@Override
		public void setMessage(String message) {
			this.map.put("message", message);
		}

		@Override
		public String getMessageKey() {
			return this.getString("messageKey");
		}

		@Override
		public void setMessageKey(String messageKey) {
			this.map.put("messageKey", messageKey);
		}

	}

}
