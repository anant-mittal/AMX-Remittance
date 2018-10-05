package com.amx.jax.dbmodel.promotion;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PromotionDetailRowMapper implements RowMapper<PromotionDetailModel> {

	@Override
	public PromotionDetailModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		PromotionDetailModel model = new PromotionDetailModel();
		model.setDocNo(rs.getBigDecimal("DOCNO"));
		model.setTrnRef(rs.getBigDecimal("TRNREF"));
		model.setPrize(rs.getString("PRIZE"));
		model.setDocFinYear(rs.getBigDecimal("DOCFYR"));
		return model;
	}

}
