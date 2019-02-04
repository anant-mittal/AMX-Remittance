package com.amx.jax.branchremittance.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dal.RoutingProcedureDao;
import com.amx.jax.dao.ApplicationProcedureDao;
import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.fx.EmployeeDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleAmiec;
import com.amx.jax.dbmodel.remittance.BeneficiaryAccountException;
import com.amx.jax.dbmodel.remittance.ViewRoutingDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.RemittanceApplicationManager;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;
import com.amx.jax.model.response.remittance.AdditionalExchAmiecDto;
import com.amx.jax.model.response.remittance.AmlCheckResponseDto;
import com.amx.jax.model.response.remittance.BranchExchangeRateBreakup;
import com.amx.jax.model.response.remittance.RoutingBankDto;
import com.amx.jax.model.response.remittance.RoutingResponseDto;
import com.amx.jax.model.response.remittance.RoutingServiceDto;
import com.amx.jax.repository.IAccountTypeFromViewDao;
import com.amx.jax.repository.IAdditionalBankRuleAmiecRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.ICollectionDetailRepository;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.fx.EmployeeDetailsRepository;
import com.amx.jax.repository.remittance.BeneficiaryAccountExceptRepository;
import com.amx.jax.repository.remittance.BranchDayTransactionRepository;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryCheckService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.services.RoutingService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.validation.FxOrderValidation;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceManager extends AbstractModel {

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

	@Autowired
	ApplicationProcedureDao applProcedureDao;

	/*
	 * @Autowired RoutingProcedureDao routing
	 */

	@Autowired
	RoutingService routingService;

	@Autowired
	RemittanceTransactionManager remitTrnxManager;

	@Autowired
	BankService bankService;

	@Autowired
	RoutingProcedureDao routingPro;

	@Autowired
	CustomerIdProofRepository customerIdProof;

	@Autowired
	BizcomponentDao bizCompDao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	CustomerIdProofRepository idProofDao;

	@Resource
	private Map<String, Object> remitApplParametersMap;

	@Autowired
	RemittanceApplicationManager remitApplTrnxManager;

	@Autowired
	IAdditionalBankRuleAmiecRepository amiecBankRuleRepo;

	public void checkingStaffIdNumberWithCustomer() {
		boolean checkStatus = Boolean.FALSE;
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal companyId = metaData.getCompanyId();
		BigDecimal countryId = metaData.getCountryId();
		String customerIdentityId = null;
		String employeeIdentityId = null;

		EmployeeDetailsView empDetails = null;
		List<Customer> customerList = null;

		if (JaxUtil.isNullZeroBigDecimalCheck(employeeId)) {
			empDetails = employeeDetailsRepository.findByEmployeeId(employeeId);
			if (empDetails != null) {
				employeeIdentityId = empDetails.getCivilId();
			} else {
				throw new GlobalException(JaxError.NULL_EMPLOYEE_ID, "Employee deails not found ");
			}
		} else {
			throw new GlobalException(JaxError.NULL_EMPLOYEE_ID, "Employee Id should not be blank");
		}
		customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);

		if (customerList != null && !customerList.isEmpty()) {
			customerIdentityId = customerList.get(0).getIdentityInt();
		}
		if (employeeIdentityId != null && customerIdentityId != null
				&& employeeIdentityId.equalsIgnoreCase(customerIdentityId)) {
			throw new GlobalException(JaxError.TRNX_NOT_ALLOWED_ON_YOUR_OWN_LOGIN,
					"Transaction cannot be done on your own login account");
		}

	}

	public boolean beneAccountException(BenificiaryListView beneficaryDetails) {
		boolean checkStatus = Boolean.TRUE;
		BigDecimal customerId = metaData.getCustomerId();
		List<BeneficiaryAccountException> accountExcepList = null;
		if (beneficaryDetails != null && beneficaryDetails.getBankAccountNumber() != null) {
			accountExcepList = beneAccountExceptionRepo.getBeneAccountExceptionList(
					beneficaryDetails.getBenificaryCountry(), beneficaryDetails.getBankId(),
					beneficaryDetails.getBankAccountNumber(), beneficaryDetails.getIsActive());
		}

		if (accountExcepList != null && !accountExcepList.isEmpty()) {
			checkStatus = Boolean.FALSE;
			throw new GlobalException(JaxError.BENE_ACCOUNT_EXCEPTION,
					"We can't proceed for transactions by using this account details!");
		}

		return checkStatus;
	}

	public void checkBeneAccountType(BenificiaryListView beneficaryDetails) {
		boolean chkAccType = Boolean.FALSE;
		List<AccountTypeFromViewModel> lstAccType = accountTypeRepository
				.getAccountTypeByCountryId(beneficaryDetails.getBenificaryCountry());
		if (lstAccType != null && lstAccType.size() != 0) {
			for (AccountTypeFromViewModel accountTypeFromView : lstAccType) {
				if (accountTypeFromView.getAdditionalAmiecId() != null && accountTypeFromView.getAdditionalAmiecId()
						.compareTo(beneficaryDetails.getBankAccountTypeId()) == 0) {
					chkAccType = Boolean.TRUE;
					break;
				}
			}
		}
		if (!chkAccType) {
			throw new GlobalException(JaxError.BENE_ACCOUNT_TYPE_MISMATCH,
					"Account Type Mismatch, Please edit and save the beneficairy ");
		}

	}

	public void beneAddCheck(BenificiaryListView beneficaryDetails) {
		BeneficiaryListDTO checkdto = beneCheckService.beneCheck(convertBeneModelToDto(beneficaryDetails));
		if (checkdto != null) {
			if (checkdto.getBeneficiaryErrorStatus() != null && checkdto.getBeneficiaryErrorStatus().size() > 0) {
				throw new GlobalException(checkdto.getBeneficiaryErrorStatus().get(0).getErrorCode(),
						checkdto.getBeneficiaryErrorStatus().get(0).getErrorDesc());
			}
		}

	}

	public void bannedBankCheck(BenificiaryListView beneficaryDetails) {
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
		inputValues.put("P_BENEFICIARY_BANK_ID", beneficaryDetails.getBankId());
		inputValues.put("P_BENEFICIARY_MASTER_ID", beneficaryDetails.getBeneficaryMasterSeqId());
		Map<String, Object> output = applProcedureDao.getBannedBankCheckProcedure(inputValues);
		if (output != null) {
			String errorMessage = (String) output.get("P_ERROR_MESSAGE");
			String alertMessage = (String) output.get("P_ALERT_MESSAGE");

			if (errorMessage != null) {
				throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, errorMessage);
			}
			if (alertMessage != null) {
				throw new GlobalException(JaxError.REMITTANCE_TRANSACTION_DATA_VALIDATION_FAIL, alertMessage);
			}
		}

	}

	// public Map<String ,Object>
	// getExchangeRateForBranch(BranchRemittanceApplRequestModel
	// requestModel,Map<String ,Object> map){
	public Map<String, Object> getExchangeRateForBranch(BranchRemittanceApplRequestModel requestModel,
			RoutingResponseDto branchRoutingDto) {

		Map<String, Object> outPut = new HashMap<>();
		try {

			BenificiaryListView beneficaryDetails = beneficiaryRepository
					.findBybeneficiaryRelationShipSeqId(requestModel.getBeneId());
			BranchExchangeRateBreakup brExchRateBreakup = requestModel.getBranchExRateBreakup();
			Customer customer = custDao.getCustById(metaData.getCustomerId());
			String customerType = getCustomerType(customer.getCustomerTypeId());
			BigDecimal routingBankId = BigDecimal.ZERO;
			BigDecimal serviceMasterId = BigDecimal.ZERO;
			BigDecimal remittancModeId = BigDecimal.ZERO;

			if (requestModel.getRoutingBankId().compareTo(BigDecimal.ZERO) > 0) {
				routingBankId = requestModel.getRoutingBankId();
			} else {
				routingBankId = branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId();
			}

			if (requestModel.getServiceMasterId().compareTo(BigDecimal.ZERO) > 0) {
				serviceMasterId = requestModel.getServiceMasterId();
			} else {
				serviceMasterId = branchRoutingDto.getServiceList().get(0).getServiceMasterId();
			}

			if (requestModel.getRemittancModeId().compareTo(BigDecimal.ZERO) > 0) {
				remittancModeId = requestModel.getRemittancModeId();
			} else {
				remittancModeId = branchRoutingDto.getRemittanceModeList().get(0).getRemittanceModeId();
			}

			Map<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_USER_TYPE", ConstantDocument.BRANCH);
			inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
			inputValues.put("P_ROUTING_COUNTRY_ID",
					branchRoutingDto.getRoutingCountrydto().get(0).getRoutingCountryId()); // map.get("P_ROUTING_COUNTRY_ID"));
			inputValues.put("P_BRANCH_ID", metaData.getCountryBranchId());
			inputValues.put("P_COMPANY_ID", metaData.getCompanyId());
			inputValues.put("P_ROUTING_BANK_ID", routingBankId);// map.get("P_ROUTING_BANK_ID"));
			inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);// map.get("P_SERVICE_MASTER_ID"));
			inputValues.put("P_DELIVERY_MODE_ID", branchRoutingDto.getDeliveryModeList().get(0).getDeliveryModeId());// map.get("P_DELIVERY_MODE_ID"));
			inputValues.put("P_REMITTANCE_MODE_ID", requestModel.getRemittancModeId());// map.get("P_REMITTANCE_MODE_ID"));
			inputValues.put("P_FOREIGN_CURRENCY_ID", beneficaryDetails.getCurrencyId()); // NC
			inputValues.put("P_SELECTED_CURRENCY_ID",
					getSelectedCurrency(beneficaryDetails.getCurrencyId(), requestModel)); // NC
			inputValues.put("P_CUSTOMER_ID", metaData.getCustomerId());
			inputValues.put("P_CUSTOMER_TYPE", customerType);
			inputValues.put("P_LOYALTY_POINTS_IND",
					requestModel.isAvailLoyalityPoints() == true ? ConstantDocument.Yes : ConstantDocument.No);
			inputValues.put("P_SPECIAL_DEAL_RATE", null);
			inputValues.put("P_OVERSEAS_CHRG_IND", null);
			inputValues.put("P_SELECTED_CURRENCY_AMOUNT",
					getSelectedCurrencyAmount(beneficaryDetails.getCurrencyId(), requestModel));// requestModel.getBranchExRateBreakup().getConvertedFCAmount());
			inputValues.put("P_SPOT_RATE", null);
			inputValues.put("P_CASH_ROUND_IND", null);
			inputValues.put("P_ROUTING_BANK_BRANCH_ID",
					branchRoutingDto.getRoutingBankBranchDto().get(0).getBankBranchId());// map.get("P_ROUTING_BANK_BRANCH_ID"));
			inputValues.put("P_BENE_ID", beneficaryDetails.getBeneficaryMasterSeqId());
			inputValues.put("P_BENE_COUNTRY_ID", beneficaryDetails.getBenificaryCountry());
			inputValues.put("P_BENE_BANK_ID", beneficaryDetails.getBenificaryCountry());
			inputValues.put("P_BENE_BANK_BRANCH_ID", beneficaryDetails.getBranchId());
			inputValues.put("P_BENE_ACCOUNT_NO", beneficaryDetails.getBankAccountNumber());
			inputValues.put("P_APPROVAL_YEAR", BigDecimal.ZERO);
			inputValues.put("P_APPROVAL_NO", BigDecimal.ZERO);
			outPut = applProcedureDao.getExchangeRateForBranch(inputValues);
			if (outPut != null && outPut.get("P_ERROR_MESSAGE") != null) {
				throw new GlobalException(JaxError.EXCHANGE_RATE_ERROR, outPut.get("P_ERROR_MESSAGE").toString());
			}

		} catch (GlobalException e) {
			logger.error("exchange rate procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}
		return outPut;
	}

	public List<AmlCheckResponseDto> amlTranxAmountCheckForRemittance(BranchRemittanceApplRequestModel requestModel,
			Map<String, Object> map) {
		Map<String, Object> outPut = new HashMap<>();
		List<AmlCheckResponseDto> listAmlMessage = new ArrayList<>();
		try {
			BenificiaryListView beneficaryDetails = beneficiaryRepository
					.findBybeneficiaryRelationShipSeqId(requestModel.getBeneId());
			Map<String, Object> inputValues = new HashMap<>();
			inputValues.put("P_APPLICATION_COUNTRY_ID", beneficaryDetails.getApplicationCountryId());
			inputValues.put("P_BENE_COUNTRY_ID", beneficaryDetails.getBenificaryCountry());
			inputValues.put("P_CUSTOMER_ID", metaData.getCustomerId());
			inputValues.put("P_BENE_ID", beneficaryDetails.getBeneficaryMasterSeqId());
			inputValues.put("P_FC_AMOUNT", map.get("P_LOCAL_NET_SENT")); // need to check
			outPut = applProcedureDao.amlTranxAmountCheckForRemittance(inputValues);

			if (outPut != null) {
				if (outPut.get("MESSAGE1") != null) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE1");
					dto.setMessageDescription(outPut.get("MESSAGE1").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					// listAmlMessage.add(dto);
				}
				if (outPut.get("MESSAGE2") != null) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE2");
					dto.setMessageDescription(outPut.get("MESSAGE2").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					// listAmlMessage.add(dto);
				}

				if (outPut.get("MESSAGE3") != null) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE3");
					dto.setMessageDescription(outPut.get("MESSAGE3").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					// listAmlMessage.add(dto);
				}
				if (outPut.get("MESSAGE4") != null) {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("MESSAGE4");
					dto.setMessageDescription(outPut.get("MESSAGE4").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}

				if (outPut.get("MESSAGE3") != null) {

					if (outPut.get("RANGE1FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE1FROM - RANGE1TO");
						dto.setMessageDescription(
								outPut.get("RANGE1FROM").toString() + " - " + outPut.get("RANGE1TO") == null ? "0"
										: outPut.get("RANGE1TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE1COUNT") == null ? "0" : outPut.get("RANGE1COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						listAmlMessage.add(dto);
					}

					if (outPut.get("RANGE2FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE2FROM - RANGE2TO");
						dto.setMessageDescription(
								outPut.get("RANGE2FROM").toString() + " - " + outPut.get("RANGE2TO") == null ? "0"
										: outPut.get("RANGE2TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE2COUNT") == null ? "0" : outPut.get("RANGE2COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						listAmlMessage.add(dto);
					}

					if (outPut.get("RANGE3FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE3FROM - RANGE3TO");
						dto.setMessageDescription(
								outPut.get("RANGE3FROM").toString() + " - " + outPut.get("RANGE3TO") == null ? "0"
										: outPut.get("RANGE3TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE3COUNT") == null ? "0" : outPut.get("RANGE3COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						// listAmlMessage.add(dto);
					}

					if (outPut.get("RANGE4FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE4FROM - RANGE4TO");
						dto.setMessageDescription(
								outPut.get("RANGE4FROM").toString() + " - " + outPut.get("RANGE4TO") == null ? "0"
										: outPut.get("RANGE4TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE4COUNT") == null ? "0" : outPut.get("RANGE4COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						// listAmlMessage.add(dto);
					}
					if (outPut.get("RANGE5FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE5FROM - RANGE5TO");
						dto.setMessageDescription(
								outPut.get("RANGE5FROM").toString() + " - " + outPut.get("RANGE5TO") == null ? "0"
										: outPut.get("RANGE5TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE5COUNT") == null ? "0" : outPut.get("RANGE5COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						// listAmlMessage.add(dto);
					}
					if (outPut.get("RANGE6FROM") != null) {
						AmlCheckResponseDto dto = new AmlCheckResponseDto();
						dto.setMessageCode("RANGE6FROM - RANGE6TO");
						dto.setMessageDescription(
								outPut.get("RANGE6FROM").toString() + " - " + outPut.get("RANGE6TO") == null ? "0"
										: outPut.get("RANGE6TO").toString());
						dto.setRangeSlab(
								outPut.get("RANGE6COUNT") == null ? "0" : outPut.get("RANGE6COUNT").toString());
						dto.setAmlFlag(ConstantDocument.Yes);
						// listAmlMessage.add(dto);
					}

				}

				if (outPut.get("AUTHTYPE1") != null && outPut.get("AUTHTYPE1") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE1");
					dto.setMessageDescription(outPut.get("MESSAGE1").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}
				if (outPut.get("AUTHTYPE2") != null && outPut.get("AUTHTYPE2") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE2");
					dto.setMessageDescription(outPut.get("MESSAGE2").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}

				if (outPut.get("AUTHTYPE3") != null && outPut.get("AUTHTYPE3") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE3");
					dto.setMessageDescription(outPut.get("MESSAGE3").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}

				if (outPut.get("AUTHTYPE4") != null && outPut.get("AUTHTYPE4") != "") {
					AmlCheckResponseDto dto = new AmlCheckResponseDto();
					dto.setMessageCode("AUTHTYPE4");
					dto.setMessageDescription(outPut.get("MESSAGE4").toString());
					dto.setAmlFlag(ConstantDocument.Yes);
					listAmlMessage.add(dto);
				}

			}

		} catch (GlobalException e) {
			logger.error("aml procedure", e.getErrorMessage() + "" + e.getErrorKey());
			throw new GlobalException(e.getErrorKey(), e.getErrorMessage());
		}
		return listAmlMessage;
	}

	public void validateBlackListedBene(BenificiaryListView beneficaryDetails) {
		remitTrnxManager.validateBlackListedBene(beneficaryDetails);
	}

	public void validateAdditionalCheck(RoutingResponseDto branchRoutingDto, Customer customer,
			BenificiaryListView beneficaryDetails, BigDecimal localNetAmount) {
		// EX_APPL_ADDL_CHECKS
		BigDecimal customerId = BigDecimal.ZERO;
		String allowNoBank = null;

		/*
		 * BigDecimal routingBankId =
		 * branchRoutingDetails.get("P_ROUTING_BANK_ID")==null?BigDecimal.ZERO:(
		 * BigDecimal)branchRoutingDetails.get("P_ROUTING_BANK_ID"); BigDecimal
		 * serviceMasterId =
		 * branchRoutingDetails.get("P_SERVICE_MASTER_ID")==null?BigDecimal.ZERO:(
		 * BigDecimal)branchRoutingDetails.get("P_SERVICE_MASTER_ID"); BigDecimal
		 * beneBankId =
		 * beneficaryDetails.getBankId()==null?BigDecimal.ZERO:beneficaryDetails.
		 * getBankId();
		 */

		BigDecimal routingBankId = branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId();
		BigDecimal serviceMasterId = branchRoutingDto.getServiceList().get(0).getServiceMasterId();
		BigDecimal beneBankId = beneficaryDetails.getBankId() == null ? BigDecimal.ZERO : beneficaryDetails.getBankId();

		if (customer != null) {
			customerId = customer.getCustomerId();
		} else {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID, "customer Id should not be blank");
		}

		BankMasterModel bankDetails = bankService.getBankById(routingBankId);
		if (bankDetails != null && bankDetails.getRecordStatus() != null
				&& bankDetails.getRecordStatus().equalsIgnoreCase(ConstantDocument.Yes)) {
			allowNoBank = bankDetails.getAllowNoBank();
		} else {
			throw new GlobalException(JaxError.INVALID_BENE_BANK, " Invalid Bank  " + routingBankId);
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(serviceMasterId)
				&& serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_DD) == 0
				&& !allowNoBank.equalsIgnoreCase(ConstantDocument.Yes)) {
			throw new GlobalException(JaxError.INVALID_BENE_BANK,
					" For Cash / DD  Bene bank and branch should be No bank and No branch " + routingBankId
							+ "\t serviceMasterId :" + serviceMasterId + "\t allowNoBank :" + allowNoBank);
		}

		if (JaxUtil.isNullZeroBigDecimalCheck(routingBankId)
				&& JaxUtil.isNullZeroBigDecimalCheck(beneBankId)
				&& routingBankId.compareTo(beneBankId) == 0
				&& serviceMasterId.compareTo(ConstantDocument.SERVICE_MASTER_ID_TT) == 0) {
			throw new GlobalException(JaxError.INVALID_BENE_BANK,
					" Routing and beneficary bank cannot be the same for TT " + routingBankId + "\t serviceMasterId :"
							+ serviceMasterId + "\t beneBankId :" + beneBankId);
		}

		BigDecimal wbLimit = routingPro.getWbLimit();
		List<CustomerIdProof> list = idProofDao.getCustomerImageValidation(customerId,
				ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);

		if (list != null && !list.isEmpty() && list.size() > 1) {
			throw new GlobalException(JaxError.INVALID_CUSTOMER, " Customer has many ID Proofs-'-" + customerId);
		}

		if (list != null && list.size() == 1 && JaxUtil.isNullZeroBigDecimalCheck(wbLimit)) {
			if (localNetAmount.compareTo(wbLimit) == 1) {
				throw new GlobalException(JaxError.INVALID_BENE_BANK,
						" Non ID card holders cannot exceed amount-" + wbLimit);
			}
		}

	}

	public Map<String, Object> validateAdditionalBeneDetails(RoutingResponseDto branchRoutingDto,
			Map<String, Object> branchExchangeRate, BenificiaryListView beneficaryDetails) {

		BigDecimal beneficaryMasterId = beneficaryDetails.getBeneficaryMasterSeqId();
		BigDecimal beneficaryBankId = beneficaryDetails.getBankId();
		BigDecimal beneficaryBankBranchId = beneficaryDetails.getBranchId();
		BigDecimal beneAccNumSeqId = beneficaryDetails.getBeneficiaryAccountSeqId();
		/*
		 * BigDecimal routingCountry = (BigDecimal)
		 * branchRoutingDetails.get("P_ROUTING_COUNTRY_ID"); BigDecimal routingBank =
		 * (BigDecimal) branchRoutingDetails.get("P_ROUTING_BANK_ID"); BigDecimal
		 * routingBranch = (BigDecimal)
		 * branchRoutingDetails.get("P_ROUTING_BANK_BRANCH_ID"); BigDecimal
		 * serviceMasterId = (BigDecimal)
		 * branchRoutingDetails.get("P_SERVICE_MASTER_ID");
		 */

		BigDecimal routingCountry = branchRoutingDto.getRoutingCountrydto().get(0).getRoutingCountryId();
		BigDecimal routingBank = branchRoutingDto.getRoutingBankDto().get(0).getRoutingBankId();
		BigDecimal routingBranch = branchRoutingDto.getRoutingBankBranchDto().get(0).getBankBranchId();
		BigDecimal serviceMasterId = branchRoutingDto.getServiceList().get(0).getServiceMasterId();

		BigDecimal applicationCountryId = beneficaryDetails.getApplicationCountryId();
		BigDecimal currencyId = beneficaryDetails.getCurrencyId();
		BigDecimal remitMode = (BigDecimal) branchExchangeRate.get("P_REMITTANCE_MODE_ID");
		BigDecimal deliveryMode = (BigDecimal) branchExchangeRate.get("P_DELIVERY_MODE_ID");
		BigDecimal beneficaryRelationSeqId = beneficaryDetails.getBeneficiaryRelationShipSeqId();

		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_BENEFICIARY_MASTER_ID", beneficaryMasterId);
		inputValues.put("P_BENEFICIARY_BANK_ID", beneficaryBankId);
		inputValues.put("P_BENEFICIARY_BRANCH_ID", beneficaryBankBranchId);
		inputValues.put("P_BENEFICARY_ACCOUNT_SEQ_ID", beneAccNumSeqId);
		inputValues.put("P_ROUTING_COUNTRY_ID", routingCountry);
		inputValues.put("P_ROUTING_BANK_ID", routingBank);
		inputValues.put("P_ROUTING_BANK_BRANCH_ID", routingBranch);
		inputValues.put("P_SERVICE_MASTER_ID", serviceMasterId);
		inputValues.put("P_APPLICATION_COUNTRY_ID", applicationCountryId);
		inputValues.put("P_CURRENCY_ID", currencyId);
		inputValues.put("P_REMITTANCE_MODE_ID", remitMode);
		inputValues.put("P_DELIVERY_MODE_ID", deliveryMode);
		inputValues.put("P_BENE_RELATION_SEQ_ID", beneficaryRelationSeqId);

		Map<String, Object> outPut = applProcedureDao.toFetchDetilaFromAddtionalBenficiaryDetails(inputValues);
		if (outPut != null && outPut.get("P_ERROR_MESSAGE") != null) {
			throw new GlobalException(JaxError.BENE_ADD_CHECK_ERROR, outPut.get("P_ERROR_MESSAGE").toString());
		}

		return outPut;
	}

	public void validateAdditionalErrorMessages(Map<String, Object> hashMap) {
		BranchRemittanceApplRequestModel applRequestModel = (BranchRemittanceApplRequestModel) hashMap
				.get("APPL_REQ_MODEL");
		Map<String, Object> branchRoutingDetails = (HashMap) hashMap.get("ROUTING_DETAILS_MAP");
		Map<String, Object> branchExchangeRate = (HashMap) hashMap.get("EXCH_RATE_MAP");
		BenificiaryListView beneDetails = (BenificiaryListView) hashMap.get("BENEFICIARY_DETAILS");

		RemittanceTransactionRequestModel requestModel = new RemittanceTransactionRequestModel();
		requestModel.setAdditionalBankRuleFiledId(applRequestModel.getAdditionalBankRuleFiledId());
		requestModel.setSrlId(applRequestModel.getSrlId());

		remitApplParametersMap.put("P_APPLICATION_COUNTRY_ID", beneDetails.getApplicationCountryId());
		remitApplParametersMap.put("P_ROUTING_COUNTRY_ID",
				(BigDecimal) branchRoutingDetails.get("P_ROUTING_COUNTRY_ID"));
		remitApplParametersMap.put("P_ROUTING_BANK_ID", (BigDecimal) branchRoutingDetails.get("P_ROUTING_BANK_ID"));
		remitApplParametersMap.put("P_FOREIGN_CURRENCY_ID", beneDetails.getCurrencyId());
		remitApplParametersMap.put("P_REMITTANCE_MODE_ID", (BigDecimal) branchExchangeRate.get("P_REMITTANCE_MODE_ID"));
		remitApplParametersMap.put("P_DELIVERY_MODE_ID", (BigDecimal) branchExchangeRate.get("P_DELIVERY_MODE_ID"));
		remitApplTrnxManager.validateAdditionalErrorMessages(requestModel);
	}

	public BigDecimal generateDocumentNumber(BigDecimal applCountryId, BigDecimal companyId, BigDecimal documentId,
			BigDecimal financialYear, String processIn, BigDecimal branchId) {
		Map<String, Object> output = applProcedureDao.getDocumentSeriality(applCountryId, companyId, documentId,
				financialYear, processIn, branchId);
		return (BigDecimal) output.get("P_DOC_NO");
	}

	/*
	 * private BigDecimal reCalculateComission() {
	 * logger.debug("recalculating comission "); BigDecimal custtype =
	 * bizcomponentDao.findCustomerTypeId("I");
	 * remitApplParametersMap.put("P_CUSTYPE_ID", custtype); BigDecimal comission =
	 * exchangeRateProcedureDao.getCommission(remitApplParametersMap);
	 * logger.debug("newCommission 95: " + comission); if (comission == null) {
	 * remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777)); comission =
	 * exchangeRateProcedureDao.getCommission(remitApplParametersMap); }
	 * logger.debug("newCommission: " + comission); return comission; }
	 * 
	 * private Map<String, Object> getCommissionRange(ExchangeRateBreakup breakup) {
	 * 
	 * remitApplParametersMap.put("P_CALCULATED_FC_AMOUNT",
	 * breakup.getConvertedFCAmount()); BigDecimal custtype =
	 * bizcomponentDao.findCustomerTypeId("I");
	 * remitApplParametersMap.put("P_CUSTYPE_ID", custtype); Map<String, Object>
	 * comissionRangeMap =
	 * exchangeRateProcedureDao.getCommissionRange(remitApplParametersMap); if
	 * (comissionRangeMap.get("FROM_AMOUNT") == null ||
	 * comissionRangeMap.get("TO_AMOUNT") == null) {
	 * remitApplParametersMap.put("P_CUSTYPE_ID", new BigDecimal(777));
	 * comissionRangeMap =
	 * exchangeRateProcedureDao.getCommissionRange(remitApplParametersMap); }
	 * logger.info("comissionRangeMap: " + comissionRangeMap.toString()); return
	 * comissionRangeMap;
	 * 
	 * }
	 * 
	 * private void recalculateDeliveryAndRemittanceModeId() {
	 * 
	 * if (remitApplParametersMap.get("P_CALCULATED_FC_AMOUNT") != null) {
	 * 
	 * BigDecimal custtype = bizcomponentDao.findCustomerTypeId("I");
	 * remitApplParametersMap.put("P_CUSTYPE_ID", custtype); Map<String, Object>
	 * outputMap = exchangeRateProcedureDao
	 * .findRemittanceAndDevlieryModeId(remitApplParametersMap); if
	 * (outputMap.size() == 0) { remitApplParametersMap.put("P_CUSTYPE_ID", new
	 * BigDecimal(777)); outputMap =
	 * exchangeRateProcedureDao.findRemittanceAndDevlieryModeId(
	 * remitApplParametersMap); } if (outputMap.size() > 2) { throw new
	 * GlobalException( TOO_MANY_COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
	 * "TOO MANY COMMISSION DEFINED for rounting bankid: " +
	 * remitApplParametersMap.get("P_ROUTING_BANK_ID")); }
	 * 
	 * if (outputMap.get("P_DELIVERY_MODE_ID") == null) { throw new
	 * GlobalException(COMISSION_NOT_DEFINED_FOR_ROUTING_BANK,
	 * "COMMISSION NOT DEFINED BankId: " +
	 * remitApplParametersMap.get("P_ROUTING_BANK_ID")); }
	 * remitApplParametersMap.putAll(outputMap); } }
	 * 
	 * private void setLoyalityPointIndicaters(RemittanceTransactionResponsetModel
	 * responseModel) { if (responseModel.getCanRedeemLoyalityPoints() == null) {
	 * BigDecimal maxLoyalityPointRedeem =
	 * responseModel.getMaxLoyalityPointsAvailableForTxn(); BigDecimal
	 * loyalityPointsAvailable = responseModel.getTotalLoyalityPoints(); if
	 * (loyalityPointsAvailable == null || (loyalityPointsAvailable.longValue() <
	 * maxLoyalityPointRedeem.longValue())) {
	 * responseModel.setCanRedeemLoyalityPoints(false);
	 * responseModel.setLoyalityPointState(LoyalityPointState.CAN_NOT_AVAIL); } else
	 * { responseModel.setCanRedeemLoyalityPoints(true); } } }
	 */

	public BigDecimal getSelectedCurrency(BigDecimal foreignCurrencyId,
			BranchRemittanceApplRequestModel requestApplModel) {
		if (JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getForeignAmount())) {
			return foreignCurrencyId;
		}
		return metaData.getDefaultCurrencyId();
	}

	public BigDecimal getSelectedCurrencyAmount(BigDecimal selectedCurrency,
			BranchRemittanceApplRequestModel requestApplModel) {
		if (JaxUtil.isNullZeroBigDecimalCheck(requestApplModel.getForeignAmount())) {
			return requestApplModel.getForeignAmount();
		} else {
			return requestApplModel.getLocalAmount();
		}

	}

	public String getCustomerType(BigDecimal componentDataId) {
		BizComponentData bizComData = bizCompDao.getBizComponentDataByComponmentDataId(componentDataId);
		String customerType = ConstantDocument.Individual;
		if (bizComData != null) {
			customerType = bizComData.getComponentCode();
		} else {
			throw new GlobalException(JaxError.INVALID_BENE_BANK, " Customer type is not defined " + componentDataId);
		}
		return customerType;
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


	public List<AdditionalExchAmiecDto> getPurposeOfTrnx(BigDecimal beneRelId) {
		BenificiaryListView beneficaryDetails = beneficiaryRepository.findBybeneficiaryRelationShipSeqId(beneRelId);

		if (beneficaryDetails == null) {
			throw new GlobalException(JaxError.BENEFICIARY_LIST_NOT_FOUND, "Beneficairy not found " + beneRelId);

		}

		List<AdditionalExchAmiecDto> purposeofTrnx = new ArrayList<>();
		CountryMaster cntMaster = new CountryMaster();
		cntMaster.setCountryId(beneficaryDetails.getBenificaryCountry());
		List<AdditionalBankRuleAmiec> amiecRuleMap = amiecBankRuleRepo.getPurposeOfTrnxByCountryId(cntMaster);

		if (amiecRuleMap != null && amiecRuleMap.size() != 0) {
			return convertPurposeOfTrnxDto(amiecRuleMap);
		} else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND, "No records found");
		}
	}

	public List<AdditionalExchAmiecDto> convertPurposeOfTrnxDto(List<AdditionalBankRuleAmiec> amiecRuleMap) {
		List<AdditionalExchAmiecDto> dto = new ArrayList<>();

		for (AdditionalBankRuleAmiec amiec : amiecRuleMap) {
			AdditionalExchAmiecDto amiecDto = new AdditionalExchAmiecDto();
			amiecDto.importFrom(amiec);
			amiecDto.setFlexField(amiec.getFlexField());
			amiecDto.setCountryId(amiec.getCountryId().getCountryId());

			// Aditional Fields : TODO:- check which ones required to set
			amiecDto.setAmiecCode(amiec.getAmiecCode());
			amiecDto.setAmiecDescription(amiec.getAmiecDescription());
			amiecDto.setAdditionalBankRuleDetailId(amiec.getAdditionalBankRuleDetailId());

			dto.add(amiecDto);
		}

		return dto;
	}

}
