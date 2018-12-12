package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
import com.amx.jax.repository.IBizComponentDataRepository;
import com.amx.utils.Constants;


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
	@Autowired
	IBizComponentDataRepository iBizComponentDataRepository;

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
	
	public BizComponentDataDesc getBizComponentDataDescByComponmentId(BigDecimal componentDataId) {
		BizComponentData bizComponentData = new BizComponentData(componentDataId);
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
	
	public List<Map<String,Object>> getAllComponentComboDataForCustomer(BigDecimal languageId) {
		
			String s= "select B.COMPONENT_DATA_ID,A.DATA_DESC,C.COMPONENT_ID,A.SHORT_CODE from "
					+ "FS_BIZ_COMPONENT_DATA_DESC A,FS_BIZ_COMPONENT_DATA B,FS_BUSINESS_COMPONENT C,FS_LANGUAGE_TYPE D "
					+ "where A.COMPONENT_DATA_ID = B.COMPONENT_DATA_ID and B.COMPONENT_ID = C.COMPONENT_ID "
					+ "and A.LANGUAGE_ID = D.LANGUAGE_ID and B.ACTIVE='Y' and C.COMPONENT_NAME= ? "
					+ "and D.LANGUAGE_ID=? and B.COMPONENT_DATA_ID in (select distinct F.COMPONENT_DATA_ID "
					+ "from FS_BIZ_COMPONENT_DATA_REF E,FS_BIZ_COMPONENT_DATA F,FS_BUSINESS_COMPONENT_CONF G "
					+ "where E.COMPONENT_DATA_ID = F.COMPONENT_DATA_ID and E.COMPONENT_CONF_ID = G.COMPONENT_CONF_ID "
					+ "and E.ACTIVE = 'Y' and F.COMPONENT_DATA_ID = E.COMPONENT_DATA_ID) order by A.DATA_DESC asc ";	
			List<Map<String,Object>> tempList= jdbcTemplate.queryForList(s, new Object[] {Constants.COMPONENT_NAME,languageId});			
			
			return tempList;
			}
	
	public String getIdentityTypeMaster(BigDecimal componentId) {
		 
		String sql = "SELECT CUSTOMER_TYPE FROM FS_IDENTITY_TYPE_MASTER A INNER JOIN FS_BUSINESS_COMPONENT B  " + 
				"ON A.BUSINESS_COMPONENT_ID = B.COMPONENT_ID WHERE A.BUSINESS_COMPONENT_ID = ?";
		
		List<String> lstIdentity = jdbcTemplate.queryForList(sql, new Object[] { componentId }, String.class);		
		 
		String rtnIdentity="";
		if(lstIdentity.size()>0)
		{
		rtnIdentity=lstIdentity.get(0);
		}
		 
		return rtnIdentity;
	}


	public BizComponentData getBizComponentDataByComponmentCode(String componentCode) {
		return iBizComponentDataRepository.findBycomponentCode(componentCode);
	}
	
	public BizComponentData getBizComponentDataByComponmentDataId(BigDecimal componentDataId) {
		return iBizComponentDataRepository.findOne(componentDataId);
	}
}
