package com.amx.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class Constants.
 */
public class Constants {

	/**
	 * Instantiates a new constants.
	 */
	protected Constants() {
		// not allowed
	}

	/** The Constant BLANK. */
	public static final String BLANK = "";

	/** The Constant defaultBoolean. */
	public static final Boolean DEFAULT_BOOLEAN = Boolean.FALSE;

	/** The Constant defaultInteger. */
	public static final Integer DEFAULT_INTEGER = Integer.valueOf(0);

	/** The Constant defaultLong. */
	public static final Long DEFAULT_LONG = Long.valueOf(0L);

	/** The Constant defaultDouble. */
	public static final Double DEFAULT_DOUBLE = Double.valueOf(0D);

	/** The Constant defaultString. */
	public static final String DEFAULT_STRING = "";

	/** The Constant defaultObject. */
	public static final Object DEFAULT_OBJECT = new Object();

	/** The Constant emptyMap. */
	public static final Map<String, Object> EMPTY_MAP = Collections.unmodifiableMap(new HashMap<String, Object>());

	/** The Constant emptyList. */
	public static final List<Object> EMPTY_LIST = Collections.unmodifiableList(new ArrayList<Object>());

	/** The Constant emptyListOfList. */
	public static final List<List<Object>> EMPTY_LISTOF_LIST = Collections
			.unmodifiableList(new ArrayList<List<Object>>());

	/** The Constant emptyListOfListOfList. */
	public static final List<List<List<Object>>> EMPTY_LISTOF_LISTOF_LIST = Collections
			.unmodifiableList(new ArrayList<List<List<Object>>>());

	/** The Constant emptyStringList. */
	public static final List<String> EMPTY_STRING_LIST = Collections.unmodifiableList(new ArrayList<String>());

	/** The Constant CANCELLED_REQUEST. */
	public static final String CANCELLED_REQUEST = "04.03";

	/** The Constant CUSTOMERTYPE_INDU. */
	public static final String CUSTOMERTYPE_INDU = "Individual";

	/** The Constant CUST_ACTIVE_INDICATOR. */
	public static final String CUST_ACTIVE_INDICATOR = "Y";

	/** The Constant IDENTITY_TYPE_ID. */
	public static final String IDENTITY_TYPE_ID = "198";

	public static final String COMPNY_TYPE = "Corporate";

	public static final String COMMON_NATIONALITY = "ALL";

	public static final String COMPONENT_NAME = "Identity Type";

	public static final String NO = "N";
	
	/** The Constant CUST_DB_SCAN. */
	public static final String CUST_DB_SCAN = "D";

	public static class Common {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAILED = "FAILED";
		public static final String UNKNOWN = "UNKNOWN";
		public static final String YES = "YES";
		public static final String NO = "NO";
		public static final String FALSE = "FALSE";
		public static final String TRUE = "TRUE";
	}

}
