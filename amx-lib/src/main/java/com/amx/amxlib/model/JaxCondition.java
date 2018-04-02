package com.amx.amxlib.model;

public class JaxCondition {
	String conditionKey;

	String conditionValue;

	public String getConditionKey() {
		return conditionKey;
	}

	public void setConditionKey(String conditionKey) {
		this.conditionKey = conditionKey;
	}

	public String getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	@Override
	public String toString() {
		return "JaxCondition [conditionKey=" + conditionKey + ", conditionValue=" + conditionValue + "]";
	}

}
