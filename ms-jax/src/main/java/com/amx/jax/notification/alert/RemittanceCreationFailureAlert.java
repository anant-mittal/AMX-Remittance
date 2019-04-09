package com.amx.jax.notification.alert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.model.notification.RemittanceTransactionFailureAlertModel;
import com.amx.jax.constants.CommunicationChannel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.error.JaxError;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IExEmailNotificationDao;
import com.amx.jax.repository.RemittanceApplicationRepository;
import com.amx.jax.service.TenantService;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.util.JaxContextUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class RemittanceCreationFailureAlert implements IAlert {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	IExEmailNotificationDao emailNotificationDao;
	
	@Autowired
	RemittanceApplicationRepository applicationDao;
	
	@Autowired
	TenantService tenantService;
	
	@Override
	public List<String> getAlertContacts(CommunicationChannel notificationType) {
		// TODO fetch alert contacts
		return null;
	}

	@Override
	public void sendAlert(AbstractJaxException ex) {
		if(!isApplicable(ex)) {
			return;
		}
		PaymentResponseDto model = (PaymentResponseDto) JaxContextUtil.getRequestModel();
		List<RemittanceApplication> lstPayIdDetails = null;
		List<ExEmailNotification> emailid = null;
		BenificiaryListView benificiaryListView = null;
		String product = "Product could not be derived";
		StringBuilder cusName = new StringBuilder();
		
		try {
			lstPayIdDetails = applicationDao.fetchRemitApplTrnxRecordsByCustomerPayId(model.getUdf3(),new Customer(model.getCustomerId()));
		    benificiaryListView = beneficiaryOnlineDao.findOne(lstPayIdDetails.get(0).getExRemittanceAppBenificiary().get(0).getBeneficiaryRelationShipSeqId());
			Customer customer = custDao.getCustById(model.getCustomerId());
	        emailid = emailNotificationDao.getEmailNotification();

			RemittanceTransactionFailureAlertModel remittanceTransactionFailure = new RemittanceTransactionFailureAlertModel();
			remittanceTransactionFailure.setBeneficiaryName(benificiaryListView.getBenificaryName());
			remittanceTransactionFailure.setBeneficiaryAccountNo(benificiaryListView.getBankAccountNumber());
			remittanceTransactionFailure.setBeneficiaryBank(benificiaryListView.getBankName());
			remittanceTransactionFailure.setBeneficiaryBranch(benificiaryListView.getBankBranchName());
			remittanceTransactionFailure.setCustomerReference(customer.getCustomerReference().toString());
			remittanceTransactionFailure.setService(product);
			remittanceTransactionFailure.setCustomerContact(customer.getMobile());
			BigDecimal localNetTranxAmount = lstPayIdDetails.get(0).getLocalNetTranxAmount();
			remittanceTransactionFailure.setTransactionAmount(localNetTranxAmount);
			String currencyQuoteName = tenantService.getDefaultCurrencyMaster().getQuoteName();
			remittanceTransactionFailure.setCurrencyQuoteName(currencyQuoteName);
			remittanceTransactionFailure.setExceptionMessage(ex.getErrorMessage());
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

	@Override
	public List<CommunicationChannel> getCommucationChannels() {
		List<CommunicationChannel> channels = new ArrayList<>();
		channels.add(CommunicationChannel.EMAIL);
		return channels;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	private boolean isApplicable(AbstractJaxException ex) {
		if (ex.getErrorKey().equals(JaxError.ADDTIONAL_FLEX_FIELD_REQUIRED.toString())) {
			return false;
		}
		return true;
	}
}
