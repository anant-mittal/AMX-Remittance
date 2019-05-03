package com.amx.jax.dal;

import java.math.BigDecimal;
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

import com.amx.jax.meta.MetaData;
import com.amx.jax.util.CountryUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NotificationDao {

	private Logger logger = Logger.getLogger(NotificationDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CountryUtil cutil;

	@Autowired
	private MetaData meta;

	@Transactional
	public String sendSmsOtpProcedure(final String mobileNo, final String smsMsg) {
		String decStr = null;
		try {
			logger.info("start sendSmsOtpProcedure");
			List<SqlParameter> declaredParameters = Arrays.asList(new SqlOutParameter("output_str", Types.VARCHAR),
					new SqlParameter(Types.VARCHAR));

			Map<String, Object> output = jdbcTemplate.call(new CallableStatementCreator() {

				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					String proc = " { call INSERT_SMS_OTP_SENDQUEUE (?,?,?,?,?,?,?,?,?,?,?,?,?) } ";
					CallableStatement cs = con.prepareCall(proc);

					cs.setString(1, getSenderId()); // Sender Id "AlMulla EXC"
					cs.setString(2, mobileNo);
					cs.setString(3, smsMsg);
					cs.setString(4, getFourthParam());
					cs.setString(5, getfifthParam());
					cs.setInt(6, 71);
					cs.setString(7, "ORS_USER");
					cs.setInt(8, getLangNumber());
					cs.setNull(9, Types.INTEGER);
					cs.setNull(10, Types.INTEGER);
					cs.setNull(11, Types.INTEGER);
					cs.setNull(12, Types.INTEGER);
					cs.registerOutParameter(13, Types.INTEGER);

					cs.execute();
					return cs;

				}

			}, declaredParameters);
			decStr = output.get("output_str").toString();
			logger.info("Done sendSmsOtpProcedure otp= " + declaredParameters.get(0));
		} catch (Exception e) {
			logger.error("error in decrypt", e);
		}
		return decStr;
	}

	private String getSenderId() {
		boolean isKw = cutil.isKuwait(meta.getCountry().getISO2Code());
		boolean isBh = cutil.isBahrain((meta.getCountry().getISO2Code()));
		boolean isOm = cutil.isOman((meta.getCountry().getISO2Code()));
		if (isKw) {
			return "AlMulla EXC";
		}
		if (isBh) {
			return "Bahrain EXC";
		}
		if (isOm) {
			return "Oman EXC";
		}
		return null;
	}

	private String getFourthParam() {
		return "EMOS";
	}

	private String getfifthParam() {
		return "61";
	}

	private int getLangNumber() {
		BigDecimal langIdBig = meta.getLanguageId();
		Integer langId = langIdBig.intValue();
		int nNo = 0;
		if (langId.equals(1)) {
			nNo = 0;
		} else if (langId.equals(2)) {
			nNo = 1;
		}
		return nNo;
	}

}
