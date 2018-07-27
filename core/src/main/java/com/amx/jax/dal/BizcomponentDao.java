package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BizComponentDataDesc;
import com.amx.jax.dbmodel.LanguageType;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBizComponentDataDescDaoRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BizcomponentDao {

	private Logger logger = Logger.getLogger(BizcomponentDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	IBizComponentDataDescDaoRepository iBizDataDecReporsitory;
	@Autowired
	MetaData metaData;

	@Transactional
	public BigDecimal findCustomerTypeId(String CustomerType) {

		String sql = "SELECT A.COMPONENT_DATA_ID" + "        FROM   FS_BIZ_COMPONENT_DATA A,FS_BUSINESS_COMPONENT B"
				+ "        WHERE  A.COMPONENT_ID = 2" + "        AND    A.COMPONENT_ID = B.COMPONENT_ID"
				+ "        AND    A.ACTIVE = 'Y'" + "        AND    A.COMPONENT_CODE = ?";
		logger.info("in findCustomerTypeId with customerType: " + CustomerType);
		BigDecimal name = jdbcTemplate.queryForObject(sql, new Object[] { CustomerType }, BigDecimal.class);

		return name;

	}

	@Transactional
	public BizComponentDataDesc getComponentId(String inputString, BigDecimal langId) {
		BizComponentDataDesc desc = null;
		List<BizComponentDataDesc> bizComDesc = iBizDataDecReporsitory.getComponentId(inputString,
				new LanguageType(langId));
		if (bizComDesc != null && bizComDesc.size() > 0) {
			return desc = bizComDesc.get(0);
		} else {
			return desc;
		}

	}

	public BizComponentDataDesc getBizComponentDataDescByComponmentId(String componentDataId) {
		BizComponentData bizComponentData = new BizComponentData(new BigDecimal(componentDataId));
		LanguageType langId = new LanguageType(metaData.getLanguageId());
		List<BizComponentDataDesc> bizComDesc = iBizDataDecReporsitory
				.findByFsBizComponentDataAndFsLanguageType(bizComponentData, langId);
		return bizComDesc.get(0);

	}
	
	
	public List<BizComponentDataDesc> getBizComponentDataDescListByComponmentId() {
				LanguageType langId = new LanguageType(metaData.getLanguageId());				
				List<BizComponentDataDesc> bizComDesc = iBizDataDecReporsitory.findByFsBizComponentDataDesc(langId);
		return bizComDesc;

	}
}
