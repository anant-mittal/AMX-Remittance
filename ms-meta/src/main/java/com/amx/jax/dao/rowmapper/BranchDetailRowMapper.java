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
		bdModel.setArea(rs.getString("GOV_NAME"));
		bdModel.setContactNumber(rs.getBigDecimal("TELEPHONE_NUMBER"));
		bdModel.setBranchName(rs.getString("BRANCH_NAME"));
		bdModel.setBranchAddress(rs.getString("ADDRESS"));
		bdModel.setLatitude(rs.getBigDecimal("LATITUDE"));
		bdModel.setLongitude(rs.getBigDecimal("LONGITUDE"));
		
		bdModel.setGovName(rs.getString("GOV_NAME"));
		bdModel.setBranches(rs.getString("BRANCHES"));
		bdModel.setBranchWebsiteName(rs.getString("BRANCH_WEBSITE_NAME"));
		bdModel.setAddressLine1(rs.getString("ADDRESS_LINE_1"));
		bdModel.setAddressLine2(rs.getString("ADDRESS_LINE_2"));
		bdModel.setCity(rs.getString("CITY"));
		bdModel.setCountry(rs.getString("COUNTRY"));
		bdModel.setPostalCode(rs.getString("PC"));
		bdModel.setBranchTiming(rs.getString("BRANCH_TIMING"));
		bdModel.setCountryBranchId(rs.getBigDecimal("COUNTRY_BRANCH_ID"));
		
		return bdModel;
	}
	
}