package com.amx.jax.test;

import com.amx.jax.types.Dnum;

public class DigitsDnum extends Dnum<DigitsDnum> {
	public final static DigitsDnum ZERO = new DigitsDnum("ZERO", 0);
	public final static DigitsDnum ONE = new DigitsDnum("ONE", 1);
	public final static DigitsDnum TWO = new DigitsDnum("TWO", 2);
	public final static DigitsDnum THREE = new DigitsDnum("THREE", 3);

	public DigitsDnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		return values(DigitsDnum.class);
	}

}