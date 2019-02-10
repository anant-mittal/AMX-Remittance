package com.amx.jax.branchremittance.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CollectionModel;
import com.amx.jax.dbmodel.ForeignCurrencyAdjust;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAml;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.error.JaxError;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.IRemittanceAdditionalInstructionRepository;
import com.amx.jax.repository.IRemittanceAmlRepository;
import com.amx.jax.repository.IRemittanceBenificiaryRepository;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.util.JaxUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchRemittanceDao {
	
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
	
	

	
	@Transactional
	@SuppressWarnings("unchecked")
	public void saveAllApplications(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx= (RemittanceApplication)  mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary)mapAllDetailApplSave.get("EX_APPL_BENE") ;
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>) mapAllDetailApplSave.get("EX_APPL_ADDL");
		List<RemitApplAmlModel> saveApplAmlList = (List<RemitApplAmlModel>) mapAllDetailApplSave.get("EX_APPL_AML");
		
		if(saveApplTrnx!=null) {
			appRepo.save(saveApplTrnx);
		}
		if(saveApplBene!=null) {
			appBeneRepo.save(saveApplBene);
		}
		
		if(saveApplAddlData!=null) {
			addlInstDataRepo.save(saveApplAddlData);
		}
		if(saveApplAmlList!=null) {
			applAmlRepository.save(saveApplAmlList);
		}
		
	}
	
	@Transactional
	@SuppressWarnings("unchecked")
	public void saveRemittanceTransaction(HashMap<String, Object> mapAllDetailRemitSave) {
		
		CollectionModel collectModel = (CollectionModel)mapAllDetailRemitSave.get("EX_COLLECT");
		List<CollectDetailModel> collectDetailsModel = (List<CollectDetailModel>)mapAllDetailRemitSave.get("EX_COLLECT_DET");
		List<ForeignCurrencyAdjust>	foreignCurrencyAdjust = (List<ForeignCurrencyAdjust>)mapAllDetailRemitSave.get("EX_CURR_ADJUST");
		List<RemittanceTransaction> remitTrnxList = (List<RemittanceTransaction>)mapAllDetailRemitSave.get("EX_REMIT_TRNX");
		Map<String,RemittanceBenificiary>  remitBeneList	=  (Map<String,RemittanceBenificiary>)mapAllDetailRemitSave.get("EX_REMIT_BENE");
		Map<String,RemittanceAdditionalInstructionData> addlTrnxList  =  (Map<String,RemittanceAdditionalInstructionData>)mapAllDetailRemitSave.get("EX_REMIT_ADDL");
		Map<String,RemittanceAml> amlTrnxList  =  (Map<String,RemittanceAml>)mapAllDetailRemitSave.get("EX_REMIT_AML");
		
		
		if(collectModel!=null) {
			BigDecimal documentNo = generateDocumentNumber(collectModel.getApplicationCountryId(),collectModel.getFsCompanyMaster().getCompanyId(),collectModel.getDocumentCode(),collectModel.getDocumentFinanceYear(),collectModel.getExBankBranch().getBranchId());
			if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
				collectModel.setDocumentNo(documentNo);
			    }else{
			    	throw new GlobalException(JaxError.INVALID_COLLECTION_DOCUMENT_NO, "Collection document should not be blank.");
			    }
			collectRepository.save(collectModel);
		}
		
		if(collectDetailsModel!=null && !collectDetailsModel.isEmpty() && JaxUtil.isNullZeroBigDecimalCheck(collectModel.getDocumentNo())){
			collectDetailRepository.save(collectDetailsModel);
		}
		if(foreignCurrencyAdjust!=null && !foreignCurrencyAdjust.isEmpty()) {
			foreignCurrAdjustRepository.save(foreignCurrencyAdjust);
		}
		
		for(RemittanceTransaction remitTrnx :remitTrnxList) {
			String key = remitTrnx.getApplicationFinanceYear()+"/"+remitTrnx.getApplicationDocumentNo();
			RemittanceBenificiary remitBene = remitBeneList.get(key);
			RemittanceAdditionalInstructionData addInsData = addlTrnxList.get(key);
			RemittanceAml aml = amlTrnxList.get(key);
			
			
			BigDecimal documentNo = generateDocumentNumber(remitTrnx.getApplicationCountryId().getCountryId(),remitTrnx.getCompanyId().getCompanyId(),remitTrnx.getDocumentCode(),remitTrnx.getDocumentFinanceYear(),remitTrnx.getBranchId().getBranchId());
			if(documentNo!=null && documentNo.compareTo(BigDecimal.ZERO)!=0){
				collectModel.setDocumentNo(documentNo);
			    }else{
			    	throw new GlobalException(JaxError.INVALID_REMITTANCE_DOCUMENT_NO, "Remittance document should not be blank.");
			    }
			remitTrnx.setDocumentNo(documentNo);
			remitTrnxRepository.save(remitTrnx);
			
			if(remitBene!=null) {
				remitBene.setDocumentNo(documentNo);
				remitBeneRepository.save(remitBene);
			}
			
			if(addInsData!=null) {
				addInsData.setDocumentNo(documentNo);
				remitAddRepository.save(addInsData);
			}
			if(aml!=null) {
				aml.setExRemittancefromAml(remitTrnx);
				remitAmlRepository.save(aml);
			}
			
			
			RemittanceApplication appl = appRepo.fetchRemitApplTrnx(remitTrnx.getApplicationDocumentNo(), remitTrnx.getApplicationFinanceYear());
			if(appl!=null) {
				appl.setRemittanceApplicationId(appl.getRemittanceApplicationId());
				appl.setTransactionFinancialyear(remitTrnx.getDocumentFinanceYear());
				appl.setExUserFinancialYearByTransactionFinanceYearID(new UserFinancialYear(remitTrnx.getDocumentFinanceYr()));
				appl.setTransactionDocumentNo(remitTrnx.getDocumentNo());
				appl.setApplicaitonStatus(ConstantDocument.T);
				appl.setBlackListIndicator(remitTrnx.getBlackListIndicator());
				appRepo.save(appl);
				
			}
			
			
		}
		
		
	}
	
	
	public BigDecimal generateDocumentNumber(BigDecimal appCountryId,BigDecimal companyId,BigDecimal documentId,BigDecimal finYear,BigDecimal branchId) {
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId,finYear, ConstantDocument.Update, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
		}
	
	

	
}



