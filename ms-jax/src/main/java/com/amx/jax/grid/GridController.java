package com.amx.jax.grid;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.repository.employee.UserSessionRepository;

@RestController
public class GridController {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	UserSessionRepository userSessionRepository;

	@RequestMapping(value = "/users/paginated/orcl", method = { RequestMethod.GET, RequestMethod.POST })
	public DataTableResults<UserSessionRecord> listUsersPaginatedForOracle(HttpServletRequest request,
			HttpServletResponse response, Model model, @RequestBody GridQuery gridQuery) {

		DataTableRequest dataTableInRQ = new DataTableRequest(gridQuery);
		PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();
		String baseQuery = "SELECT USER_NAME, LOGIN_TIME, USER_TOKEN, IP_ADDRESS FROM USER_SESSION";
		String paginatedQuery = GridUtil.buildPaginatedQueryForOracle(baseQuery, pagination);

		System.out.println(paginatedQuery);

		Query query = entityManager.createNativeQuery(paginatedQuery, UserSessionRecord.class);

		@SuppressWarnings("unchecked")
		List<UserSessionRecord> userList = query.getResultList();

		DataTableResults<UserSessionRecord> dataTableResult = new DataTableResults<UserSessionRecord>();
		dataTableResult.setDraw(dataTableInRQ.getDraw());
		dataTableResult.setListOfDataObjects(userList);
		if (!GridUtil.isObjectEmpty(userList)) {
			dataTableResult.setRecordsTotal(userList.get(0).getTotalRecords()
					.toString());
			if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
				dataTableResult.setRecordsFiltered(userList.get(0).getTotalRecords()
						.toString());
			} else {
				dataTableResult.setRecordsFiltered(Integer.toString(userList.size()));
			}
		}
		return dataTableResult;
	}
}
