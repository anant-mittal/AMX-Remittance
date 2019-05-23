package com.amx.jax.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.boot.jackson.JsonComponent;

import com.amx.utils.ArgUtil;
import com.amx.utils.ArgUtil.EnumById;
import com.amx.utils.EnumType;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CommonSerilizers {
	@JsonComponent
	public static class EnumByIdSerializer extends JsonSerializer<EnumById> {

		@Override
		public void serialize(EnumById value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			gen.writeString(value.getId());
		}
	}

	@JsonComponent
	public static class EnumTypeSerializer extends JsonSerializer<EnumType> {

		@Override
		public void serialize(EnumType value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (!ArgUtil.isEmpty(value)) {
				gen.writeString(value.enumValue().name());
			}
		}
	}

	@JsonComponent
	public static class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

		@Override
		public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
				throws IOException, JsonProcessingException {
			if (!ArgUtil.isEmpty(value)) {
				// gen.writeString(value.toPlainString());
				// gen.writeNumber(value);
				// gen.writeNumber(value.doubleValue());
				gen.writeNumber(value.toPlainString());
			}
		}
	}
}
