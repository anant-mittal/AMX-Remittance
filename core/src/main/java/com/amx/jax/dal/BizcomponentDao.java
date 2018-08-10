package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.BizComponentDataDesc;
import com.amx.jax.dbmodel.BizComponentDataRef;
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
	
	/*@SuppressWarnings("unchecked")
	public Map<BigDecimal, String> getAllComponentComboDataForCustomer(
			BigDecimal languageId,
			String CustomerType,String identitiyType) {
			SessionFactory factory = new Configuration().configure().buildSessionFactory();; 
			Session session = factory.openSession();
			Map<BigDecimal, String> mapComponentComboData = new LinkedHashMap<BigDecimal, String>();
			DetachedCriteria criteria = DetachedCriteria.forClass(BizComponentDataDesc.class, "bizComponentDataDesc");
			criteria.setFetchMode("bizComponentDataDesc.fsBizComponentData", FetchMode.JOIN);
			criteria.createAlias("bizComponentDataDesc.fsBizComponentData", "fsBizComponentData", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("fsBizComponentData.active", "Y"));			 
			criteria.setFetchMode("bizComponentDataDesc.fsBizComponentData.fsBusinessComponent", FetchMode.JOIN);
			criteria.createAlias("bizComponentDataDesc.fsBizComponentData.fsBusinessComponent", "fsBusinessComponent", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("fsBusinessComponent.componentName", identitiyType));			 
			criteria.setFetchMode("bizComponentDataDesc.fsLanguageType", FetchMode.JOIN);
			criteria.createAlias("bizComponentDataDesc.fsLanguageType", "fsLanguageType", JoinType.INNER_JOIN);
			criteria.add(Restrictions.eq("fsLanguageType.languageId", languageId));
			DetachedCriteria subCriteria = DetachedCriteria.forClass(BizComponentDataRef.class, "bizComponentDataRef");
			subCriteria.setFetchMode("bizComponentDataRef.fsBusinessComponentConf", FetchMode.JOIN);
			subCriteria.createAlias("bizComponentDataRef.fsBusinessComponentConf", "fsBusinessComponentConf", JoinType.INNER_JOIN);
			subCriteria.add(Restrictions.eq("bizComponentDataRef.active", "Y"));
			//subCriteria.add(Restrictions.eq("fsBusinessComponentConf.componentConfId", componentConfId));
			subCriteria.setFetchMode("bizComponentDataRef.fsBizComponentData", FetchMode.JOIN);
			subCriteria.createAlias("bizComponentDataRef.fsBizComponentData", "bizComponentData", JoinType.INNER_JOIN);
			subCriteria.add(Restrictions.eqProperty("bizComponentData.componentDataId", "fsBizComponentData.componentDataId"));
			subCriteria.setProjection(Projections.distinct(Projections.property("bizComponentData.componentDataId")));
			criteria.add(Subqueries.propertyIn("fsBizComponentData.componentDataId", subCriteria));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("fsBizComponentData.componentDataId"));
			projectionList.add(Projections.property("bizComponentDataDesc.dataDesc"));
			projectionList.add(Projections.property("fsBusinessComponent.componentId"));
			criteria.setProjection(projectionList);
			criteria.addOrder(Order.asc("bizComponentDataDesc.dataDesc"));
			
			String s= "SELECT * FROM FS_BIZ_COMPONENT_DATA_DESC FB INNER_JOIN FS_BIZ_COMPONENT_DATA F ON F.COMPONENT_DATA_ID = FB.COMPONENT_DATA_ID"
					+ " INNER JOIN FS_BUSINESS_COMPONENT FBC ON F.COMPONENT_ID = FBC.COMPONENT_ID INNER JOIN FS_LANGUAGE_TYPE FLT ON FLT.LANGUAGE_ID = FB.LANGUAGE_ID"
					+ " INNER JOIN FS_BIZ_COMPONENT_DATA_REF FBR on "
					+ "WHERE F.ISACTIVE = 'Y' AND FBC.COMPONENT_NAME ='Identiy Type' ";			
			
			List<Object[]> tempList = criteria.getExecutableCriteria(session).list();
					//(List<Object[]>) criteria;			
			 
			for (Object[] row : tempList) {
			 
			String idType=getIdentityTypeMaster((BigDecimal) row[0]);
			if(idType.equalsIgnoreCase(CustomerType))
			{
			mapComponentComboData.put((BigDecimal) row[0], (String) row[1]);
			}
			 
			 
			}
			return mapComponentComboData;
			}
	
	public String getIdentityTypeMaster(BigDecimal componentId) {
		 
		String sql = "SELECT CUSTOMER_TYPE FROM FS_IDENTITY_TYPE_MASTER A INNER JOIN FS_BUSINESS_COMPONENT B "
				+ "WHERE A.BUSINESS_COMPONENT_ID = ? ";
		
		List<String> lstIdentity = jdbcTemplate.queryForList(sql, new Object[] { componentId }, String.class);		
		 
		String rtnIdentity="";
		if(lstIdentity.size()>0)
		{
		rtnIdentity=lstIdentity.get(0);
		}
		 
		return rtnIdentity;
		}*/
}
