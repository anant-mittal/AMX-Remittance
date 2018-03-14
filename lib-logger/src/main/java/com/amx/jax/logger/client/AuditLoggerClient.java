package com.amx.jax.logger.client;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.AuditLoggerResponse;
import com.amx.jax.logger.AuditLoggerUrls;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.AbstractAuditEvent;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

@Component
public class AuditLoggerClient implements AuditService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditService.class);

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

	@Value("${app.enabled.logger}")
	private Boolean loggerEnable;

	public AuditLoggerResponse log(AbstractAuditEvent event) {
		LOGGER.info(JsonUtil.toJson(event));
		return null;
	}

	@Override
	public AuditLoggerResponse logRest(AbstractAuditEvent event) {
		HttpResponse<AuditLoggerResponse> response = null;
		try {
			if (loggerEnable) {
				response = Unirest.post(loggerUrl + AuditLoggerUrls.SESSION_LOG)
						.header("content-type", "application/json").body(event).asObject(AuditLoggerResponse.class);
			}
		} catch (Exception e) {
			LOGGER.error("Audit Log Error : ", e);
		}
		if (response == null) {
			return null;
		}
		return response.getBody();
	}

}
