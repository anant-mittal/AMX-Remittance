package com.amx.jax.postman.model;

public class PushMessage extends Message {
	
	public static final String FORMAT_TO_ALL = "%s-all";
	public static final String FORMAT_TO_NATIONALITY = "%s-nationality-%s";
	public static final String FORMAT_TO_MOBILE = "%s-mobile-%s";

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
