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
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.util.DateUtil;

/**
 * 
 * @author : Rabil Date :
 *
 */
@Component
public class ImageCheckDao {

	private Logger logger = Logger.getLogger(ImageCheckDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public Map<String, Object> dmsImageCheck(final BigDecimal identityIntId, final String identityInt,
			final String identityExpDate) {

		logger.info("In put Parameters : identityIntId :" + identityIntId + "\t identityInt :" + identityInt
				+ "\t identityExpDate :" + identityExpDate);

		List<SqlParameter> declareOutputParameters = Arrays.asList(new SqlOutParameter("docBlobId", Types.INTEGER),
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

		}, declareOutputParameters);

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
}
