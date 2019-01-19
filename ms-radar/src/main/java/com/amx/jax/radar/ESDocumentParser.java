package com.amx.jax.radar;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import com.amx.jax.es.ESDocFormat;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class ESDocumentParser {

	public static class SampleModel {
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
		public Date date;
	}

	public static JsonFormat JSONFORMAT_DATE;

	public static ObjectMapper mapper;

	public static class ESAnnotationIntrospector extends JacksonAnnotationIntrospector {
		private static final long serialVersionUID = -8055392698768556216L;

		@Override
		public JsonFormat.Value findFormat(Annotated ann) {
			ESDocFormat f = _findAnnotation(ann, ESDocFormat.class);
			if (f != null) {
				if (ESDocFormat.Type.DATE == f.value() && JSONFORMAT_DATE != null) {
					return new JsonFormat.Value(JSONFORMAT_DATE);
				}
			}
			return super.findFormat(ann);
		}

		@Override // Ignoring all fields which doesn't have CustomAnnotation
		public boolean hasIgnoreMarker(AnnotatedMember m) {
			ESDocFormat an = _findAnnotation(m, ESDocFormat.class);
			if (an != null && ESDocFormat.Type.IGNORE != an.value()) {
				return false;
			}
			return super.hasIgnoreMarker(m);
		}

	}

	static {
		try {
			Field fieldDate = SampleModel.class.getField("date");
			JSONFORMAT_DATE = fieldDate.getAnnotation(JsonFormat.class);

		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		mapper = JsonUtil.createNewMapper("AESDocumentParser");
		mapper.setAnnotationIntrospector(new ESAnnotationIntrospector());
	}

	public static ObjectMapper getMapper() {
		return mapper;
	}

	public static String toJson(Object object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(Object object) {
		return mapper.convertValue(object, Map.class);
	}

}
