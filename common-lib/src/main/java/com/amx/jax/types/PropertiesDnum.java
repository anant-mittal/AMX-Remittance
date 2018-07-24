package com.amx.jax.types;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;

public class PropertiesDnum extends Dnum<PropertiesDnum> {
	protected PropertiesDnum(String name, int ordinal) {
		super(name, ordinal);
	}

	public static <E> Dnum<? extends Dnum<?>>[] values() {
		return values(PropertiesDnum.class);
	}

	protected static <E> void init(Class<E> clazz) {
		try {
			initProps(clazz);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	private static <E> void initProps(Class<E> clazz) throws Exception {
		String rcName = clazz.getName().replace('.', '/') + ".properties";
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(rcName)));

		Constructor<E> minimalConstructor = getConstructor(clazz, new Class[] { String.class, int.class });
		Constructor<E> additionalConstructor = getConstructor(clazz,
				new Class[] { String.class, int.class, String.class });
		int ordinal = 0;
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			line = line.replaceFirst("#.*", "").trim();
			if (line.equals("")) {
				continue;
			}
			String[] parts = line.split("\\s*=\\s*");
			if (parts.length == 1 || additionalConstructor == null) {
				minimalConstructor.newInstance(parts[0], ordinal);
			} else {
				additionalConstructor.newInstance(parts[0], ordinal, parts[1]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static <E> Constructor<E> getConstructor(Class<E> clazz, Class<?>[] argTypes) {
		for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
			try {
				return (Constructor<E>) c.getDeclaredConstructor(String.class, int.class, String.class);
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}
}
