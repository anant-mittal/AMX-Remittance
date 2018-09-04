package com.amx.jax.postman;

import java.util.HashMap;
import java.util.Map;

public class PostManResponse {

	private Map<String, Object> respData;

	public PostManResponse() {
		super();
		this.respData = new HashMap<String, Object>();
	}

	public Map<String, Object> getRespData() {
		return respData;
	}

	public void setRespData(Map<String, Object> respData) {
		this.respData = respData;
	}

}
