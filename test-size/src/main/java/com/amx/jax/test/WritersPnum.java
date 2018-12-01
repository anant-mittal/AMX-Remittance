package com.amx.jax.test;

import com.amx.jax.types.Pnum;

public class WritersPnum extends Pnum {

	public static final WritersPnum AMIT = new WritersPnum("AMITT", 0, "TT");

	private final String nationality;

	protected WritersPnum(String name, int ordinal, String nationality) {
		super(name, ordinal);
		this.nationality = nationality;
	}

	public String nationality() {
		return nationality;
	}

	/**
	 * Explicit definition of values() is needed here to trigger static initializer.
	 * 
	 * @return
	 */
	public static WritersPnum[] values() {
		return values(WritersPnum.class);
	}

	public static WritersPnum valueOf(String name) {
		return fromString(WritersPnum.class, name);
	}

	static {
		init(WritersPnum.class);
	}

}
