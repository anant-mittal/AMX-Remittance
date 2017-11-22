package com.amx.amxlib.model.response;

import java.util.List;


//@JsonDeserialize(using = ResponseDataDeserializer.class)
public class ResponseData extends AbstractResponeData {

	private List<Object> values;

	private int metaInfo;

	public int getMetaInfo() {
		return metaInfo;
	}

	public void setMetaInfo(int metaInfo) {
		this.metaInfo = metaInfo;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

}
