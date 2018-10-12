package com.amx.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class ContextUtil.
 */
public final class ContextUtil {

	/**
	 * Instantiates a new context util.
	 */
	private ContextUtil() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/** The Constant TRACE_ID. */
	public static final String TRACE_ID = "traceId";

	/** The Constant context. */
	private static final ThreadLocal<Map<String, Object>> context = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<String, Object>();
		}
	};

	/**
	 * Gets the.
	 *
	 * @return the map
	 */
	public static Map<String, Object> map() {
		return context.get();
	}

	/**
	 * Sets the.
	 *
	 * @param map
	 *            the map
	 */
	public static void map(Map<String, Object> map) {
		context.set(map);
	}

	/**
	 * Clear.
	 */
	public static void clear() {
		context.get().clear();
		context.remove();
	}

	/**
	 * Sets the trace id.
	 *
	 * @param traceId
	 *            the new trace id
	 */
	public static void setTraceId(long traceId) {
		String trace_id = ArgUtil.parseAsString(traceId);
		if (trace_id == null) {
			trace_id = UniqueID.generateString();
		}
		setTraceId(trace_id);
	}

	/**
	 * Sets the trace id.
	 *
	 * @param trace_id
	 *            the new trace id
	 */
	public static void setTraceId(String trace_id) {
		context.get().put(TRACE_ID, trace_id);
	}

	/**
	 * Gets the trace id.
	 *
	 * @return the trace id
	 */
	public static String getTraceId() {
		return getTraceId(true);
	}

	/**
	 * Gets the trace id.
	 *
	 * @param generate
	 *            the generate
	 * @return the trace id
	 */
	public static String getTraceId(boolean generate) {
		String traceId = (String) context.get().get(TRACE_ID);
		if (traceId == null) {
			if (!generate) {
				return "";
			}
			traceId = UniqueID.generateString();
			context.get().put(TRACE_ID, traceId);
		}
		return traceId;
	}

	/**
	 * Gets the trace id.
	 *
	 * @param generate
	 *            the generate
	 * @param midfix
	 *            the midfix
	 * @return the trace id
	 */
	public static String getTraceId(boolean generate, String midfix) {
		String traceId = (String) context.get().get(TRACE_ID);
		if (traceId == null) {
			if (!generate) {
				return "";
			}
			traceId = UniqueID.generateSystemString(midfix);
			context.get().put(TRACE_ID, traceId);
		}
		return traceId;
	}

	public static String generateTraceId(String midfix) {
		String traceId = UniqueID.generateSystemString(midfix);
		context.get().put(TRACE_ID, traceId);
		return traceId;
	}

}
