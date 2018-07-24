package com.amx.jax.types;

public class WritersDynaEnum extends PropertiesDnum {
	static {
		init(WritersDynaEnum.class);
	}
	private final String nationality;

	protected WritersDynaEnum(String name, int ordinal, String nationality) {
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
	public static WritersDynaEnum[] values() {
		return values(WritersDynaEnum.class);
	}

}
