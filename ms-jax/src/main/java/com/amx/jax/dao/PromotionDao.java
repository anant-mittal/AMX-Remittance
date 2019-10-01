package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.promotion.PromotionDetailModel;
import com.amx.jax.dbmodel.promotion.PromotionDetailRowMapper;
import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionHeaderPK;
import com.amx.jax.dbmodel.promotion.PromotionLocationModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.promotion.PromotionHeaderRepository;
import com.amx.jax.repository.promotion.PromotionLocationRowMapper;

/**
 * @author Prashant
 *
 */
@Component
public class PromotionDao {

	private static final Logger LOGGER = LoggerService.getLogger(PromotionDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	MetaData metaData;
	@Autowired
	PromotionHeaderRepository promotionHeaderRepository;

	public String callGetPromotionPrize(BigDecimal documentNoRemit, BigDecimal documentFinYearRemit,
			BigDecimal branchId) {
		LOGGER.info("callGetPromotionPrize Input Parameters: documentNoRemit:{}, documentFinYearRemit:{}, branchId:{}",
				documentNoRemit, documentFinYearRemit, branchId);
		Map<String, Object> output;
		String prizeMessage = null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC),
					new SqlParameter(Types.NUMERIC), new SqlParameter(Types.NUMERIC),
					new SqlOutParameter("P_PRIZE", Types.NUMERIC), new SqlOutParameter("P_ERROR", Types.NUMERIC));

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call GET_PROMOTION_PRIZE (?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, documentFinYearRemit);
					cs.setBigDecimal(2, documentNoRemit);
					cs.setBigDecimal(3, branchId);
					cs.registerOutParameter(4, java.sql.Types.VARCHAR);
					cs.registerOutParameter(5, java.sql.Types.VARCHAR);
					return cs;
				}

			}, declareInAndOutputParameters);
			prizeMessage = (String) output.get("P_PRIZE");
			String errorMessage = (String) output.get("P_ERROR");
			if (prizeMessage != null) {
				LOGGER.info("Prize message:" + prizeMessage);

			}
			if (errorMessage != null) {
				LOGGER.error("Error occured calling GET_PROMOTION_PRIZE, error message: {}", errorMessage);
			}

		} catch (Exception e) {
			LOGGER.error("Error occured in  calling procedure GET_PROMOTION_PRIZE", e);
		}
		return prizeMessage;
	}

	public String callGetPromotionMessage(BigDecimal documentNoRemit, BigDecimal documentFinYearRemit,
			BigDecimal branchId) {

		Map<String, Object> output;
		String prizeMessage = null;
		try {
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.NUMERIC),
					new SqlParameter(Types.NUMERIC));

			output = jdbcTemplate.call(new CallableStatementCreator() {
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					String proc = "{call EX_PROMOTION_MESSAGE (?,?,?,?,?)}";
					CallableStatement cs = con.prepareCall(proc);
					cs.setBigDecimal(1, documentFinYearRemit);
					cs.setBigDecimal(2, documentNoRemit);
					cs.registerOutParameter(3, java.sql.Types.VARCHAR);
					return cs;
				}

			}, declareInAndOutputParameters);
			prizeMessage = (String) output.get("output_str");

		} catch (Exception e) {
			LOGGER.error("Error occured in  calling procedure EX_PROMOTION_MESSAGE", e);
		}
		return prizeMessage;
	}

	/**
	 * @param docFinYear
	 *            - fin year
	 * @param locCode
	 *            - branch Id
	 * @return list of promotionlocations
	 * 
	 */
	public List<PromotionLocationModel> checkforLocationHeader(BigDecimal docFinYear, BigDecimal locCode) {
		return jdbcTemplate.query(
				"select * from promotion_locations where COMCOD=20 and doccod=72 and DOCFYR=? and LOCCOD=?",
				new PromotionLocationRowMapper(), docFinYear, locCode);
	}

	public List<PromotionHeader> getPromotionHeader(BigDecimal docFinYear, List<PromotionLocationModel> promoLocations,
			Date trnxDate) {
		List<PromotionHeader> promotionHeaderaData = null;
		List<PromotionHeaderPK> promotHeaderPks = promoLocations.stream()
				.map(pl -> new PromotionHeaderPK(pl.getDocumentNo(), pl.getDocFinYear())).collect(Collectors.toList());
		if(promotHeaderPks != null && promotHeaderPks.size() != 0) {
			promotionHeaderaData = promotionHeaderRepository.findPromotioHeader(promotHeaderPks, trnxDate);
		}
		
		return promotionHeaderaData;
	}

	public List<PromotionDetailModel> getPromotionDetailModel(BigDecimal finYearRemit, BigDecimal docNoRemit) {
		List<PromotionDetailModel> promoDetails = jdbcTemplate
				.query("SELECT B.TRNREF,NVL(B.PRIZE,A.PRIZE) PRIZE, B.DOCNO, B.DOCNO, B.DOCFYR  "
						+ "       FROM   PROMOTION_HD A,PROMOTION_DT B" + "       WHERE  A.COMCOD = B.COMCOD "
						+ "       AND    A.DOCCOD = B.DOCCOD" + "       AND    A.DOCFYR = B.DOCFYR "
						+ "       AND    A.DOCNO  = B.DOCNO" + "       AND    A.COMCOD = (SELECT DEF_COMPANY "
						+ "        FROM   APP_SETT)" + "       AND    A.DOCCOD = 72 "
						+ "      AND    NVL(B.TRNREF,0)     <> 0" + "       AND    NVL(A.RECSTS,' ')    = ' ' "
						+ "       AND    NVL(A.UTLZ_FLAG,' ') = 'U'" + "       AND    TRNFYR = ? "
						+ "       AND    TRNREF = ? ", new PromotionDetailRowMapper(), finYearRemit, docNoRemit);

		return promoDetails;
	}
}
