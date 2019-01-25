package com.amx.jax.radar.jobs.customer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.grid.GridColumn;
import com.amx.jax.grid.GridQuery;
import com.amx.jax.grid.GridService;
import com.amx.jax.grid.SortOrder;
import com.amx.jax.grid.GridConstants.FilterDataType;
import com.amx.jax.grid.GridConstants.FilterOperater;
import com.amx.jax.radar.ARadarTask;
import com.amx.jax.radar.ESRepository;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.Constants;

public abstract class AbstractDBSyncTask extends ARadarTask {

	@Autowired
	public AppConfig appConfig;

	@Autowired
	public ESRepository esRepository;

	@Autowired
	public GridService gridService;

	@Autowired
	public OracleVarsCache oracleVarsCache;

	public void doBothTask() {
		AppContextUtil.setTenant(TenantContextHolder.currentSite(appConfig.getDefaultTenant()));
		AppContextUtil.getTraceId(true, true);
		AppContextUtil.init();
		doTask(0, Constants.BLANK);
		doTaskRev(0, Constants.BLANK);
	}

	public abstract void doTask(int lastPage, String lastId);

	public abstract void doTaskRev(int lastPage, String lastId);

	public GridQuery getForwardQuery(int lastPage, int pageSize, String timeTrackKey, String fromValueString,
			String toValueString) {
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPageNo(lastPage);
		gridQuery.setPageSize(pageSize);
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());

		GridColumn column = new GridColumn();
		column.setKey(timeTrackKey);
		column.setOperator(FilterOperater.GTE);
		column.setDataType(FilterDataType.TIME);
		column.setValue(fromValueString);
		column.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column);

		GridColumn column2 = new GridColumn();
		column2.setKey(timeTrackKey);
		column2.setOperator(FilterOperater.ST);
		column2.setDataType(FilterDataType.TIME);
		column2.setValue(toValueString);
		column2.setSortDir(SortOrder.ASC);
		gridQuery.getColumns().add(column2);

		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.ASC);
		return gridQuery;
	}

	public GridQuery getReverseQuery(int lastPage, int pageSize, String timeTrackKey, String fromValueString,
			String toValueString) {
		GridQuery gridQuery = new GridQuery();
		gridQuery.setPageNo(lastPage);
		gridQuery.setPageSize(pageSize);
		gridQuery.setPaginated(false);
		gridQuery.setColumns(new ArrayList<GridColumn>());
		GridColumn column = new GridColumn();
		column.setKey(timeTrackKey);
		column.setOperator(FilterOperater.STE);
		column.setDataType(FilterDataType.TIME);
		column.setValue(fromValueString);
		column.setSortDir(SortOrder.DESC);
		gridQuery.getColumns().add(column);

		GridColumn column2 = new GridColumn();
		column2.setKey(timeTrackKey);
		column2.setOperator(FilterOperater.GT);
		column2.setDataType(FilterDataType.TIME);
		column2.setValue(toValueString);
		column2.setSortDir(SortOrder.DESC);
		gridQuery.getColumns().add(column2);

		gridQuery.setSortBy(0);
		gridQuery.setSortOrder(SortOrder.DESC);
		return gridQuery;
	}

}
