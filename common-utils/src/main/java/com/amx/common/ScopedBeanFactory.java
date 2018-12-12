package com.amx.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.utils.ContextUtil;

/**
 * A factory for creating ScopedBean objects.
 *
 * @author lalittanwar
 * @param <E> Must have toString() function which returns unique id for each
 *        member;
 * @param <T> the generic type
 */
public abstract class ScopedBeanFactory<E, T> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8272275904163644429L;

	/** The logger. */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	/** The libs by code. */
	private final Map<String, T> libsByCode = new HashMap<>();

	/**
	 * Instantiates a new scoped bean factory.
	 *
	 * @param beans the beans
	 */
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
	 * Returns keys against bean which will be used to map against it.
	 *
	 * @param bean the bean
	 * @return the keys
	 */
	abstract public E[] getKeys(T bean);

	/**
	 * Returns key against bean which is either default or current, set by setKey.
	 *
	 * @return the key
	 */
	@SuppressWarnings("unchecked")
	public E getKey() {
		return (E) ContextUtil.map().get(this.getClass().getName());
	};

	/**
	 * 
	 * @param serviceCode
	 */
	public void setKey(E key) {
		ContextUtil.map().put(this.getClass().getName(), key);
	}

	/**
	 * Register key and bean.
	 *
	 * @param key  the key
	 * @param bean the bean
	 */
	public void register(E key, T bean) {
		this.libsByCode.put(key.toString().toLowerCase(), bean);
	}

	/**
	 * Returns bean against key.
	 *
	 * @param key the key
	 * @return the t
	 */
	public T get(E key) {
		if (this.libsByCode.containsKey(key.toString().toLowerCase())) {
			return this.libsByCode.get(key.toString().toLowerCase());
		}
		LOGGER.error("libsByCode Not Exists for Code== {}", key);
		return null;
	}

	/**
	 * Returns current Value of Key.
	 *
	 * @return the t
	 */
	public T get() {
		return this.get(getKey());
	}

}
