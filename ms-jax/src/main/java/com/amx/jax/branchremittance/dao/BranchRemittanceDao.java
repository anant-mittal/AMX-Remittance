package com.amx.jax.branchremittance.dao;

/** 
 * @author rabil
 */
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.CollectDetailMdlv1;
import com.amx.jax.dbmodel.CollectionMdlv1;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjustMdlv1;
import com.amx.jax.dbmodel.PaygDetailsModel;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.partner.RemitApplSrvProv;
import com.amx.jax.dbmodel.partner.RemitTrnxSrvProv;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.LoyaltyClaimRequest;
import com.amx.jax.dbmodel.remittance.LoyaltyPointsModel;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAml;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceApplicationSplitting;
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.dbmodel.remittance.RemittanceTransactionSplitting;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchApplicationDto;
import com.amx.jax.model.response.remittance.RemittanceApplicationResponseModel;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.ILoyaltyClaimRequestRepository;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.IRemitApplSrvProvRepository;
import com.amx.jax.repository.IRemitTrnxSrvProvRepository;
import com.amx.jax.repository.IRemittanceAdditionalInstructionRepository;
import com.amx.jax.repository.IRemittanceAmlRepository;
import com.amx.jax.repository.IRemittanceApplSplitRepository;
import com.amx.jax.repository.IRemittanceBenificiaryRepository;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.IRemittanceTrnxSplitRepository;
import com.amx.jax.repository.PaygDetailsRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.ILoyaltyPointRepository;
import com.amx.jax.services.RemittanceTransactionService;
import com.amx.jax.util.JaxUtil;

@Component
 @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode =
 ScopedProxyMode.TARGET_CLASS)
public class BranchRemittanceDao {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	RemittanceApplicationRepository appRepo;

	@Autowired
	RemittanceApplicationBeneRepository appBeneRepo;

	@Autowired
	AdditionalInstructionDataRepository addlInstDataRepo;

	@Autowired
	IRemitApplAmlRepository applAmlRepository;

	@Autowired
	ICollectionRepository collectRepository;

	@Autowired
	ICollectionDetailRepository collectDetailRepository;

	@Autowired
	ForeignCurrencyAdjustRepository foreignCurrAdjustRepository;

	@Autowired
	ApplicationProcedureDao applicationProcedureDao;

	@Autowired
	IRemittanceTransactionRepository remitTrnxRepository;

	@Autowired
	IRemittanceBenificiaryRepository remitBeneRepository;

	@Autowired
	IRemittanceAdditionalInstructionRepository remitAddRepository;

	@Autowired
	IRemittanceAmlRepository remitAmlRepository;

	@Autowired
	ILoyaltyClaimRequestRepository loyaltyClaimRepo;

	@Autowired
	ILoyaltyPointRepository loyalPointsRepository;

	@Autowired
	MetaData metaData;

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	IRemitApplSrvProvRepository remitApplSrvProvRepository;
	
	@Autowired
	IRemitTrnxSrvProvRepository remitTrnxSrvProvRepository;
	
    @Autowired
    IRemittanceTransactionDao remittanceTransactionDao;
    
    
    @Autowired
    IRemittanceApplSplitRepository remittanceApplSplitRepository;
    
    @Autowired
    IRemittanceTrnxSplitRepository remittanceTrnxSplitRepository;

	@Autowired
	PaygDetailsRepository pgRepository;
	
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	
	@Autowired
	RemittanceTransactionService remittanceTransactionService;

