package com.amx.amxlib.model.response;

import com.amx.jax.api.BoolRespModel;

@Deprecated
public class BooleanResponse extends BoolRespModel {
	private static final long serialVersionUID = -7761028175728771365L;

	public BooleanResponse(boolean success) {
		super(true);
	}

}
