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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
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
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.payg.PaymentResponseDto;
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
import com.amx.jax.repository.IRemittanceBenificiaryRepository;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.ILoyaltyPointRepository;
import com.amx.jax.util.JaxUtil;

@Component
// @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode =
// ScopedProxyMode.TARGET_CLASS)
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


	@Transactional
	@SuppressWarnings("unchecked")
	public void saveAllApplications(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx = (RemittanceApplication) mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary) mapAllDetailApplSave.get("EX_APPL_BENE");
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>) mapAllDetailApplSave.get("EX_APPL_ADDL");
		List<RemitApplAmlModel> saveApplAmlList = (List<RemitApplAmlModel>) mapAllDetailApplSave.get("EX_APPL_AML");
		RemitApplSrvProv saveApplSrvProv = (RemitApplSrvProv) mapAllDetailApplSave.get("EX_APPL_SRV_PROV");

		if (saveApplTrnx != null) {
			BigDecimal documentNo = generateDocumentNumber(saveApplTrnx.getFsCountryMasterByApplicationCountryId().getCountryId(), saveApplTrnx.getFsCompanyMaster().getCompanyId(),
					saveApplTrnx.getDocumentCode(), saveApplTrnx.getDocumentFinancialyear(), saveApplTrnx.getLoccod());
			if (!JaxUtil.isNullZeroBigDecimalCheck(documentNo)) {
				throw new GlobalException(JaxError.INVALID_APPLICATION_DOCUMENT_NO, "Application document number shouldnot be null or blank");
			}
			saveApplTrnx.setDocumentNo(documentNo);
			appRepo.save(saveApplTrnx);
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

			CollectionModel collectModel = (CollectionModel) mapAllDetailRemitSave.get("EX_COLLECT");
			List<CollectDetailModel> collectDetailsModel = (List<CollectDetailModel>) mapAllDetailRemitSave.get("EX_COLLECT_DET");
			List<ForeignCurrencyAdjust> foreignCurrencyAdjust = (List<ForeignCurrencyAdjust>) mapAllDetailRemitSave.get("EX_CURR_ADJUST");
			Map<BigDecimal, RemittanceTransaction> remitTrnxList = (Map<BigDecimal, RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
			Map<BigDecimal, RemittanceBenificiary> remitBeneList = (Map<BigDecimal, RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
			Map<BigDecimal, List<RemittanceAdditionalInstructionData>> addlTrnxList = (Map<BigDecimal, List<RemittanceAdditionalInstructionData>>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
			Map<BigDecimal, List<RemittanceAml>> amlTrnxList = (Map<BigDecimal, List<RemittanceAml>>) mapAllDetailRemitSave.get("EX_REMIT_AML");
			Map<BigDecimal, RemitTrnxSrvProv> remitSprProvList = (Map<BigDecimal, RemitTrnxSrvProv>) mapAllDetailRemitSave.get("EX_REMIT_SRV_PROV");
			
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
				for(CollectDetailModel collectDetModel : collectDetailsModel) {
					collectDetModel.setDocumentNo(collectModel.getDocumentNo());
					collectDetailRepository.save(collectDetModel);
				}
			}

			if (lylClaim != null && JaxUtil.isNullZeroBigDecimalCheck(lylClaim.getVoucherNo())) {
				loyaltyClaimRepo.save(lylClaim);
			}

			if (foreignCurrencyAdjust != null && !foreignCurrencyAdjust.isEmpty()) {
				for(ForeignCurrencyAdjust foreignCurrAdju:foreignCurrencyAdjust) {
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
					
					RemittanceTransaction remitTrnx1 = remitTrnxRepository.save(remitTrnx);

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
	
	public void updateSignatureHash(RemittanceTransactionView trnxDetails,String Signature) {
	if(trnxDetails!=null && !StringUtils.isBlank(Signature) && JaxUtil.isNullZeroBigDecimalCheck(trnxDetails.getRemittanceTransactionId())) {
		RemittanceTransaction remit = remitTrnxRepository.findOne(trnxDetails.getRemittanceTransactionId());
		if(remit!=null) {
			remit.setCustomerSignature(Signature);
			remitTrnxRepository.save(remit);
		}
	}
	
	}
}
