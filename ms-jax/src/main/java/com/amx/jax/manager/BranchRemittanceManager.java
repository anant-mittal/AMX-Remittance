package com.amx.jax.manager;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.BranchDayTransactionView;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.util.DateUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceManager  extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4602595256039337910L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	BranchDayTransactionRepository branchTrnxRepository;
	
	
	
	public List<UserwiseTransactionDto> getTotalTrnxUserWise(BigDecimal countryBranchId,BigDecimal employeeId,String transactionDate){
		String accMonthYear =DateUtil.getAccountingMonthYear(transactionDate); 
		List<BranchDayTransactionView> totalTrnx =branchTrnxRepository.getTotalTrnxCount(accMonthYear, countryBranchId, employeeId, transactionDate);
		return null;
	}
	
	
	/*public UserwiseTransactionDto getTotalTrnxCount(List<BranchDayTransactionView> totalTrnx) {
		
		UserwiseTransactionDto dto = new UserwiseTransactionDto();
		if (totalTrnx != null && !totalTrnx.isEmpty()) {
			for (BranchDayTransactionView branchDayTransactionView : totalTrnx) {

			
				UserwiseTransactionDto userwiseTransactionDataTable = new UserwiseTransactionDto();
				userwiseTransactionDataTable.setCustomerRef(branchDayTransactionView.getCustomerRef());
				userwiseTransactionDataTable.setTransactionType(branchDayTransactionView.getTransactionType());
				userwiseTransactionDataTable.setLocalTranxAmount(branchDayTransactionView.getLocalTranxAmount());
				userwiseTransactionDataTable.setOldEmos(branchDayTransactionView.getOldEmos());
				userwiseTransactionDataTable.setMtcNo(branchDayTransactionView.getMtcNo());
				userwiseTransactionDataTable.setDocumentFinanceYear(branchDayTransactionView.getDocumentFinanceYear());
				userwiseTransactionDataTable.setDocumentNo(branchDayTransactionView.getDocumentNo());
				userwiseTransactionDataTable.setRefundAmount(branchDayTransactionView.getRefundAmount());

				branchDayCoolectionDetailList = currencyEnquiryService.getBranchDayCollectionDetail(branchDayTransactionView.getCollectionDocNumber(), branchDayTransactionView.getCollectionDocCode(), branchDayTransactionView.getCollectionDocFinanceYear());

				if (branchDayCoolectionDetailList != null) {
					for (CollectDetail collectDetail : branchDayCoolectionDetailList) {
						if (collectDetail.getCollectionMode() != null) {
							if (collectDetail.getCollectionMode().equalsIgnoreCase("C")) {
								userwiseTransactionDataTable.setCash(branchDayTransactionView.getLocalTranxAmount());
								tempTotalCash = +branchDayTransactionView.getLocalTranxAmount().doubleValue();
								tempTotalCash1 = tempTotalCash1 + tempTotalCash;
							} else if (collectDetail.getCollectionMode().equalsIgnoreCase("K")) {
								userwiseTransactionDataTable.setkNet(branchDayTransactionView.getLocalTranxAmount());
								tempTotalKnet = +branchDayTransactionView.getLocalTranxAmount().doubleValue();
								tempTotalKnet1 = tempTotalKnet1 + tempTotalKnet;
							} else if (collectDetail.getCollectionMode().equalsIgnoreCase("B")) {
								userwiseTransactionDataTable.setCheque(branchDayTransactionView.getLocalTranxAmount());
								tempTotalCheque = +branchDayTransactionView.getLocalTranxAmount().doubleValue();
								tempTotalCheque1 = tempTotalCheque1 + tempTotalCheque;
							} else {
								userwiseTransactionDataTable.setOthers(branchDayTransactionView.getLocalTranxAmount());
								tempTotalOthers = +branchDayTransactionView.getLocalTranxAmount().doubleValue();
								tempTotalOthers1 = tempTotalOthers1 + tempTotalOthers;
							}
							
						}

						
					}
				}
				userwiseTransactionDataTableList.add(userwiseTransactionDataTable);
				setTotalCash(GetRound.roundBigDecimal(new BigDecimal(tempTotalCash1), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
				setTotalCheque(GetRound.roundBigDecimal(new BigDecimal(tempTotalCheque1), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
				setTotalKnet(GetRound.roundBigDecimal(new BigDecimal(tempTotalKnet1), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
				setTotalOthers(GetRound.roundBigDecimal(new BigDecimal(tempTotalOthers1), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
				tempTotalRefund = +branchDayTransactionView.getRefundAmount().doubleValue();
				tempTotalRefund1 = tempTotalRefund1+tempTotalRefund;
				setTotalRefund(GetRound.roundBigDecimal(new BigDecimal(tempTotalRefund1), foreignLocalCurrencyDenominationService.getDecimalPerCurrency(new BigDecimal(sessionStateManage.getCurrencyId()))));
			
		}
		return dto; 
	}
	}*/
}	

