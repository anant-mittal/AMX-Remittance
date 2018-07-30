package com.amx.jax.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.dao.rowmapper.DocLocRowMapper;
import com.amx.jax.dbmodel.meta.DocLocModel;

@Component
public class DocumentSerialityNumberDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<DocLocModel> getDocLocByDocumentCode(BigDecimal documentCode) {
		String sql = "select * from docloc where DOCCOD=?";
		List<DocLocModel> docLocModels = jdbcTemplate.query(sql, new DocLocRowMapper(), documentCode);
		return docLocModels;
	}

}
