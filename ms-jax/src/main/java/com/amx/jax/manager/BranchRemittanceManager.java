package com.amx.jax.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CollectDetailModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.BeneficiaryAccountException;
import com.amx.jax.dbmodel.remittance.BranchDayTransactionView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.remittance.UserwiseTransactionDto;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.BeneficiaryAccountExceptRepository;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.StringUtil;
import com.amx.jax.validation.FxOrderValidation;


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
	
	@Autowired
	ICurrencyDao currencyDao;
	
	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;
	
	
	@Autowired
	EmployeeDetailsRepository employeeDetailsRepository;
	
	@Autowired
	FxOrderValidation validateHeaderInfo;
	

	@Autowired
	ICustomerRepository customerDao;
	
	@Autowired
	BeneficiaryAccountExceptRepository beneAccountExceptionRepo;
	
	@Autowired
	IBeneficiaryOnlineDao beneficiaryRepository;
	
	@Autowired
	IAccountTypeFromViewDao accountTypeRepository;
	
	
	@Autowired
	BeneficiaryCheckService beneCheckService;
	
	@Autowired
	BeneficiaryService beneService;
	
	
	
	
	public boolean checkingStaffIdNumberWithCustomer() {
		boolean checkStatus = Boolean.FALSE;
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryId = metaData.getCountryId();
		String customerIdentityId =null;
		String employeeIdentityId =null;
		
		EmployeeDetailsView empDetails = null;
		List<Customer> customerList = null;
		
		if(JaxUtil.isNullZeroBigDecimalCheck(employeeId)) {
			empDetails = employeeDetailsRepository.findByEmployeeId(employeeId);
			employeeIdentityId = empDetails.getCivilId();
		}else {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID,"Employee Id should not be blank");
		}
		 customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		 
		 if(customerList!=null && !customerList.isEmpty()) {
			 customerIdentityId = customerList.get(0).getIdentityInt();
		 }
		 if(employeeIdentityId!= null && customerIdentityId!=null && employeeIdentityId.equalsIgnoreCase(customerIdentityId)) {
			 checkStatus = Boolean.TRUE;
		 }else {
			 throw new GlobalException(JaxError.TRNX_NOT_ALLOWED_ON_YOUR_OWN_LOGIN,"Transaction cannot be done on your own login account");
		 }
		
		return checkStatus;
	}
	
	public boolean beneAccountException(BigDecimal beneRelId) {
		boolean checkStatus = Boolean.TRUE;
		BigDecimal customerId = metaData.getCustomerId();
		List<BeneficiaryAccountException> accountExcepList = null;  
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);
		
		if(beneficaryDetails !=null && beneficaryDetails.getBankAccountNumber()!=null) {
			accountExcepList = beneAccountExceptionRepo.getBeneAccountExceptionList(beneficaryDetails.getBenificaryCountry(), beneficaryDetails.getBankId(), beneficaryDetails.getBankAccountNumber(), beneficaryDetails.getIsActive());
		}
		
		if(accountExcepList != null && !accountExcepList.isEmpty()) {
			checkStatus = Boolean.FALSE;
			 throw new GlobalException(JaxError.BENE_ACCOUNT_EXCEPTION,"We can't proceed for transactions by using this account details!");
		}
		
		return checkStatus;
	}
	
	
	public boolean checkBeneAccountType(BigDecimal beneRelId) {
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);
		boolean chkAccType = Boolean.FALSE;
		List<AccountTypeFromViewModel> lstAccType = accountTypeRepository.getAccountTypeByCountryId(beneficaryDetails.getBenificaryCountry());
		if (lstAccType != null && lstAccType.size() != 0) {
			for (AccountTypeFromViewModel accountTypeFromView : lstAccType) {
				if (accountTypeFromView.getAdditionalAmiecId() != null && accountTypeFromView.getAdditionalAmiecId().compareTo(beneficaryDetails.getBankAccountTypeId()) == 0) {
					chkAccType = Boolean.TRUE;
					break;
				}
			}
		}
		if(!chkAccType) {
			 throw new GlobalException(JaxError.BENE_ACCOUNT_TYPE_MISMATCH,"Account Type Mismatch, Please edit and save the beneficairy ");
		}
		
	return chkAccType;
	}
	
	
	public void beneAddCheck(BigDecimal beneRelId) {
		BenificiaryListView beneficaryDetails =beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);
		BeneficiaryListDTO checkdto = beneCheckService.beneCheck(convertBeneModelToDto(beneficaryDetails));
		if(checkdto!=null) {
			if(checkdto.getBeneficiaryErrorStatus()!=null && checkdto.getBeneficiaryErrorStatus().size()>0) {
				throw new GlobalException(checkdto.getBeneficiaryErrorStatus().get(0).getErrorCode(),checkdto.getBeneficiaryErrorStatus().get(0).getErrorDesc());
			}
		}
		
		
	}
	
	
	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}
	
}	

