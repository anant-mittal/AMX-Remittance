package com.amx.jax.dao;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dal.ExchangeRateProcedureDao;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.repository.ISwiftMasterDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneficiaryDao {

	@Autowired
	ISwiftMasterDao swiftMasterRepo;

	public SwiftMasterView getSwiftMasterBySwiftBic(String swiftBic) {
		return swiftMasterRepo.getSwiftMasterDetails(swiftBic).get(0);
	}
	private static final Logger LOGGER = Logger.getLogger(BeneficiaryDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Transactional
	public List<BigDecimal> getRoutingBankMasterList() {
		String sql = "select distinct(COUNTRY_ID) from V_EX_ROUTING_AGENTS where SERVICE_GROUP_ID = "
				+ "(select service_group_id from ex_service_group where service_group_code='C' and " + "isactive='Y')";
		List<BigDecimal> outputList = new ArrayList<>();
		try {
			outputList = jdbcTemplate.queryForList(sql, BigDecimal.class);
		} catch (Exception e) {
			LOGGER.info("error in getRoutingBankMasterList : ", e);
		}
		return outputList;
	}
}
