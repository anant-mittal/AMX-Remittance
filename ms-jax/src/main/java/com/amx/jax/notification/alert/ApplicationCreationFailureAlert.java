package com.amx.jax.notification.alert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.model.notification.RemittanceTransactionFailureAlertModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.dict.ContactType;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IExEmailNotificationDao;
import com.amx.jax.service.TenantService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ApplicationCreationFailureAlert implements IAlert {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	IExEmailNotificationDao emailNotificationDao;
	
	@Autowired
	TenantService tenantService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public List<String> getAlertContacts(ContactType notificationType) {
		// TODO fetch alert contacts
		return null;
	}

	@Override
	public void sendAlert(AbstractJaxException ex) {
		if(!isApplicable(ex)) {
			return;
		}
		
		RemittanceTransactionRequestModel model = null;
		RemittanceTransactionDrRequestModel modelDr = null;
		
		
		//RemittanceTransactionRequestModel model = (RemittanceTransactionRequestModel) JaxContextUtil.getRequestModel();
		
		Object Object = (Object)JaxContextUtil.getRequestModel();
		
		if(Object instanceof RemittanceTransactionRequestModel) {
			model = (RemittanceTransactionRequestModel) JaxContextUtil.getRequestModel();
		}else if(Object instanceof RemittanceTransactionDrRequestModel) {
			modelDr = (RemittanceTransactionDrRequestModel) JaxContextUtil.getRequestModel();
		}
		
		
		BenificiaryListView benificiaryListView = null;
		List<ExEmailNotification> emailid =null;
		String product = "Product could not be derived";
		StringBuilder cusName = new StringBuilder();
		
		try {
			if(model!=null) {
			benificiaryListView = beneficiaryOnlineDao.findOne(model.getBeneId());
			}else {
				benificiaryListView = beneficiaryOnlineDao.findOne(modelDr.getBeneId());
			}
			BigDecimal customerId = benificiaryListView.getCustomerId();
			Customer customer = custDao.getCustById(customerId);
		    emailid = emailNotificationDao.getEmailNotification();

			RemittanceTransactionFailureAlertModel remittanceTransactionFailure = new RemittanceTransactionFailureAlertModel();
			remittanceTransactionFailure.setBeneficiaryName(benificiaryListView.getBenificaryName());
			remittanceTransactionFailure.setBeneficiaryAccountNo(benificiaryListView.getBankAccountNumber());
			remittanceTransactionFailure.setBeneficiaryBank(benificiaryListView.getBankName());
			remittanceTransactionFailure.setBeneficiaryBranch(benificiaryListView.getBankBranchName());
			remittanceTransactionFailure.setCustomerReference(customer.getCustomerReference().toString());
			remittanceTransactionFailure.setCurrencyQuoteName(benificiaryListView.getCurrencyQuoteName());
			remittanceTransactionFailure.setService(product);
			remittanceTransactionFailure.setCustomerContact(customer.getMobile());
			if(model!=null) {
			remittanceTransactionFailure.setTransactionAmount(model.getLocalAmount());
			}else if(modelDr!=null) {
				remittanceTransactionFailure.setTransactionAmount(modelDr.getLocalAmount());
			}
			remittanceTransactionFailure.setExceptionMessage(ex.getErrorMessage());
			String currencyQuoteName = tenantService.getDefaultCurrencyMaster().getQuoteName();
			remittanceTransactionFailure.setCurrencyQuoteName(currencyQuoteName);
			if(customer.getFirstName() !=null){
	        	cusName.append(customer.getFirstName());
	        	cusName.append(" ");
	        }
	        if(customer.getMiddleName() !=null){
	        	cusName.append(customer.getMiddleName());
	        	cusName.append(" ");
	        }
	        if(customer.getLastName() !=null){
	        	cusName.append(customer.getLastName());
	        }
			remittanceTransactionFailure.setCustomerName(cusName.toString());

			jaxNotificationService.sendErrorEmail(remittanceTransactionFailure, emailid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean isApplicable(AbstractJaxException ex) {
		if (ex.getErrorKey().equals(JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED.toString())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public List<ContactType> getCommucationChannels() {
		List<ContactType> channels = new ArrayList<>();
		channels.add(ContactType.EMAIL);
		return channels;
	}

}
