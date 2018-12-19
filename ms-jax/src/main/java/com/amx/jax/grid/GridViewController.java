package com.amx.jax.grid;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;

@RestController
public class GridViewController {

	Logger LOGGER = LoggerService.getLogger(getClass());

	@Autowired
	GridService gridService;

	@RequestMapping(value = "/grid/view/USER_SESSION", method = { RequestMethod.GET, RequestMethod.POST })
	public AmxApiResponse<UserSessionRecord, DataTableMeta> listUsersPaginatedForOracle(HttpServletRequest request,
			HttpServletResponse response, Model model, @RequestBody GridQuery gridQuery) {
		return gridService.getView("SELECT * FROM USER_SESSION", gridQuery,
				UserSessionRecord.class);
	}

	@PersistenceContext
	private EntityManager entityManager;

	@RequestMapping(value = "/grid/views/USER_SESSION", method = { RequestMethod.GET, RequestMethod.POST })
	public AmxApiResponse<Map<String, Object>, DataTableMeta> listUsersPaginateds(HttpServletRequest request,
			HttpServletResponse response, Model model, @RequestBody GridQuery gridQuery) {

		String baseQuery = "SELECT * FROM USER_SESSION";
		Class<UserSessionRecord> GridViewRecordClass = UserSessionRecord.class;
		DataTableRequest dataTableInRQ = new DataTableRequest(gridQuery);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		String paginatedQuery = GridUtil.buildPaginatedQueryForOracle(baseQuery, pagination);
		LOGGER.debug(paginatedQuery);
		Query query = entityManager.createNativeQuery(paginatedQuery);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> userList = query.getResultList();
		query.getHints();

		DataTableMeta meta = new DataTableMeta();
//		if (!GridUtil.isObjectEmpty(userList)) {
//			meta.setRecordsTotal(userList.get(0).getTotalRecords()
//					.toString());
//			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
//				meta.setRecordsFiltered(userList.get(0).getTotalRecords()
//						.toString());
//			} else {
//				meta.setRecordsFiltered(Integer.toString(userList.size()));
//			}
//		}
		return AmxApiResponse.buildList(userList, meta);
	}
}
