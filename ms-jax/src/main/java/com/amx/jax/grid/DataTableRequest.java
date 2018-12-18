package com.amx.jax.grid;

import java.util.ArrayList;
import java.util.List;

import com.amx.utils.ArgUtil;

public class DataTableRequest {

	/** The unique id. */
	private String uniqueId;

	/** The draw. */
	private String draw;

	/** The start. */
	private Integer start;

	/** The length. */
	private Integer length;

	/** The search. */
	private String search;

	/** The regex. */
	private boolean regex;

	/** The columns. */
	private List<DataTableColumnSpecs> columns;

	/** The order. */
	private DataTableColumnSpecs order;

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
	 * Gets the unique id.
	 *
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * Sets the unique id.
	 *
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
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
	 * Checks if is regex.
	 *
	 * @return the regex
	 */
	public boolean isRegex() {
		return regex;
	}

	/**
	 * Sets the regex.
	 *
	 * @param regex the regex to set
	 */
	public void setRegex(boolean regex) {
		this.regex = regex;
	}

	/**
	 * Gets the columns.
	 *
	 * @return the columns
	 */
	public List<DataTableColumnSpecs> getColumns() {
		return columns;
	}

	/**
	 * Sets the columns.
	 *
	 * @param columns the columns to set
	 */
	public void setColumns(List<DataTableColumnSpecs> columns) {
		this.columns = columns;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public DataTableColumnSpecs getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 *
	 * @param order the order to set
	 */
	public void setOrder(DataTableColumnSpecs order) {
		this.order = order;
	}

	/**
	 * Gets the draw.
	 *
	 * @return the draw
	 */
	public String getDraw() {
		return draw;
	}

	/**
	 * Sets the draw.
	 *
	 * @param draw the draw to set
	 */
	public void setDraw(String draw) {
		this.draw = draw;
	}

	/**
	 * Checks if is global search.
	 *
	 * @return the isGlobalSearch
	 */
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

		this.setStart(gridQuery.getStart());
		this.setLength(gridQuery.getLength());
		this.setUniqueId(gridQuery.getUniqueId());
		this.setDraw(gridQuery.getDraw());

		this.setSearch(gridQuery.getSearch());
		this.setRegex(gridQuery.isRegex());

		int sortableCol = gridQuery.getSortBy();

		List<DataTableColumnSpecs> columns = new ArrayList<DataTableColumnSpecs>();

		if (!GridUtil.isObjectEmpty(this.getSearch())) {
			this.setGlobalSearch(true);
		}

		int maxParamsToCheck = gridQuery.getColumns().size();

		for (int i = 0; i < maxParamsToCheck; i++) {
			DataTableColumnSpecs colSpec = gridQuery.getColumns().get(i);
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
			sortBy.addSort(this.getOrder().getData(), this.getOrder().getSortDir());
		}

		FilterBy filterBy = new FilterBy();
		filterBy.setGlobalSearch(this.isGlobalSearch());
		for (DataTableColumnSpecs colSpec : this.getColumns()) {
			if (colSpec.isSearchable()) {
				if (!GridUtil.isObjectEmpty(this.getSearch()) || !GridUtil.isObjectEmpty(colSpec.getSearch())) {
					filterBy.addFilter(colSpec.getData(),
							(this.isGlobalSearch()) ? this.getSearch() : colSpec.getSearch());
				}
			}
		}

		pagination.setSortBy(sortBy);
		pagination.setFilterBy(filterBy);

		return pagination;
	}
}