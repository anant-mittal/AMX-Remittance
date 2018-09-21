package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.PromotionDto;
import com.amx.jax.dao.PromotionDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.promotion.PromotionDetailModel;
import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionLocation;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.service.FinancialService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PromotionManager {

	Logger logger = LoggerFactory.getLogger(PromotionManager.class);

	@Autowired
	PromotionDao promotionDao;
	@Autowired
	CountryBranchService countryBranchService;
	@Autowired
	MetaData metaData;
	@Autowired
	FinancialService financialService;
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;

	/**
	 * @return gives the latest promotion header applicable for current branch
	 * 
	 */
	public PromotionHeader getPromotionHeader() {
		BigDecimal branchId = countryBranchService.getCountryBranchByCountryBranchId(metaData.getCountryBranchId())
				.getBranchId();
		UserFinancialYear userFinancialYear = financialService.getUserFinancialYear();
		List<PromotionLocation> promoLocations = promotionDao
				.checkforLocationHeader(userFinancialYear.getFinancialYear(), branchId);
		// fetch first one
		return promotionDao.getPromotionHeader(userFinancialYear.getFinancialYear(),
				promoLocations.get(0).getDocumentNo());
	}

	public PromotionDto getPromotionDto(BigDecimal docNoRemit, BigDecimal docFinyear) {
		try {
			PromotionDto dto = null;
			List<PromotionDetailModel> models = promotionDao.getPromotionDetailModel(docFinyear, docNoRemit);
			if (models != null && models.size() > 0) {
				dto = new PromotionDto();
				dto.setPrize(models.get(0).getPrize());
				dto.setPrizeMessage("CONGRATULATIONS! YOU WON " + models.get(0).getPrize());
			} else {
				PromotionHeader promoHeader = getPromotionHeader();
				RemittanceTransaction remittanceTransaction = remittanceApplicationDao
						.getRemittanceTransaction(docNoRemit, docFinyear);
				Date transactionDate = remittanceTransaction.getCreatedDate();
				if (transactionDate != null && transactionDate.after(promoHeader.getFromDate())
						&& transactionDate.before(promoHeader.getToDate())) {
					dto = new PromotionDto();
					dto.setPrize("CHICKEN KING/SAGAR VOUCHER");
					dto.setPrizeMessage("CONGRATULATIONS! YOU WON CHICKEN KING/SAGAR VOUCHER");
				}
			}
			return dto;
		} catch (Exception e) {
			return null;
		}
	}

	public PromotionDto promotionWinnerCheck(BigDecimal documentNoRemit, BigDecimal documentFinYearRemit) {
		try {
			BigDecimal branchId = countryBranchService.getCountryBranchByCountryBranchId(metaData.getCountryBranchId())
					.getBranchId();
			promotionDao.callGetPromotionPrize(documentNoRemit, documentFinYearRemit, branchId);
			return getPromotionDto(documentNoRemit, documentFinYearRemit);
		} catch (Exception e) {
			logger.error("error in promotionWinnerCheck", e);
			return null;
		}
	}
}
