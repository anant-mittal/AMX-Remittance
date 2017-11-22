package com.amx.amxlib.model.response;

import java.util.List;

import com.amx.amxlib.model.AbstractModel;

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
