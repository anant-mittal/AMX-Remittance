package com.amx.jax.polls;

import com.fasterxml.jackson.annotation.JsonInclude;

//@Document(indexName = "polls", type = "vote")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Vote {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String vseries;

	public String getVseries() {
		return vseries;
	}

	public void setVseries(String vseries) {
		this.vseries = vseries;
	}

	public String getVcategory() {
		return vcategory;
	}

	public void setVcategory(String vcategory) {
		this.vcategory = vcategory;
	}

	public String getVtitle() {
		return vtitle;
	}

	public void setVtitle(String vtitle) {
		this.vtitle = vtitle;
	}

	public String getVuid() {
		return vuid;
	}

	public void setVuid(String vuid) {
		this.vuid = vuid;
	}

	public String getVdid() {
		return vdid;
	}

	public void setVdid(String vdid) {
		this.vdid = vdid;
	}

	public String getVip() {
		return vip;
	}

	public void setVip(String vip) {
		this.vip = vip;
	}

	public String getVoption() {
		return voption;
	}

	public void setVoption(String voption) {
		this.voption = voption;
	}

	private String vcategory;
	private String vtitle;

	private String vuid;
	private String vdid;
	private String vip;

	private String voption;

}
