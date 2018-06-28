package com.amx.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {

	protected Constants() {
		// not allowed
	}

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

	public static final String CUSTOMERTYPE_INDU = "Individual";

	public static final String CUST_ACTIVE_INDICATOR = "Y";

	public static final String IDENTITY_TYPE_ID = "198";
}
