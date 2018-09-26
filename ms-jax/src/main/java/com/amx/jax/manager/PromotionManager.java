package com.amx.jax.manager;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

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

import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.PromotionDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.PromotionDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.promotion.PromotionDetailModel;
import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionLocation;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.repository.employee.AmgEmployeeRepository;

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
	@Autowired
	PostManService postManService;
	@Autowired
	UserService userService;
	@Autowired
	AmgEmployeeRepository amgEmployeeRepository;

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
			RemittanceTransaction remittanceTransaction = remittanceApplicationDao
					.getRemittanceTransactionByRemitDocNo(docNoRemit, docFinyear);

			List<PromotionDetailModel> models = promotionDao.getPromotionDetailModel(docFinyear, docNoRemit);
			if (models != null && models.size() > 0) {
				dto = new PromotionDto();
				dto.setPrize(models.get(0).getPrize());
				dto.setPrizeMessage("CONGRATULATIONS! YOU WON " + models.get(0).getPrize()
						+ ". Kindly contact 22057194 to claim prize");
			} else {
				PromotionHeader promoHeader = getPromotionHeader();
				Date transactionDate = remittanceTransaction.getCreatedDate();
				if (transactionDate != null && transactionDate.after(promoHeader.getFromDate())
						&& transactionDate.before(promoHeader.getToDate())) {
					dto = new PromotionDto();
					dto.setPrize(ConstantDocument.VOUCHER_ONLINE_PROMOTION_STR);
					dto.setPrizeMessage(
							"CONGRATULATIONS! YOU WON CHICKEN KING/SAGAR VOUCHER. Kindly contact 22057194 to claim prize");
				}
			}
			if (dto != null && remittanceTransaction.getDocumentNo() != null) {
				dto.setTransactionReference(remittanceTransaction.getDocumentFinancialyear().toString() + " / "
						+ remittanceTransaction.getDocumentNo().toString());
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
			PromotionDto promotDto = getPromotionDto(documentNoRemit, documentFinYearRemit);
			if (promotDto != null) {
				logger.info("Sending promo winner Email to helpdesk : ");
				try {
					RemittanceTransaction remittanceApplication = remittanceApplicationDao
							.getRemittanceTransactionByRemitDocNo(documentNoRemit, documentFinYearRemit);
					PersonInfo personInfo = userService.getPersonInfo(remittanceApplication.getCustomerId());
					Email email = new Email();
					email.setSubject(
							"Today's winner " + DateUtil.todaysDateWithDDMMYY(Calendar.getInstance().getTime(), ""));
					if (promotDto.isChichenVoucher()) {
						email.addTo("App-support@almullaexchange.com");
					} else {
						email.addTo("online@almullaexchange.com");
						email.addTo("huzefa.abbasi@almullaexchange.com");
					}
					email.setTemplate(Templates.PROMOTION_WINNER);
					email.setHtml(true);
					email.getModel().put(RESP_DATA_KEY, personInfo);
					email.getModel().put("promotDto", promotDto);
					postManService.sendEmailAsync(email);
				} catch (Exception e) {
					logger.error("error in promotionWinnerCheck", e);
				}
			}
			return promotDto;
		} catch (Exception e) {
			logger.error("error in promotionWinnerCheck", e);
			return null;
		}
	}

	public void sendVoucherEmail(PromotionDto promotDto, PersonInfo personInfo) {

		try {
			if (promotDto != null && ConstantDocument.VOUCHER_ONLINE_PROMOTION_STR.equals(promotDto.getPrize())) {
				// voucher mail to customer
				logger.info("Sending promo voucher Email to customer : ");
				Email voucherEmail = new Email();
				voucherEmail.setSubject("Congratulations! You have got a coupon from Al Mulla Exchange.");
				voucherEmail.addTo(personInfo.getEmail());
				voucherEmail.setTemplate(Templates.PROMOTION_COUPON);
				voucherEmail.setHtml(true);
				postManService.sendEmailAsync(voucherEmail);
			}
		} catch (Exception e) {
		}
	}
}
