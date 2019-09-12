package com.amx.jax.radar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;
import com.amx.jax.exception.AmxException;
import com.amx.utils.ArgUtil;
import com.querydsl.core.annotations.QueryEntity;

@Component
public class AMXSharedValues extends CacheBox<String> {

	public static final String SHARED_KEY_VALUE_COLLECTION = "SharedKeyValue";

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	public AMXSharedValues() {
		super("AMXSharedValues");
	}

	private MongoTemplate getMongoTemplate() {
		if (mongoTemplate == null) {
			throw new AmxException("MONGO DB IS NOT AVAIALBLE");
		}
		return mongoTemplate;
	}

	@QueryEntity
	@Document(collection = SHARED_KEY_VALUE_COLLECTION)
	public static class SharedKeyValue {
		@Id
		private String id;
		@Indexed
		private String key;
		private String value;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	private SharedKeyValue getSharedKeyValue(String key) {
		Query query = new Query();
		query.addCriteria(Criteria.where("key").is(key));

		return getMongoTemplate().findOne(query, SharedKeyValue.class, SHARED_KEY_VALUE_COLLECTION);
	}

	public String getValue(String key) {
		String value = this.get(key);
		if (ArgUtil.isEmpty(value)) {
			SharedKeyValue config = getSharedKeyValue(key);
			if (!ArgUtil.isEmpty(config) && !ArgUtil.isEmpty(config.getValue())) {
				this.put(key, config.getValue());
			}
		}
		return value;
	}

	public String putValue(String key, String value) {
		SharedKeyValue config = getSharedKeyValue(key);
		if (ArgUtil.isEmpty(config)) {
			config = new SharedKeyValue();
		}
		config.setKey(key);
		config.setValue(value);
		getMongoTemplate().save(config, SHARED_KEY_VALUE_COLLECTION);
		this.put(key, value);
		return value;
	}

	public long removeValue(String key) {
		SharedKeyValue config = getSharedKeyValue(key);
		if (ArgUtil.isEmpty(config)) {
			config = new SharedKeyValue();
		}
		config.setKey(key);
		getMongoTemplate().remove(config, SHARED_KEY_VALUE_COLLECTION);
		return this.fastRemove(key);
	}
}
