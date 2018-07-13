package com.amx.jax.dao.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.amx.jax.dbmodel.meta.DocLocModel;

public class DocLocRowMapper implements RowMapper<DocLocModel> {

	@Override
	public DocLocModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		DocLocModel docLocModel = new DocLocModel();
		docLocModel.setComCod(rs.getBigDecimal("COMCOD"));
		docLocModel.setDocCode(rs.getBigDecimal("DOCCOD"));
		docLocModel.setDocFyr(rs.getBigDecimal("DOCFYR"));
		docLocModel.setLocCod(rs.getBigDecimal("LOCCOD"));
		docLocModel.setCreatedDate(rs.getDate("CRTDAT"));
		docLocModel.setUpdatedDate(rs.getDate("UPDDAT"));
		return docLocModel;
	}

}
