package com.amx.jax.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.NotificationConstants;
import com.amx.amxlib.model.DailyPromotionDTO;
import com.amx.amxlib.model.PromotionDto;
import com.amx.jax.config.JaxTenantProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.DailyPromotionDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.promotion.DailyPromotion;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dict.AmxEnums.CommunicationEvents;
import com.amx.jax.dict.Language;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PushNotifyClient;
import com.amx.jax.postman.client.WhatsAppClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.PushMessage;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.postman.model.WAMessage;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.repository.promotion.DailyPromotionRepository;
import com.amx.jax.util.CommunicationPrefsUtil;
import com.amx.jax.util.CommunicationPrefsUtil.CommunicationPrefsResult;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.DateUtil;
import com.amx.utils.JsonUtil;

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
	
	@Autowired
	DailyPromotionDao dailyPromotionDao;
	
	@Autowired
	CommunicationPrefsUtil communicationPrefsUtil;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	PushNotifyClient pushNotifyClient;
	
	@Autowired
	WhatsAppClient whatsAppClient;
	
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;

	Logger logger = LoggerFactory.getLogger(DailyPromotionManager.class);

	public void applyWantITbuyITCoupans(BigDecimal remittanceTransactionId, PersonInfo personInfo) {
		try {
			Date date = new Date();
			String startDateStr = jaxTenantProperties.getStartDate();
			String endDateStr = jaxTenantProperties.getEndDate();
			logger.info("Calling Want IT Buy IT Start Date String : " +startDateStr+ " End Date String : " + endDateStr);
			if(startDateStr != null && endDateStr != null) {
				Date startDate = DateUtil.parseDate(startDateStr);
				startDate.setHours(0);
				startDate.setMinutes(0);
				Date endDate = DateUtil.parseDate(endDateStr);
				endDate.setHours(0);
				endDate.setMinutes(0);
				logger.info("INSIDE if Start Date : " +startDate+ " End Date String : " + endDate);
				if (date.after(startDate) && date.before(endDate)) {
					logger.info("------ Comparison in Start Date & End Date ------");
					DailyPromotionDTO dailyPromotionDTO = getDailyPromotions(remittanceTransactionId, personInfo);
					sendVoucherEmail(dailyPromotionDTO, personInfo);
				}
			}
			else {
				logger.info(" INSIDE else Start Date End Date ");
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
				if (personInfo.getEmail() != null && !StringUtils.isBlank(personInfo.getEmail())) {
					wantITbuyITEmail.addTo(personInfo.getEmail());
				} else {
					wantITbuyITEmail.addTo("huzefa.abbasi@almullaexchange.com");
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

	public PromotionDto getWanitBuyitMsg(RemittanceTransaction remittanceTransaction) {
		DailyPromotion wantItPromoCode = dailyPromotionDao.getWantitByTrnxId(remittanceTransaction.getRemittanceTransactionId());
		PromotionDto dto = null;
		if(wantItPromoCode != null) {
			dto = new PromotionDto();
			String wantItBuyItCoupon = "wantitbuyit";
			dto.setPrize(wantItBuyItCoupon);
			return dto;
		}
		
		return dto;
	}

	public void applyJolibeePadalaCoupons(BigDecimal documentFinanceYear, BigDecimal documentNumber, BigDecimal branchCode) {
		logger.debug("DocumentFinYear "+documentFinanceYear+"document No "+documentNumber+"branchcode "+branchCode);
		List<RemittanceTransaction> remittanceTransaction =null;
		DailyPromotionDTO dailyPromotionDTO=new DailyPromotionDTO();
		if(!ConstantDocument.ONLINE_BRANCH_LOC_CODE.equals(branchCode)) {
			 remittanceTransaction=remittanceTransactionRepository.findByCollectionDocFinanceYearAndCollectionDocumentNo(documentFinanceYear, documentNumber);
			 for(RemittanceTransaction remittanceTransaction2:remittanceTransaction) {
				 dailyPromotionDTO=dailyPromotionDao.applyJolibeePadalaCoupons(remittanceTransaction2.getDocumentFinanceYear(), remittanceTransaction2.getDocumentNo(), remittanceTransaction2.getBranchId().getBranchId());
				 //logger.debug("Jolibee output "+JsonUtil.toJson(dailyPromotionDTO));
				 communicateJolibee(dailyPromotionDTO,remittanceTransaction2);
					
			 }
			 
		}else {
			remittanceTransaction=remittanceTransactionRepository.findByCollectionDocFinanceYearAndCollectionDocumentNo(documentNumber, documentFinanceYear);
			for(RemittanceTransaction remittanceTransaction2:remittanceTransaction) {
				dailyPromotionDTO=dailyPromotionDao.applyJolibeePadalaCoupons(remittanceTransaction2.getDocumentFinanceYear(),remittanceTransaction2.getDocumentNo(),branchCode);
				communicateJolibee(dailyPromotionDTO, remittanceTransaction2);
			}
		}
		
		
	}
	public void communicateJolibee(DailyPromotionDTO dailyPromotionDTO, RemittanceTransaction remittanceTransaction2) {
		if(!ArgUtil.isEmpty(dailyPromotionDTO.getPromotionMsg())&&ArgUtil.isEmpty(dailyPromotionDTO.getErrorMsg())) {
			Customer customer = customerRepository.getCustomerByCustomerIdAndIsActive(metaData.getCustomerId(), "Y");
			CommunicationPrefsResult communicationPrefsResult = communicationPrefsUtil.forCustomer(CommunicationEvents.BPI_JOLLIBEE, customer);
			Date createdDate = remittanceTransaction2.getCreatedDate();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Map<String, Object> modeldata = new HashMap<String, Object>();
			Map<String, Object> wrapper = new HashMap<String, Object>();
			String transactionDate = simpleDateFormat.format(createdDate);
			modeldata.put("transactionDate", transactionDate);
			wrapper.put("data", modeldata);
			logger.debug("Data for comm is "+JsonUtil.toJson(wrapper));
			if(communicationPrefsResult.isEmail()) {
				Email email = new Email();
				email.setITemplate(TemplatesMX.BPI_JOLLIBEE);
				if(metaData.getLanguageId().equals(ConstantDocument.L_ENG)) {
					email.setLang(Language.EN);
				}
				else {
					email.setLang(Language.AR);
				}
				email.addTo(customer.getEmail());
				email.setModel(wrapper);
				postManService.sendEmailAsync(email);
			}
			if(communicationPrefsResult.isSms()) {
				SMS sms = new SMS();
				sms.setITemplate(TemplatesMX.BPI_JOLLIBEE);
				sms.addTo(customer.getPrefixCodeMobile()+customer.getMobile());
				sms.setModel(wrapper);
				postManService.sendSMSAsync(sms);
			}
			
			if (communicationPrefsResult.isWhatsApp()) {
				WAMessage waMessage = new WAMessage();
				waMessage.setITemplate(TemplatesMX.BPI_JOLLIBEE);
				waMessage.addTo(customer.getWhatsappPrefix() + customer.getWhatsapp());
				waMessage.setModel(wrapper);
				whatsAppClient.send(waMessage);
			}
			if(communicationPrefsResult.isPushNotify()) {
				PushMessage pushMessage = new PushMessage();
				pushMessage.setITemplate(TemplatesMX.BPI_JOLLIBEE);
				pushMessage.addToUser(metaData.getCustomerId());
				pushMessage.setModel(wrapper);
				pushNotifyClient.send(pushMessage);
			}
		}
	}
}
