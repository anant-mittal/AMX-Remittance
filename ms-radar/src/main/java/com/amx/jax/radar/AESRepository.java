package com.amx.jax.radar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

public class AESRepository {

	public static final Logger LOGGER = LoggerService.getLogger(AESRepository.class);

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	public Map<String, Object> insert(String index, String type, AESDocument rate) {

		// vote.setId(UUID.randomUUID().toString());
		Map<String, Object> dataMap = JsonUtil.toMap(rate);
		IndexRequest indexRequest = new IndexRequest(index, type, rate.getId())
				.source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			LOGGER.error("ElasticsearchException", e);
		} catch (java.io.IOException ex) {
			LOGGER.error("java.io.IOException", ex);
		}

		return dataMap;
	}

	public Map<String, Object> getById(String index, String type, String id) {
		GetRequest getRequest = new GetRequest(index, type, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest);
		} catch (java.io.IOException e) {
			LOGGER.error("java.io.IOException", e);
		}
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		return sourceAsMap;
	}

	public Map<String, Object> update(String index, String type, AESDocument vote) {
		return this.updateById(index, type, vote.getId(), vote);
	}

	public Map<String, Object> updateById(String index, String type, String id, AESDocument vote) {
		UpdateRequest updateRequest = new UpdateRequest(index, type, id)
				.fetchSource(true); // Fetch Object after its update
		Map<String, Object> error = new HashMap<>();
		error.put("Error", "Unable to update vote");
		try {
			String voteJson = JsonUtil.getMapper().writeValueAsString(vote);
			updateRequest.upsert(voteJson, XContentType.JSON);
			updateRequest.doc(voteJson, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
			Map<String, Object> sourceAsMap = updateResponse.getGetResult().sourceAsMap();
			return sourceAsMap;
		} catch (JsonProcessingException e) {
			LOGGER.error("JsonProcessingException", e);
		} catch (java.io.IOException e) {
			LOGGER.error("java.io.IOException", e);
		}
		return error;
	}

	public void deleteById(String index, String type, String id) {
		DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
		try {
			DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		} catch (java.io.IOException e) {
			LOGGER.error("java.io.IOException", e);
		}
	}

	public Map<String, Object> bulk(BulkRequest bulk) {
		Map<String, Object> error = new HashMap<>();
		try {
			BulkResponse bulkResponse = restHighLevelClient.bulk(bulk);
			error.put("items", bulkResponse.getItems());
		} catch (IOException e) {
			error.put("Error", "Unable to update vote");
		}
		return error;
	}

	public static class BulkRequestBuilder {

		BulkRequest request;

		public BulkRequestBuilder() {
			this.request = new BulkRequest();
		}

		public BulkRequest build() {
			return this.request;
		}

		public BulkRequestBuilder update(String index, String type, AESDocument vote) {
			return this.updateById(index, type, vote.getId(), vote);
		}

		public BulkRequestBuilder updateById(String index, String type, String id, AESDocument vote) {
			UpdateRequest updateRequest = new UpdateRequest(index, type, id);
			try {
				String voteJson = JsonUtil.getMapper().writeValueAsString(vote);
				updateRequest.upsert(voteJson, XContentType.JSON);
				updateRequest.doc(voteJson, XContentType.JSON);
				this.request.add(updateRequest);
			} catch (JsonProcessingException e) {
				LOGGER.error("JsonProcessingException", e);
			}
			return this;
		}

	}

}
