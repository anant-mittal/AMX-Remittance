package com.amx.jax.io;

import com.amx.utils.JsonUtil;

public interface JSONable {

	default String toJSON() {
		return JsonUtil.toJson(this);
	}

}
