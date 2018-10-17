package com.amx.common;


/**
 * The Interface ScoppedTarget.
 */
public @interface ScoppedTarget {
	
	/**
	 * Value.
	 *
	 * @return the class<? extends enum<?>>
	 */
	Class<? extends Enum<?>> value();
}
