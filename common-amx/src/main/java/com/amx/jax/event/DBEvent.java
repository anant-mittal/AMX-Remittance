package com.amx.jax.event;

import java.io.Serializable;
import java.util.Map;

public class DBEvent implements Serializable{

	private static final long serialVersionUID = 1L;
	String priority;
	Map<String, String> data;

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
