package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.IncomeRangeMaster;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IArticleDetailsRepository;
import com.amx.jax.repository.IIncomeRangeRepository;
import com.amx.utils.Constants;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ArticleDao {

	public static final Logger LOGGER = LoggerFactory.getLogger(ArticleDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	IArticleDetailsRepository iArticleDetailsRepository;

	@Autowired
	IIncomeRangeRepository iIncomeRangeRepository;
	@Autowired
	MetaData metaData;

	public List<Map<String, Object>> getArticles(BigDecimal languageId) {
		String sql = "select FAMD.ARTICLE_DESC,FAMD.ARTICLE_DESC_ID, FAMD.ARTICLE_ID,FAMD.LANGUAGE_ID "
				+ "from FS_ARTICLE_MASTER_DESC FAMD INNER JOIN FS_ARTICLE_MASTER FAM ON FAMD.ARTICLE_ID = FAM.ARTICLE_ID "
				+ "where FAMD.LANGUAGE_ID = ? AND FAM.ISACTIVE = ? AND FAM.CUSTOMER_TYPE <> ?";
		List<Map<String, Object>> articleList = jdbcTemplate.queryForList(sql,
				new Object[] { languageId, Constants.CUST_ACTIVE_INDICATOR, Constants.COMPNY_TYPE });
		return articleList;

	}

	public List<Map<String, Object>> getDesignationData(BigDecimal articleId, BigDecimal languageId) {

		String sql = "SELECT FADD.ARTICLE_DETAIL_DESC,FADD.ARTICLE_DETAILS_DESC_ID,"
				+ "		FADD.ARTICLE_DETAILS_ID,FADD.LANGUAGE_ID"
				+ "		FROM FS_ARTICLE_DETAILS_DESC FADD INNER JOIN  FS_ARTICLE_DETAILS FAD "
				+ "		ON FAD.ARTICLE_DETAIL_ID = FADD.ARTICLE_DETAILS_ID "
				+ "		WHERE FAD.ARTICLE_ID = ? AND FAD.ISACTIVE= ? AND FADD.LANGUAGE_ID = ?";
		List<Map<String, Object>> designationList = jdbcTemplate.queryForList(sql,
				new Object[] { articleId, Constants.CUST_ACTIVE_INDICATOR, languageId });
		return designationList;
	}
	
	public List<Map<String, Object>> getDesignationsByCustomer(BigDecimal languageId, BigDecimal customerId) {

		String sql = "SELECT C.ARTICLE_DETAIL_ID,D.ARTICLE_DETAIL_DESC"
				
				+ " FROM FS_ARTICLE_DETAILS C, FS_ARTICLE_DETAILS_DESC D"
				
				+ "	WHERE C.ARTICLE_DETAIL_ID = D.ARTICLE_DETAILS_ID AND D.LANGUAGE_ID = ? AND C.ISACTIVE = 'Y' AND C.ARTICLE_ID = (SELECT B.ARTICLE_ID FROM FS_ARTICLE_DETAILS B   WHERE B.ARTICLE_DETAIL_ID = (SELECT A.ARTICLE_DETAIL_ID FROM FS_CUSTOMER A WHERE A.CUSTOMER_ID = ?)) ORDER BY C.ARTICLE_DETAIL_ID ";
		List<Map<String, Object>> designationList = jdbcTemplate.queryForList(sql,
				new Object[] {languageId, customerId });
		return designationList;
	}

	public List<Map<String, Object>> getIncomeRange(BigDecimal countryId, BigDecimal articleDetailId) {

		String sql = "SELECT FIRM.INCOME_RANGE_ID,FIRM.INCOME_FROM,FIRM.INCOME_TO,FIRM.ARTICLE_DETAIL_ID,FIRM.APPLICATION_COUNTRY_ID ,FIRM.ISACTIVE "
				+ "FROM FS_INCOME_RANGE_MASTER FIRM INNER JOIN FS_ARTICLE_DETAILS FAD ON  FIRM.ARTICLE_DETAIL_ID= FAD.ARTICLE_DETAIL_ID "
				+ "INNER JOIN FS_COUNTRY_MASTER FCM ON FIRM.APPLICATION_COUNTRY_ID = FCM.COUNTRY_ID "
				+ "WHERE FCM.COUNTRY_ID = ? AND FIRM.ISACTIVE= ? AND FAD.ARTICLE_DETAIL_ID = ? ";
		List<Map<String, Object>> incomeRangeList = jdbcTemplate.queryForList(sql,
				new Object[] { countryId, Constants.CUST_ACTIVE_INDICATOR, articleDetailId });
		return incomeRangeList;
	}

	public ArticleDetails getArticleDetailsByArticleDetailId(BigDecimal id) {
		return iArticleDetailsRepository.getArticleDetailsByArticleDetailId(id);
	}

	public IncomeRangeMaster getIncomeRangeMasterByIncomeRangeId(BigDecimal id) {
		return iIncomeRangeRepository.getIncomeRangeMasterByIncomeRangeId(id);
	}

	public List<Map<String, Object>> getIncomeRangeForCustomer(Customer customer) {

		String sql = "select FADD.article_detail_desc, FAMD.ARTICLE_DESC, FIRM.MONTHLY_INCOME from  "
				+ "                FS_ARTICLE_DETAILS_desc FADD, " + "                FS_ARTICLE_MASTER_DESC FAMD, "
				+ "                FS_ARTICLE_DETAILS FAD, " + "				FS_INCOME_RANGE_MASTER FIRM "
				+ "                where FAMD.LANGUAGE_ID=? " + "                and FADD.LANGUAGE_ID=? "
				+ "                and FADD.ARTICLE_DETAILS_ID=? "
				+ "				and FAD.ARTICLE_ID= FAMD.ARTICLE_ID  "
				+ "                and FAD.ARTICLE_DETAIL_ID = FADD.ARTICLE_DETAILS_ID "
				+ "				and FADD.ARTICLE_DETAILS_DESC_ID=FIRM.ARTICLE_DETAIL_ID  "
				+ "                and FIRM.INCOME_RANGE_ID=?";
		List<Map<String, Object>> incomeRangeList = jdbcTemplate.queryForList(sql,
				new Object[] { metaData.getLanguageId(), metaData.getLanguageId(),
						customer.getFsArticleDetails().getArticleDetailId(),
						customer.getFsIncomeRangeMaster().getIncomeRangeId() });
		return incomeRangeList;
	}
	
	public String getAricleDetailDesc(Customer customer) {
		String articleDetailDesc = null;
		try {
			String sql = "select ARTICLE_DETAIL_DESC from FS_ARTICLE_DETAILS_desc where ARTICLE_DETAILS_ID = ? and LANGUAGE_ID= ?";
			articleDetailDesc = jdbcTemplate.queryForObject(sql,
					new Object[] { customer.getFsArticleDetails().getArticleDetailId(), metaData.getLanguageId() },
					String.class);
		} catch (Exception e) {
			LOGGER.debug("Error occured in getAricleDetailDesc", e);
		}
		return articleDetailDesc;
	}
	
	public List<Map<String, Object>> getArticleDescriptionByArticleDetailId(BigDecimal articleDetailId , BigDecimal languageId , BigDecimal customerId){
			LOGGER.info("article detailed id set is "+articleDetailId+languageId+customerId);
			String sql = "SELECT D.ARTICLE_DETAIL_DESC FROM FS_ARTICLE_DETAILS C,FS_ARTICLE_DETAILS_DESC D"+
						" WHERE C.ARTICLE_DETAIL_ID = D.ARTICLE_DETAILS_ID AND D.ARTICLE_DETAILS_ID = ? AND D.LANGUAGE_ID = ? AND C.ARTICLE_ID = (SELECT B.ARTICLE_ID FROM FS_ARTICLE_DETAILS B WHERE B.ARTICLE_DETAIL_ID = (SELECT A.ARTICLE_DETAIL_ID FROM FS_CUSTOMER A WHERE A.CUSTOMER_ID = ?)) AND C.ISACTIVE = 'Y'";
			
			List<Map<String, Object>> articleDetailDesc = jdbcTemplate.queryForList(sql,
					new Object[] { articleDetailId, metaData.getLanguageId(), metaData.getCustomerId() });
			
			return articleDetailDesc;
	}
	
	public String getMonthlyIncomeRange(Customer customer) {
		String articleDetailDesc = null;
		try {
			String sql = "select MONTHLY_INCOME from FS_INCOME_RANGE_MASTER where INCOME_RANGE_ID = ?";
			articleDetailDesc = jdbcTemplate.queryForObject(sql,
					new Object[] { customer.getFsIncomeRangeMaster().getIncomeRangeId() }, String.class);
		} catch (Exception e) {
			LOGGER.debug("Error occured in getMonthlyIncomeRange", e);
		}
		return articleDetailDesc;
	}

	public String getArticleDesc(Customer customer) {
		String articleDesc;
		String sql = "select ARTICLE_DESC from FS_ARTICLE_MASTER_DESC where ARTICLE_ID = "
				+ "(select ARTICLE_ID from FS_ARTICLE_DETAILS where ARTICLE_DETAIL_ID = ?) and language_id = ?";
		articleDesc = jdbcTemplate.queryForObject(sql,
				new Object[] { customer.getFsArticleDetails().getArticleDetailId(), metaData.getLanguageId() },
				String.class);
		return articleDesc;
	}
}
