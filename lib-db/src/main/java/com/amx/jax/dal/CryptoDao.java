package com.amx.jax.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CryptoDao {

	private Logger logger = Logger.getLogger(CryptoDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public String decrypt(String saltKey, final String encryptedValue) {
		String decStr = null;
		try {
			logger.debug("start decrypt");
			String salt = new StringBuffer(saltKey).reverse().toString();
			salt = salt.substring(0, 4);
			final String key = "almullaexchangeonlineremitt2010" + salt;
			List<SqlParameter> declaredParameters = Arrays.asList(new SqlOutParameter("output_str", Types.VARCHAR),
					new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR));
			Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = "Begin ? := PKG_ENCRYPT.decrypt_string ( ?, ?); End;";
					CallableStatement callableStatement = con.prepareCall(proc);

					callableStatement.setString(2, encryptedValue);
					callableStatement.setString(3, key);
					callableStatement.registerOutParameter(1, Types.VARCHAR); // Encrypted
					callableStatement.execute();
					return callableStatement;

				}
			}, declaredParameters);
			decStr = output.get("output_str").toString();
			logger.debug("Done decrypt=" + declaredParameters.get(0));
		} catch (Exception e) {
			logger.error("error in decrypt", e);
		}
		return decStr;
	}

	@Transactional
	public String encrypt(String saltKey, final String value) {
		String encStr = null;
		try {
			logger.debug("start encrypt");
			String salt = new StringBuffer(saltKey).reverse().toString();
			salt = salt.substring(0, 4);
			final String key = "almullaexchangeonlineremitt2010" + salt;
			List<SqlParameter> declaredParameters = Arrays.asList(new SqlOutParameter("output_str", Types.VARCHAR),
					new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR));
			Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = "Begin ? := PKG_ENCRYPT.encrypt_string ( ? , ? ); End;";
					CallableStatement callableStatement = con.prepareCall(proc);

					callableStatement.setString(2, value);
					callableStatement.setString(3, key);
					callableStatement.registerOutParameter(1, Types.VARCHAR); // Encrypted
					callableStatement.execute();
					callableStatement.getString(1);
					return callableStatement;

				}
			}, declaredParameters);
			encStr = output.get("output_str").toString();
			logger.debug("Done encrypt=" + encStr);
		} catch (Exception e) {
			logger.error("error in ecnrypt", e);
		}
		return encStr;
	}
}
