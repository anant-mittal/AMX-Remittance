package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.amxlib.model.response.RemittanceTransactionResponsetModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.IBeneficiaryOnlineDao;


@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceApplicationManager {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	/**
	 * @param validatedObjects:
	 *            - contains objects obtained after being passed through beneficiary
	 *            validation process, validationResults- validation result like exchange
	 *            rate, net amount etc
	 **/
	public void createRemittanceApplication(RemittanceTransactionRequestModel model,
			Map<String, Object> validatedObjects, RemittanceTransactionResponsetModel validationResults) {

		RemittanceApplication remittanceApplication = new RemittanceApplication();
		BenificiaryListView beneficiary = beneficiaryOnlineDao.findOne(model.getBeneId());
		BigDecimal currencyId = beneficiary.getCurrencyId();
		//TODO set document code and number

		CurrencyMasterModel forcurrencymaster = new CurrencyMasterModel();
		forcurrencymaster.setCurrencyId(currencyId);
		remittanceApplication.setExCurrencyMasterByForeignCurrencyId(forcurrencymaster);
		
		CurrencyMasterModel commisioncurrencymaster = new CurrencyMasterModel();
		commisioncurrencymaster.setCurrencyId(currencyId);
		remittanceApplication.setExCurrencyMasterByLocalCommisionCurrencyId(commisioncurrencymaster);

	}
}
