package com.amx.jax.tunnel;

import java.io.Serializable;

import com.amx.jax.AppContext;
import com.amx.utils.UniqueID;

public class TunnelMessage<M> implements Serializable {

	private static final long serialVersionUID = -638169239630153108L;

	String id;
	String topic;
	AppContext context;

	M data;

	TunnelMessage(M data) {
		this.id = UniqueID.generateString();
		this.data = data;
	}

	TunnelMessage(M data, AppContext context) {
		this.id = UniqueID.generateString();
		this.data = data;
		this.context = context;
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

}
