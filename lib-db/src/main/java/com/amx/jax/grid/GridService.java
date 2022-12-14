package com.amx.jax.grid;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.ArgUtil;

@Component
public class GridService {

	public static Logger LOGGER = LoggerService.getLogger(GridService.class);

	@PersistenceContext
	private EntityManager entityManager;

	public <T> GridViewBuilder<T> view(GridView gridView,
			GridQuery gridQuery) {

		@SuppressWarnings("unchecked")
		GridInfo<T> gridInfo = (GridInfo<T>) GridViewFactory.get(gridView);

		String baseQuery = gridInfo.getQuery();
		Class<T> gridViewRecordClass = gridInfo.getResultClass();
		Map<String, String> map = gridInfo.getMap();

		if (!ArgUtil.isEmpty(gridQuery.getColumns())) {
			for (GridColumn column : gridQuery.getColumns()) {
				String data = column.getKey();
				if (map.containsKey(data)) {
					column.setKey(map.get(data));
				}
			}
		}

		DataTableRequest dataTableInRQ = new DataTableRequest(gridQuery);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		String paginatedQuery = GridUtil.buildPaginatedQueryForOracle(baseQuery, pagination,
				gridQuery.isPaginated() && GridViewRecord.class.isAssignableFrom(gridViewRecordClass),
				gridInfo.isCustomeQuery(), gridInfo);
		try {
			LOGGER.debug(paginatedQuery);
			Query query = entityManager.createNativeQuery(paginatedQuery, gridViewRecordClass);
			return new GridViewBuilder<T>(query, dataTableInRQ).queryStr(paginatedQuery).gridView(gridView);
		} catch (Exception e) {
			LOGGER.error("GridViewError V:{} Q {}", gridView, paginatedQuery);
			throw e;
		}

	}

	public <T> GridViewBuilder<T> view(GridView gridView,
			String sqlQuery) {
		@SuppressWarnings("unchecked")
		GridInfo<T> gridInfo = (GridInfo<T>) GridViewFactory.get(gridView);
		Class<T> gridViewRecordClass = gridInfo.getResultClass();
		try {
			LOGGER.debug(sqlQuery);
			Query query = entityManager.createNativeQuery(sqlQuery, gridViewRecordClass);
			return new GridViewBuilder<T>(query, null).queryStr(sqlQuery).gridView(gridView);
		} catch (Exception e) {
			LOGGER.error("GridViewError V:{} Q {}", gridView, sqlQuery);
			throw e;
		}
	}

	public static class GridViewBuilder<T> {
		Query query;
		DataTableRequest dataTableRequest;
		String queryStr;
		GridView gridView;

		GridViewBuilder(Query query, DataTableRequest dataTableRequest) {
			this.query = query;
			this.dataTableRequest = dataTableRequest;
		}

		public GridViewBuilder<T> queryStr(String queryStr) {
			this.queryStr = queryStr;
			return this;
		}

		public GridViewBuilder<T> gridView(GridView gridView) {
			this.gridView = gridView;
			return this;
		}

		public AmxApiResponse<T, GridMeta> get() {
			try {
				@SuppressWarnings("unchecked")
				List<T> userList = query.getResultList();
				GridMeta meta = new GridMeta();
				if (!ArgUtil.isEmpty(userList)) {
					T firstElement = userList.get(0);
					int totalRecords = 0;
					if (firstElement instanceof GridViewRecord) {
						totalRecords = ((GridViewRecord) firstElement).getTotalRecords();
					}
					meta.setRecordsTotal(ArgUtil.parseAsString(totalRecords));
					if (dataTableRequest!=null && dataTableRequest.getPaginationRequest().isFilterByEmpty()) {
						meta.setRecordsFiltered(ArgUtil.parseAsString(totalRecords));
					} else {
						meta.setRecordsFiltered(ArgUtil.parseAsString(userList.size()));
					}
				}
				return AmxApiResponse.buildList(userList, meta);
			} catch (Exception e) {
				LOGGER.error("GridViewError V:{} Q {}", gridView, queryStr);
				throw e;
			}

		}

	}

}