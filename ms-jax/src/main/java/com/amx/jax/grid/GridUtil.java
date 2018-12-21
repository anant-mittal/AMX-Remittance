package com.amx.jax.grid;

import com.amx.utils.ArgUtil;

/**
 * The Class AppUtil.
 *
 * @author pavan.solapure
 */
public class GridUtil {

	/**
	 * Builds the paginated query.
	 *
	 * @param baseQuery          the base query
	 * @param paginationCriteria the pagination criteria
	 * @return the string
	 */
	public static String buildPaginatedQuery(String baseQuery, PaginationCriteria paginationCriteria) {
		// String queryTemplate = "SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT
		// BASEINFO.* FROM ( %s ) BASEINFO %s %s ) FILTERED_ORDERD_RESULTS LIMIT %d,
		// %d";
		StringBuilder sb = new StringBuilder(
				"SELECT FILTERED_ORDERD_RESULTS.* FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO #WHERE_CLAUSE#  #ORDER_CLASUE# ) FILTERED_ORDERD_RESULTS LIMIT #PAGE_START#, #PAGE_SIZE#");
		String finalQuery = null;
		if (!ArgUtil.isEmpty(paginationCriteria)) {
			int pageStart = paginationCriteria.getPageNumber() * paginationCriteria.getPageSize();
			String whereClaus = paginationCriteria.getFilterByClause();
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", baseQuery)
					.replaceAll("#WHERE_CLAUSE#",
							((ArgUtil.isEmpty(whereClaus)) ? "" : " WHERE ")
									+ whereClaus)
					.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
					.replaceAll("#PAGE_START#", ArgUtil.parseAsString(pageStart))
					.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());
		}
		return (null == finalQuery) ? baseQuery : finalQuery;
	}

	public static String buildPaginatedQueryForOracle(String baseQuery, PaginationCriteria paginationCriteria) {

		StringBuilder sb = new StringBuilder(
				"SELECT * FROM (SELECT FILTERED_ORDERED_RESULTS.*, COUNT(1) OVER() total_records, ROWNUM AS RN FROM (SELECT BASEINFO.* FROM ( #BASE_QUERY# ) BASEINFO ) FILTERED_ORDERED_RESULTS #WHERE_CLAUSE# #ORDER_CLASUE# ) WHERE RN > (#PAGE_NUMBER# * #PAGE_SIZE#) AND RN <= (#PAGE_NUMBER# + 1) * #PAGE_SIZE# ");
		String finalQuery = null;

		String baseQueryFilter = paginationCriteria.getWhereFilterByClause();
		String finalBaseQuery = baseQuery + ((ArgUtil.isEmpty(baseQueryFilter)) ? " " : " WHERE ")
				+ baseQueryFilter;

		// Datatable start is set to 0, 5, 10 ..etc (5 is page size)
		// For oracle paginated query we need page start from 1,2,3

//		int pageNo = paginationCriteria.getPageNumber() / Math.max(paginationCriteria.getPageSize(), 1);
//		paginationCriteria.setPageNumber(pageNo);

		if (!ArgUtil.isEmpty(paginationCriteria)) {
			String whereClaus = paginationCriteria.getFilterByClause();
			finalQuery = sb.toString().replaceAll("#BASE_QUERY#", finalBaseQuery)
					.replaceAll("#WHERE_CLAUSE#",
							((ArgUtil.isEmpty(whereClaus)) ? "" : " WHERE ")
									+ whereClaus)
					.replaceAll("#ORDER_CLASUE#", paginationCriteria.getOrderByClause())
					.replaceAll("#PAGE_NUMBER#", paginationCriteria.getPageNumber().toString())
					.replaceAll("#PAGE_SIZE#", paginationCriteria.getPageSize().toString());
		}
		return (null == finalQuery) ? finalBaseQuery : finalQuery;
	}
}
