package com.amx.jax.model.request.benebranch;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ListBeneRequest {

	@NotNull
	@ApiMockModelProperty("94")
	@Min(1)
	Integer beneCoutryId;

	public Integer getBeneCoutryId() {
		return beneCoutryId;
	}

	public void setBeneCoutryId(Integer beneCoutryId) {
		this.beneCoutryId = beneCoutryId;
	}

}
