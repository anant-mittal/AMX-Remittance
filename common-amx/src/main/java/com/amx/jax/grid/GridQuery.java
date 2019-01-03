package com.amx.jax.grid;

import java.util.List;

import com.amx.jax.swagger.ApiMockModelProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GridQuery {

	@ApiMockModelProperty(example = "0", value = "page")
	private Integer pageNo;

	@ApiMockModelProperty(example = "5", value = "Length of page")
	private Integer pageSize;

	@ApiMockModelProperty(example = "", value = "Global Search String")
	private String search;

	@ApiMockModelProperty(example = "false", value = "false,if pagination is not required, faster")
	private boolean paginated;

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

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer start) {
		this.pageNo = start;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer length) {
		this.pageSize = length;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public boolean isPaginated() {
		return paginated;
	}

	public void setPaginated(boolean paginated) {
		this.paginated = paginated;
	}

}
