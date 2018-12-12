package com.amx.jax.model.request;

import java.io.Serializable;
import java.util.List;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ImageSubmissionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<String> image;
	
	@ApiMockModelProperty(example="Y")
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
