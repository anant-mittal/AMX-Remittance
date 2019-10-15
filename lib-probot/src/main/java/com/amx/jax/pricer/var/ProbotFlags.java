package com.amx.jax.pricer.var;

import com.amx.utils.BitFlags;

public class ProbotFlags extends BitFlags {

	public final BitFlags.Field isPrivate = this.new Field(0, 1);
	
	public ProbotFlags(long value) {
		super(value);
	}

}
