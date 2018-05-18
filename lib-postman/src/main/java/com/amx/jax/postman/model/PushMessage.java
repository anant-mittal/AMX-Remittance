package com.amx.jax.postman.model;

public class PushMessage extends Message {

	private static final long serialVersionUID = -1354844357577261297L;

	Object result = null;

	String image = null;

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}