package com.amx.jax.tunnel;

import java.io.Serializable;

import com.amx.utils.UniqueID;

public class TunnelMessage<M> implements Serializable {

	private static final long serialVersionUID = 3616406827684793142L;

	String id;
	M data;

	TunnelMessage(M data) {
		this.id = UniqueID.generateString();
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public M getData() {
		return data;
	}

	public void setData(M data) {
		this.data = data;
	}

}
