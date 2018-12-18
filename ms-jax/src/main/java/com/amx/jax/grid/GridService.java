package com.amx.jax.grid;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;


@Component
public class GridService {

	Logger LOGGER = LoggerService.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	public <T extends GridViewRecord> AmxApiResponse<T, DataTableMeta> getView(String baseQuery, GridQuery gridQuery,
			Class<T> GridViewRecordClass) {
		DataTableRequest dataTableInRQ = new DataTableRequest(gridQuery);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		String paginatedQuery = GridUtil.buildPaginatedQueryForOracle(baseQuery, pagination);
		LOGGER.debug(paginatedQuery);
		Query query = entityManager.createNativeQuery(paginatedQuery, GridViewRecordClass);
		@SuppressWarnings("unchecked")
		List<T> userList = query.getResultList();
		query.getHints();

		DataTableMeta meta = new DataTableMeta();
		if (!GridUtil.isObjectEmpty(userList)) {
			meta.setRecordsTotal(userList.get(0).getTotalRecords()
					.toString());
			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
				meta.setRecordsFiltered(userList.get(0).getTotalRecords()
						.toString());
			} else {
				meta.setRecordsFiltered(Integer.toString(userList.size()));
			}
		}
		return AmxApiResponse.buildList(userList, meta);
	}

}
