package com.amx.jax.dal;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.util.DateUtil;

/**
 * 
 * @author : Rabil Date :
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ImageCheckDao {

	private Logger logger = Logger.getLogger(ImageCheckDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public Map<String, Object> dmsImageCheck(final BigDecimal identityIntId, final String identityInt,
			final String identityExpDate) {

		logger.info("In put Parameters : identityIntId :" + identityIntId + "\t identityInt :" + identityInt
				+ "\t identityExpDate :" + identityExpDate);

		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.INTEGER),
				new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR), new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.DATE), new SqlOutParameter("docBlobId", Types.INTEGER),
				new SqlOutParameter("docFinYr", Types.INTEGER));
		/**
		 * DMS_P_GET_LST_BLOB@AGDMSLNK(1, 'CMAP', W_IMG_ID, C0.IDENTITY_INT,
		 * C0.IDENTITY_EXPIRY_DATE, W_DOC_BLOB_ID, W_DOC_FIN_YR );
		 */

		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call DMS_P_GET_LST_BLOB@AGDMSLNK (?,?,?,?,?,?,?) } ";
				CallableStatement cs = con.prepareCall(proc);
				cs.setInt(1, 1);
				cs.setString(2, "CMAP");
				cs.setString(3, getImageId(identityIntId));
				cs.setString(4, identityInt);
				cs.setDate(5, DateUtil.convretStringToSqlDate(identityExpDate));
				cs.registerOutParameter(6, Types.INTEGER);
				cs.registerOutParameter(7, Types.INTEGER);
				cs.execute();
				return cs;
			}

		}, declareInAndOutputParameters);

		logger.info("Out put Parameters :" + output.toString());

		return output;
	}

	private String getImageId(BigDecimal identityIntId) {
		String imageId = null;

		if (identityIntId.compareTo(BigDecimal.ZERO) != 0) {
			if ((identityIntId.compareTo(new BigDecimal(2000)) == 0
					|| identityIntId.compareTo(new BigDecimal(198)) == 0)) {
				imageId = "040";
			} else if (identityIntId.compareTo(new BigDecimal(201)) == 0) {
				imageId = "080";
			} else if (identityIntId.compareTo(new BigDecimal(204)) == 0) {
				imageId = "041";
			}
		}
		return imageId;
	}

	/**
	 * Test ID : 841303185 // 12/03/2019 Blob ID : 861036433 // 21/06/2017
	 */

	public BigDecimal callTogenerateBlobID(BigDecimal docFinYear) {
		List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.VARCHAR),
				new SqlParameter(Types.INTEGER), new SqlOutParameter("docBlobId", Types.INTEGER));

		Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {
			@Override
			public CallableStatement createCallableStatement(Connection con) throws SQLException {

				String proc = " { call NEXT_AMG_DOC_SRNO_LCK@AGDMSLNK (?,?,?) } ";
				CallableStatement cs = con.prepareCall(proc);
				cs.setString(1, "BLID");
				cs.setBigDecimal(2, docFinYear);
				cs.registerOutParameter(3, Types.INTEGER);
				cs.execute();
				return cs;
			}
		}, declareInAndOutputParameters);
		return new BigDecimal(output.get("docBlobId").toString());
	}

}
