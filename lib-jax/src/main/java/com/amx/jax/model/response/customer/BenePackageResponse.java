package com.amx.jax.model.response.customer;

import java.util.List;

import com.amx.jax.model.response.remittance.ParameterDetailsDto;

public class BenePackageResponse {
	private List<ParameterDetailsDto> packages;

	public List<ParameterDetailsDto> getPackages() {
		return packages;
	}

	public void setPackages(List<ParameterDetailsDto> packages) {
		this.packages = packages;
	}
}
