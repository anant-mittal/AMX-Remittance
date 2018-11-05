package com.amx.jax.task.events;

import com.amx.jax.dict.Nations;
import com.amx.jax.tunnel.TunnelEvent;

public class PromoNotifyTask extends TunnelEvent {

	private static final long serialVersionUID = 7415624585226619390L;

	private Nations nationality;
	private String message;
	private String title;

	public Nations getNationality() {
		return nationality;
	}

	public void setNationality(Nations nationality) {
		this.nationality = nationality;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
