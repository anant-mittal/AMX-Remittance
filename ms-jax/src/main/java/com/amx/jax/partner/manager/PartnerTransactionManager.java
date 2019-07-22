package com.amx.jax.partner.manager;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branchremittance.manager.BranchRoutingManager;
import com.amx.jax.client.serviceprovider.ServiceProviderClient;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.BankDao;
import com.amx.jax.dbmodel.AccountTypeFromViewModel;
import com.amx.jax.dbmodel.BankBranchView;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.CollectionDetailViewModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CustomerDetailsView;
import com.amx.jax.dbmodel.ParameterDetails;
import com.amx.jax.dbmodel.partner.BankExternalReferenceDetail;
import com.amx.jax.dbmodel.partner.BankExternalReferenceHead;
import com.amx.jax.dbmodel.partner.PaymentModeLimitsView;
import com.amx.jax.dbmodel.partner.TransactionDetailsView;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleAmiec;
import com.amx.jax.dbmodel.remittance.AmiecAndBankMapping;
import com.amx.jax.dbmodel.remittance.RemittanceBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.request.partner.RemittanceTransactionPartnerDTO;
import com.amx.jax.model.request.partner.SrvProvBeneficiaryTransactionDTO;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.ServiceProviderCallRequestDto;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.remittance.RemittanceResponseDto;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.partner.dao.PartnerTransactionDao;
import com.amx.jax.partner.dto.BeneficiaryDetailsDTO;
import com.amx.jax.partner.dto.CustomerDetailsDTO;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.var.PricerServiceConstants;
import com.amx.jax.pricer.var.PricerServiceConstants.SERVICE_PROVIDER_BANK_CODE;
import com.amx.jax.repository.IAdditionalBankRuleAmiecRepository;
import com.amx.jax.repository.IAmiecAndBankMappingRepository;
import com.amx.jax.repository.ICollectionDetailViewDao;
import com.amx.jax.repository.ISourceOfIncomeDao;
import com.amx.jax.services.BankService;
import com.amx.jax.services.BeneficiaryService;
import com.amx.jax.util.AmxDBConstants;
import com.amx.utils.Constants;
import com.amx.utils.JsonUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class PartnerTransactionManager extends AbstractModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	MetaData metaData;

	@Autowired
	PartnerTransactionDao partnerTransactionDao;

	@Autowired
	BankDao bankBranchViewDao;

	@Autowired
	ISourceOfIncomeDao sourceOfIncomeDao;

	@Autowired
	BranchRoutingManager branchRoutingManager;

	@Autowired
	private BeneficiaryService beneficiaryService;

	@Autowired
	ServiceProviderClient serviceProviderClient;

	@Autowired
	private BankService bankService;

	@Autowired
	IAdditionalBankRuleAmiecRepository amiecBankRuleRepo;

	@Autowired
	IAmiecAndBankMappingRepository amiecAndBankMappingRepository;

	@Autowired
	ICollectionDetailViewDao collectionDetailViewDao;

	public void callingPartnerApi(Map<BigDecimal,RemittanceTransaction> remitTrnxList,Map<BigDecimal,RemittanceBenificiary> remitBeneList,RemittanceResponseDto responseDto) {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal collectionDocYear = responseDto.getCollectionDocumentFYear();
		BigDecimal collectionDocNumber = responseDto.getCollectionDocumentNo();

		convertingTransactionPartnerDetails(customerId, collectionDocYear, collectionDocNumber);
	}

	public void convertingTransactionPartnerDetails(BigDecimal customerId,BigDecimal collectionDocYear,BigDecimal collectionDocNumber) {
		String destinationCountryAlpha3 = null;
		String destinationCountryAlpha2 = null;
		AmxApiResponse<ServiceProviderResponse, Object> srvPrvResp = null;

		List<ServiceProviderCallRequestDto> lstTransactionRequestDto = new ArrayList<>();

		CustomerDetailsDTO customerDetailsDTO = fetchCustomerDetails(customerId);
		Customer customerDto = fetchSPCustomerDto(customerDetailsDTO);

		Map<BigDecimal,SrvProvBeneficiaryTransactionDTO> trnxPartnerData = fetchTransactionDetails(collectionDocYear, collectionDocNumber, customerId);
		
		BigDecimal applicationCountryId = metaData.getCountryId();
		String applicationCountryAlpha3 = null;
		if(applicationCountryId != null){
			CountryMaster countryMaster = fetchCountryMasterDetails(applicationCountryId);
			if(countryMaster != null) {
				applicationCountryAlpha3 = countryMaster.getCountryAlpha3Code();
			}
		}

		for (Entry<BigDecimal, SrvProvBeneficiaryTransactionDTO> srvProvBeneTrnx : trnxPartnerData.entrySet()) {

			ServiceProviderCallRequestDto serviceProviderCallRequestDto = new ServiceProviderCallRequestDto();

			serviceProviderCallRequestDto.setCustomerDto(customerDto);

			BeneficiaryDetailsDTO beneficiaryDetailsDTO = srvProvBeneTrnx.getValue().getBeneficiaryDetailsDTO();
			RemittanceTransactionPartnerDTO remittanceTransactionPartnerDTO = srvProvBeneTrnx.getValue().getRemittanceTransactionPartnerDTO();

			if(customerDetailsDTO.getNationalityId() != null){
				CountryMaster countryMaster = fetchCountryMasterDetails(beneficiaryDetailsDTO.getBenificaryCountry());
				if(countryMaster != null) {
					destinationCountryAlpha3 = countryMaster.getCountryAlpha3Code();
					destinationCountryAlpha2 = countryMaster.getCountryAlpha2Code();
				}
			}

			Benificiary beneficiaryDto = fetchSPBeneficiaryDto(customerDetailsDTO, beneficiaryDetailsDTO,remittanceTransactionPartnerDTO,destinationCountryAlpha3);
			serviceProviderCallRequestDto.setBeneficiaryDto(beneficiaryDto);

			TransactionData transactionDto = fetchSPTransactionDto(customerDetailsDTO, beneficiaryDetailsDTO,remittanceTransactionPartnerDTO,destinationCountryAlpha3,destinationCountryAlpha2,applicationCountryAlpha3);
			serviceProviderCallRequestDto.setTransactionDto(transactionDto);

			lstTransactionRequestDto.add(serviceProviderCallRequestDto);
		}

		for (ServiceProviderCallRequestDto serviceProviderCallRequestDto : lstTransactionRequestDto) {
			// final commit of transaction with service provider
			logger.info("Inputs passed to Service Provider Home Send : ServiceProviderCallRequestDto : " + JsonUtil.toJson(serviceProviderCallRequestDto));
			srvPrvResp = serviceProviderClient.sendRemittance(serviceProviderCallRequestDto);
			logger.info("Output from Service Provider Home Send : " + JsonUtil.toJson(srvPrvResp));
			fetchServiceProviderData(serviceProviderCallRequestDto,srvPrvResp);
		}

	}

	public Customer fetchSPCustomerDto(CustomerDetailsDTO customerDetailsDTO) {
		Customer customerDto = new Customer();

		// Customer Details
		customerDto.setAddress_zip(Constants.SENDER_ZIP_CODE);

		String address[] = customerDetailsDTO.getAddress1().split(",");
		String buildingNo = address[0];
		String streetNo = address[1];
		String flatNo = address[2];
		String custAddress = (buildingNo == null ? "" : buildingNo.concat(" ")).concat(streetNo == null ? "" : streetNo.concat(" ").concat(flatNo == null ? "" : flatNo));
		customerDto.setBuilding_no(removeSpaces(custAddress));

		String cityName = null;
		if(customerDetailsDTO.getCityName() != null && customerDetailsDTO.getCityName().length() != 0){
			cityName = customerDetailsDTO.getCityName();
		}else if(customerDetailsDTO.getDistrictName() != null && customerDetailsDTO.getDistrictName().length() != 0){
			cityName = customerDetailsDTO.getDistrictName();
		}else if(customerDetailsDTO.getStateName() != null && customerDetailsDTO.getStateName().length() != 0){
			cityName = customerDetailsDTO.getStateName();
		}
		customerDto.setCity(removeSpaces(cityName));
		customerDto.setContact_no(removeSpaces(customerDetailsDTO.getContactNumber()));

		if(customerDetailsDTO.getCustomerReference() != null) {
			customerDto.setCustomer_reference(customerDetailsDTO.getCustomerReference().toString());
		}

		customerDto.setCustomer_type(removeSpaces(customerDetailsDTO.getCustomerTypeCode()));
		customerDto.setDate_of_birth(customerDetailsDTO.getDateOfBirth());
		customerDto.setDistrict(removeSpaces(customerDetailsDTO.getDistrictName()));
		customerDto.setEmail(removeSpaces(customerDetailsDTO.getEmail()));
		customerDto.setEmployer_name(removeSpaces(customerDetailsDTO.getEmployerName()));
		customerDto.setFirst_name(removeSpaces(customerDetailsDTO.getFirstName()));
		customerDto.setFull_addrerss(removeSpaces(customerDetailsDTO.getAddress1()));

		String gender = null;
		if(customerDetailsDTO.getGender() != null && customerDetailsDTO.getGender().equalsIgnoreCase(Constants.MALE)){
			gender = Constants.GENDER_MALE;
		}else if(customerDetailsDTO.getGender() != null && customerDetailsDTO.getGender().equalsIgnoreCase(Constants.FEMALE)){
			gender = Constants.GENDER_FEMALE;
		}else{
			gender = Constants.GENDER_MALE;
		}

		customerDto.setGender(gender);
		customerDto.setIdentity_expiry_date(customerDetailsDTO.getIdExpiryDate());
		customerDto.setIdentity_no(removeSpaces(customerDetailsDTO.getIdNumber()));
		customerDto.setIdentity_type_desc(removeSpaces(customerDetailsDTO.getIdTypeFor()));
		customerDto.setLast_name(removeSpaces(customerDetailsDTO.getLastName()));
		customerDto.setMiddle_name(removeSpaces(customerDetailsDTO.getMiddleName()));

		String senderBirthCountryAlpha3 = null;
		if(customerDetailsDTO.getNationalityId() != null){
			CountryMaster countryMaster = fetchCountryMasterDetails(customerDetailsDTO.getNationalityId());
			if(countryMaster != null) {
				senderBirthCountryAlpha3 = countryMaster.getCountryAlpha3Code();
			}
		}
		customerDto.setNationality_3_digit_ISO(removeSpaces(senderBirthCountryAlpha3));
		customerDto.setProfession(removeSpaces(customerDetailsDTO.getDesignation()));
		customerDto.setState(removeSpaces(customerDetailsDTO.getStateName()));
		customerDto.setStreet(removeSpaces(custAddress));

		return customerDto;
	}

	public Benificiary fetchSPBeneficiaryDto(CustomerDetailsDTO customerDetailsDTO,BeneficiaryDetailsDTO beneficiaryDetailsDTO,RemittanceTransactionPartnerDTO remitTrnxDto,String destinationCountryAlpha3) {
		String swiftBicCode = null;
		String bankCode = null;
		String bankBranchCode = null;
		String bankAccountTypeDesc = null;

		Benificiary beneficiaryDto = new Benificiary();

		// Beneficiary Details
		beneficiaryDto.setBeneficiary_account_number(removeSpaces(beneficiaryDetailsDTO.getBankAccountNumber()));

		AccountTypeFromViewModel accountTypeFromViewModel = partnerTransactionDao.getAccountTypeDetails(beneficiaryDetailsDTO.getBankAccountTypeId());
		if(accountTypeFromViewModel != null) {
			bankAccountTypeDesc = accountTypeFromViewModel.getAmiecDesc();
		}
		beneficiaryDto.setBeneficiary_account_type(removeSpaces(bankAccountTypeDesc));

		String routingNumber_Indic2 = remitTrnxDto.getRoutingNumber_Indic2();
		String bsbNumber_Indic3 = remitTrnxDto.getBsbNumber_Indic3();

		BankBranchView bankBranchView = bankBranchViewDao.getBankBranchById(beneficiaryDetailsDTO.getBankId(), beneficiaryDetailsDTO.getBranchId());

		if(bankBranchView != null) {
			if(bankBranchView.getSwift() != null){
				swiftBicCode = bankBranchView.getSwift();
			}else if(bankBranchView.getIfscCode() != null){
				swiftBicCode = bankBranchView.getIfscCode();
			}
		}

		bankCode = beneficiaryDetailsDTO.getBankCode();
		if(routingNumber_Indic2 != null) {
			bankCode = routingNumber_Indic2;
		}else if(bsbNumber_Indic3 != null){
			bankCode = bsbNumber_Indic3;
		}else {
			// bank code mapping based on Home request
			bankCode = fetchBankCodeHomeSend(beneficiaryDetailsDTO.getBenificaryCountry(),remitTrnxDto.getRoutingBankId(),beneficiaryDetailsDTO.getBankId());
		}
		if(swiftBicCode == null) {
			// bank branch code mapping based on Home request
			bankBranchCode = fetchBankBranchCodeHomeSend(beneficiaryDetailsDTO.getBenificaryCountry(),remitTrnxDto.getRoutingBankId(),beneficiaryDetailsDTO.getBankId(),beneficiaryDetailsDTO.getBranchId());
		}

		beneficiaryDto.setBeneficiary_bank_branch_swift_code(swiftBicCode);
		beneficiaryDto.setBeneficiary_bank_code(bankCode);
		beneficiaryDto.setBeneficiary_branch_code(bankBranchCode);
		beneficiaryDto.setBeneficiary_bank_name(beneficiaryDetailsDTO.getBankName());
		beneficiaryDto.setBeneficiary_branch_name(beneficiaryDetailsDTO.getBankBranchName());

		if(beneficiaryDetailsDTO.getMapSequenceId() != null) {
			beneficiaryDto.setBeneficiary_reference(beneficiaryDetailsDTO.getMapSequenceId().toPlainString());
		}

		beneficiaryDto.setBeneficiary_type(removeSpaces(beneficiaryDetailsDTO.getBenificaryStatusName()));

		int bicValue = 0;
		int bankBranch = 0;
		HashMap<String, Integer> mapBICandBankDt = fetchBICandBankCodeData();

		HashMap<String, String> mapBicAndBankCode = fetchBICandBankCode(destinationCountryAlpha3);
		if (mapBicAndBankCode != null && mapBICandBankDt != null) {
			String bicCode = mapBicAndBankCode.get("BIC_CODE");
			String bankBranchDt = mapBicAndBankCode.get("BANK_BRANCH");
			if(bicCode != null) {
				bicValue = mapBICandBankDt.get(bicCode.trim());
			}
			if(bankBranchDt != null) {
				bankBranch = mapBICandBankDt.get(bankBranchDt.trim());
			}
		}
		beneficiaryDto.setBic_indicator(bicValue);
		beneficiaryDto.setBeneficiary_bank_branch_indicator(bankBranch);

		boolean status = checkBeneCountryParam(destinationCountryAlpha3);
		if(!status){
			beneficiaryDto.setState(removeSpaces(beneficiaryDetailsDTO.getStateName())); 
			beneficiaryDto.setStreet(removeSpaces(beneficiaryDetailsDTO.getStreetNo()));

			StringBuffer strAdd = new StringBuffer();
			if (beneficiaryDetailsDTO.getStateName() != null) {
				strAdd.append(beneficiaryDetailsDTO.getStateName());
			}
			if (beneficiaryDetailsDTO.getDistrictName() != null) {
				strAdd.append("," + beneficiaryDetailsDTO.getDistrictName());
			}
			if (beneficiaryDetailsDTO.getCityName() != null) {
				strAdd.append("," + beneficiaryDetailsDTO.getCityName());
			}
			if (strAdd != null) {
				beneficiaryDto.setFull_addrerss(removeSpaces(strAdd.toString())); 
			}
			beneficiaryDto.setDistrict(removeSpaces(beneficiaryDetailsDTO.getDistrictName())); 
			beneficiaryDto.setCity(removeSpaces(beneficiaryDetailsDTO.getCityName())); 
			beneficiaryDto.setAddress_zip(beneficiaryDetailsDTO.getBeneficiaryZipCode());
		}

		String telNumber = beneficiaryService.getBeneficiaryContactNumber(beneficiaryDetailsDTO.getBeneficaryMasterSeqId());
		beneficiaryDto.setContact_no(telNumber);

		beneficiaryDto.setDate_of_birth(beneficiaryDetailsDTO.getDateOfBirth());

		if(beneficiaryDetailsDTO.getIbanNumber() != null) {
			beneficiaryDto.setIs_iban_number_holder(Boolean.TRUE);
		}else {
			beneficiaryDto.setIs_iban_number_holder(Boolean.FALSE);
		}

		String beneFirstName = null;
		String beneMiddleName = null;
		String beneLastName = null;
		if(beneficiaryDetailsDTO.getFirstName() != null){
			beneFirstName = beneficiaryDetailsDTO.getFirstName();
		}

		if(beneficiaryDetailsDTO.getThirdName() != null){
			if(beneficiaryDetailsDTO.getSecondName() != null){
				beneMiddleName = beneficiaryDetailsDTO.getSecondName();
			}
			beneLastName = beneficiaryDetailsDTO.getThirdName();
		}else{
			if(beneficiaryDetailsDTO.getSecondName() != null){
				beneLastName = beneficiaryDetailsDTO.getSecondName();
			}
			if(beneficiaryDetailsDTO.getThirdName() != null){
				beneLastName = beneLastName +  " " + beneficiaryDetailsDTO.getThirdName();
			}
		}

		if(beneficiaryDetailsDTO.getFourthName() != null){
			beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFourthName();
		}

		if(beneficiaryDetailsDTO.getFiftheName() != null){
			beneLastName = beneLastName +  " " +beneficiaryDetailsDTO.getFiftheName();
		}

		if(beneLastName != null && beneLastName.length() > 80){
			beneLastName = beneLastName.substring(0,79);
		}

		beneficiaryDto.setFirst_name(beneFirstName);
		beneficiaryDto.setMiddle_name(beneMiddleName);
		beneficiaryDto.setLast_name(beneLastName);

		String beneficiaryNationalityCountryCode = null;
		if(beneficiaryDetailsDTO.getNationality() != null){
			CountryMaster countryMaster = fetchCountryMasterDetails(new BigDecimal(beneficiaryDetailsDTO.getNationality()));
			if(countryMaster != null) {
				beneficiaryNationalityCountryCode = countryMaster.getCountryAlpha3Code();
			}
		}
		beneficiaryDto.setNationality_3_digit_ISO(beneficiaryNationalityCountryCode);

		beneficiaryDto.setProfession(removeSpaces(beneficiaryDetailsDTO.getOccupation()));
		beneficiaryDto.setRelation_to_beneficiary(removeSpaces(beneficiaryDetailsDTO.getRelationShipName()));

		// not passing any value
		beneficiaryDto.setBeneficiary_id_number(null);
		beneficiaryDto.setBeneficiary_id_type(null);
		beneficiaryDto.setWallet_service_provider(null);

		return beneficiaryDto;
	}

	public TransactionData fetchSPTransactionDto(CustomerDetailsDTO customerDetailsDTO,BeneficiaryDetailsDTO beneficiaryDetailsDTO,RemittanceTransactionPartnerDTO remitTrnxDto,String destinationCountryAlpha3,String destinationCountryAlpha2,String applicationCountryAlpha3) {
		TransactionData transactionDto = new TransactionData();

		//Transaction Details
		transactionDto.setApplication_country_3_digit_ISO(applicationCountryAlpha3);
		transactionDto.setDestination_country_2_digit_ISO(destinationCountryAlpha2);
		transactionDto.setDestination_country_3_digit_ISO(destinationCountryAlpha3);
		transactionDto.setDestination_currency(beneficiaryDetailsDTO.getCurrencyQuoteName());
		transactionDto.setSettlement_currency(PricerServiceConstants.SETTLEMENT_CURRENCY_CODE);
		transactionDto.setOrigin_country_3_digit_ISO(applicationCountryAlpha3);
		transactionDto.setRemittance_mode(remitTrnxDto.getRemittanceMode());
		transactionDto.setDelivery_mode(remitTrnxDto.getDeliveryMode());
		transactionDto.setDestination_amount(remitTrnxDto.getDestinationAmount());
		transactionDto.setFurther_instruction(remitTrnxDto.getFurtherInstruction());
		transactionDto.setOut_going_transaction_reference(remitTrnxDto.getOutGoingTransactionReference());
		transactionDto.setPartner_transaction_reference(remitTrnxDto.getPartnerTransactionReference());
		transactionDto.setPurpose_of_remittance(remitTrnxDto.getPurposeOfTransaction_Indic1());
		transactionDto.setRequest_sequence_id(remitTrnxDto.getRequestSequenceId());
		transactionDto.setRoutting_bank_code(remitTrnxDto.getRoutingBankCode());
		transactionDto.setSettlement_amount(remitTrnxDto.getSettlementAmount());
		transactionDto.setSource_of_fund_desc(remitTrnxDto.getSourceOfFundDesc());
		transactionDto.setTxn_collocation_type(remitTrnxDto.getTrnxCollectionType());

		return transactionDto;
	}

	// fetch Customer details
	public CustomerDetailsDTO fetchCustomerDetails(BigDecimal customerId) {
		CustomerDetailsDTO customerdto = new CustomerDetailsDTO();
		CustomerDetailsView customerDetails = partnerTransactionDao.getCustomerDetails(customerId);
		if(customerDetails != null) {
			try {
				BeanUtils.copyProperties(customerdto, customerDetails);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Unable to convert Customer Details : None Found : " + customerId + " Exception " +e);
				throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
						"Unable to convert Customer Details : None Found : " + customerId);
			}
		}else {
			// fail
			logger.warn("Invalid Customer : None Found : " + customerId);
			throw new PricerServiceException(PricerServiceError.INVALID_CUSTOMER,
					"Invalid Customer : None Found : " + customerId);
		}

		return customerdto;
	}

	// fetch beneficiary details
	public BeneficiaryDetailsDTO fetchBeneficiaryDetails(BigDecimal customerId,BigDecimal beneficiaryRelationShipId) {
		BeneficiaryDetailsDTO beneficiaryDto = new BeneficiaryDetailsDTO();
		BenificiaryListView beneficaryDetails = partnerTransactionDao.getBeneficiaryDetails(customerId,beneficiaryRelationShipId);
		if(beneficaryDetails != null) {
			try {
				BeanUtils.copyProperties(beneficiaryDto, beneficaryDetails);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.error("Unable to convert Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId + " Exception " +e);
				throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
						"Unable to convert Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
			}
		}else {
			// fail
			logger.warn("Invalid Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
			throw new PricerServiceException(PricerServiceError.INVALID_BENEFICIARY,
					"Invalid Beneficiary Details : None Found : Customer Id " + customerId + " Beneficiary Relation Ship Id " + beneficiaryRelationShipId);
		}

		return beneficiaryDto;
	}

	public static String removeSpaces(String obj) {
		String value = null;

		if(obj != null && !obj.equalsIgnoreCase("")) {
			value = obj.trim().replaceAll(" +"," ").replaceAll("\t"," ").replaceAll("\n"," ");
		}

		return value;
	}

	// external bank codes
	public String fetchBankCodeHomeSend(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId){
		String mapBankCode = null;
		try {
			List<BankExternalReferenceHead> lstBankExtRefHead = partnerTransactionDao.fetchBankExternalReferenceHeadDetails(countryId, corBankId, beneBankId);
			if(lstBankExtRefHead != null && lstBankExtRefHead.size() == 1) {
				BankExternalReferenceHead bankExternalReferenceHead = lstBankExtRefHead.get(0);
				mapBankCode = bankExternalReferenceHead.getBankExternalId();
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return mapBankCode;
	}

	// external bank branch codes
	public String fetchBankBranchCodeHomeSend(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId,BigDecimal beneBankBranchId){
		String mapBankBranchCode = null;
		try {
			List<BankExternalReferenceDetail> lstBankExtRefBranchDetails = partnerTransactionDao.fetchBankExternalReferenceBranchDetails(countryId, corBankId, beneBankId, beneBankBranchId);
			if(lstBankExtRefBranchDetails != null && lstBankExtRefBranchDetails.size() == 1) {
				BankExternalReferenceDetail bankExternalReferenceDetail = lstBankExtRefBranchDetails.get(0);
				mapBankBranchCode = bankExternalReferenceDetail.getBankBranchExternalId();
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}

		return mapBankBranchCode;
	}

	// codes for the bic and bank code
	public HashMap<String, Integer> fetchBICandBankCodeData() {
		HashMap<String, Integer> mapBankCode = new HashMap<String, Integer>();

		mapBankCode.put("SWIFT", 0);
		mapBankCode.put("BANK_CODE", 1);
		mapBankCode.put("BRANCH_CODE", 2);
		mapBankCode.put("BRANCH_NAME", 3);

		return mapBankCode;
	}

	// parameter for 0-swift , 1-bank code , 2-branch code and 3-branch name
	public HashMap<String, String> fetchBICandBankCode(String beneCountryCode) {
		HashMap<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("BIC_CODE", null);
		mapParam.put("BANK_BRANCH", null);
		ParameterDetails parameterDetails = partnerTransactionDao.fetchServPrvBankCode(PricerServiceConstants.PARAM_BIC_BRANCH,beneCountryCode);
		if(parameterDetails != null) {
			mapParam.put("BIC_CODE", parameterDetails.getCharField3());
			mapParam.put("BANK_BRANCH", parameterDetails.getCharField4());
		}
		return mapParam;
	}

	public CountryMaster fetchCountryMasterDetails(BigDecimal countryId) {
		CountryMaster countryMaster = partnerTransactionDao.getCountryMasterDetails(countryId);
		return countryMaster;
	}

	// check the beneficiary country is available in Param. Don't pass Bene address
	public boolean checkBeneCountryParam(String beneCountryCode){
		boolean status = Boolean.FALSE;
		ParameterDetails parameterDetails = partnerTransactionDao.fetchBeneCountryBeneAddressNotReq(PricerServiceConstants.PARAM_BENE_ADDRESS, beneCountryCode);
		if(parameterDetails != null){
			status = Boolean.TRUE;
		}
		return status;
	}

	public Map<BigDecimal,SrvProvBeneficiaryTransactionDTO> fetchTransactionDetails(BigDecimal collectionDocYear,BigDecimal collectionDocNumber,BigDecimal customerId) {
		List<String> dupcheck = new ArrayList<>();
		BigDecimal docFinanceYear = null;
		BigDecimal docNo = null;
		BigDecimal beneficiaryRelationShipId = null;
		BigDecimal remitTrnxId = null;

		Map<BigDecimal,SrvProvBeneficiaryTransactionDTO> mapTrnxPartnerData = new HashMap<BigDecimal,SrvProvBeneficiaryTransactionDTO>();

		List<TransactionDetailsView> lstTrnxDetails = partnerTransactionDao.fetchTrnxSPDetails(customerId,collectionDocYear,collectionDocNumber);

		for (TransactionDetailsView transactionDetailsView : lstTrnxDetails) {
			if(transactionDetailsView.getBankCode().equalsIgnoreCase(SERVICE_PROVIDER_BANK_CODE.HOME.name())) {
				String docYearNo = transactionDetailsView.getDocumentFinanceYear().toString()+transactionDetailsView.getDocumentNo();
				if(!dupcheck.contains(docYearNo)) {
					dupcheck.add(docYearNo);
					docFinanceYear = transactionDetailsView.getDocumentFinanceYear();
					docNo = transactionDetailsView.getDocumentNo();
					beneficiaryRelationShipId = transactionDetailsView.getBeneficiaryRelationShipId();
					remitTrnxId = transactionDetailsView.getRemittanceTransactionId();

					List<TransactionDetailsView> lstTrnxWiseDetails = partnerTransactionDao.fetchTrnxWiseDetails(customerId,docFinanceYear,docNo);
					RemittanceTransactionPartnerDTO trnxWiseData = fetchTransactionWiseDetails(lstTrnxWiseDetails,collectionDocYear,collectionDocNumber);

					BeneficiaryDetailsDTO beneficiaryDetailsDTO = fetchBeneficiaryDetails(customerId, beneficiaryRelationShipId);

					SrvProvBeneficiaryTransactionDTO srvProvBeneficiaryTransactionDTO = new SrvProvBeneficiaryTransactionDTO();
					srvProvBeneficiaryTransactionDTO.setBeneficiaryDetailsDTO(beneficiaryDetailsDTO);
					srvProvBeneficiaryTransactionDTO.setRemittanceTransactionPartnerDTO(trnxWiseData);

					mapTrnxPartnerData.put(remitTrnxId, srvProvBeneficiaryTransactionDTO);
				}
			}
		}

		return mapTrnxPartnerData;
	}

	public RemittanceTransactionPartnerDTO fetchTransactionWiseDetails(List<TransactionDetailsView> lstTrnxWiseDetails,BigDecimal docYear,BigDecimal docNumber) {
		RemittanceTransactionPartnerDTO transactionPartnerRequest = null;
		List<String> dupcheck = new ArrayList<>();

		for (TransactionDetailsView transactionDetailsView : lstTrnxWiseDetails) {
			String docYearNo = transactionDetailsView.getDocumentFinanceYear().toString()+transactionDetailsView.getDocumentNo();
			if(!dupcheck.contains(docYearNo)) {
				dupcheck.add(docYearNo);

				transactionPartnerRequest = new RemittanceTransactionPartnerDTO();

				transactionPartnerRequest.setRemittanceMode(transactionDetailsView.getRemittanceCode());
				transactionPartnerRequest.setDeliveryMode(transactionDetailsView.getDeliveryCode());
				transactionPartnerRequest.setRoutingBankId(transactionDetailsView.getBankId());
				transactionPartnerRequest.setRoutingBankCode(transactionDetailsView.getBankCode());
				transactionPartnerRequest.setDestinationAmount(transactionDetailsView.getForeignTrnxAmount());

				transactionPartnerRequest.setSettlementAmount(transactionDetailsView.getSettlementAmount());

				String paymentType = assigningPaymentMode(transactionDetailsView.getCollDocumentFinanceYear(),transactionDetailsView.getCollDocumentNo(),transactionDetailsView.getCollDocumentCode());
				transactionPartnerRequest.setTrnxCollectionType(paymentType);

				if(transactionDetailsView.getDocumentFinanceYear() != null && transactionDetailsView.getDocumentNo() != null){
					String str = String.format("%08d", transactionDetailsView.getDocumentNo().intValue());
					transactionPartnerRequest.setOutGoingTransactionReference(removeSpaces(transactionDetailsView.getDocumentFinanceYear().toString()+str));
				}
				transactionPartnerRequest.setPartnerTransactionReference(transactionDetailsView.getPartnerSessionId());
				transactionPartnerRequest.setRequestSequenceId(transactionDetailsView.getAmgSessionId().toString());
				//transactionPartnerRequest.setSourceOfFundDesc(transactionDetailsView.getSourceOfIncomeDesc());
				transactionPartnerRequest.setSourceOfFundDesc(transactionDetailsView.getBankSourceOfFund());
				transactionPartnerRequest.setFurtherInstruction(transactionDetailsView.getFurtherInstruction());

			}

			if(transactionDetailsView.getFlexField() != null && transactionDetailsView.getFlexField().equalsIgnoreCase(AmxDBConstants.INDIC1)) {
				transactionPartnerRequest.setPurposeOfTransaction_Indic1(transactionDetailsView.getFlexFieldValue());
			}else if(transactionDetailsView.getFlexField() != null && transactionDetailsView.getFlexField().equalsIgnoreCase(AmxDBConstants.INDIC2)) {
				transactionPartnerRequest.setRoutingNumber_Indic2(transactionDetailsView.getFlexFieldValue());
			}else if(transactionDetailsView.getFlexField() != null && transactionDetailsView.getFlexField().equalsIgnoreCase(AmxDBConstants.INDIC3)) {
				transactionPartnerRequest.setBsbNumber_Indic3(transactionDetailsView.getFlexFieldValue());
			}
		}

		return transactionPartnerRequest;
	}

	public  AdditionalBankRuleAmiec getBankRuleAmiecDescription(BigDecimal purposeOfTrnxId) {
		AdditionalBankRuleAmiec amiec = amiecBankRuleRepo.findOne(purposeOfTrnxId);
		return amiec;
	}

	public AmiecAndBankMapping getAmicAndBankMapping(BigDecimal bankCountryId,BigDecimal bankId,AdditionalBankRuleAmiec amiecDetails) {
		return amiecAndBankMappingRepository.fetchAmiecBankData(bankCountryId,bankId,amiecDetails.getFlexField(),amiecDetails.getAmiecCode(),ConstantDocument.Yes);
	}

	// checking the payment mode 
	public String assigningPaymentMode(BigDecimal collectionDocYear,BigDecimal collectionDocNumber,BigDecimal collectionDocCode){
		String paymentMode = null;
		List<String> lstPayment = new ArrayList<String>();
		HashMap<Integer, String> mapPayment = new HashMap<Integer, String>();
		mapPayment.put(1, paymentMode);
		mapPayment.put(2, paymentMode);
		mapPayment.put(3, paymentMode);
		mapPayment.put(4, paymentMode);

		List<CollectionDetailViewModel> collectionDetailList = collectionDetailViewDao.getCollectionDetailView(metaData.getCompanyId(),collectionDocNumber,collectionDocYear,collectionDocCode);

		if(collectionDetailList != null){
			for (CollectionDetailViewModel data : collectionDetailList) {
				if(!lstPayment.contains(data.getCollectionMode())){
					lstPayment.add(data.getCollectionMode());
				}
			}

			if(lstPayment != null && lstPayment.size() != 0){
				for (String payMode : lstPayment) {
					if(payMode.equalsIgnoreCase(AmxDBConstants.CHEQUE)){
						paymentMode = AmxDBConstants.HOME_SEND_PAYMENT_TYPE_CHEQUE;
						mapPayment.put(1, paymentMode);
					}else if(payMode.equalsIgnoreCase(AmxDBConstants.BANK_TRANSFER)){
						paymentMode = AmxDBConstants.HOME_SEND_PAYMENT_TYPE_BANK_TRANSFER;
						mapPayment.put(2, paymentMode);
					}else if(payMode.equalsIgnoreCase(AmxDBConstants.KNET_CODE)){
						paymentMode = AmxDBConstants.HOME_SEND_PAYMENT_TYPE_KNET;
						mapPayment.put(3, paymentMode);
					}else if(payMode.equalsIgnoreCase(AmxDBConstants.CASH)){
						paymentMode = AmxDBConstants.HOME_SEND_PAYMENT_TYPE_CASH;
						mapPayment.put(4, paymentMode);
					}else{
						// not the one need
					}
				}
			}

			paymentMode = mapPayment.get(1) != null ? mapPayment.get(1) : (mapPayment.get(2) != null ? mapPayment.get(2) : (mapPayment.get(3) != null ? mapPayment.get(3) : (mapPayment.get(4) != null ? mapPayment.get(4) : null)));
		}

		return paymentMode;
	}

	// fetch the payment Limits
	public PaymentModeLimitsView fetchPaymentLimits(BigDecimal bankId,BigDecimal currencyId,BigDecimal customerTypeFrom,BigDecimal customerTypeTo,BigDecimal amount) {
		PaymentModeLimitsView paymentModeLimitsView = null;
		List<PaymentModeLimitsView> lstpaymentLimit = partnerTransactionDao.fetchPaymentLimitDetails(bankId, currencyId, customerTypeFrom, customerTypeTo);
		// check the usd amount
		if(lstpaymentLimit != null && lstpaymentLimit.size() == 1) {
			paymentModeLimitsView = lstpaymentLimit.get(0);
		}
		return paymentModeLimitsView;
	}

	public void validateTransactionData(BigDecimal collectionDocYear,BigDecimal collectionDocNumber,CustomerDetailsDTO customerDetailsDTO,BeneficiaryDetailsDTO beneficiaryDetailsDTO) {

		if(null == collectionDocYear) {
			// fail
		}

		if(null == collectionDocNumber) {
			// fail
		}

		if(null == customerDetailsDTO) {
			// fail
		}

		if(null == beneficiaryDetailsDTO) {
			// fail
		}

	}

	// iterate the response and insert the log table
	public void fetchServiceProviderData(ServiceProviderCallRequestDto serviceProviderCallRequestDto,AmxApiResponse<ServiceProviderResponse, Object> srvPrvResp) {
		String requestXml = null;
		String responseXml = null;
		String referenceNo = null;
		String partnerReference = null;
		String trnxType = PricerServiceConstants.SEND_TRNX;
		BigDecimal reqSeq = new BigDecimal(3);
		BigDecimal resSeq = new BigDecimal(4);

		if(serviceProviderCallRequestDto != null) {
			if(serviceProviderCallRequestDto.getTransactionDto() != null &&  serviceProviderCallRequestDto.getTransactionDto().getOut_going_transaction_reference() != null) {
				referenceNo = serviceProviderCallRequestDto.getTransactionDto().getOut_going_transaction_reference();
			}
			if(srvPrvResp.getResult().getRequest_XML() != null) {
				requestXml = srvPrvResp.getResult().getRequest_XML();
				saveServiceProviderXml("feeEnquiryReq", requestXml, referenceNo, reqSeq, PricerServiceConstants.REQUEST, trnxType, partnerReference,serviceProviderCallRequestDto);
			}
			if(srvPrvResp.getResult().getResponse_XML() != null) {
				responseXml = srvPrvResp.getResult().getResponse_XML();
				saveServiceProviderXml("feeEnquiryRes", responseXml, referenceNo, resSeq, PricerServiceConstants.RESPONSE, trnxType, partnerReference,serviceProviderCallRequestDto);
			}
		}
	}

	public void saveServiceProviderXml(String filename, String content,String referenceNo,BigDecimal seq,String xmlType,String trnxType,String bene_Bank_Txn_Ref,ServiceProviderCallRequestDto serviceProviderCallRequestDto) {

		try {

			/*ServiceProviderXmlLog serviceProviderXmlLog = new ServiceProviderXmlLog();

			serviceProviderXmlLog.setApplicationCountryId(srvPrvFeeInqReqDTO.getApplicationCountryId());
			serviceProviderXmlLog.setCompanyId(srvPrvFeeInqReqDTO.getCompanyId());
			serviceProviderXmlLog.setCountryBranchId(srvPrvFeeInqReqDTO.getCountryBranchId());
			//serviceProviderXmlLog.setCreatedBy(createdBy);
			serviceProviderXmlLog.setCreatedDate(new Date());
			serviceProviderXmlLog.setCustomerId(srvPrvFeeInqReqDTO.getCustomerId());
			//serviceProviderXmlLog.setCustomerReference(customerReference);
			//serviceProviderXmlLog.setEmosBranchCode(emosBranchCode);
			serviceProviderXmlLog.setFileName(filename);
			//serviceProviderXmlLog.setForeignTerminalId(foreignTerminalId);
			//serviceProviderXmlLog.setIdentifier(identifier);
			serviceProviderXmlLog.setMtcNo(bene_Bank_Txn_Ref);
			if(referenceNo != null) {
				serviceProviderXmlLog.setRefernceNo(new BigDecimal(referenceNo));
			}
			serviceProviderXmlLog.setSequence(seq);
			serviceProviderXmlLog.setTransactionType(trnxType);
			serviceProviderXmlLog.setXmlData(IoUtils.stringToClob(content));
			serviceProviderXmlLog.setXmlType(xmlType);*/

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
