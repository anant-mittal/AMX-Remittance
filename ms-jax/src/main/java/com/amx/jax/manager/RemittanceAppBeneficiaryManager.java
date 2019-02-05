package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CompanyMaster;
import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.dbmodel.remittance.Document;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceAppBeneficiaryManager {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	MetaData metaData;

	@Autowired
	private ApplicationProcedureDao applicationProcedureDao;

	@Resource
	//@Qualifier("remitApplParametersMap")
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	private RemittanceApplicationDao remitAppDao;

	@Autowired
	private BankService bankService;

	@Autowired
	private BeneficiaryService beneficiaryService;

	public RemittanceAppBenificiary createRemittanceAppBeneficiary(RemittanceApplication remittanceApplication) {

		logger.info(" Enter into saveRemittanceAppBenificary ");
		RemittanceAppBenificiary remittanceAppBenificary = new RemittanceAppBenificiary();
		BenificiaryListView beneficiaryDT = (BenificiaryListView) remitApplParametersMap.get("BENEFICIARY");
		String telNumber = beneficiaryService.getBeneficiaryContactNumber(beneficiaryDT.getBeneficaryMasterSeqId());
		remittanceAppBenificary = new RemittanceAppBenificiary();

		// Document Id
		Document documentid = new Document();
		documentid.setDocumentID(remittanceApplication.getExDocument().getDocumentID());
		remittanceAppBenificary.setExDocument(documentid);

		// company Id
		CompanyMaster companymaster = new CompanyMaster();
		companymaster.setCompanyId(remittanceApplication.getFsCompanyMaster().getCompanyId());
		remittanceAppBenificary.setFsCompanyMaster(companymaster);

		// company code
		remittanceAppBenificary.setCompanyCode(remittanceApplication.getCompanyCode());

		// User Financial Year for Transaction
		UserFinancialYear userfinancialyear = new UserFinancialYear();
		userfinancialyear.setFinancialYearID(remittanceApplication.getExUserFinancialYearByDocumentFinanceYear().getFinancialYearID());
		remittanceAppBenificary.setExUserFinancialYear(userfinancialyear);

		// RemittanceApplication Id
		remittanceAppBenificary.setExRemittanceAppfromBenfi(remittanceApplication);
		remittanceAppBenificary.setDocumentCode(remittanceApplication.getDocumentCode());
		remittanceAppBenificary.setDocumentNo(remittanceApplication.getDocumentNo());
		remittanceAppBenificary.setBeneficiaryId(beneficiaryDT.getBeneficaryMasterSeqId());

		setBeneDetails(remittanceAppBenificary, beneficiaryDT);

		setSwiftCodes(remittanceAppBenificary, remittanceApplication);

		remittanceAppBenificary.setCreatedBy(remittanceApplication.getCreatedBy());
		remittanceAppBenificary.setCreatedDate(new Date());
		remittanceAppBenificary.setIsactive(ConstantDocument.Yes);

		setAdditionalBeneficiaryDetails(remittanceAppBenificary);
		remittanceAppBenificary.setBeneficiaryTelephoneNumber(telNumber);

		logger.info(" Exit from saveRemittanceAppBenificary ");

		return remittanceAppBenificary;
	}

	private void setBeneDetails(RemittanceAppBenificiary remittanceAppBenificary, BenificiaryListView beneficiaryDT) {
		// Bene details
		remittanceAppBenificary.setBeneficiaryAccountNo(getAccountNumber(beneficiaryDT));
		remittanceAppBenificary.setBeneficiaryName(beneficiaryDT.getBenificaryName());
		remittanceAppBenificary.setBeneficiaryBank(beneficiaryDT.getBankName());
		remittanceAppBenificary.setBeneficiaryBranch(beneficiaryDT.getBankBranchName());
	}
	
	private String getAccountNumber(BenificiaryListView beneficiaryDT) {
		String iBanFlag = bankService.getBankById(beneficiaryDT.getBankId()).getIbanFlag();
		String accountNumber = beneficiaryDT.getBankAccountNumber();
		logger.info("iBanFlag: {} , iBANNum: {}", iBanFlag, beneficiaryDT.getIbanNumber());
		if (ConstantDocument.Yes.equalsIgnoreCase(iBanFlag) && StringUtils.isNotBlank(beneficiaryDT.getIbanNumber())) {
			accountNumber = beneficiaryDT.getIbanNumber();
		}
		return accountNumber;
	}

	private void setAdditionalBeneficiaryDetails(RemittanceAppBenificiary remittanceAppBenificary) {
		BenificiaryListView beneficiaryDT = (BenificiaryListView) remitApplParametersMap.get("BENEFICIARY");
		remittanceAppBenificary.setBeneficiaryName((String) remitApplParametersMap.get("P_BENEFICIARY_NAME"));
		remittanceAppBenificary.setBeneficiaryFirstName((String) remitApplParametersMap.get("P_BENEFICIARY_FIRST_NAME"));
		remittanceAppBenificary.setBeneficiarySecondName((String) remitApplParametersMap.get("P_BENEFICIARY_SECOND_NAME"));
		remittanceAppBenificary.setBeneficiaryThirdName((String) remitApplParametersMap.get("P_BENEFICIARY_THIRD_NAME"));
		remittanceAppBenificary.setBeneficiaryFourthName((String) remitApplParametersMap.get("P_BENEFICIARY_FOURTH_NAME"));
		remittanceAppBenificary.setBeneficiaryFifthName((String) remitApplParametersMap.get("P_BENEFICIARY_FIFTH_NAME"));
		//remittanceAppBenificary.setBeneficiaryBank((String) remitApplParametersMap.get("P_BENEFICIARY_BANK_NAME"));
		//remittanceAppBenificary.setBeneficiaryBranch((String) remitApplParametersMap.get("P_BENEFICIARY_BRANCH_NAME"));
		remittanceAppBenificary.setBeneficiaryBranchStateId((BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_STATE_ID"));
		remittanceAppBenificary.setBeneficiaryBranchDistrictId((BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_DISTRICT_ID"));
		remittanceAppBenificary.setBeneficiaryBranchCityId((BigDecimal) remitApplParametersMap.get("P_BENEFICIARY_CITY_ID"));
		remittanceAppBenificary.setBeneficiaryBankCountryId(beneficiaryDT.getBenificaryCountry());
		remittanceAppBenificary.setBeneficiaryBankId(beneficiaryDT.getBankId());
		remittanceAppBenificary.setBeneficiaryBankBranchId(beneficiaryDT.getBranchId());
		remittanceAppBenificary.setBeneficiaryAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId());
		remittanceAppBenificary.setBeneficiaryRelationShipSeqId(beneficiaryDT.getBeneficiaryRelationShipSeqId());
		//remittanceAppBenificary.setBeneficiaryBankSwift(beneficiaryDT.getSwiftBic());
	}

	private void setSwiftCodes(RemittanceAppBenificiary remittanceAppBenificary,RemittanceApplication remittanceApplication) {
		String beneficiarySwiftBank1 = (String) remitApplParametersMap.get("P_BENEFICIARY_SWIFT_BANK1");
		if (beneficiarySwiftBank1 != null) {
			// Beneficiary Bank Swift Code - V_EX_SWIFT_MASTER - SWIFT_BIC
			remittanceAppBenificary.setBeneficiarySwiftBank1(beneficiarySwiftBank1);
			SwiftMasterView lstSwiftRecords = beneficiaryService.getSwiftMasterBySwiftBic(beneficiarySwiftBank1);
			if (lstSwiftRecords != null) {
				remittanceAppBenificary.setBeneficiarySwiftBank1Id(lstSwiftRecords.getSwiftId());
				remittanceAppBenificary.setBeneficiarySwiftAddr1(lstSwiftRecords.getBankName());
			}
		}
		BenificiaryListView beneficiaryDT = (BenificiaryListView) remitApplParametersMap.get("BENEFICIARY");
		//BigDecimal bankId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_ID");
		//BigDecimal bankBranchId = (BigDecimal) remitApplParametersMap.get("P_ROUTING_BANK_BRANCH_ID");
		if (beneficiaryDT.getSwiftBic() != null) {
			remittanceAppBenificary.setBeneficiaryBankSwift(beneficiaryDT.getSwiftBic());
		} else {
			//remittanceAppBenificary.setBeneficiaryBankSwift(bankService.getBranchSwiftCode(bankId,bankBranchId));
			remittanceAppBenificary.setBeneficiaryBankSwift(bankService.getBranchSwiftCode(beneficiaryDT.getBankId(),beneficiaryDT.getBranchId()));
			
		}
	}
}
