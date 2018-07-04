package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.dao.rowmapper.BranchDetailRowMapper;
import com.amx.jax.dbmodel.BranchDetailModel;

/**
 * @author Subodh Bhoir
 *
 */
@Component
public class BranchDetailDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * @param countryId
	 * @return get branch detail
	 */
	public List<BranchDetailModel> getBranchDetailList(BigDecimal countryId) {
		List<BranchDetailModel> branchDetail = jdbcTemplate.query(
				"SELECT BRANCH_NAME, GOV_NAME, TELEPHONE_NUMBER, ADDRESS, LATITUDE, LONGITUDE from EX_COUNTRY_BRANCH  WHERE ISACTIVE='Y' AND "
						+ "COUNTRY_ID=?  and CORPORATE_STATUS='N' and branch_name != 'ONLINE' and HEAD_OFFICE_INDICATOR=0  and "
						+ " ADDRESS IS NOT NULL AND LATITUDE IS NOT NULL AND LONGITUDE IS NOT NULL ORDER BY BRANCH_NAME ASC",
				new BranchDetailRowMapper(), countryId);

		return branchDetail;
	}

}