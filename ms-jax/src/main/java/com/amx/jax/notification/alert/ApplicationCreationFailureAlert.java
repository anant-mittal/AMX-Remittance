package com.amx.jax.notification.alert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.model.notification.RemittanceTransactionFailureAlertModel;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.ExEmailNotification;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IExEmailNotificationDao;
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

	@Override
	public List<String> getAlertContacts(CommunicationChannel notificationType) {
		// TODO fetch alert contacts
		return null;
	}

	@Override
	public void sendAlert(AbstractJaxException ex) {

		// TODO fetch bene and customer details
		// TODO fill data in RemittanceTransactionFailureAlertModel
		RemittanceTransactionRequestModel model = (RemittanceTransactionRequestModel) JaxContextUtil.getRequestModel();
		BenificiaryListView benificiaryListView = beneficiaryOnlineDao.findOne(model.getBeneId());

		BigDecimal customerId = benificiaryListView.getCustomerId();
		Customer customer = custDao.getCustById(customerId);

		List<ExEmailNotification> emailid = emailNotificationDao.getEmailNotification();

		RemittanceTransactionFailureAlertModel remittanceTransactionFailure = new RemittanceTransactionFailureAlertModel();

		remittanceTransactionFailure.setBeneName(benificiaryListView.getBenificaryName());
		remittanceTransactionFailure.setBeneAccountNumber(benificiaryListView.getBankAccountNumber());
		remittanceTransactionFailure.setBeneBankName(benificiaryListView.getBankName());
		remittanceTransactionFailure.setBeneBankBranchName(benificiaryListView.getBankBranchName());
		remittanceTransactionFailure.setCustomerReference(customer.getCustomerReference().toString());
		remittanceTransactionFailure.setCurrencyQuoteName(benificiaryListView.getCurrencyQuoteName());
		remittanceTransactionFailure.setSelectedProduct(ex.getErrorMessage());
		remittanceTransactionFailure.setCustomerContact(customer.getMobile());
		remittanceTransactionFailure.setCustomerName(
				customer.getFirstName() + " " + customer.getMiddleName() + " " + customer.getLastName());

		jaxNotificationService.sendErrorEmail(remittanceTransactionFailure, emailid);

	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public List<CommunicationChannel> getCommucationChannels() {
		List<CommunicationChannel> channels = new ArrayList<>();
		channels.add(CommunicationChannel.EMAIL);
		return channels;
	}

}