	@Transactional
	@SuppressWarnings("unchecked")
	public void saveAllApplications(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx = (RemittanceApplication) mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary) mapAllDetailApplSave.get("EX_APPL_BENE");
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>) mapAllDetailApplSave.get("EX_APPL_ADDL");
		List<RemitApplAmlModel> saveApplAmlList = (List<RemitApplAmlModel>) mapAllDetailApplSave.get("EX_APPL_AML");
		RemitApplSrvProv saveApplSrvProv = (RemitApplSrvProv) mapAllDetailApplSave.get("EX_APPL_SRV_PROV");
		List<RemittanceApplicationSplitting> applSplitList =(List<RemittanceApplicationSplitting>)mapAllDetailApplSave.get("EX_APPL_SPLIT");
		
		if (saveApplTrnx != null) {
			BigDecimal documentNo = generateDocumentNumber(saveApplTrnx.getFsCountryMasterByApplicationCountryId().getCountryId(), saveApplTrnx.getFsCompanyMaster().getCompanyId(),
					saveApplTrnx.getDocumentCode(), saveApplTrnx.getDocumentFinancialyear(), saveApplTrnx.getLoccod());
			if (!JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
				throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO, "Application document number shouldnot be null or blank");
			}
			saveApplTrnx.setDocumentNo(documentNo);
			RemittanceApplication applSave1 = appRepo.save(saveApplTrnx);
			
			if(applSplitList !=null && !applSplitList.isEmpty()) {
				for(RemittanceApplicationSplitting applSplit : applSplitList) {				
					applSplit.setDocumentNo(documentNo);
					applSplit.setRemittanceApplicationId(applSave1);
					remittanceApplSplitRepository.save(applSplit);
				}
			}
		}else {
			throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO, "Application document number shouldnot be null or blank");
		}
		
		if (saveApplBene != null) {
			appBeneRepo.save(saveApplBene);
		}

		if (saveApplAddlData != null) {
			addlInstDataRepo.save(saveApplAddlData);
		}
		
		if (saveApplAmlList != null) {
			applAmlRepository.save(saveApplAmlList);
		}
		
		if (saveApplSrvProv != null) {
			saveApplSrvProv.setRemittanceApplicationId(saveApplTrnx.getRemittanceApplicationId());
			remitApplSrvProvRepository.save(saveApplSrvProv);
		}

	}

	@Transactional
	@SuppressWarnings("unchecked")
	public RemittanceResponseDto saveRemittanceTransaction(HashMap<String, Object> mapAllDetailRemitSave) {

		try {
			RemittanceResponseDto responseDto = new RemittanceResponseDto();

			CollectionMdlv1 collectModel = (CollectionMdlv1) mapAllDetailRemitSave.get("EX_COLLECT");
			List<CollectDetailMdlv1> collectDetailsModel = (List<CollectDetailMdlv1>) mapAllDetailRemitSave.get("EX_COLLECT_DET");
			List<ForeignCurrencyAdjustMdlv1> foreignCurrencyAdjust = (List<ForeignCurrencyAdjustMdlv1>) mapAllDetailRemitSave.get("EX_CURR_ADJUST");
			Map<BigDecimal, RemittanceTransaction> remitTrnxList = (Map<BigDecimal, RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
			Map<BigDecimal, RemittanceBenificiary> remitBeneList = (Map<BigDecimal, RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
			Map<BigDecimal, List<RemittanceAdditionalInstructionData>> addlTrnxList = (Map<BigDecimal, List<RemittanceAdditionalInstructionData>>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
			Map<BigDecimal, List<RemittanceAml>> amlTrnxList = (Map<BigDecimal, List<RemittanceAml>>) mapAllDetailRemitSave.get("EX_REMIT_AML");
			Map<BigDecimal, RemitTrnxSrvProv> remitSprProvList = (Map<BigDecimal, RemitTrnxSrvProv>) mapAllDetailRemitSave.get("EX_REMIT_SRV_PROV");
			Map<BigDecimal,  List<RemittanceTransactionSplitting>> remitTrnxSplitList = (Map<BigDecimal, List<RemittanceTransactionSplitting>>) mapAllDetailRemitSave.get("EX_REMIT_SPLIT"); 
			
			LoyaltyClaimRequest lylClaim = (LoyaltyClaimRequest) mapAllDetailRemitSave.get("LYL_CLAIM");
			List<LoyaltyPointsModel> loyaltyPoitns = (List<LoyaltyPointsModel>) mapAllDetailRemitSave.get("LOYALTY_POINTS");

			if (collectModel != null) {
				BigDecimal documentNo = generateDocumentNumber(collectModel.getApplicationCountryId(), collectModel.getFsCompanyMaster().getCompanyId(),
						collectModel.getDocumentId(), collectModel.getDocumentFinanceYear(), collectModel.getLocCode());
				if (!JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
					throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document should not be blank.");
				}
				collectModel.setDocumentNo(documentNo);
				collectRepository.save(collectModel);
				responseDto.setReceiptNo(collectModel.getDocumentFinanceYear() + "/" + collectModel.getDocumentNo());
				responseDto.setCollectionDocumentNo(collectModel.getDocumentNo());
				responseDto.setCollectionDocumentFYear(collectModel.getDocumentFinanceYear());
				responseDto.setCollectionDocumentCode(collectModel.getDocumentCode());
				if (!StringUtils.isBlank(collectModel.getCashDeclarationIndicator()) && collectModel.getCashDeclarationIndicator().equalsIgnoreCase(ConstantDocument.Yes)) {
					responseDto.setDeclarationReport(true);
				}
			} else {
				throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection details not found.");
			}

			if (collectDetailsModel != null && !collectDetailsModel.isEmpty() && JaxUtil.isNullZeroBigDecimalCheck(collectModel.getDocumentNo())) {
				for(CollectDetailMdlv1 collectDetModel : collectDetailsModel) {
					collectDetModel.setDocumentNo(collectModel.getDocumentNo());
					collectDetailRepository.save(collectDetModel);
				}
			}

			if (lylClaim != null && JaxUtil.isNullZeroBigDecimalCheck(lylClaim.getVoucherNo())) {
				loyaltyClaimRepo.save(lylClaim);
			}

			if (foreignCurrencyAdjust != null && !foreignCurrencyAdjust.isEmpty()) {
				for(ForeignCurrencyAdjustMdlv1 foreignCurrAdju:foreignCurrencyAdjust) {
					foreignCurrAdju.setDocumentNo(collectModel.getDocumentNo());
					foreignCurrAdjustRepository.save(foreignCurrAdju);
				}
			}

			if (remitTrnxList != null && !remitTrnxList.isEmpty() && collectModel != null) {
				for (BigDecimal applicationId : remitTrnxList.keySet()) {

					RemittanceTransaction remitTrnx = remitTrnxList.get(applicationId);
					BigDecimal documentNo = generateDocumentNumber(remitTrnx.getApplicationCountryId().getCountryId(), remitTrnx.getCompanyId().getCompanyId(),
							remitTrnx.getDocumentId().getDocumentCode(), remitTrnx.getDocumentFinanceYear(), remitTrnx.getLoccod());

					if (!JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
						throw new GlobalException(JaxError.INVALID_REMITTANCE_DOCUMENT_NO, "Document Seriality  setup  not defined for Remittance.");
					}
					remitTrnx.setDocumentNo(documentNo);
					remitTrnx.setCollectionDocumentNo(collectModel.getDocumentNo());
					RemittanceApplication remittanceApplication = remittanceApplicationDao.getApplication(applicationId);
					if(ConstantDocument.PB_PAYMENT.equalsIgnoreCase(remittanceApplication.getPaymentType())) { /** why u pplied this logic **/
						remitTrnx.setPaymentType(ConstantDocument.PB_PAYMENT);
					}
					
					RemittanceTransaction remitTrnx1 = remitTrnxRepository.save(remitTrnx);
					
					
					if(remitTrnxSplitList!=null && !remitTrnxSplitList.isEmpty()) {
						List<RemittanceTransactionSplitting> trnxRemitSplitList = remitTrnxSplitList.get(applicationId);
						if(trnxRemitSplitList!=null && !trnxRemitSplitList.isEmpty()) {
						for(RemittanceTransactionSplitting remitSplit : trnxRemitSplitList) {
							remitSplit.setRemittanceTransactionId(remitTrnx1);
							remitSplit.setDocumentNo(documentNo);
							remittanceTrnxSplitRepository.save(remitSplit);
						}
						}
						
					}
					
					
					

					if (remitBeneList != null && !remitBeneList.isEmpty()) {
						logger.debug("remitBeneRepository.save ApplicationId :"+applicationId);
						RemittanceBenificiary remitBene = remitBeneList.get(applicationId);
						if (remitBene != null) {
							remitBene.setExRemittancefromBenfi(remitTrnx1);
							remitBene.setDocumentNo(documentNo);
							remitBeneRepository.save(remitBene);
						} else {
							throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance bene  details not found");
						}
					}
					if (addlTrnxList != null && !addlTrnxList.isEmpty()) {
						
						List<RemittanceAdditionalInstructionData> lstAddIns = addlTrnxList.get(applicationId);
						for (RemittanceAdditionalInstructionData remitAdd : lstAddIns) {
							remitAdd.setExRemittanceTransaction(remitTrnx1);
							remitAdd.setDocumentNo(documentNo);
							remitAddRepository.save(remitAdd);
						}
					} else {
						throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance additional details not found");
					}

					if (amlTrnxList != null && !amlTrnxList.isEmpty()) {
						List<RemittanceAml> remitamlList = amlTrnxList.get(applicationId);
						for (RemittanceAml remitaml : remitamlList) {
							remitaml.setExRemittancefromAml(remitTrnx1);
							remitAmlRepository.save(remitaml);
						}
					}
					
					if(remitSprProvList != null && !remitSprProvList.isEmpty()) {
						logger.debug("remit service provider Repository.save ApplicationId :"+applicationId);
						RemitTrnxSrvProv remitTrnxSrvProv = remitSprProvList.get(applicationId);
						if(remitTrnxSrvProv != null) {
							remitTrnxSrvProv.setRemittanceTransactionId(remitTrnx1.getRemittanceTransactionId());
							remitTrnxSrvProvRepository.save(remitTrnxSrvProv);
						}
					}
				}

				if (loyaltyPoitns != null && !loyaltyPoitns.isEmpty()) {
					loyalPointsRepository.save(loyaltyPoitns);
				}
				
			} else {
				throw new GlobalException(JaxError.NO_RECORD_FOUND, "Remittance trnx details not found");
			}

			return responseDto;
		} catch (GlobalException e){
			throw new GlobalException(e.getErrorKey(),e.getErrorMessage());
		}

	}
	
	

	public BigDecimal generateDocumentNumber(BigDecimal appCountryId, BigDecimal companyId, BigDecimal documentId, BigDecimal finYear, BigDecimal branchId) {
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId, finYear, ConstantDocument.Update, branchId);
		BigDecimal no = (BigDecimal) output.get("P_DOC_NO");
		return (BigDecimal) output.get("P_DOC_NO");
	}


	public void updateApplication(RemittanceTransaction remitTrnx) {
			logger.info("remitTrnx.getApplicationDocumentNo() :" + remitTrnx.getApplicationDocumentNo() + "\t docfyr :" + remitTrnx.getApplicationFinanceYear());
		RemittanceApplication appl = appRepo.fetchRemitApplTrnx(remitTrnx.getApplicationDocumentNo(), remitTrnx.getApplicationFinanceYear());
		if (appl != null) {
			appl = appRepo.findOne(appl.getRemittanceApplicationId());
			appl.setTransactionFinancialyear(remitTrnx.getDocumentFinanceYear());
			appl.setExUserFinancialYearByTransactionFinanceYearID(new UserFinancialYear(remitTrnx.getDocumentFinanceYr()));
			appl.setTransactionDocumentNo(remitTrnx.getDocumentNo());
			appl.setApplicaitonStatus(ConstantDocument.T);
			appl.setBlackListIndicator(remitTrnx.getBlackListIndicator());
			appRepo.save(appl);
		}

	}

	@Transactional
	public void deleteFromCart(BigDecimal applNo, String status) {
		// RemittanceApplication appl = getApplicationDetails(applNo);
		RemittanceApplication appl = appRepo.getApplicationForDelete(new Customer(metaData.getCustomerId()), applNo);

		if (appl != null) {
			appl.setRemittanceApplicationId(appl.getRemittanceApplicationId());
			appl.setIsactive(status);
			appRepo.save(appl);
		}
	}

	@Transactional
	private RemittanceApplication getApplicationDetails(BigDecimal applNo) {
		RemittanceApplication appl = appRepo.findOne(applNo);
		return appl;
	}

	public void deleteFromCartUsingJdbcTemplate(BigDecimal applNo, String status) {
		String sql = "UPDATE EX_APPL_TRNX set ISACTIVE='" + status + "' where REMITTANCE_APPLICATION_ID =" + applNo;
		if (JaxUtil.isNullZeroBigDecimalCheck(applNo)) {
			jdbcTemplate.update(sql);
		}
	}

	public void updateApplicationToMoveEmos(RemittanceResponseDto responseDto) {
		List<RemittanceTransaction> remitTrnxList = remitTrnxRepository.findByCollectionDocIdAndCollectionDocFinanceYearAndCollectionDocumentNo(
				responseDto.getCollectionDocumentCode(), responseDto.getCollectionDocumentFYear(), responseDto.getCollectionDocumentNo());
		if (remitTrnxList != null && !remitTrnxList.isEmpty()) {
			for (RemittanceTransaction remitTrnx : remitTrnxList) {
				RemittanceApplication appl = appRepo.getApplicationDetailsForUpdate(remitTrnx.getCustomerId(), remitTrnx.getApplicationDocumentNo(),
						remitTrnx.getApplicationFinanceYear());
				if (JaxUtil.isNullZeroBigDecimalCheck(appl.getRemittanceApplicationId())) {
					String sql = "UPDATE EX_APPL_TRNX set APPLICATION_STATUS='T' ,TRANSACTION_FINANCE_YEAR =" + remitTrnx.getDocumentFinanceYear()
							+ ", TRANSACTION_FINANCE_YEAR_ID =" + remitTrnx.getDocumentFinanceYr() + " ,TRANSACTION_DOCUMENT_NO=" + remitTrnx.getDocumentNo()
							+ " ,BLACK_LIST_INDICATOR ='" + remitTrnx.getBlackListIndicator() + "' where REMITTANCE_APPLICATION_ID =" + appl.getRemittanceApplicationId();
					System.out.println("sql :" + sql);
					jdbcTemplate.update(sql);
				}
			}
		}
	}
	


	
	@Transactional
	@SuppressWarnings("unchecked")
	public RemittanceApplicationResponseModel saveAndUpdateAll(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplicationResponseModel responseModel = new RemittanceApplicationResponseModel();
		if (mapAllDetailApplSave != null) {
			PaygDetailsModel pgModel = (PaygDetailsModel) mapAllDetailApplSave.get("PG_DETAILS");
			List<BranchApplicationDto> applList = (List<BranchApplicationDto>) mapAllDetailApplSave.get("APPL");

			if (pgModel != null) {
				PaygDetailsModel pgDetails = pgRepository.save(pgModel);
				responseModel.setDocumentIdForPayment(pgDetails.getPaygTrnxSeqId().toString());
				responseModel.setRemittanceAppId(pgDetails.getPaygTrnxSeqId());
				
			}
			
			if(ConstantDocument.PB_PAYMENT.equalsIgnoreCase(applList.get(0).getPaymentType())) {
				responseModel = remittanceTransactionService.savePayAtBranchAppl(applList,responseModel);
				responseModel.setPgCode(PayGServiceCode.PB);
				responseModel.setApplIds(pgModel.getApplIds());
				logger.info("Response of status api is "+responseModel.toString());
			}else {
				for (BranchApplicationDto applIdDto : applList) {
					RemittanceApplication appl = appRepo.findOne(applIdDto.getApplicationId());
					responseModel.setDocumentFinancialYear(appl.getDocumentFinancialyear());
					if (appl != null && appl.getIsactive().equalsIgnoreCase(ConstantDocument.Yes)) {
						appl.setPaygTrnxDetailId(responseModel.getRemittanceAppId());
						appl.setPaymentId(responseModel.getRemittanceAppId() == null ? appl.getPaymentId(): responseModel.getRemittanceAppId().toString());
						appl.setPaymentType(applIdDto.getPaymentType());
						appRepo.save(appl);
					}
				}
				
				logger.info("Response of KNET status api is "+responseModel.toString());
			}
			
		}

		return responseModel;
	}
	

	public void updateSignatureHash(RemittanceTransactionView trnxDetails,String signature) {
		if(trnxDetails!=null && !StringUtils.isBlank(signature) && JaxUtil.isNullZeroBigDecimalCheck(trnxDetails.getRemittanceTransactionId())) {
			RemittanceTransaction remit = remitTrnxRepository.findOne(trnxDetails.getRemittanceTransactionId());
			if(remit!=null) {
				remit.setCustomerSignature(signature);
				remitTrnxRepository.save(remit);
			}
		}
	
	}

}
