package com.amx.amxlib.model;

import com.amx.jax.model.AbstractModel;

public class LinkResponseModel extends AbstractModel implements Cloneable{
	private static final long serialVersionUID = 1L;
	private String linkId;	    	
		
	public LinkResponseModel() {
		
	}
	
	public String getLinkId() {
		return linkId;
	}
	
	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}	
}
