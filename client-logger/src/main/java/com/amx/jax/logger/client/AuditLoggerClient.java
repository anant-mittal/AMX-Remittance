package com.amx.jax.logger.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.AuditLoggerService;
import com.amx.jax.logger.AuditLoggerUrls;
import com.amx.jax.logger.events.AuditEvent;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class AuditLoggerClient implements AuditLoggerService {

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

	public AuditEvent log(AuditEvent event) throws UnirestException {
		HttpResponse<AuditEvent> response = Unirest.post(loggerUrl + AuditLoggerUrls.SEND_SMS)
				.header("content-type", "application/json").body(event).asObject(AuditEvent.class);
		return response.getBody();
	}

}
