package com.amx.jax.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.amx.jax.dbmodel.BranchDetailModel;

/**
 * @author Subodh Bhoir
 *
 */

public class BranchDetailRowMapper implements RowMapper<BranchDetailModel>
{
	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	@Override
	public BranchDetailModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		BranchDetailModel bdModel = new BranchDetailModel();
		bdModel.setArea(rs.getString("AREA"));
		bdModel.setContactNumber(rs.getBigDecimal("TELEPHONE_NUMBER"));
		bdModel.setBranchName(rs.getString("BRANCH_NAME"));
		bdModel.setBranchAddress(rs.getString("ADDRESS"));
		bdModel.setBranchAddress(rs.getString("LATITUDE"));
		bdModel.setBranchAddress(rs.getString("LONGITUDE"));
		
		return bdModel;
	}
	
}