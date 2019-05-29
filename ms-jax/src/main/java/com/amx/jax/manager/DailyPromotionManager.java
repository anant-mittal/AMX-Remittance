package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.NotificationConstants;
import com.amx.amxlib.model.DailyPromotionDTO;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.promotion.DailyPromotion;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.promotion.DailyPromotionRepository;
import com.amx.utils.Constants;
import com.amx.utils.DateUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DailyPromotionManager {

	@Autowired
	DailyPromotionRepository dailyPromotionRepository;

	@Autowired
	PostManService postManService;

	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;

	@Autowired
	JaxTenantProperties jaxTenantProperties;

	Logger logger = LoggerFactory.getLogger(DailyPromotionManager.class);

	public void applyWantITbuyITCoupans(BigDecimal remittanceTransactionId, PersonInfo personInfo) {
		try {
			Date date = new Date();
			String startDateStr = jaxTenantProperties.getStartDate();
			String endDateStr = jaxTenantProperties.getEndDate();
			if(startDateStr != null && endDateStr != null) {
				Date startDate = DateUtil.parseDate(startDateStr);
				Date endDate = DateUtil.parseDate(endDateStr);
				if (date.after(startDate) && date.before(endDate)) {
					DailyPromotionDTO dailyPromotionDTO = getDailyPromotions(remittanceTransactionId, personInfo);
					//sendVoucherEmail(dailyPromotionDTO, personInfo);
					sendVoucherEmailMarketing(dailyPromotionDTO, personInfo);
				}
			}
			else {
				return;
			}
			
		} catch (Exception e) {
			logger.error("Error in WantIT BuyIT Coupon " + e.getMessage());
		}
	}

	public DailyPromotionDTO getDailyPromotions(BigDecimal remittanceTransactionId, PersonInfo personInfo) {

		DailyPromotionDTO dto = null;
		DailyPromotion firstPromoCode = dailyPromotionRepository.findFirstByUtilizeIsNull();

		if (firstPromoCode != null) {
			logger.info("Promotion Code for WantIT BuyIT : " + firstPromoCode.getPromoCode());

			dto = new DailyPromotionDTO();
			dto.setPromotionCode(firstPromoCode.getPromoCode());
			if(personInfo.getFirstName() != null && personInfo.getLastName() != null) {
				dto.setCustomerName(personInfo.getFirstName() + " " + personInfo.getLastName());
			}
			dto.setIdentityInt(personInfo.getIdentityInt());

			RemittanceTransaction remittanceData = remittanceApplicationDao
					.getRemittanceTransactionById(remittanceTransactionId);
			DailyPromotion dailyPromotion = firstPromoCode;

			dailyPromotion.setComCode(remittanceData.getCompanyCode());
			dailyPromotion.setDocCode(remittanceData.getDocumentCode());
			dailyPromotion.setDocFyr(remittanceData.getDocumentFinanceYear());
			dailyPromotion.setDocNo(remittanceData.getDocumentNo());
			dailyPromotion.setModifiedBy(personInfo.getIdentityInt().toString());
			dailyPromotion.setModifiedDate(new Date());
			dailyPromotion.setRemitTrnxId(remittanceData.getRemittanceTransactionId());
			dailyPromotion.setUtilize(Constants.YES);
			dailyPromotion.setUtilizeDate(new Date());

			dailyPromotionRepository.save(dailyPromotion);
		}

		return dto;
	}

	public void sendVoucherEmail(DailyPromotionDTO dailyPromotionDTO, PersonInfo personInfo) {
		try {
			if (dailyPromotionDTO.getPromotionCode() != null) {
				logger.info("Sending WantIT BuyIT voucher Email to customer : ");
				Email wantITbuyITEmail = new Email();
				wantITbuyITEmail.setSubject("Congratulations! You have got a coupon from Al Mulla Exchange.");
				if (personInfo.getEmail() != null) {
					wantITbuyITEmail.addTo(personInfo.getEmail());
				} else {
					wantITbuyITEmail.addTo("raynatest1234@gmail.com");
				}
				wantITbuyITEmail.setITemplate(TemplatesMX.WANTIT_BUYIT_PROMOTION);
				wantITbuyITEmail.setHtml(true);
				wantITbuyITEmail.getModel().put(NotificationConstants.RESP_DATA_KEY, dailyPromotionDTO);
				postManService.sendEmailAsync(wantITbuyITEmail);
			}
			
		} catch (Exception e) {
			logger.error("Error while sending mail WantIT BuyIT : " + e.getMessage());
		}
	}
	
	private void sendVoucherEmailMarketing(DailyPromotionDTO dailyPromotionDTO, PersonInfo personInfo) {
		try {
			if (dailyPromotionDTO.getPromotionCode() != null) {
				logger.info("Sending WantIT BuyIT voucher Email to Marketing New : ");
				Email wantITbuyITEmailM = new Email();
				wantITbuyITEmailM.setSubject("Congratulations! You have got a coupon from Al Mulla Exchange.");
				wantITbuyITEmailM.addTo("preeti.pawar@almullaexchange.com");
				wantITbuyITEmailM.addTo("almullatest@gmail.com");
				wantITbuyITEmailM.setITemplate(TemplatesMX.WANTIT_BUYIT_PROMOTION);
				wantITbuyITEmailM.setHtml(true);
				wantITbuyITEmailM.getModel().put(NotificationConstants.RESP_DATA_KEY, dailyPromotionDTO);
				postManService.sendEmailAsync(wantITbuyITEmailM);
			}
		} catch (Exception e) {
			logger.error("Error while sending mail WantIT BuyIT to Marketing : " + e.getMessage());
		}
	}

}
