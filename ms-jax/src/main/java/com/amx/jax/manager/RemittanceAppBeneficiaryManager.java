package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.ApplicationProcedureDao;
import com.amx.jax.dao.BankDao;
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

	@Autowired
	@Qualifier("remitApplParametersMap")
	private Map<String, Object> parametersMap;

	@Autowired
	private RemittanceApplicationDao remitAppDao;

	@Autowired
	private BankService bankService;

	@Autowired
	private BeneficiaryService beneficiaryService;

	public RemittanceAppBenificiary createRemittanceAppBeneficiary(RemittanceApplication remittanceApplication) {

		logger.info(" Enter into saveRemittanceAppBenificary ");
		RemittanceAppBenificiary remittanceAppBenificary = new RemittanceAppBenificiary();
		BenificiaryListView beneficiaryDT = (BenificiaryListView) parametersMap.get("BENEFICIARY");
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
		userfinancialyear.setFinancialYearID(
				remittanceApplication.getExUserFinancialYearByDocumentFinanceYear().getFinancialYearID());
		remittanceAppBenificary.setExUserFinancialYear(userfinancialyear);

		// RemittanceApplication Id
		remittanceAppBenificary.setExRemittanceAppfromBenfi(remittanceApplication);
		remittanceAppBenificary.setDocumentCode(remittanceApplication.getDocumentCode());
		remittanceAppBenificary.setDocumentNo(remittanceApplication.getDocumentNo());
		remittanceAppBenificary.setBeneficiaryId(beneficiaryDT.getBeneficaryMasterSeqId());

		if (beneficiaryDT.getBankAccountNumber() != null) {
			remittanceAppBenificary.setBeneficiaryAccountNo(beneficiaryDT.getBankAccountNumber());
		}

		setSwiftCodes(remittanceAppBenificary, remittanceApplication);

		remittanceAppBenificary.setCreatedBy(remittanceApplication.getCreatedBy());
		remittanceAppBenificary.setCreatedDate(new Date());
		remittanceAppBenificary.setIsactive(ConstantDocument.Yes);

		setAdditionalBeneficiaryDetails(remittanceAppBenificary);
		remittanceAppBenificary.setBeneficiaryTelephoneNumber(telNumber);

		logger.info(" Exit from saveRemittanceAppBenificary ");

		return remittanceAppBenificary;
	}

	private void setAdditionalBeneficiaryDetails(RemittanceAppBenificiary remittanceAppBenificary) {
		BenificiaryListView beneficiaryDT = (BenificiaryListView) parametersMap.get("BENEFICIARY");
		remittanceAppBenificary.setBeneficiaryName((String) parametersMap.get("P_BENE_NAME"));
		remittanceAppBenificary.setBeneficiaryFirstName((String) parametersMap.get("P_BENEFICIARY_FIRST_NAME"));
		remittanceAppBenificary.setBeneficiarySecondName((String) parametersMap.get("P_BENEFICIARY_SECOND_NAME"));
		remittanceAppBenificary.setBeneficiaryThirdName((String) parametersMap.get("P_BENEFICIARY_THIRD_NAME"));
		remittanceAppBenificary.setBeneficiaryFourthName((String) parametersMap.get("P_BENEFICIARY_FOURTH_NAME"));
		remittanceAppBenificary.setBeneficiaryFifthName((String) parametersMap.get("P_BENEFICIARY_FIFTH_NAME"));
		remittanceAppBenificary.setBeneficiaryBank((String) parametersMap.get("P_BENEFICIARY_BANK_NAME"));
		remittanceAppBenificary.setBeneficiaryBranch((String) parametersMap.get("P_BENEFICIARY_BRANCH_NAME"));
		remittanceAppBenificary.setBeneficiaryBranchStateId((BigDecimal) parametersMap.get("P_BENEFICIARY_STATE_ID"));
		remittanceAppBenificary
				.setBeneficiaryBranchDistrictId((BigDecimal) parametersMap.get("P_BENEFICIARY_DISTRICT_ID"));
		remittanceAppBenificary.setBeneficiaryBranchCityId((BigDecimal) parametersMap.get("P_BENEFICIARY_CITY_ID"));
		remittanceAppBenificary.setBeneficiaryBankCountryId(beneficiaryDT.getBenificaryCountry());
		remittanceAppBenificary.setBeneficiaryBankId(beneficiaryDT.getBankId());
		remittanceAppBenificary.setBeneficiaryBankBranchId(beneficiaryDT.getBranchId());
		remittanceAppBenificary.setBeneficiaryAccountSeqId(beneficiaryDT.getBeneficiaryAccountSeqId());
		remittanceAppBenificary.setBeneficiaryRelationShipSeqId(beneficiaryDT.getBeneficiaryRelationShipSeqId());
	}

	private void setSwiftCodes(RemittanceAppBenificiary remittanceAppBenificary,
			RemittanceApplication remittanceApplication) {
		String beneficiarySwiftBank1 = (String) parametersMap.get("P_BENEFICIARY_SWIFT_BANK1");
		if (beneficiarySwiftBank1 != null) {
			// Beneficiary Bank Swift Code - V_EX_SWIFT_MASTER - SWIFT_BIC
			remittanceAppBenificary.setBeneficiarySwiftBank1(beneficiarySwiftBank1);
			SwiftMasterView lstSwiftRecords = beneficiaryService.getSwiftMasterBySwiftBic(beneficiarySwiftBank1);
			if (lstSwiftRecords != null) {
				remittanceAppBenificary.setBeneficiarySwiftBank1Id(lstSwiftRecords.getSwiftId());
				remittanceAppBenificary.setBeneficiarySwiftAddr1(lstSwiftRecords.getBankName());
			}
		}
		BenificiaryListView beneficiaryDT = (BenificiaryListView) parametersMap.get("BENEFICIARY");
		if (beneficiaryDT.getSwiftBic() != null) {
			remittanceAppBenificary.setBeneficiaryBankSwift(beneficiaryDT.getSwiftBic());
		} else {
			remittanceAppBenificary.setBeneficiaryBankSwift(
					bankService.getBranchSwiftCode(remittanceAppBenificary.getBeneficiaryBankId(),
							remittanceAppBenificary.getBeneficiaryBankBranchId()));
		}
	}
}