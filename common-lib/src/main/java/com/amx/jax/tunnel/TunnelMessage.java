package com.amx.jax.tunnel;

import java.io.Serializable;

import com.amx.jax.AppContext;
import com.amx.utils.UniqueID;

public class TunnelMessage<M> implements Serializable {

	private static final long serialVersionUID = -638169239630153108L;

	long timestamp;
	String id;
	String topic;
	AppContext context;

	M data;

	TunnelMessage(M data) {
		this.id = UniqueID.generateString();
		this.timestamp = System.currentTimeMillis();
		this.data = data;
	}

	public TunnelMessage(M data, AppContext context) {
		this(data);
		this.context = context;
	}

	public TunnelMessage() {
		this(null);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public M getData() {
		return data;
	}

	public void setData(M data) {
		this.data = data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
