package com.amx.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ArgUtil.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
/**
 * Utility function for parsing the objects as Integer, Long, Double, String,
 * Boolean, Date
 * 
 */
public final class ArgUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ArgUtil.class);

	/**
	 * Instantiates a new arg util.
	 */
	private ArgUtil() {
		// Sonar code fix --> Utility classes should not have a public of
		// default constructor
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/**
	 * The Interface EnumById.
	 */
	public static interface EnumById {
		/* must return uppercase letters */
		/**
		 * Gets the id.
		 *
		 * @return the id
		 */
		public String getId();
	}

	/**
	 * This method is called for generating the error message in case of Parameter
	 * missing or invalid exceptions.
	 *
	 * @param object
	 *            the object
	 * @return : String indicating the type of the given object
	 */
	public static String getType(Object object) {
		if (object instanceof Date) {
			return "date";
		} else if (object instanceof String) {
			return "string";
		} else if (object instanceof Double) {
			return "double";
		} else if (object instanceof Long) {
			return "long";
		} else if (object instanceof Integer) {
			return "integer";
		} else if (object instanceof Boolean) {
			return "boolean";
		} else if (object == Constants.EMPTY_MAP) {
			return "object";
		}
		return "unknown";
	}

	/**
	 * Gets the type enum.
	 *
	 * @param enumValue
	 *            the enum value
	 * @return the type enum
	 */
	public static String[] getTypeEnum(Enum enumValue) {
		ArrayList<String> list = new ArrayList<String>();
		for (Object element : enumValue.getClass().getEnumConstants()) {
			if (element instanceof EnumById) {
				list.add(((EnumById) element).getId().toLowerCase());
			} else {
				list.add(((Enum) element).name().toLowerCase());
			}
		}
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Parse as T.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @param required
	 *            the required
	 * @return the t
	 */
	public static <T> T parseAsT(Object value, T defaultValue, boolean required) {
		if (value == null) {
			if (!required) {
				return defaultValue;
			}
			throw ArgExceptions.paramMissingOrInvalid(null, null, defaultValue);
		}
		Object ret = value;
		if (defaultValue instanceof Boolean) {
			ret = parseAsBoolean(value);
		} else if (defaultValue instanceof Integer) {
			ret = parseAsInteger(value);
		} else if (defaultValue instanceof Long) {
			ret = parseAsLong(value);
		} else if (defaultValue instanceof Double) {
			ret = parseAsDouble(value);
		} else if (defaultValue instanceof String) {
			ret = parseAsString(value);
		} else if (defaultValue instanceof Enum) {
			ret = parseAsEnum(value, (Enum) defaultValue);
		} else if (defaultValue instanceof BigDecimal) {
			ret = parseAsBigDecimal(value, (BigDecimal) defaultValue);
		} else if (defaultValue instanceof Byte) {
			ret = Byte.parseByte((String) value);
		} else if (defaultValue instanceof Short) {
			ret = Short.parseShort((String) value);
		} else if (defaultValue instanceof Short) {
			ret = Short.parseShort((String) value);
		} else if (defaultValue instanceof Float) {
			ret = Float.parseFloat((String) value);
		}
		if (ret == null) {
			if (!required) {
				return defaultValue;
			}
			throw ArgExceptions.paramMissingOrInvalid(null, value, defaultValue);
		}
		return (T) ret;
	}

	public static Object parseAsObject(Class clazz, Object objectvalue) {
		String value = ArgUtil.parseAsString(objectvalue);
		if (Boolean.class == clazz)
			return Boolean.parseBoolean(value);
		if (Byte.class == clazz)
			return Byte.parseByte(value);
		if (Short.class == clazz)
			return Short.parseShort(value);
		if (Integer.class == clazz)
			return Integer.parseInt(value);
		if (Long.class == clazz)
			return Long.parseLong(value);
		if (Float.class == clazz)
			return Float.parseFloat(value);
		if (Double.class == clazz)
			return Double.parseDouble(value);
		if (BigDecimal.class == clazz)
			return parseAsBigDecimal(value);
		return value;
	}

	/**
	 * Parse as List &lt;T&gt;.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @param defaultListValue
	 *            the default list value
	 * @param required
	 *            the required
	 * @return the list
	 */
	public static <T> List<T> parseAsListOfT(Object value, T defaultValue, List<T> defaultListValue, boolean required) {
		if (value == null) {
			if (!required) {
				return defaultListValue;
			}
			throw ArgExceptions.paramMissingOrInvalid(null, null, defaultValue);

		}
		if (value instanceof Object[]) {
			List<Object> list = new ArrayList<Object>();
			CollectionUtil.addAll(list, value);
			value = list;
		}
		if (value instanceof List) {
			List<T> list = (List<T>) value;
			for (int i = 0; i < list.size(); i++) {
				list.set(i, parseAsT(list.get(i), defaultValue, required));
			}
			return list;
		}
		throw ArgExceptions.paramMissingOrInvalid(null, value, defaultValue);
	}

	/**
	 * Parse as List&lt;List&lt;T&gt;&gt;.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @param defaultListValue
	 *            the default list value
	 * @param defaultListOfListValue
	 *            the default list of list value
	 * @param required
	 *            the required
	 * @return the list
	 */
	public static <T> List<List<T>> parseAsListListOfT(Object value, T defaultValue, List<T> defaultListValue,
			List<List<T>> defaultListOfListValue, boolean required) {
		if (value == null) {
			if (!required) {
				return defaultListOfListValue;
			}
			throw ArgExceptions.paramMissingOrInvalid(null, null, defaultValue);
		}
		if (value instanceof List) {
			List<List<T>> list = (List<List<T>>) value;
			for (int i = 0; i < list.size(); i++) {
				list.set(i, parseAsListOfT(list.get(i), defaultValue, defaultListValue, required));
			}
			return list;
		}
		throw ArgExceptions.paramMissingOrInvalid(null, value, defaultValue);
	}

	/**
	 * Parse as List&lt;List&lt;List&lt;T&gt;&gt;&gt;.
	 *
	 * @param <T>
	 *            the generic type
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @param defaultListValue
	 *            the default list value
	 * @param defaultListListValue
	 *            the default list list value
	 * @param defaultListListOfListValue
	 *            the default list list of list value
	 * @param required
	 *            the required
	 * @return the list
	 */
	public static <T> List<List<List<T>>> parseAsListListListOfT(Object value, T defaultValue, List<T> defaultListValue,
			List<List<T>> defaultListListValue, List<List<List<T>>> defaultListListOfListValue, boolean required) {
		if (value == null) {
			if (!required) {
				return defaultListListOfListValue;
			}
			throw ArgExceptions.paramMissingOrInvalid(null, null, defaultValue);
		}
		if (value instanceof List) {
			List<List<List<T>>> list = (List<List<List<T>>>) value;
			for (int i = 0; i < list.size(); i++) {
				list.set(i, parseAsListListOfT(list.get(i), defaultValue, defaultListValue, defaultListListValue,
						required));
			}
			return list;
		}
		throw ArgExceptions.paramMissingOrInvalid(null, value, defaultValue);
	}

	/**
	 * <pre>
	 * Parses the given object as a Boolean
	 * 
	 * Formats Supported -
	 * 1) java.lang.Boolean
	 * 2) java.lang.Number (0 == Boolean.FALSE, rest all Boolean.TRUE) 
	 * 3) String ("true" / "false")
	 * </pre>
	 *
	 * @param value
	 *            the value
	 * @return : Boolean object if valid else null
	 */
	public static Boolean parseAsBoolean(Object value) {
		if (value instanceof Boolean) {
			return ((Boolean) value);
		} else if (value instanceof Number) {
			return Boolean.valueOf(((Number) value).intValue() != 0);
		} else if (value instanceof String) {
			return Boolean.valueOf(((String) value).equalsIgnoreCase("true"));
		}
		return null;
	}

	/**
	 * Parses the as boolean.
	 *
	 * @param value
	 *            the value
	 * @param nullValue
	 *            the null value
	 * @return the boolean
	 */
	public static Boolean parseAsBoolean(Object value, Boolean nullValue) {
		if (value == null) {
			return nullValue;
		}
		return parseAsBoolean(value);
	}

	/**
	 * <pre>
	 * Parses the given object as an Integer
	 * 
	 * Formats Supported -
	 * 1) java.lang.Integer
	 * 2) java.lang.Number 
	 * 3) String ("1" / "2" etc.)
	 * 4) String ("0x1231" / "0x1a3e" etc.) - Hexadecimal or base 16 if starts with 0x
	 * 5) String ("023567" / "011256" etc.) - Octal or base 8 if starts with 0
	 * </pre>
	 *
	 * @param value
	 *            the value
	 * @return : Integer object if valid else null
	 */
	public static Integer parseAsInteger(Object value) {
		if (value instanceof Integer) {
			return ((Integer) value);
		} else if (value instanceof Number) {
			return Integer.valueOf(((Number) value).intValue());
		} else if (value instanceof String) {
			try {
				if (((String) value).startsWith("0x")) {
					return Integer.valueOf(Integer.parseInt(((String) value).substring(2), 16));
				} else if (((String) value).startsWith("0") && ((String) value).length() > 1) {
					return Integer.valueOf(Integer.parseInt(((String) value).substring(1), 8));
				} else {
					return Integer.valueOf(Integer.parseInt((String) value));
				}
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * Parses the given object as an Long
	 * 
	 * Formats Supported -
	 * 1) java.lang.Long
	 * 2) java.lang.Number 
	 * 3) String ("1" / "2" etc.)
	 * 4) String ("0x1231" / "0x1a3e" etc.) - Hexadecimal or base 16 if starts with 0x
	 * 5) String ("023567" / "011256" etc.) - Octal or base 8 if starts with 0
	 * </pre>
	 *
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @return : Long object if valid else null
	 */
	public static Long parseAsLong(Object value, Long defaultValue) {
		if (value instanceof Long) {
			return ((Long) value);
		} else if (value instanceof Number) {
			return Long.valueOf(((Number) value).longValue());
		} else if (value instanceof String) {
			try {
				if (((String) value).startsWith("0x")) {
					return Long.valueOf(Long.parseLong(((String) value).substring(2), 16));
				} else if (((String) value).startsWith("0") && ((String) value).length() > 1) {
					return Long.valueOf(Long.parseLong(((String) value).substring(1), 8));
				} else {
					return Long.valueOf(Long.parseLong((String) value));
				}
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * @param value
	 *            the value
	 * @return the long
	 */
	public static Long parseAsLong(Object value) {
		return parseAsLong(value, null);
	}

	/**
	 * <pre>
	 * Parses the given object as an BigDecimal
	 * 
	 * Formats Supported -
	 * 1) java.lang.BigDecimal
	 * 2) String ("1" / "2" etc.)
	 * </pre>
	 * 
	 * @param value
	 * @param defaultValue
	 *            [optional, default : null]
	 * @return
	 */
	public static BigDecimal parseAsBigDecimal(Object value, BigDecimal defaultValue) {
		BigDecimal ret = defaultValue;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			}
		}
		return ret;
	}

	/**
	 * {@link ArgUtil#parseAsBigDecimal(Object, BigDecimal)}
	 * 
	 * @param value
	 * @return
	 */
	public static BigDecimal parseAsBigDecimal(Object value) {
		return parseAsBigDecimal(value, null);
	}

	/**
	 * <pre>
	 * Parses the given object as an Double
	 * 
	 * Formats Supported -
	 * 1) java.lang.Double
	 * 2) java.lang.Number 
	 * 3) String ("1" / "2" etc.)
	 * </pre>
	 *
	 * @param value
	 *            the value
	 * @return : Double object if valid else null
	 */

	public static Double parseAsDouble(Object value) {
		if (value instanceof Double) {
			return (Double) value;
		} else if (value instanceof Number) {
			return Double.valueOf(((Number) value).doubleValue());
		} else if (value instanceof String) {
			try {
				return Double.valueOf(Double.parseDouble((String) value));
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * Parses the given object as date. It keeps the time information intact.
	 * 
	 * Formats Supported 
	 * 1) java.util.Date
	 * 2) unix timestamp
	 * </pre>
	 *
	 * @param value
	 *            the value
	 * @return : Date object if valid else null
	 */
	public static Date parseAsSimpleDate(Object value) {
		if (value instanceof Date) {
			return (Date) value;
		} else if (value instanceof Number) {
			return new Date(((Number) value).longValue());
		}
		return null;
	}

	/**
	 * Returns object.toString() if the object is not null else returns null
	 *
	 * @param object
	 *            the object
	 * @return : Returns object.toString() if the object is not null else returns
	 *         null
	 */
	public static String parseAsString(Object object) {
		if (object != null) {
			return object.toString();
		}
		return null;
	}

	/**
	 * Parses the as string.
	 *
	 * @param object
	 *            the object
	 * @param defaultValue
	 *            - if passed value is null or empty then default is returned.
	 * @return the string
	 */
	public static String parseAsString(Object object, String defaultValue) {
		if (object == null || Constants.DEFAULT_STRING.equals(object)) {
			return defaultValue;
		}
		return parseAsString(object);
	}

	/**
	 * Parses the as string.
	 *
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String[] parseAsStringArray(Object object) {
		String str = parseAsString(object, Constants.BLANK);
		return str.split(",");
	}

	/**
	 * Parses the as enum.
	 *
	 * @param value
	 *            the value
	 * @param defaultValue
	 *            the default value
	 * @return the enum
	 */
	public static Enum parseAsEnum(Object value, Enum defaultValue) {
		String enumString = parseAsString(value);
		if (enumString == null) {
			return null;
		}
		String enumStringCaps = enumString.toUpperCase();
		if (defaultValue instanceof EnumById) {
			for (Object enumValue : defaultValue.getClass().getEnumConstants()) {
				if (enumString.equals(((EnumById) enumValue).getId())
						|| enumStringCaps.equals(((EnumById) enumValue).getId())) {
					return (Enum) enumValue;
				}
			}
			return defaultValue;
		}
		try {
			return Enum.valueOf(defaultValue.getClass(), enumString);
		} catch (IllegalArgumentException e) {
			try {
				return Enum.valueOf(defaultValue.getClass(), enumStringCaps);
			} catch (IllegalArgumentException e2) {
				return defaultValue;
			}
		}
	}

	public static Enum parseAsEnum(Object value, Type type) {
		try {
			String enumString = parseAsString(value);
			if (enumString == null) {
				return null;
			}
			Class clazz = (Class) type;
			if (clazz.isEnum()) {
				return Enum.valueOf(clazz, enumString);
			}
		} catch (Exception e) {
			LOGGER.error("Enum Cast Exception", e);
		}
		return null;
	}

	public static <T extends Enum<T>> T[] parseAsEnumArray(Object value, Type componentType) {
		Class<T> type = (Class<T>) componentType;
		String[] str = ArgUtil.parseAsStringArray(value);
		Object o = Array.newInstance(type, str.length);
		for (int i = 0; i < str.length; i++) {
			Array.set(o, i, ArgUtil.parseAsEnum(str[i].toUpperCase(), componentType));
		}
		return (T[]) o;
	}

	/**
	 * Checks if is object empty.
	 *
	 * @param object
	 *            the object
	 * @return true, if is object empty
	 */
	public static boolean isEmpty(Object object) {
		if (object == null)
			return true;
		else if (object instanceof String) {
			if (((String) object).trim().length() == 0) {
				return true;
			}
		} else if (object instanceof Collection) {
			return ArgUtil.isCollectionEmpty((Collection<?>) object);
		}
		return false;
	}

	/**
	 * Checks if is collection empty.
	 *
	 * @param collection
	 *            the collection
	 * @return true, if is collection empty
	 */
	static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if is empty string.
	 *
	 * @param str
	 *            the str
	 * @return true, if is empty string
	 */
	public static boolean isEmptyString(String str) {
		return (str == null || Constants.BLANK.equals(str));
	}

	public static boolean areEqual(Object a, Object b) {
		if (a == null || b == null) {
			return (a == null && b == null);
		}
		String strA = parseAsString(a, Constants.BLANK);
		String strB = parseAsString(b, Constants.BLANK);
		return strA.equals(strB);
	}

	public static <T> T ifNotEmpty(T... strs) {
		for (T str : strs) {
			if (!isEmpty(str)) {
				return str;
			}
		}
		return null;
	}

}
