package com.amx.jax.ui.service;

import com.amx.jax.types.Pnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonPropertyOrder({ "latitude", "longitude" })
public class GeoHotPoints extends Pnum {

	private String latitude;

	private String longitude;

	private String id;

	public GeoHotPoints(String name, int ordinal, String latitude, String longitude) {
		super(name, ordinal);
		this.id = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getId() {
		return id;
	}

	/**
	 * Explicit definition of values() is needed here to trigger static initializer.
	 * 
	 * @return
	 */
	public static GeoHotPoints[] values() {
		return values(GeoHotPoints.class);
	}

	public static GeoHotPoints valueOf(String name) {
		return fromString(GeoHotPoints.class, name);
	}

	static {
		init(GeoHotPoints.class);
	}

}
