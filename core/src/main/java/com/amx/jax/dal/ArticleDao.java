package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.ArticleDetails;
import com.amx.jax.dbmodel.IncomeRangeMaster;
import com.amx.jax.repository.IArticleDetailsRepository;
import com.amx.jax.repository.IIncomeRangeRepository;
import com.amx.utils.Constants;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ArticleDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	IArticleDetailsRepository iArticleDetailsRepository;
	
	@Autowired
	IIncomeRangeRepository iIncomeRangeRepository;
	
	public List<Map<String, Object>> getArticles(BigDecimal languageId) {
		String sql = "select FAMD.ARTICLE_DESC,FAMD.ARTICLE_DESC_ID, FAMD.ARTICLE_ID,FAMD.LANGUAGE_ID " + 
				"from FS_ARTICLE_MASTER_DESC FAMD INNER JOIN FS_ARTICLE_MASTER FAM ON FAMD.ARTICLE_ID = FAM.ARTICLE_ID " + 
				"where FAMD.LANGUAGE_ID = ? AND FAM.ISACTIVE = ? AND FAM.CUSTOMER_TYPE <> ?" ; 
		List<Map<String, Object>> articleList = jdbcTemplate.queryForList(sql,new Object[] 
				{languageId,Constants.CUST_ACTIVE_INDICATOR,Constants.COMPNY_TYPE});
		return articleList;	
		
		}	
	
	public List<Map<String, Object>> getDesignationData(BigDecimal articleId, BigDecimal languageId) {
		
		String sql = "SELECT FADD.ARTICLE_DETAIL_DESC,FADD.ARTICLE_DETAILS_DESC_ID," + 
				"		FADD.ARTICLE_DETAILS_ID,FADD.LANGUAGE_ID" + 
				"		FROM FS_ARTICLE_DETAILS_DESC FADD INNER JOIN  FS_ARTICLE_DETAILS FAD " + 
				"		ON FAD.ARTICLE_DETAIL_ID = FADD.ARTICLE_DETAILS_ID " + 
				"		WHERE FAD.ARTICLE_ID = ? AND FAD.ISACTIVE= ? AND FADD.LANGUAGE_ID = ?";
		List<Map<String, Object>> designationList = jdbcTemplate.queryForList(sql,new Object[] 
				{articleId,Constants.CUST_ACTIVE_INDICATOR,languageId});		
		return designationList;		
		}

	public List<Map<String, Object>> getIncomeRange(BigDecimal countryId, BigDecimal articleDetailId) {
		

		String sql = "SELECT FIRM.INCOME_RANGE_ID,FIRM.INCOME_FROM,FIRM.INCOME_TO,FIRM.ARTICLE_DETAIL_ID,FIRM.APPLICATION_COUNTRY_ID ,FIRM.ISACTIVE " + 
				"FROM FS_INCOME_RANGE_MASTER FIRM INNER JOIN FS_ARTICLE_DETAILS FAD ON  FIRM.ARTICLE_DETAIL_ID= FAD.ARTICLE_DETAIL_ID " + 
				"INNER JOIN FS_COUNTRY_MASTER FCM ON FIRM.APPLICATION_COUNTRY_ID = FCM.COUNTRY_ID " + 
				"WHERE FCM.COUNTRY_ID = ? AND FIRM.ISACTIVE= ? AND FAD.ARTICLE_DETAIL_ID = ? ";
		List<Map<String, Object>> incomeRangeList = jdbcTemplate.queryForList(sql,new Object[] 
				{countryId,Constants.CUST_ACTIVE_INDICATOR,articleDetailId});
		return incomeRangeList;		
		}
	
	public ArticleDetails getArticleDetailsByArticleDetailId(BigDecimal id)
	{
		return iArticleDetailsRepository.getArticleDetailsByArticleDetailId(id);
	}
	
	public IncomeRangeMaster getIncomeRangeMasterByIncomeRangeId(BigDecimal id)
	{
		return iIncomeRangeRepository.getIncomeRangeMasterByIncomeRangeId(id);
		}
}
