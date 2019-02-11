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
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.ForeignCurrencyAdjustRepository;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICollectionRepository;
import com.amx.jax.repository.ILoyaltyClaimRequestRepository;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.IRemittanceAdditionalInstructionRepository;
import com.amx.jax.repository.IRemittanceAmlRepository;
import com.amx.jax.repository.IRemittanceBenificiaryRepository;
import com.amx.jax.repository.IRemittanceTransactionRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.repository.remittance.ILoyaltyPointRepository;
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

	@Autowired
	ILoyaltyClaimRequestRepository loyaltyClaimRepo;
	
	@Autowired
	ILoyaltyPointRepository loyalPointsRepository;

	@Transactional
	@SuppressWarnings("unchecked")
	public void saveAllApplications(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx = (RemittanceApplication) mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary) mapAllDetailApplSave.get("EX_APPL_BENE");
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>) mapAllDetailApplSave.get("EX_APPL_ADDL");
		List<RemitApplAmlModel> saveApplAmlList = (List<RemitApplAmlModel>) mapAllDetailApplSave.get("EX_APPL_AML");

		if (saveApplTrnx != null) {
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

	}

	@Transactional
	@SuppressWarnings("unchecked")
	public RemittanceResponseDto saveRemittanceTransaction(HashMap<String, Object> mapAllDetailRemitSave) {

		RemittanceResponseDto responseDto = new RemittanceResponseDto();

		CollectionModel collectModel = (CollectionModel) mapAllDetailRemitSave.get("EX_COLLECT");
		List<CollectDetailModel> collectDetailsModel = (List<CollectDetailModel>) mapAllDetailRemitSave.get("EX_COLLECT_DET");
		List<ForeignCurrencyAdjust> foreignCurrencyAdjust = (List<ForeignCurrencyAdjust>) mapAllDetailRemitSave.get("EX_CURR_ADJUST");
		List<RemittanceTransaction> remitTrnxList = (List<RemittanceTransaction>) mapAllDetailRemitSave.get("EX_REMIT_TRNX");
		List<RemittanceBenificiary> remitBeneList = (List<RemittanceBenificiary>) mapAllDetailRemitSave.get("EX_REMIT_BENE");
		List<RemittanceAdditionalInstructionData> addlTrnxList = (List<RemittanceAdditionalInstructionData>) mapAllDetailRemitSave.get("EX_REMIT_ADDL");
		List<RemittanceAml> amlTrnxList = (List<RemittanceAml>) mapAllDetailRemitSave.get("EX_REMIT_AML");
		LoyaltyClaimRequest lylClaim = (LoyaltyClaimRequest) mapAllDetailRemitSave.get("LYL_CLAIM");
		List<LoyaltyPointsModel> loyaltyPoitns = (List<LoyaltyPointsModel> )mapAllDetailRemitSave.get("LOYALTY_POINTS");
		

		if (collectModel != null) {
			collectRepository.save(collectModel);
			responseDto.setCollectionDocumentNo(collectModel.getDocumentNo());
			responseDto.setCollectionDocumentFYear(collectModel.getDocumentFinanceYear());
			responseDto.setCollectionDocumentCode(collectModel.getDocumentCode());

		}

		if (collectDetailsModel != null && !collectDetailsModel.isEmpty() && JaxUtil.isNullZeroBigDecimalCheck(collectModel.getDocumentNo())) {
			collectDetailRepository.save(collectDetailsModel);
		}

		if (lylClaim != null) {
			loyaltyClaimRepo.save(lylClaim);
		}

		if (foreignCurrencyAdjust != null && !foreignCurrencyAdjust.isEmpty()) {
			foreignCurrAdjustRepository.save(foreignCurrencyAdjust);
		}

		if (remitTrnxList != null && !remitTrnxList.isEmpty()) {
			remitTrnxRepository.save(remitTrnxList);

			if (remitBeneList != null && !remitBeneList.isEmpty()) {
				remitBeneRepository.save(remitBeneList);
			}

			if (addlTrnxList != null && !addlTrnxList.isEmpty()) {
				remitAddRepository.save(addlTrnxList);
			}

			if (amlTrnxList != null && !amlTrnxList.isEmpty()) {
				remitAmlRepository.save(amlTrnxList);
			}
			
			if(loyaltyPoitns!=null && !loyaltyPoitns.isEmpty()) {
				loyalPointsRepository.save(loyaltyPoitns);
			}
			
		}
		updateApplication(remitTrnxList);

		return responseDto;
	}

	public BigDecimal generateDocumentNumber(BigDecimal appCountryId, BigDecimal companyId, BigDecimal documentId, BigDecimal finYear, BigDecimal branchId) {
		Map<String, Object> output = applicationProcedureDao.getDocumentSeriality(appCountryId, companyId, documentId, finYear, ConstantDocument.Update, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	public void updateApplication(List<RemittanceTransaction> remitTrnxList) {
		for (RemittanceTransaction remitTrnx : remitTrnxList) {
			RemittanceApplication appl = appRepo.fetchRemitApplTrnx(remitTrnx.getApplicationDocumentNo(), remitTrnx.getApplicationFinanceYear());
			if (appl != null) {
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
}
