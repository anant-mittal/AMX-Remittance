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
		List<BranchDetailModel> branchDetail = jdbcTemplate.query("SELECT BRANCH_NAME, GOV_NAME, TELEPHONE_NUMBER, ADDRESS, LATITUDE, LONGITUDE from EX_COUNTRY_BRANCH  WHERE ISACTIVE='Y' AND\r\n" + 
				"COUNTRY_ID=? ORDER BY BRANCH_NAME ASC", new BranchDetailRowMapper(), countryId);
		
		return branchDetail;
	}

}