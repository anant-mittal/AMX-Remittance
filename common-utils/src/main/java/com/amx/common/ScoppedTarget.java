package com.amx.common;

public @interface ScoppedTarget {
	Class<? extends Enum<?>> value();
}
