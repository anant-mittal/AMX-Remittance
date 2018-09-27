package com.amx.jax.model.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ImageSubmissionRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	private List<String> image;
	private String politicallyExposed;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

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

	@Override
	public String toString() {
		return "ImageSubmissionRequest [customerId=" + customerId + ", image=" + image + ", politicallyExposed="
				+ politicallyExposed + "]";
	}

}
