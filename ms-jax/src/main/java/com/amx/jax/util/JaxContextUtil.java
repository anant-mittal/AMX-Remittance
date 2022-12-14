package com.amx.jax.util;

import java.util.HashMap;
import java.util.Map;

import com.amx.jax.constant.JaxEvent;

/**
 * The Class ContextUtil.
 */
/**
 * @author Prashant
 *
 */
public final class JaxContextUtil {

	/**
	 * Instantiates a new context util.
	 */
	private JaxContextUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/** The Constant JAX_EVENT. */
	public static final String JAX_EVENT = "jaxEvent";

	public static final String REQUEST_MODEL = "requestModel";

	/** The Constant context. */
	private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};

	/**
	 * Clear.
	 */
	public static void clear() {
		context.get().clear();
		context.remove();
	}

	/**
	 * @param event
	 *            - jaxevent
	 * 
	 */
	public static void setJaxEvent(JaxEvent event) {
		context.get().put(JAX_EVENT, event);
	}

	/**
	 * @return jaxevent
	 * 
	 */
	public static JaxEvent getJaxEvent() {
		return (JaxEvent) context.get().get(JAX_EVENT);
	}

	/**
	 * @return request model
	 * 
	 */
	public static Object getRequestModel() {
		return context.get().get(REQUEST_MODEL);
	}

	/**
	 * @param value
	 * 
	 */
	public static void setRequestModel(Object value) {
		context.get().put(REQUEST_MODEL, value);
	}
}