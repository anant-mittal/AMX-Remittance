package com.amx.jax.manager.remittance;

import static com.amx.amxlib.constant.ApplicationProcedureParam.P_APPLICATION_COUNTRY_ID;
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

import com.amx.jax.dbmodel.BankMasterModel;
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
		BankMasterModel routintBankMaster = bankService.getBankById(remittanceApplicationParams.getCorrespondanceBankIdBD());
		P_APPLICATION_COUNTRY_ID.putValue(remitApplParametersMap, beneficiaryView.getApplicationCountryId());
		P_ROUTING_COUNTRY_ID.putValue(remitApplParametersMap, routintBankMaster.getBankCountryId());
		P_FOREIGN_CURRENCY_ID.putValue(remitApplParametersMap, beneficiaryView.getCurrencyId());
		P_ROUTING_BANK_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getCorrespondanceBankIdBD());
		P_REMITTANCE_MODE_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getRemitModeIdBD());
		P_DELIVERY_MODE_ID.putValue(remitApplParametersMap, remittanceApplicationParams.getDeliveryModeIdBD());
		if (exchangeRateBreakup != null) {
			P_CALCULATED_FC_AMOUNT.putValue(remitApplParametersMap, exchangeRateBreakup.getConvertedFCAmount());
		}
		if (remittanceApplicationParams.getLocalAmountBD() != null) {
			P_LC_AMOUNT.putValue(remitApplParametersMap, remittanceApplicationParams.getLocalAmountBD());
		}
		if (remittanceApplicationParams.getForeignAmountBD() != null) {
			P_FC_AMOUNT.putValue(remitApplParametersMap, remittanceApplicationParams.getForeignAmountBD());
		}
	}
}
