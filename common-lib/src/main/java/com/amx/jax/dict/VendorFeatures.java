package com.amx.jax.dict;

import com.amx.jax.types.Pnum;
import com.amx.utils.EnumType;

public class VendorFeatures extends Pnum implements EnumType {

	public VendorFeatures(String name, int ordinal) {
		super(name, ordinal);
	}

	/**
	 * Explicit definition of values() is needed here to trigger static initializer.
	 * 
	 * @return
	 */
	public static VendorFeatures[] values() {
		return values(VendorFeatures.class);
	}

	public static VendorFeatures valueOf(String name) {
		return fromString(VendorFeatures.class, name);
	}

	static {
		init(VendorFeatures.class);
	}

}
