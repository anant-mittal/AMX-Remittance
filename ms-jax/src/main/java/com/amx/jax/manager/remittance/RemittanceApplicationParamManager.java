package com.amx.jax.manager.remittance;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.model.request.remittance.IRemittanceApplicationParams;
import com.amx.jax.services.BankService;
import static com.amx.amxlib.constant.ApplicationProcedureParam.*;

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
			BenificiaryListView beneficiaryView) {
		 BankMasterModel routintBankMaster = bankService.getBankById(new BigDecimal( remittanceApplicationParams.getCorrespondanceBankId()));
		P_ROUTING_COUNTRY_ID.putValue(remitApplParametersMap, routintBankMaster.getBankCountryId());
		P_FOREIGN_CURRENCY_ID.putValue(remitApplParametersMap, beneficiaryView.getCurrencyId());
		P_ROUTING_BANK_ID.putValue(remitApplParametersMap,	remittanceApplicationParams.getCorrespondanceBankId());
		//P_REMITTANCE_MODE_ID.putValue(remitApplParametersMap, remittanceApplicationParams.get);
	}
}
