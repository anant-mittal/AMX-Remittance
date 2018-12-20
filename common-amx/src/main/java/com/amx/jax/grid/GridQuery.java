package com.amx.jax.grid;

import java.util.List;

public class GridQuery {
	private Integer start;
	private Integer length;

	private String search;

	List<GridColumn> columns;
	int sortBy;
	SortOrder sortOrder;

	public List<GridColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}

	public int getSortBy() {
		return sortBy;
	}

	public void setSortBy(int sortBy) {
		this.sortBy = sortBy;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

}
