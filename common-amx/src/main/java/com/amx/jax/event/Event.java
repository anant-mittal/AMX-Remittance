package com.amx.jax.event;

import java.io.Serializable;
import java.util.Map;

public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	private String event_code;
	private String priority;
	private String description;
	private Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getEvent_code() {
		return event_code;
	}

	public void setEvent_code(String event_code) {
		this.event_code = event_code;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Event [event_code=" + event_code + ", priority=" + priority + ", description=" + description + ", data="
				+ data + "]";
	}
	
}