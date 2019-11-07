package com.amx.jax.model.request.tpc;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.jax.swagger.ApiMockModelProperty;

public class TpcCustomerLoginRequest {

	@NotNull
	@ApiMockModelProperty(example = "284052306594")
	String identityInt;

	@NotNull
	@Pattern(regexp = "^[0-9]+$", message = "Invalid mobile number")
	@Size(min = 1)
	@ApiMockModelProperty(example = "66670196")
	String mobileNumber;

	@NotNull
	@Pattern(regexp = "^[0-9]+$", message = "invalid prefix code")
	@Size(min = 1)
	@ApiMockModelProperty(example = "965")
	String prefix;

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "TpcCustomerLoginRequest [identityInt=" + identityInt + ", mobileNumber=" + mobileNumber + ", prefix="
				+ prefix + "]";
	}

}
