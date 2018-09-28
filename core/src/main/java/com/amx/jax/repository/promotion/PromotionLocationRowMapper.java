package com.amx.jax.repository.promotion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.amx.jax.dbmodel.promotion.PromotionLocationModel;

public class PromotionLocationRowMapper implements RowMapper<PromotionLocationModel> {

	@Override
	public PromotionLocationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		PromotionLocationModel model = new PromotionLocationModel();
		model.setComCode(rs.getBigDecimal("COMCOD"));
		model.setDocFinYear(rs.getBigDecimal("DOCFYR"));
		model.setLocCode(rs.getBigDecimal("LOCCOD"));
		model.setDocumentNo(rs.getBigDecimal("DOCNO"));
		return model;
	}

}
