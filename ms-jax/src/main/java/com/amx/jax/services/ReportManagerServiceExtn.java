package com.amx.jax.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.RemittanceReportBean;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.constant.BankConstants;
import com.amx.jax.dbmodel.BankMasterMdlv1;
import com.amx.jax.dbmodel.RemittanceTransactionView;

/**
 * Class is extention of reportmanager service. Any new business logic need to
 * amend in this class
 *
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ReportManagerServiceExtn {

	@Autowired
	BankService bankService;

	@SuppressWarnings("rawtypes")
	public ApiResponse generatePersonalRemittanceReceiptReportDetails(TransactionHistroyDTO transactionHistroyDTO, Boolean promotion,
			ApiResponse response, List<RemittanceTransactionView> remittanceApplicationList) {
		List<RemittanceReceiptSubreport> remittanceReceiptSubreportList = response.getResults();
		RemittanceReceiptSubreport remittanceReceiptSubreport = remittanceReceiptSubreportList.get(0);
		List<RemittanceReportBean> remittanceReportBeanList = remittanceReceiptSubreport.getRemittanceApplList();
		// size of remittanceApplList is same as size of remittanceApplicationList and
		// all elements are in sequnce
		for (int i = 0; i < remittanceReportBeanList.size(); i++) {
			RemittanceReportBean remittanceReportBean = remittanceReportBeanList.get(i);
			RemittanceTransactionView remittanceApplication = remittanceApplicationList.get(i);
			BankMasterMdlv1 bankMaster = bankService.getBankById(remittanceApplication.getBankId());
			if (BankConstants.VINTJA_BANK_CODE.equals(bankMaster.getBankCode())) {
				boolean isPinAvailable = !StringUtils.isEmpty(remittanceReportBean.getPinNo());
				remittanceReceiptSubreport.setShowPinNotAvailMessage(!isPinAvailable);
			} else {
				remittanceReceiptSubreport.setShowPinNotAvailMessage(false);
			}
		}

		return response;
	}
}
