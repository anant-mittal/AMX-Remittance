package com.amx.jax.radar;

import java.util.Date;

import com.amx.jax.AppContextUtil;
import com.amx.jax.dict.Tenant;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AESDocument {

	protected String id;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ")
	@JsonProperty(value = "@timestamp")
	protected Date timestamp;
	protected Tenant tnt = Tenant.DEFAULT;

	@JsonIgnore
	protected String type;

	public AESDocument() {
		this.timestamp = new Date(System.currentTimeMillis());
		this.tnt = AppContextUtil.getTenant();
	}

	public AESDocument(String type) {
		this();
		this.type = type;
	}

	public AESDocument(String type, String id) {
		this();
		this.type = type;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Tenant getTnt() {
		return tnt;
	}

	public void setTnt(Tenant tnt) {
		this.tnt = tnt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
