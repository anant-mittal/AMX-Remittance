package com.amx.jax.converter;

import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang.StringUtils;

import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

@Converter
public class JsonStrListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> attribute) {

		if (null == attribute || attribute.isEmpty()) {
			return null;
		}

		return JsonUtil.toJson(attribute);

	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {

		if (StringUtils.isEmpty(dbData)) {
			return null;
		}

		return JsonUtil.fromJson(dbData, new TypeReference<List<String>>() {
		});

	}

}
