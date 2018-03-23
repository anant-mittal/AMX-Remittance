package com.amx.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
	public static final String BLANK = "";

	/** The Constant defaultBoolean. */
	public static final Boolean defaultBoolean = Boolean.FALSE;

	/** The Constant defaultInteger. */
	public static final Integer defaultInteger = Integer.valueOf(0);

	/** The Constant defaultLong. */
	public static final Long defaultLong = Long.valueOf(0L);

	/** The Constant defaultDouble. */
	public static final Double defaultDouble = Double.valueOf(0D);

	/** The Constant defaultString. */
	public static final String defaultString = "";

	/** The Constant defaultObject. */
	public static final Object defaultObject = new Object();

	/** The Constant emptyMap. */
	public static final Map<String, Object> emptyMap = Collections.unmodifiableMap(new HashMap<String, Object>());

	/** The Constant emptyList. */
	public static final List<Object> emptyList = Collections.unmodifiableList(new ArrayList<Object>());

	/** The Constant emptyListOfList. */
	public static final List<List<Object>> emptyListOfList = Collections
			.unmodifiableList(new ArrayList<List<Object>>());

	/** The Constant emptyListOfListOfList. */
	public static final List<List<List<Object>>> emptyListOfListOfList = Collections
			.unmodifiableList(new ArrayList<List<List<Object>>>());

	/** The Constant emptyStringList. */
	public static final List<String> emptyStringList = Collections.unmodifiableList(new ArrayList<String>());

	/** The Constant CANCELLED_REQUEST. */
	public static final String CANCELLED_REQUEST = "04.03";
}
