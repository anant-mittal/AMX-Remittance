package com.amx.jax.grid;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.slf4j.Logger;

import com.amx.jax.logger.LoggerService;
import com.amx.utils.ArgUtil;

public class GridInfo<T> {

	public static final Logger LOGGER = LoggerService.getLogger(GridInfo.class);

	public static <T> Map<String, String> map(Class<?> clazz, Map<String, String> fieldMap) {
		Class<?> tmpClass = clazz;
		do {
			Field[] f = tmpClass.getDeclaredFields();
			for (Field field : f) {
				Column y = field.getAnnotation(Column.class);
				if (!ArgUtil.isEmpty(y) && !ArgUtil.isEmpty(y.name()) && !ArgUtil.isEmpty(field.getName())) {
					LOGGER.info("UserSessionRecord : {} {}", field.getName(), y.name());
					fieldMap.put(field.getName(), y.name());
				}
			}
			tmpClass = tmpClass.getSuperclass();
		} while (tmpClass != null);
		return fieldMap;
	}

	public static <T> String query(Class<?> clazz, String query) {
		if (ArgUtil.isEmpty(query)) {
			Class<?> tmpClass = clazz;
			Table tableAnnot = tmpClass.getAnnotation(Table.class);
			if (!ArgUtil.isEmpty(tableAnnot) && !ArgUtil.isEmpty(tableAnnot.name())) {
				return String.format("SELECT *  FROM %s", tableAnnot.name());
			}
		}
		return query;
	}

	Map<String, String> map;
	String query;
	Class<T> resultClass;
	boolean customeQuery;

	GridInfo(String query, Class<T> resultClass) {
		if (!ArgUtil.isEmpty(query)) {
			this.customeQuery = true;
		}
		this.query = query(resultClass, query);
		this.resultClass = resultClass;
		this.map = map(resultClass, new HashMap<String, String>());
	}

	GridInfo(Class<T> resultClass, String query) {
		this(query, resultClass);
	}

	GridInfo(Class<T> resultClass) {
		this(null, resultClass);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Class<T> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<T> resultClass) {
		this.resultClass = resultClass;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public boolean isCustomeQuery() {
		return customeQuery;
	}

	public void setCustomeQuery(boolean customeQuery) {
		this.customeQuery = customeQuery;
	}

}
