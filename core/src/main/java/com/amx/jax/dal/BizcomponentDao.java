package com.amx.jax.dal;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BizcomponentDao {

	private Logger logger = Logger.getLogger(BizcomponentDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Transactional
	public BigDecimal findCustomerTypeId(String CustomerType) {

		String sql = "SELECT A.COMPONENT_DATA_ID" + "        FROM   FS_BIZ_COMPONENT_DATA A,FS_BUSINESS_COMPONENT B"
				+ "        WHERE  A.COMPONENT_ID = 2" + "        AND    A.COMPONENT_ID = B.COMPONENT_ID"
				+ "        AND    A.ACTIVE = 'Y'" + "        AND    A.COMPONENT_CODE = ?";
		logger.info("in findCustomerTypeId with customerType: " + CustomerType);
		BigDecimal name = jdbcTemplate.queryForObject(sql, new Object[] { CustomerType }, BigDecimal.class);

		return name;

	}
}
