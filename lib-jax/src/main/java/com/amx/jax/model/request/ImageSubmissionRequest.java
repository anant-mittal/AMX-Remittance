package com.amx.jax.model.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ImageSubmissionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<String> image;
	
	@ApiModelProperty(example="Y")
	private String politicallyExposed;	

	public List<String> getImage() {
		return image;
	}

	public void setImage(List<String> image) {
		this.image = image;
	}

	public String getPoliticallyExposed() {
		return politicallyExposed;
	}

	public void setPoliticallyExposed(String politicallyExposed) {
		this.politicallyExposed = politicallyExposed;
	}	
}
