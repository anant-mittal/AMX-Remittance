package com.amx.jax.exception;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.api.AmxFieldError;
import com.amx.jax.types.Dnum;
import com.amx.utils.ArgUtil;

public class ExceptionMessageKey extends Dnum<ExceptionMessageKey> implements IMessageKey {

	public static final Map<String, String> MAP = new HashMap<String, String>();

	public static final ExceptionMessageKey NULL_NOT_ALLOWED = new ExceptionMessageKey("NULL_NOT_ALLOWED", 1,
			"may not be null", "NotNUll");

	public static int ordinalCounter = 0;

	int argCount = 0;
	String messageKeyFormat;

	public ExceptionMessageKey(String messageKeyName, int argCount, String... matchings) {
		super(messageKeyName, ordinalCounter++);
		this.argCount = argCount;
		for (String matching : matchings) {
			MAP.put(matching.toLowerCase(), messageKeyName);
		}
		StringBuilder builder = new StringBuilder(messageKeyName);
		for (int i = 0; i < argCount; i++) {
			builder.append(":%s");
		}
		messageKeyFormat = builder.toString();
	}

	@Override
	public int getArgCount() {
		return argCount;
	}

	@Override
	public String getMessageKey(Object... args) {
		return String.format(this.messageKeyFormat, args);
	}

	@Override
	public String toString() {
		return this.messageKeyFormat;
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		return values(ExceptionMessageKey.class);
	}

	public static ExceptionMessageKey valueOf(String name) {
		if (ArgUtil.isEmpty(name)) {
			return null;
		}
		String exceptionMessageKeyStr = MAP.get(name.toLowerCase());
		if (!ArgUtil.isEmpty(exceptionMessageKeyStr)) {
			return fromString(ExceptionMessageKey.class, exceptionMessageKeyStr);
		}
		return null;
	}

	public static void resolveLocalMessage(AmxApiError apiError) {
		if (apiError.getErrors() != null) {
			AmxFieldError fieldError = apiError.getErrors().get(0);
			if (fieldError != null) {
				if (ArgUtil.isEmpty(apiError.getMessageKey())) {
					ExceptionMessageKey exceptionMessageKey = ExceptionMessageKey.valueOf(fieldError.getDescription());
					if (exceptionMessageKey != null) {
						apiError.setMessageKey(exceptionMessageKey
								.getMessageKey(ArgUtil.ifNotEmpty(fieldError.getField(), fieldError.getObzect()))
								.toString());
					} else {
						exceptionMessageKey = ExceptionMessageKey.valueOf(fieldError.getCode());
						if (exceptionMessageKey != null) {
							apiError.setMessageKey(exceptionMessageKey
									.getMessageKey(ArgUtil.ifNotEmpty(fieldError.getField(), fieldError.getObzect()))
									.toString());
						}
					}
				}
				if (ArgUtil.isEmpty(apiError.getMessage())) {
					apiError.setMessage(fieldError.getDescription());
				}
			}
		}
	}

}
