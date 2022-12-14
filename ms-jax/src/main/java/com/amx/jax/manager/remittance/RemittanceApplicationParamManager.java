package com.amx.jax.manager.remittance;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_APPLICATION_COUNTRY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_BENEFICIARY_ACCOUNT_NO;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_BENEFICIARY_BANK_COUNTRY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_BENEFICIARY_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_BENEFICIARY_BRANCH_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_BENEFICIARY_COUNTRY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_CALCULATED_FC_AMOUNT;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_DELIVERY_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_FC_AMOUNT;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_FOREIGN_CURRENCY_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_LC_AMOUNT;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_REMITTANCE_MODE_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_BANK_ID;
import static com.amx.amxlib.constant.ApplicationProcedureParam.P_ROUTING_COUNTRY_ID;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.services.BankService;

/**
 * @author Prashant
 *
 */
@Component
public class RemittanceApplicationParamManager {

	@Resource
	Map<String, Object> remitApplParametersMap;
	@Autowired
	BankService bankService;

	public void populateRemittanceApplicationParamMap(IRemittanceApplicationParams remittanceApplicationParams,
			BenificiaryListView beneficiaryView, ExchangeRateBreakup exchangeRateBreakup) {
		BankMasterMdlv1 routintBankMaster = null;
		if(remittanceApplicationParams!=null) {
		 routintBankMaster = bankService.getBankById(remittanceApplicationParams.getCorrespondanceBankIdBD());
		 P_ROUTING_BANK_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getCorrespondanceBankIdBD());
		 P_REMITTANCE_MODE_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getRemitModeIdBD());
		 P_DELIVERY_MODE_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getDeliveryModeIdBD());
		 
		}
		P_APPLICATION_COUNTRY_ID.putValue(remitApplParametersMap, beneficiaryView.getApplicationCountryId());
		if(routintBankMaster!=null) {
		P_ROUTING_COUNTRY_ID.putValue(remitApplParametersMap, routintBankMaster.getBankCountryId());
		}
		P_FOREIGN_CURRENCY_ID.putValue(remitApplParametersMap, beneficiaryView.getCurrencyId());
		
		
		if (exchangeRateBreakup != null) {
			P_CALCULATED_FC_AMOUNT.putValue(remitApplParametersMap, exchangeRateBreakup.getConvertedFCAmount());
		}
		if (remittanceApplicationParams != null && remittanceApplicationParams.getLocalAmountBD() != null) {
			P_LC_AMOUNT.putValue(remitApplParametersMap, remittanceApplicationParams.getLocalAmountBD());
		}
		if (remittanceApplicationParams != null &&  remittanceApplicationParams.getForeignAmountBD() != null) {
			P_FC_AMOUNT.putValue(remitApplParametersMap, remittanceApplicationParams.getForeignAmountBD());
		}
		P_BENEFICIARY_BANK_ID.putValue(remitApplParametersMap, beneficiaryView.getBankId());
		P_BENEFICIARY_COUNTRY_ID.putValue(remitApplParametersMap, beneficiaryView.getCountryId());
		P_BENEFICIARY_ACCOUNT_NO.putValue(remitApplParametersMap, beneficiaryView.getBankAccountNumber());
		P_BENEFICIARY_BRANCH_ID.putValue(remitApplParametersMap, beneficiaryView.getBranchId());
		P_BENEFICIARY_BANK_COUNTRY_ID.putValue(remitApplParametersMap, beneficiaryView.getBenificaryCountry());
	}
}


