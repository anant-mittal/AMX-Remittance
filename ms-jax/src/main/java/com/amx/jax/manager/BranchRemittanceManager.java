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

import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.remittance.BranchDayTransactionView;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.repository.ICollectionDetailRepository;
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
	MetaData metaData;
	
	@Autowired
	BranchDayTransactionRepository branchTrnxRepository;
	
	@Autowired
	ICollectionDetailRepository collecDetailRepository;
	
	
	
	public UserwiseTransactionDto getTotalTrnxUserWise(String transactionDate){
		String accMonthYear =DateUtil.getAccountingMonthYearNew(transactionDate);
		BigDecimal employeeId =metaData.getEmployeeId();
		BigDecimal countryBranchId =new BigDecimal(56); //metaData.getCountryBranchId();
		logger.debug("accMonthYear :"+accMonthYear+"\t employeeId :"+employeeId+"\t countryBranchId :"+countryBranchId);
		List<BranchDayTransactionView> totalTrnx =branchTrnxRepository.getTotalTrnxCount(accMonthYear, countryBranchId, employeeId, transactionDate);
		UserwiseTransactionDto dto = getTotalTrnxCount(totalTrnx);
		return dto;
	}
	
	
	public UserwiseTransactionDto getTotalTrnxCount(List<BranchDayTransactionView> totalTrnxList) {
		

		BigDecimal totalCash =new BigDecimal(0);
		BigDecimal totalKnet =new BigDecimal(0);
		BigDecimal totalBankTransfer =new BigDecimal(0);
		BigDecimal totalOther =new BigDecimal(0);
		BigDecimal totalRefund = new BigDecimal(0); 
		BigDecimal totalCheque =new BigDecimal(0);
		
		
		
		UserwiseTransactionDto dto = new UserwiseTransactionDto();
		
		if (totalTrnxList != null && !totalTrnxList.isEmpty()) {
			dto.setTotaltrnx(new BigDecimal(totalTrnxList.size()));
			for (BranchDayTransactionView branchDayTransactionView : totalTrnxList) {
				List<CollectDetailModel> branchDayCoolectionDetailList = collecDetailRepository.findByDocumentNoAndDocumentCodeAndDocumentFinanceYear(branchDayTransactionView.getCollectionDocNumber(), branchDayTransactionView.getCollectionDocCode(), branchDayTransactionView.getCollectionDocFinanceYear());
				if (branchDayCoolectionDetailList != null && !branchDayCoolectionDetailList.isEmpty()) {
					for (CollectDetailModel collectDetail : branchDayCoolectionDetailList) {
						if (collectDetail.getCollectionMode() != null) {
							if (collectDetail.getCollectionMode().equalsIgnoreCase(ConstantDocument.CASH)) {
								totalCash = totalCash.add(collectDetail.getCollAmt());
							} else if (collectDetail.getCollectionMode().equalsIgnoreCase(ConstantDocument.KNET_CODE)) {
								totalKnet =totalKnet.add(collectDetail.getCollAmt());
							} else if (collectDetail.getCollectionMode().equalsIgnoreCase(ConstantDocument.BANK_TRANSFER)) {
								totalBankTransfer = totalBankTransfer.add(collectDetail.getCollAmt());
							} else if (collectDetail.getCollectionMode().equalsIgnoreCase(ConstantDocument.CHEQUE)) {
								totalCheque = totalCheque.add(collectDetail.getCollAmt());
							} else {
								totalOther = totalOther.add(totalOther);
							}
							
						}

						
					}
				}
				totalRefund = totalRefund.add(totalRefund);
				
			
		}
		
	}	
		
		
		
		dto.setCash(totalCash);
		dto.setOthers(totalOther);
		dto.setkNet(totalKnet);
		dto.setBankTransfer(totalBankTransfer);
		dto.setCheqe(totalCheque);
		dto.setRefundAmount(totalRefund);
		
		
	return dto;	
	}
}	

