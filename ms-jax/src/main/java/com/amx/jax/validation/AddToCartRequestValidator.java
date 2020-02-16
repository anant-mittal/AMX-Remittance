package com.amx.jax.validation;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.dao.BranchRemittancePaymentDao;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.remittance.ShoppingCartDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.partner.manager.PartnerTransactionManager;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.BankMasterRepository;

/**
 * Validator class specific to add to cart functionality
 * 
 * @author prashant
 *
 */
@Component
public class AddToCartRequestValidator {

	@Autowired
	BranchRemittancePaymentDao branchRemittancePaymentDao;
	@Autowired
	BankMasterRepository bankMasterRepo;
	@Autowired
	MetaData metaData;
	@Autowired
	PartnerTransactionManager partnerTransactionManager;

	public static final SERVICE_PROVIDER_BANK_CODE[] SINGLE_APPLICATION_SERVICE_PROVIDERS = { SERVICE_PROVIDER_BANK_CODE.HOME,
			SERVICE_PROVIDER_BANK_CODE.VINTJA };

	public void checkServiceProviderValidation(BranchRemittanceApplRequestModel requestApplModel) {
		if (requestApplModel != null) {
			// fetch any shopping records available
			List<ShoppingCartDetails> lstCustomerShopping = branchRemittancePaymentDao.fetchCustomerShoppingCart(metaData.getCustomerId());
			if (lstCustomerShopping != null && !lstCustomerShopping.isEmpty() && lstCustomerShopping.size() != 0) {
				Map<BigDecimal, Integer> bankIdVsNoOfApplicationMap = new HashMap<>();
				lstCustomerShopping.stream().forEach(i -> {
					Integer count = bankIdVsNoOfApplicationMap.get(i.getRoutingBankId());
					if (count == null) {
						count = 1;
					} else {
						count++;
					}
					bankIdVsNoOfApplicationMap.put(i.getRoutingBankId(), count);

				});
				for (SERVICE_PROVIDER_BANK_CODE bankCode : SINGLE_APPLICATION_SERVICE_PROVIDERS) {
					BankMasterMdlv1 bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(bankCode.name(), PricerServiceConstants.Yes);
					if(bankMaster == null) {
						continue;
					}
					Integer count = bankIdVsNoOfApplicationMap.get(bankMaster.getBankId());
					String errorMessage = null;
					if (count != null) {
						if (count == 1) {
							errorMessage = String.format("You cannot create the next application, as %s application is created.",
									bankMaster.getBankFullName());
						} else if (count > 1) {
							errorMessage = String.format(
									"You cannot create the next application, as %s application is created as the last application.",
									bankMaster.getBankFullName());
						}
						if (errorMessage != null) {
							throw new GlobalException(JaxError.SINGLE_TRANSACTION_SERVICE_PROVIDER, errorMessage);
						}
					}
				}
			}

			if (requestApplModel.getDynamicRroutingPricingBreakup() != null
					&& requestApplModel.getDynamicRroutingPricingBreakup().getServiceProviderDto() != null) {
				BankMasterMdlv1 bankMaster = bankMasterRepo.findByBankCodeAndRecordStatus(SERVICE_PROVIDER_BANK_CODE.HOME.name(),
						PricerServiceConstants.Yes);
				// home send related validation check
				if (bankMaster != null && requestApplModel.getRoutingBankId() != null
						&& requestApplModel.getRoutingBankId().compareTo(bankMaster.getBankId()) == 0) {
					partnerTransactionManager.validateServiceProvider(requestApplModel.getAdditionalFields(), requestApplModel.getBeneId());
				}
			}
		}
	}
}
