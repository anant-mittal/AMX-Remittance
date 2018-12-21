package com.amx.jax.grid;

import java.util.List;

import com.amx.utils.ArgUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class DataTableRequest {

	/** The start. */
	private Integer start;

	/** The length. */
	private Integer length;

	/** The search. */
	private String search;

	/** The columns. */
	private List<GridColumn> columns;

	/** The order. */
	private GridColumn order;

	/** The is global search. */
	private boolean isGlobalSearch;

	/**
	 * Instantiates a new data table request.
	 *
	 * @param request the request
	 */
	public DataTableRequest(GridQuery gridQuery) {
		prepareDataTableRequest(gridQuery);
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 *
	 * @param start the start to set
	 */
	public void setStart(Integer start) {
		this.start = start;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
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
	 * @param search the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public List<GridColumn> getColumns() {
		return columns;
	}

	/**
	 * Sets the columns.
	 *
	 * @param columns the columns to set
	 */
	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public GridColumn getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 *
	 * @param order the order to set
	 */
	public void setOrder(GridColumn order) {
		this.order = order;
	}

	/**
	 * Checks if is global search.
	 *
	 * @return the isGlobalSearch
	 */
	@JsonIgnore
	public boolean isGlobalSearch() {
		return isGlobalSearch;
	}

	/**
	 * Sets the global search.
	 *
	 * @param isGlobalSearch the isGlobalSearch to set
	 */
	public void setGlobalSearch(boolean isGlobalSearch) {
		this.isGlobalSearch = isGlobalSearch;
	}

	/**
	 * Prepare data table request.
	 *
	 * @param request the request
	 */
	private void prepareDataTableRequest(GridQuery gridQuery) {

		this.setStart(gridQuery.getPageNo());
		this.setLength(gridQuery.getPageSize());

		this.setSearch(gridQuery.getSearch());

		int sortableCol = gridQuery.getSortBy();

		if (!GridUtil.isObjectEmpty(this.getSearch())) {
			this.setGlobalSearch(true);
		}

		int maxParamsToCheck = gridQuery.getColumns().size();

		for (int i = 0; i < maxParamsToCheck; i++) {
			GridColumn colSpec = gridQuery.getColumns().get(i);
			colSpec.setIndex(i);
			if (i == sortableCol) {
				colSpec.setSortDir(gridQuery.getSortOrder());
				this.setOrder(colSpec);
			}
			if (!ArgUtil.isEmpty(colSpec.getSearch())) {
				this.setGlobalSearch(false);
			}
		}

		if (!ArgUtil.isEmpty(gridQuery.getColumns())) {
			this.setColumns(gridQuery.getColumns());
		}
	}

	/**
	 * Gets the pagination request.
	 *
	 * @return the pagination request
	 */
	public PaginationCriteria getPaginationRequest() {

		PaginationCriteria pagination = new PaginationCriteria();
		pagination.setPageNumber(this.getStart());
		pagination.setPageSize(this.getLength());

		SortBy sortBy = null;
		if (!GridUtil.isObjectEmpty(this.getOrder())) {
			sortBy = new SortBy();
			sortBy.addSort(this.getOrder().getKey(), this.getOrder().getSortDir());
		}

		FilterBy filterBy = new FilterBy();
		filterBy.setGlobalSearch(this.isGlobalSearch());
		if (!ArgUtil.isEmpty(this.getColumns())) {
			for (GridColumn colSpec : this.getColumns()) {
				if (colSpec.isSearchable() || !ArgUtil.isEmpty(colSpec.getSearch())) {
					if (!GridUtil.isObjectEmpty(this.getSearch()) || !GridUtil.isObjectEmpty(colSpec.getSearch())) {
						filterBy.addSearchFilter(colSpec.getKey(),
								(this.isGlobalSearch()) ? this.getSearch() : colSpec.getSearch());
					}
				}

				if (!ArgUtil.isEmpty(colSpec.getValue())) {
					filterBy.addWhereFilter(colSpec.getKey(), colSpec.getValue());
				}
			}
		}
		pagination.setSortBy(sortBy);
		pagination.setFilterBy(filterBy);

		return pagination;
	}
}