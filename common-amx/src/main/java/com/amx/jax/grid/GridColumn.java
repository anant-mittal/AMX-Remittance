package com.amx.jax.grid;

import com.amx.jax.grid.GridConstants.ColumnFunction;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridColumn {

	private int index;

	@ApiMockModelProperty(example = "", value = "nameKey for column")
	private String key;

	/** The name. */
	private String name;

	/** The searchable. */
	private boolean searchable;

	@ApiMockModelProperty(example = "", value = "Search String")
	private String search;

	@ApiMockModelProperty(example = "EQ", value = "GT, GTE, EQ, STE, ST")
	private FilterOperater operator;

	@ApiMockModelProperty(example = "STRING", value = "STRING, NUMBER, DATE, TIME, TIMESTAMP")
	private FilterDataType dataType;

	@ApiMockModelProperty(example = "", value = "Exact Value to Filter")
	private String value;

	/** The sort dir. */
	private SortOrder sortDir;

	private ColumnFunction func;

	public ColumnFunction getFunc() {
		return func;
	}

	public void setFunc(ColumnFunction func) {
		this.func = func;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets the data.
	 *
	 * @param data
	 *            the data to set
	 */
	public void setKey(String data) {
		this.key = data;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks if is searchable.
	 *
	 * @return the searchable
	 */
	@JsonIgnore
	public boolean isSearchable() {
		return searchable;
	}

	/**
	 * Sets the searchable.
	 *
	 * @param searchable
	 *            the searchable to set
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * Gets the search.
	 *
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * Sets the search.
	 *
	 * @param search
	 *            the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * Gets the sort dir.
	 *
	 * @return the sortDir
	 */
	public SortOrder getSortDir() {
		return sortDir;
	}

	/**
	 * Sets the sort dir.
	 *
	 * @param sortDir
	 *            the sortDir to set
	 */
	public void setSortDir(SortOrder sortDir) {
		this.sortDir = sortDir;
	}

	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	@JsonIgnore
	public int getIndex() {
		return index;
	}

	/**
	 * Sets the index.
	 *
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FilterOperater getOperator() {
		return operator;
	}

	public void setOperator(FilterOperater operator) {
		this.operator = operator;
	}

	public FilterDataType getDataType() {
		return dataType;
	}

	public void setDataType(FilterDataType dataType) {
		this.dataType = dataType;
	}

}