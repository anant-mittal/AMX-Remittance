package com.amx.jax.def;

import java.util.List;

public class MockParamBuilder {

	public static enum MockParamType {
		HEADER, COOKIE, BODY, QUERY
	}

	public static class MockParam {
		MockParamType type;
		String name;
		String description;
		String defaultValue;
		boolean required = false;
		List<String> values = null;
		public String valueType;

		public String getDefaultValue() {
			return defaultValue;
		}

		public String getName() {
			return name;
		}

		public String getDescription() {
			return description;
		}

		public MockParamType getType() {
			return type;
		}

		public boolean isRequired() {
			return required;
		}

		public List<String> getValues() {
			return values;
		}

		public String getValueType() {
			return valueType;
		}

	}

	MockParam mockParam;

	public MockParamBuilder() {
		this.mockParam = new MockParam();
	}

	public MockParamBuilder name(String name) {
		this.mockParam.name = name;
		return this;
	}

	public MockParamBuilder description(String description) {
		this.mockParam.description = description;
		return this;
	}

	public MockParamBuilder defaultValue(String defaultValue) {
		this.mockParam.defaultValue = defaultValue;
		return this;
	}

	public MockParamBuilder required(boolean required) {
		this.mockParam.required = required;
		return this;
	}

	public MockParamBuilder parameterType(MockParamType type) {
		this.mockParam.type = type;
		return this;
	}

	public MockParamBuilder allowableValues(List<String> values, String valueType) {
		this.mockParam.values = values;
		this.mockParam.valueType = valueType;
		return this;
	}

	public MockParam build() {
		return mockParam;
	}

}
