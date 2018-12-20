package com.amx.jax.grid;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SortBy.
 * 
 * @author pavan.solapure
 */
public class FilterBy {

	/** The map of sorts. */
	private Map<String, String> mapOfLikeFilters;
	private Map<String, String> mapOfWhereFilters;

	/** The global search. */
	private boolean globalSearch;

	/**
	 * Instantiates a new sort by.
	 */
	public FilterBy() {
		if (null == mapOfLikeFilters) {
			mapOfLikeFilters = new HashMap<String, String>();
		}
		if (null == mapOfWhereFilters) {
			mapOfWhereFilters = new HashMap<String, String>();
		}
	}

	/**
	 * Gets the map of filters.
	 *
	 * @return the mapOfFilters
	 */
	public Map<String, String> getMapOfLikeFilters() {
		return mapOfLikeFilters;
	}

	/**
	 * Sets the map of filters.
	 *
	 * @param mapOfFilters the mapOfFilters to set
	 */
	public void setMapOfFilters(Map<String, String> mapOfFilters) {
		this.mapOfLikeFilters = mapOfFilters;
	}

	/**
	 * Adds the sort.
	 *
	 * @param filterColumn the filter column
	 * @param filterValue  the filter value
	 */
	public void addSearchFilter(String filterColumn, String filterValue) {
		mapOfLikeFilters.put(filterColumn, filterValue);
	}

	public void addWhereFilter(String filterColumn, String filterValue) {
		mapOfWhereFilters.put(filterColumn, filterValue);
	}

	/**
	 * Checks if is global search.
	 *
	 * @return the globalSearch
	 */
	public boolean isGlobalSearch() {
		return globalSearch;
	}

	/**
	 * Sets the global search.
	 *
	 * @param globalSearch the globalSearch to set
	 */
	public void setGlobalSearch(boolean globalSearch) {
		this.globalSearch = globalSearch;
	}

	public Map<String, String> getMapOfWhereFilters() {
		return mapOfWhereFilters;
	}

	public void setMapOfWhereFilters(Map<String, String> mapOfWhereFilters) {
		this.mapOfWhereFilters = mapOfWhereFilters;
	}

}
