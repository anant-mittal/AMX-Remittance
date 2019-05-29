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

import com.amx.amxlib.model.PromotionDto;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.PromotionDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.CountryBranch;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.promotion.PromotionDetailModel;
import com.amx.jax.dbmodel.promotion.PromotionHeader;
import com.amx.jax.dbmodel.promotion.PromotionLocationModel;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CountryBranchRepository;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.employee.AmgEmployeeRepository;
import com.amx.jax.service.CountryBranchService;
import com.amx.jax.service.FinancialService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.DateUtil;

import javassist.bytecode.stackmap.BasicBlock.Catch;

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
	
	@Autowired
	IRemittanceTransactionRepository remitTrnxRepository;
	
	@Autowired
	CountryBranchRepository countryBranchRepository;
	


	/**
	 * @return gives the latest promotion header applicable for current branch
	 * 
	 */
	public List<PromotionHeader> getPromotionHeader(Date trnxDate) {
		BigDecimal branchId = countryBranchService.getCountryBranchByCountryBranchId(metaData.getCountryBranchId())
				.getBranchId();
		UserFinancialYear userFinancialYear = financialService.getUserFinancialYear();
		List<PromotionLocationModel> promoLocations = promotionDao
				.checkforLocationHeader(userFinancialYear.getFinancialYear(), branchId);
		// fetch first one
		return promotionDao.getPromotionHeader(userFinancialYear.getFinancialYear(), promoLocations, trnxDate);
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
				Date transactionDate = remittanceTransaction.getCreatedDate();
				List<PromotionHeader> promoHeaders = getPromotionHeader(transactionDate);
				if (isPromotionValid(promoHeaders)) {
					dto = new PromotionDto();
					dto.setPrize(ConstantDocument.VOUCHER_ONLINE_PROMOTION_STR);
					dto.setPrizeMessage(
							"CONGRATULATIONS! YOU WON CHICKEN KING/SAGAR VOUCHER. Kindly contact 22057194 to claim prize");
				}
			}
			if (dto != null && remittanceTransaction.getDocumentNo() != null) {
				dto.setTransactionReference(remittanceTransaction.getDocumentFinanceYear().toString() + " / "
						+ remittanceTransaction.getDocumentNo().toString());
			}
			return dto;
		} catch (Exception e) {
			logger.debug("error occured in get promo dto", e);
			return null;
		}
	}

	private boolean isPromotionValid(List<PromotionHeader> promoHeaders) {
		if (promoHeaders != null && promoHeaders.size() > 0) {
			for (PromotionHeader ph : promoHeaders) {
				if (!ConstantDocument.Deleted.equals(ph.getRecSts())) {
					return true;
				}
			}
		}
		return false;
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
					PersonInfo personInfo = userService.getPersonInfo(remittanceApplication.getCustomerId().getCustomerId());
					Email email = new Email();
					email.setSubject(
							"Today's winner " + DateUtil.todaysDateWithDDMMYY(Calendar.getInstance().getTime(), ""));
					if (promotDto.isChichenVoucher()) {
						email.addTo("App-support@almullaexchange.com");
					} else {
						email.addTo("online@almullaexchange.com");
						email.addTo("huzefa.abbasi@almullaexchange.com");
					}
					email.setITemplate(TemplatesMX.PROMOTION_WINNER);
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
				voucherEmail.setITemplate(TemplatesMX.PROMOTION_COUPON);
				voucherEmail.setHtml(true);
				postManService.sendEmailAsync(voucherEmail);
			}
		} catch (Exception e) {
		}
	}
	
	/** added by Rabil on 19 May 2019 **/
	
	public String getPromotionPrizeForBranch(RemittanceResponseDto responseDto) {
		try {
		String promotionMessage = null;	
		List<RemittanceTransaction> remitTrnxList = remitTrnxRepository.findByCollectionDocIdAndCollectionDocFinanceYearAndCollectionDocumentNo(
				responseDto.getCollectionDocumentCode(), responseDto.getCollectionDocumentFYear(), responseDto.getCollectionDocumentNo());
		if(!remitTrnxList.isEmpty() && remitTrnxList.get(0)!=null) {
		 promotionMessage = promotionDao.callGetPromotionPrize(remitTrnxList.get(0).getDocumentNo(), remitTrnxList.get(0).getDocumentFinanceYear(), remitTrnxList.get(0).getLoccod());
		}
		return promotionMessage;
	}catch(Exception e) {
		e.printStackTrace();
		return null;
	}
	}
	/** added by rabil **/
	public PromotionDto getPromotionMessage(BigDecimal documentNoRemit,BigDecimal documentFinYearRemit,BigDecimal branchId,String currencyCode) {
		try {
			String promotionMessage = null;	
			//promotionMessage = promotionDao.callGetPromotionMessage(documentNoRemit, documentFinYearRemit, branchId);
			PromotionDto dto = new PromotionDto();
			CountryBranch countryBranch = countryBranchRepository.findByCountryBranchId(branchId);
			BigDecimal locationcode = countryBranch.getBranchId();
			//RemittanceTransaction remittanceTransaction = remittanceApplicationDao.getRemittanceTransactionByRemitDocNo(documentNoRemit, documentFinYearRemit);
			List<PromotionDetailModel> models = promotionDao.getPromotionDetailModel(documentFinYearRemit, documentNoRemit);
			if (models != null && !models.isEmpty() && locationcode.compareTo(ConstantDocument.ONLINE_BRANCH_LOC_CODE)!=0) {
				dto.setPrize(models.get(0).getPrize()==null?"":models.get(0).getPrize());
				dto.setPrizeMessage("Congratulations , you are now eligiable for Half "+currencyCode+" cash prize");
			}
	return dto;
	}catch(Exception e) {
		e.printStackTrace();
		return null;
	}
}	
}	
