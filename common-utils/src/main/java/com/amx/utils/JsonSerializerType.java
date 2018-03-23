package com.amx.utils;

//TODO: Auto-generated Javadoc
/**
 * The Interface JsonSerializerType.
 *
 * @param <T>
 *            the generic type
 */
public interface JsonSerializerType<T> {

	/**
	 * Create the object value from object of type T.
	 *
	 * @return the object
	 */
	Object toObject();
}