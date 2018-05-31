package com.amx.jax.dict;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonPropertyOrder({ "latitude", "longitude" })
public enum HotPoints {

	SALMIYA2(" 29.331993", "48.061422"), MURGAB3("29.369429", "47.978551"), SALMIYA4(" 29.325602", "48.058039");

	private String latitude;
	private String longitude;
	private String id;

	HotPoints(String latitude, String longitude) {
		this.id = this.name();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
