package com.amx.jax.logger.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.AuditLoggerResponse;
import com.amx.jax.logger.AuditLoggerService;
import com.amx.jax.logger.AuditLoggerUrls;
import com.amx.jax.logger.events.SessionEvent;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

@Component
public class AuditLoggerClient implements AuditLoggerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditLoggerClient.class);

	{
		Unirest.setObjectMapper(new ObjectMapper() {

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return JsonUtil.getMapper().readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return JsonUtil.getMapper().writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Value("${jax.logger.url}")
	private String loggerUrl;

	@Async
	public AuditLoggerResponse log(SessionEvent event) {
		HttpResponse<AuditLoggerResponse> response = null;
		try {
			response = Unirest.post(loggerUrl + AuditLoggerUrls.SESSION_LOG).header("content-type", "application/json")
					.body(event).asObject(AuditLoggerResponse.class);
		} catch (Exception e) {
			LOGGER.error("Audit Log Error : ", e);
		}
		if (response == null) {
			return null;
		}
		return response.getBody();
	}

}
