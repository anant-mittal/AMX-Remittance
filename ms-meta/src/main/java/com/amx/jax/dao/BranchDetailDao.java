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
		List<BranchDetailModel> branchDetail = jdbcTemplate.query("select BRANCH_NAME,  A.AREADESC, TELEPHONE_NUMBER from EX_COUNTRY_BRANCH B INNER join v_ex_area A on B.AREA_CODE=A.SEQ_ID  where B.isactive='Y' and\r\n" + 
				"B.COUNTRY_ID=?", new BranchDetailRowMapper(), countryId);
		
		return branchDetail;
	}

}