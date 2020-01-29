package com.amx.jax;

import java.util.Map;

public interface AppSharedConfig {
	default void clear(Map<String, String> map) {
		// DO NOTHING
	};

	default String name() {
		return null;
	};
}
