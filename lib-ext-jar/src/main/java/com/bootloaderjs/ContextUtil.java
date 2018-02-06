package com.bootloaderjs;

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
	public static final String TRACE_ID = "trace_id";

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
		context.remove();
	}

	/**
	 * Sets the trace id.
	 *
	 * @param traceId
	 *            the new trace id
	 */
	public static void setTraceId(long traceId) {
		context.get().put(TRACE_ID, Long.valueOf(traceId));
	}

	/**
	 * Sets the trace id.
	 *
	 * @param trace_id
	 *            the new trace id
	 */
	public static void setTraceId(String trace_id) {
		Long traceId = ArgUtil.parseAsLong(trace_id);
		if (traceId == null) {
			traceId = Long.valueOf(UniqueID.generate());
		}
		setTraceId(traceId.longValue());
	}

	/**
	 * Gets the trace id.
	 *
	 * @return the trace id
	 */
	public static long getTraceId() {
		return getTraceId(true);
	}

	/**
	 * Gets the trace id.
	 *
	 * @param generate
	 *            the generate
	 * @return the trace id
	 */
	public static long getTraceId(boolean generate) {
		Long traceId = (Long) context.get().get(TRACE_ID);
		if (traceId == null) {
			if (!generate) {
				return 0L;
			}
			traceId = Long.valueOf(UniqueID.generate());
			context.get().put(TRACE_ID, traceId);
		}
		return traceId.longValue();
	}

}