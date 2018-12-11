package com.amx.jax.rates;

import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
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
import org.springframework.stereotype.Repository;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

@Repository
public class AmxCurRateRepository {

	Logger LOGGER = LoggerService.getLogger(AmxCurRateRepository.class);

	private final String INDEX = "marketrate";
	private final String TYPE = "currate";

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	public AmxCurRate insertRate(AmxCurRate rate) {

		// vote.setId(UUID.randomUUID().toString());

		Map<String, Object> dataMap = JsonUtil.toMap(rate);
		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, rate.getId())
				.source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest);
		} catch (ElasticsearchException e) {
			LOGGER.error("ElasticsearchException", e);
		} catch (java.io.IOException ex) {
			LOGGER.error("java.io.IOException", ex);
		}
		return rate;
	}

	public Map<String, Object> getRateById(String id) {
		GetRequest getRequest = new GetRequest(INDEX, TYPE, id);
		GetResponse getResponse = null;
		try {
			getResponse = restHighLevelClient.get(getRequest);
		} catch (java.io.IOException e) {
			LOGGER.error("java.io.IOException", e);
		}
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		return sourceAsMap;
	}

	public Map<String, Object> updateRateById(String id, AmxCurRate vote) {
		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
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

	public void deleteRateById(String id) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
		try {
			DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		} catch (java.io.IOException e) {
			LOGGER.error("java.io.IOException", e);
		}
	}

}
