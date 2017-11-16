package com.amx.jax.model.response;

import java.util.List;

import com.amx.jax.model.AbstractModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//@JsonDeserialize(using = ResponseDataDeserializer.class)
public class ResponseData extends AbstractResponeData {

	private List<AbstractModel> values;

	private int metaInfo;

	public int getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(int metaInfo) {
		this.metaInfo = metaInfo;
	}

	public List<AbstractModel> getValues() {
		return values;
	}

	public void setValues(List<AbstractModel> values) {
		this.values = values;
	}

}
