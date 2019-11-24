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
	
	Logger logger = LoggerFactory.getLogger(getClass());

	public DailyPromotion getWantitByTrnxId(BigDecimal remittanceTransactionId) {

		return dailyPromotionRepository.getWantitByTrnxId(remittanceTransactionId);
	}
	public DailyPromotionDTO applyJolibeePadalaCoupons(BigDecimal documentFinanceyear, BigDecimal documentNumber, BigDecimal branchCode) {
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
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String callProcedure = "{call EX_PROMOTION_MESSAGE (?,?,?)}";
			cs = connection.prepareCall(callProcedure);
			cs.setBigDecimal(1, documentFinanceyear);
			cs.setBigDecimal(2, documentNumber);
			cs.registerOutParameter(0, java.sql.Types.VARCHAR);
			cs.executeUpdate();
			dailyPromotionDTO.setPromotionMsg(cs.getString(0));
		}catch(DataAccessException | SQLException e) {
			logger.info("Exception in procedure to get promotion prize" + e.getMessage());
		}finally {
			DBUtil.closeResources(cs, connection);
		}
		return dailyPromotionDTO;
	}
}
