package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.DailyPromotionDTO;
import com.amx.jax.dbmodel.promotion.DailyPromotion;
import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;
import com.amx.jax.repository.promotion.DailyPromotionRepository;
import com.amx.jax.util.DBUtil;

@Component
public class DailyPromotionDao {

	@Autowired
	DailyPromotionRepository dailyPromotionRepository;
	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	Logger logger = LoggerFactory.getLogger(getClass());

	public DailyPromotion getWantitByTrnxId(BigDecimal remittanceTransactionId) {

		return dailyPromotionRepository.getWantitByTrnxId(remittanceTransactionId);
	}
	public DailyPromotionDTO applyJolibeePadalaCoupons(BigDecimal documentFinanceyear, BigDecimal documentNumber, BigDecimal branchCode) {
		logger.debug("procedure values are  "+documentFinanceyear+documentNumber+branchCode);
		DailyPromotionDTO dailyPromotionDTO = new DailyPromotionDTO();
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String callProcedure = "{call GET_PROMOTION_PRIZE (?,?,?,?,?)}";
			cs = connection.prepareCall(callProcedure);
			cs.setBigDecimal(1, documentFinanceyear);
			cs.setBigDecimal(2, documentNumber);
			cs.setBigDecimal(3, branchCode);
			cs.registerOutParameter(4, java.sql.Types.VARCHAR);
			cs.registerOutParameter(5, java.sql.Types.VARCHAR);
			cs.executeUpdate();
			dailyPromotionDTO.setPromotionMsg(cs.getString(4));
			dailyPromotionDTO.setErrorMsg(cs.getString(5)); 
		}catch(DataAccessException | SQLException e) {
			logger.info("Exception in procedure to get promotion prize" + e.getMessage());
		}finally {
			DBUtil.closeResources(cs, connection);
		}
		return dailyPromotionDTO;
	}
	
	public DailyPromotionDTO applyJolibeePadalaCouponReceipt(BigDecimal documentFinanceyear, BigDecimal documentNumber) {
		DailyPromotionDTO dailyPromotionDTO = new DailyPromotionDTO();
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withFunctionName("EX_PROMOTION_MESSAGE");
		SqlParameterSource paramMap = new MapSqlParameterSource().addValue("P_TRNFYR", documentFinanceyear).addValue("P_TRNREF",
				documentNumber);
		// First parameter is function output parameter type.
		String promotionMsg = jdbcCall.executeFunction(String.class, paramMap);
		dailyPromotionDTO.setPromotionMsg(promotionMsg);
		return dailyPromotionDTO;
	}
}
