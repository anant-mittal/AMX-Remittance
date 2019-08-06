package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.request.RemittanceTransactionStatusRequestModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.amxlib.model.response.RemittanceTransactionStatusResponseModel;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.client.compliance.ComplianceBlockedTrnxType;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dao.RemittanceApplicationDao;
import com.amx.jax.dao.RemittanceProcedureDao;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.dbmodel.SourceOfIncomeView;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.dbmodel.remittance.RemittanceTransaction;
import com.amx.jax.exrateservice.service.NewExchangeRateService;
import com.amx.jax.manager.RemittanceTransactionManager;
import com.amx.jax.model.request.remittance.RemittanceTransactionDrRequestModel;
import com.amx.jax.model.request.remittance.RemittanceTransactionRequestModel;
import com.amx.jax.model.response.ExchangeRateBreakup;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PayGModel;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.repository.ISourceOfIncomeDao;
import com.amx.jax.repository.RemittanceTransactionRepository;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.CurrencyMasterService;
import com.amx.jax.userservice.service.UserService;
import com.amx.libjax.model.postman.SuspiciousTransactionPaymentDto;

@Service
@SuppressWarnings("rawtypes")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceTransactionService extends AbstractService {

	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;

	@Autowired
	RemittanceTransactionManager remittanceTxnManger;

	@Autowired
	ISourceOfIncomeDao sourceOfIncomeDao;
	@Autowired
	BeneficiaryService beneficiaryService; 
	@Autowired
	CurrencyMasterService currencyMasterService ; 
	@Autowired
	NewExchangeRateService newExchangeRateService;  
	@Autowired
	RemittanceApplicationDao remittanceApplicationDao;
	@Autowired
	RemittanceApplicationService remittanceApplicationService ; 
	@Autowired
	UserService userSerivce;
	@Autowired
	CountryService countryService;
	@Autowired
	RemittanceProcedureDao  remittanceProcedureDao;
	@Autowired
	RemittanceTransactionRepository remittanceTransactionRepository;
	
	public ApiResponse getRemittanceTransactionDetails(BigDecimal collectionDocumentNo, BigDecimal fYear,
			BigDecimal collectionDocumentCode) {

		List<RemittanceTransactionView> transctionDetail = remittanceTransactionDao
				.getRemittanceTransaction(collectionDocumentNo, fYear, collectionDocumentCode);
		ApiResponse response = getBlackApiResponse();
		if (transctionDetail.isEmpty()) {
			throw new GlobalException("Transaction details not avaliable");
		} else {
			response.getData().getValues().addAll(transctionDetail);
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("remittance_transaction");
		return response;

	}

	public ApiResponse getSourceOfIncome(BigDecimal languageId) {

		List<SourceOfIncomeView> sourceOfIncomeList = sourceOfIncomeDao.getSourceofIncome(languageId);
		ApiResponse response = getBlackApiResponse();
		if (sourceOfIncomeList.isEmpty()) {
			throw new GlobalException("No data found");
		} else {
			response.getData().getValues().addAll(convertSourceOfIncome(sourceOfIncomeList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("sourceofincome");
		return response;
	}

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public ApiResponse validateRemittanceTransaction(RemittanceTransactionRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceTransactionResponsetModel responseModel  = remittanceTxnManger.validateTransactionData(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(model.getModelType());
		return response;

	}
	
	
	
	public ApiResponse validateRemittanceTransactionV2(RemittanceTransactionDrRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceTransactionResponsetModel responseModel  = remittanceTxnManger.validateTransactionDataV2(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(model.getModelType());
		return response;

	}
	

	public List<SourceOfIncomeDto> convertSourceOfIncome(List<SourceOfIncomeView> sourceOfIncomeList) {
		List<SourceOfIncomeDto> list = new ArrayList<>();
		for (SourceOfIncomeView model : sourceOfIncomeList) {
			SourceOfIncomeDto dto = new SourceOfIncomeDto();
			dto.setSourceofIncomeId(model.getSourceofIncomeId());
			dto.setShortDesc(model.getShortDesc());
			dto.setLanguageId(model.getLanguageId());
			dto.setDescription(model.getDescription());
			list.add(dto);
		}
		return list;

	}

	public ApiResponse saveApplication(RemittanceTransactionRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceApplicationResponseModel responseModel = remittanceTxnManger.saveApplication(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(responseModel.getModelType());
		return response;
	}

	
	public ApiResponse saveApplicationV2(RemittanceTransactionDrRequestModel model) {
		ApiResponse response = getBlackApiResponse();
		RemittanceApplicationResponseModel responseModel = remittanceTxnManger.saveApplicationV2(model);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(responseModel.getModelType());
		return response;
	}
	
	public ApiResponse saveRemittance(PayGModel paymentResponseDto) {
		ApiResponse response = getBlackApiResponse();
		return response;

	}

	public ApiResponse getTransactionStatus(RemittanceTransactionStatusRequestModel request) {
		ApiResponse response = getBlackApiResponse();
		RemittanceTransactionStatusResponseModel responseModel = remittanceTxnManger.getTransactionStatus(request);
		response.getData().getValues().add(responseModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(responseModel.getModelType());
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public ApiResponse<RemittanceTransactionResponsetModel> calcEquivalentAmount(
@RequestBody RemittanceTransactionRequestModel model) {
		ApiResponse<RemittanceTransactionResponsetModel> response = getBlackApiResponse();
		RemittanceTransactionResponsetModel respModel = remittanceTxnManger.validateTransactionData(model);
		BigDecimal fcCurrencyId = beneficiaryService.getBeneByIdNo(model.getBeneId()).getCurrencyId();
		BigDecimal fcDecimalNumber = currencyMasterService.getCurrencyMasterById(fcCurrencyId).getDecinalNumber();
		if (model.getDomXRate() == null) {
			model.setDomXRate(respModel.getExRateBreakup().getRate());
		}
		ExchangeRateBreakup exRateBreakup = newExchangeRateService.calcEquivalentAmount(model,fcDecimalNumber.intValue());
		exRateBreakup.setFcDecimalNumber(respModel.getExRateBreakup().getFcDecimalNumber());
		exRateBreakup.setLcDecimalNumber(respModel.getExRateBreakup().getLcDecimalNumber());
		respModel.setExRateBreakup(exRateBreakup);
		response.getData().getValues().add(respModel);
		response.setResponseStatus(ResponseStatus.OK);
		response.getData().setType(respModel.getModelType());
		return response;
	}

	public SuspiciousTransactionPaymentDto getSuspiciousTransactionPaymentDto(BigDecimal remittanceApplicationId,Long noOfAttempts) {
		SuspiciousTransactionPaymentDto dto = new SuspiciousTransactionPaymentDto();
		BenificiaryListView beneView = getBeneBybeneficiaryView(remittanceApplicationId);
		dto.setBankName(beneView.getBankName());
		dto.setBeneBankName(beneView.getBankName());
		dto.setBeneName(beneView.getBenificaryName());
		dto.setCountryName(beneView.getCountryName());
		Customer customer = userSerivce.getCustById(beneView.getCustomerId());
		dto.setCustomerEmailId(customer.getEmail());
		dto.setCustomerMobile(customer.getMobile());
		dto.setNationalityName(
				countryService.getCountryMasterDesc(customer.getNationalityId(), BigDecimal.ONE).getCountryName());
		dto.setNoOfAttempts(noOfAttempts);
		dto.setProduct(beneView.getServiceGroupId().equals(BigDecimal.ONE) ? "CASH" : "BANK");
		dto.setRemitterName(customer.getFirstName() + " " + customer.getLastName());
		dto.setRemitterReferenceNo(customer.getIdentityInt());
		return dto;
	}
	
	public RemittanceTransaction getRemittanceTransactionById(BigDecimal remittanceTransactionId) {
		return remittanceApplicationDao.getRemittanceTransactionById(remittanceTransactionId);
	}
	
	public BenificiaryListView getBeneBybeneficiaryView(BigDecimal remittanceApplicationId) {

		RemittanceApplication remittanceApplication = remittanceApplicationService
				.getRemittanceApplicationById(remittanceApplicationId);
		BigDecimal beneficiaryRelationShipSeqId = remittanceApplication.getExRemittanceAppBenificiary().get(0)
				.getBeneficiaryRelationShipSeqId();
		return beneficiaryService.getBeneBybeneficiaryRelationShipSeqId(beneficiaryRelationShipSeqId);
	}
	
	@Transactional
	public void clearHighValueTransaction(BigDecimal remittanceTransactionId, ComplianceBlockedTrnxType trnxType) {
		RemittanceTransaction trnx = getRemittanceTransactionById(remittanceTransactionId);
		Map<String, Object> inputValues = new HashMap<>();
		inputValues.put("P_REMITTANCE_TRANSACTION_ID", remittanceTransactionId);
		inputValues.put("P_REMARKS", "");
		inputValues.put("P_AUTHORIZED_BY", remittanceTransactionId);
		inputValues.put("P_HIGH_VALUE_TRANX", trnxType.equals(ComplianceBlockedTrnxType.HVT_LOCAL) ? "Y" : "N");
		inputValues.put("P_FC_HIGH_VALUE_TRANX", trnxType.equals(ComplianceBlockedTrnxType.HVT_FC) ? "Y" : "N");
		switch (trnxType) {
		case HVT_FC:
		case HVT_LOCAL:
			remittanceProcedureDao.updateAmlAuth(inputValues);
			break;
		case SUSPICIOUS:
			trnx.setSuspicousTransaction(ConstantDocument.No);
			remittanceTransactionRepository.save(trnx);
			break;
		}
	}
}
