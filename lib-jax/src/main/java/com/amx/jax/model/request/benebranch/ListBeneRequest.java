package com.amx.jax.model.request.benebranch;

import javax.validation.constraints.Min;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ListBeneRequest {

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
