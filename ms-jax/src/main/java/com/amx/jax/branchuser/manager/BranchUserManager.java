package com.amx.jax.branchuser.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.BranchDayTransactionView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.FxOrderValidation;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchUserManager  extends AbstractModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	BranchDayTransactionRepository branchTrnxRepository;
	
	@Autowired
	ICollectionDetailRepository collecDetailRepository;
	
	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;
	
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	FxOrderValidation validateHeaderInfo;
	
	@Autowired
	RoutingProcedureDao routingProDao;
	

	@Autowired
	ICustomerRepository customerDao;
	
	public UserwiseTransactionDto getTotalTrnxUserWise(String transactionDate){
		transactionDate = DateUtil.todaysDateWithDDMMYY(transactionDate==null?new Date():DateUtil.convertStringToDate(transactionDate),"0");
		String accMonthYear =DateUtil.getAccountingMonthYearNew(transactionDate);
		BigDecimal employeeId =metaData.getEmployeeId();
		BigDecimal countryBranchId =metaData.getCountryBranchId();
		logger.debug("accMonthYear :"+accMonthYear+"\t employeeId :"+employeeId+"\t countryBranchId :"+countryBranchId);
		
		EmployeeDetailsView empDetails = employeeDetailsRepository.findByEmployeeId(metaData.getEmployeeId());
		if(empDetails==null) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee detais not found "+metaData.getEmployeeId());
		}else if(empDetails!=null && !empDetails.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)) {
			throw new GlobalException(JaxError.INACTIVE_EMPLOYEE,"Employee is not active "+metaData.getEmployeeId());
		}else if(empDetails!=null && StringUtils.isBlank(empDetails.getUserName())) {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"User name should not be  blank "+metaData.getEmployeeId());
		}
		
		
		List<BranchDayTransactionView> totalTrnx =branchTrnxRepository.getTotalTrnxCount(accMonthYear, empDetails.getCountryBranchId(), employeeId, transactionDate);
		UserwiseTransactionDto dto = getTotalTrnxCount(totalTrnx,accMonthYear);
		return dto;
	}
	
	
	public UserwiseTransactionDto getTotalTrnxCount(List<BranchDayTransactionView> totalTrnxList,String accMonthYear) {
		

		BigDecimal totalCash =new BigDecimal(0);
		BigDecimal totalKnet =new BigDecimal(0);
		BigDecimal totalBankTransfer =new BigDecimal(0);
		BigDecimal totalOther =new BigDecimal(0);
		BigDecimal totalRefund = new BigDecimal(0); 
		BigDecimal totalCheque =new BigDecimal(0);
		BigDecimal localCurrencyId = metaData.getDefaultCurrencyId();
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal lastTrnx = new BigDecimal(0);
		String currencyQuoteName ="";
		EmployeeDetailsView empDetails = null;
		List<CustomerRemittanceTransactionView> lastTrnxDetailsList  = null;
		UserwiseTransactionDto dto = new UserwiseTransactionDto();
		
		if(JaxUtil.isNullZeroBigDecimalCheck(employeeId)) {
			empDetails = employeeDetailsRepository.findByEmployeeId(employeeId);
		}else {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee Id should not be blank");
		}
		
		if (totalTrnxList != null && !totalTrnxList.isEmpty()) {
			dto.setTotaltrnx(new BigDecimal(totalTrnxList.size()));
			for (BranchDayTransactionView branchDayTransactionView : totalTrnxList) {
				totalRefund = totalRefund.add(branchDayTransactionView.getRefundAmount());
			}
				List<CollectDetailModel> branchDayCoolectionDetailList = collecDetailRepository.getCollectionDetailsForUser(empDetails.getUserName(), accMonthYear,empDetails.getCountryBranchId());
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
	}	
		if(JaxUtil.isNullZeroBigDecimalCheck(localCurrencyId)){
		 currencyQuoteName = currencyDao.getCurrencyList(localCurrencyId).get(0).getQuoteName();
		}
		if(empDetails!=null) {
			lastTrnx = transactionHistroyDao.getLastTrnxAmountFortheUser(empDetails.getUserName(), accMonthYear, empDetails.getCountryBranchId());
		}
		
		if(lastTrnx==null) {
			lastTrnx =BigDecimal.ZERO;
		}
		
		
		dto.setCash(currencyQuoteName+" "+totalCash);
		dto.setOthers(currencyQuoteName+" "+totalOther);
		dto.setkNet(currencyQuoteName+" "+totalKnet);
		dto.setBankTransfer(currencyQuoteName+" "+totalBankTransfer);
		dto.setCheque(currencyQuoteName+" "+totalCheque);
		dto.setRefundAmount(currencyQuoteName+" "+totalRefund);
		dto.setLastTrnx(currencyQuoteName+" "+lastTrnx);
		dto.setEmployeeId(metaData.getEmployeeId());
	return dto;	
	}
	
	

}
