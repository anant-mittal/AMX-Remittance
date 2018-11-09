/**
 * 
 */
package com.amx.jax.device;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.IDeviceStateData;
import com.amx.jax.services.ReportManagerService;
import com.amx.jax.services.TransactionHistroyService;

/**
 * @author Prashant
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SignaturePadRemittanceManager extends ASignaturePadManager {

	@Autowired
	RemittanceApplicationDao remitAppDao;
	@Autowired
	TransactionHistroyService transactionHistroyService;
	@Autowired
	ReportManagerService reportManagerService;
	@Autowired
	MetaData metaData;

	Logger logger = LoggerFactory.getLogger(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.device.ASignaturePadManager#updateStatus(com.amx.jax.device.
	 * IDeviceStateInfo)
	 */
	@Override
	public void updateStatus(IDeviceStateData info) {

	}

	@Deprecated
	public IDeviceStateData getRemittanceReceiptData(BigDecimal remittanceTransactionId) {
		RemittanceTransaction remittanceTransaction = remitAppDao.getRemittanceTransactionById(remittanceTransactionId);
		logger.debug("inside getRemittanceReceiptData with remittanceTransactionId: {} ", remittanceTransactionId);
		TransactionHistroyDTO trxnDto = transactionHistroyService.getTransactionHistoryDto(
				remittanceTransaction.getCustomerId(), remittanceTransaction.getDocumentFinancialyear(),
				remittanceTransaction.getDocumentNo());
		trxnDto.setApplicationCountryId(metaData.getCountryId());
		reportManagerService.generatePersonalRemittanceReceiptReportDetails(trxnDto, Boolean.FALSE);
		List<RemittanceReceiptSubreport> rrsrl = reportManagerService.getRemittanceReceiptSubreportList();
		//SignaturePadRemittanceInfo info = new SignaturePadRemittanceInfo(rrsrl.get(0));
		return null;
	}

}
