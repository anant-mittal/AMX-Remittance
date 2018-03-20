package com.amx.jax.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author lalittanwar
 *
 * @param <E>
 *            Must have toString() function which returns unique id for each
 *            member;
 * @param <T>
 */
public abstract class ScopedBeanFactory<E, T> {

	private Logger log = LoggerFactory.getLogger(getClass());

	private final Map<String, T> libsByCode = new HashMap<>();

	public ScopedBeanFactory(List<T> beans) {
		for (T bean : beans) {
			E[] keys = getKeys(bean);
			for (E key : keys) {
				if (key != null) {
					register(key, bean);
				}
			}
		}
	}

	/**
	 * Returns keys against bean which will be used to map against it
	 * 
	 * @param bean
	 * @return
	 */
	abstract public E[] getKeys(T bean);

	/**
	 * Returns key against bean which is either default or current;
	 * 
	 * @param bean
	 * @return
	 */
	abstract public E getKey();

	/**
	 * Register key and bean
	 * 
	 * @param key
	 * @param bean
	 */
	public void register(E key, T bean) {
		this.libsByCode.put(key.toString().toLowerCase(), bean);
	}

	/**
	 * Returns bean against key
	 * 
	 * @param key
	 * @return
	 */
	public T get(E key) {
		if (this.libsByCode.containsKey(key.toString().toLowerCase())) {
			return this.libsByCode.get(key.toString().toLowerCase());
		}
		log.error("libsByCode Not Exists for Code== " + key);
		return null;
	}

	/**
	 * Returns current Value of Key
	 * 
	 * @return
	 */
	public T get() {
		return this.get(getKey());
	}

}
