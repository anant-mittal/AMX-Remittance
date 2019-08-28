package com.amx.amxlib.model.response;

import java.io.IOException;
import java.util.List;

import com.amx.amxlib.meta.model.AccountTypeDto;
import com.amx.amxlib.meta.model.AddAdditionalBankDataDto;
import com.amx.amxlib.meta.model.ApplicationSetupDTO;
import com.amx.amxlib.meta.model.AuthenticationLimitCheckDTO;
import com.amx.amxlib.meta.model.BeneCountryDTO;
import com.amx.amxlib.meta.model.BranchDetailDTO;
import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.meta.model.EmployeeDetailsDTO;
import com.amx.amxlib.meta.model.JaxMetaParameter;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.RemittancePageDto;
import com.amx.amxlib.meta.model.RemittanceReceiptSubreport;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.meta.model.TransactionHistroyDTO;
import com.amx.amxlib.meta.model.UserDetailsDTO;
import com.amx.amxlib.meta.model.UserFinancialYearDTO;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.meta.model.WhyDoAskInformationDTO;
import com.amx.amxlib.model.BeneAccountModel;
import com.amx.amxlib.model.BeneRelationsDescriptionDto;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.CustomerNotificationDTO;
import com.amx.amxlib.model.JaxConditionalFieldDto;
import com.amx.amxlib.model.MinMaxExRateDTO;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.PlaceOrderDTO;
import com.amx.amxlib.model.PlaceOrderNotificationDTO;
import com.amx.amxlib.model.RateAlertDTO;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.amxlib.model.trnx.BeneficiaryTrnxModel;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.model.BeneficiaryListDTO;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.response.BankMasterDTO;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.SourceOfIncomeDto;
import com.amx.jax.model.response.benebranch.BankBranchDto;
import com.amx.jax.model.response.fx.FxExchangeRateDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.remittance.RemittanceTransactionResponsetModel;
import com.amx.jax.payg.PaymentResponseDto;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@Deprecated
public class ResponseDataDeserializer extends StdDeserializer<ResponseData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ResponseDataDeserializer(Class<?> vc) {
		super(vc);
	}

	public ResponseDataDeserializer() {
		this(null);
	}

	@Override
	public ResponseData deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		String type = node.get("type").asText();
		String values = node.get("values").toString();
		ResponseData responseData = new ResponseData();
		responseData.setMetaInfo(0);
		responseData.setType(type);
		List<Object> models = null;
		switch (type) {
		case "user":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserModel>>() {
			});
			break;
		case "otp":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CivilIdOtpModel>>() {
			});
			break;
		case "customer":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CustomerModel>>() {
			});
			break;
		case "appl_country":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ApplicationSetupDTO>>() {
			});
			break;
		case "country":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CountryMasterDTO>>() {
			});
			break;	
		case "quest":
			models = new ObjectMapper().readValue(values, new TypeReference<List<QuestModelDTO>>() {
			});
			break;	
			
		case "terms":
			models = new ObjectMapper().readValue(values, new TypeReference<List<TermsAndConditionDTO>>() {
			});
			break;		
			
		case "why":
			models = new ObjectMapper().readValue(values, new TypeReference<List<WhyDoAskInformationDTO>>() {
			});
			break;		
			
		case "fyear":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserFinancialYearDTO>>() {
			});
			break;	
			
		case "parameter":
			models = new ObjectMapper().readValue(values, new TypeReference<List<AuthenticationLimitCheckDTO>>() {
			});
			break;		
				
		case "checklist":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserVerificationCheckListDTO>>() {
			});
			break;	
		case "boolean_response":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BooleanResponse>>() {
			});
			break;
			
		case "trnxHist":
			models = new ObjectMapper().readValue(values, new TypeReference<List<TransactionHistroyDTO>>() {
			});
			break;	
		case "remitReport":
			models = new ObjectMapper().readValue(values, new TypeReference<List<RemittanceReceiptSubreport>>() {
			});
			break;	

		case "ex_rate":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ExchangeRateResponseModel>>() {
			});
			break;	
		case "currencyMaster":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CurrencyMasterDTO>>() {
			});
			break;
		case "bankmaster":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BankMasterDTO>>() {
			});
			break;
		case "beneList":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BeneficiaryListDTO>>() {
			});
			break;		
			
		case "benecountry":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BeneCountryDTO>>() {
			});
			break;	
		case "district":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ViewDistrictDto>>() {
			});
			break;
		case "state":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ViewStateDto>>() {
			});
			break;
			
		case "accountType":
			models = new ObjectMapper().readValue(values, new TypeReference<List<AccountTypeDto>>() {
			});
			break;	
			
		case "city":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ViewCityDto>>() {
			});
			break;	
		case "remittance_transaction":
			models = new ObjectMapper().readValue(values,
					new TypeReference<List<RemittanceTransactionResponsetModel>>() {
					});
			break;		
		case "sourceofincome":
			models = new ObjectMapper().readValue(values, new TypeReference<List<SourceOfIncomeDto>>() {
			});
			break;
			
		case "additionaldata":
			models = new ObjectMapper().readValue(values, new TypeReference<List<AddAdditionalBankDataDto>>() {
			});
			break;	

		case "remittance-application":
			models = new ObjectMapper().readValue(values,
					new TypeReference<List<RemittanceApplicationResponseModel>>() {
					});
			break;

		case "purpose-of-txn":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PurposeOfTransactionModel>>() {
			});
			break;
			
		case "remittance-page-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<RemittancePageDto>>() {
			});
			break;
			
		case "rate-alert-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<RateAlertDTO>>() {
			});
			break;	
		case "remittance-transaction-status-model":
			models = new ObjectMapper().readValue(values,
					new TypeReference<List<RemittanceTransactionStatusResponseModel>>() {
					});
			break;
		case "customer-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CustomerDto>>() {
			});
			break;
		case "pg_remit_response":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PaymentResponseDto>>() {
			});
			break;
		case "online-config":
			models = new ObjectMapper().readValue(values, new TypeReference<List<OnlineConfigurationDto>>() {
			});
			break;
		case "bank-branch-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BankBranchDto>>() {
			});
			break;
		case "bene-relation-desc":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BeneRelationsDescriptionDto>>() {
			});
			break;
		case "bene-account-details":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BeneAccountModel>>() {
			});
			break;
		case "jax-field-rules":
			models = new ObjectMapper().readValue(values, new TypeReference<List<JaxConditionalFieldDto>>() {
			});
			break;
		case "jax-trnx-response":
			models = new ObjectMapper().readValue(values, new TypeReference<List<JaxTransactionResponse>>() {
			});
			break;
		case "bene-trnx-model":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BeneficiaryTrnxModel>>() {
			});
			break;
		case "service-group-model":
			models = new ObjectMapper().readValue(values, new TypeReference<List<ServiceGroupMasterDescDto>>() {
			});
			break;
		case "routingBankMaster":
			models = new ObjectMapper().readValue(values, new TypeReference<List<RoutingBankMasterDTO>>() {
			});
			break;	
		case "jaxmetaparameter":
			models = new ObjectMapper().readValue(values, new TypeReference<List<JaxMetaParameter>>() {
			});
			break;	
		case "send-otp-model":
			models = new ObjectMapper().readValue(values, new TypeReference<List<SendOtpModel>>() {
			});
			break;	
		case "prefix":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PrefixDTO>>() {
			});
			break;	
		case "branch-detail":
			models = new ObjectMapper().readValue(values, new TypeReference<List<BranchDetailDTO>>() {
			});
			break;
		case "place-order-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PlaceOrderDTO>>() {
			});
			break;	
		case "min-max-exrate":
			models = new ObjectMapper().readValue(values, new TypeReference<List<MinMaxExRateDTO>>() {
			});
			break;
		case "place-order-not-dto":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PlaceOrderNotificationDTO>>() {
			});
			break;	
		case "jax-push-notification":
			models = new ObjectMapper().readValue(values, new TypeReference<List<CustomerNotificationDTO>>() {
			});
			break;	
		case "employee-detail":
			models = new ObjectMapper().readValue(values, new TypeReference<List<EmployeeDetailsDTO>>() {
			});
			break;
		case "user-detail":
			models = new ObjectMapper().readValue(values, new TypeReference<List<UserDetailsDTO>>() {
			});
			break;
		case "fx-purpose-of-trnx":
			models = new ObjectMapper().readValue(values, new TypeReference<List<PurposeOfTransactionDto>>() {
			});
			break;
		case "fc_sale_xrate":
			models = new ObjectMapper().readValue(values, new TypeReference<List<FxExchangeRateDto>>() {
			});
			break;
			
			
			
			
	}
		responseData.setValues(models);
		return responseData;
	}

}
