package com.amx.jax.dal;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

@Component
public class CustomerDocumentDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger log = LoggerFactory.getLogger(CustomerDocumentDao.class);

	public void copyBlobDataIntoJava(BigDecimal blobId, BigDecimal docFinYear) {
		CallableStatementCreator csc = new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call SAVE_BLOB_DOCS_REMOTE_JAVA(?, ?) } ";
				CallableStatement cs = con.prepareCall(proc);
				cs.setBigDecimal(1, blobId);
				cs.setBigDecimal(2, docFinYear);
				cs.execute();
				return cs;
			}

		};
		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.INTEGER),
				new SqlParameter(Types.INTEGER));
		jdbcTemplate.call(csc, declareInAndOutputParameters);
		log.debug("procedure called success SAVE_BLOB_DOCS_REMOTE_JAVA params {}, {}", blobId, docFinYear);
	}

	public void copyBlobDataFromJava(BigDecimal blobId, BigDecimal docFinYear) {
		CallableStatementCreator csc = new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call SAVE_BLOB_DOCS_DB_JAVA(?, ?) } ";
				CallableStatement cs = con.prepareCall(proc);
				cs.setBigDecimal(1, blobId);
				cs.setBigDecimal(2, docFinYear);
				cs.execute();
				return cs;
			}

		};
		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.INTEGER),
				new SqlParameter(Types.INTEGER));
		jdbcTemplate.call(csc, declareInAndOutputParameters);
		log.debug("procedure called success SAVE_BLOB_DOCS_DB_JAVA params {}, {}", blobId, docFinYear);
	}
}
