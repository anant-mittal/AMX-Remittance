package com.amx.service_provider.api_gates.vintaja;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.Get_Rmittance_Details_Call_Response;
import com.amx.jax.model.response.serviceprovider.Remittance_Call_Response;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.jax.model.response.serviceprovider.Status_Call_Response;
import com.amx.service_provider.api_gates.common.Common_API_Utils;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class VintajaUtils
{
	@Autowired
	static com.amx.service_provider.repository.webservice.ExOwsLoginCredentialsRepository exOwsLoginCredentialsRepository;

	private final static String DATE_FORMAT_IN_USE = "yyyy-MM-dd";
	private final static String SEND_TXN_METHOD_IND = new String("2"),
			VALIDATE_SEND_TXN_INPUTS_METHOD_IND = new String("13"),
			GET_REMITTANCE_DETAILS_METHOD_IND = new String("12"), STATUS_INQ_METHOD_IND = new String("3");

	public static String form_api_call_input(TransactionData txn_data, Customer customer_data, Benificiary bene_data,
			ExOwsLoginCredentials owsLoginCredentialsObject, String ws_call_type) throws Exception
	{
		GovermantPaymentServices target_payment_service =
				get_goverment_payment_service(txn_data.getRemittance_mode(), txn_data.getDelivery_mode());

		String api_request =
				"{\"id\":\"" +
						owsLoginCredentialsObject.getWsAgentId() +
						"\"," +
						"\"uid\":\"" +
						owsLoginCredentialsObject.getWsUserName() +
						"\"," +
						"\"pwd\":\"" +
						owsLoginCredentialsObject.getWsPassword() +
						"\"," +
						"\"code\":" +
						target_payment_service.getService_int_value() +
						"," +
						"\"countryCode\":\"" +
						txn_data.getApplication_country_2_digit_ISO() +
						"\",";

		String data = "\"data\":{";

		if (ws_call_type.equals(VALIDATE_SEND_TXN_INPUTS_METHOD_IND) || ws_call_type.equals(SEND_TXN_METHOD_IND))
		{
			if (target_payment_service == GovermantPaymentServices.PHIL_HEALTH_YEAR_WISE)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.PHIL_HEALTH_MONTH_WISE)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +

								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"startMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_SAVING)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"startMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"endYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"endMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_MP2)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"startMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"endYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"endMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"mp2\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_SHORT_TERM_LOAN)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_HOUSE_LOAN)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"startMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"endYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"endMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"loanNumber\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_NEW_PRN)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +

								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"startYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"startMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_start_date()) +
								"\"," +
								"\"endYear\":\"" +
								new SimpleDateFormat("yyyy").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"endMonth\":\"" +
								new SimpleDateFormat("MM").format(txn_data.getCoverage_end_date()) +
								"\"," +
								"\"flexiFund\":" +
								txn_data.getFlexi_field_1() +
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_EXISTING_PRN)
			{
				data +=
						"\"prn\":\"" +
								txn_data.getPartner_transaction_reference() +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_SHORT_TERM_LOAN)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_REAL_ESTATE_LOAN)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +
								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"loanNumber\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_MISCELLANOUS)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"lastName\":\"" +
								bene_data.getLast_name() +
								"\"," +
								"\"firstName\":\"" +
								bene_data.getFirst_name() +

								"\"," +
								"\"middleName\":\"" +
								"" +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"," +
								"\"mobileNumber\":\"" +
								bene_data.getContact_no() +
								"\"," +
								"\"emailAddress\":\"" +
								bene_data.getEmail() +
								"\"," +
								"\"memberType\":" +
								bene_data.getPartner_beneficiary_type() +
								"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"paymentType\":" +
								txn_data.getFlexi_field_2() +
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.BILL_PAYMENTS)
			{
				data +=
						"\"billerCode\":\"" +
								bene_data.getBeneficiary_bank_code() + // TODO: Check with Kanmani, refer to page 112 in
																		// API
								// document. There is type and sub-type selection to
								// reach to the target biller code
								"\"," +
								"\"accountNumber\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," +
								"\"identifier\":\"" +
								bene_data.getFirst_name() +
								"\"," + // TODO: Changeable as per biller, check the identifier for each biller and find a way to pass the require value correctly.
								// account
								// name, mobile number or contact
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.TOP_UP_WALLET)
			{
				data +=
						"\"payremitNo\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," + // Member’s assigned
								// PayRemit number
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"currencyCode\":\"" +
								txn_data.getDestination_currency() +
								"\"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
			else if (target_payment_service == GovermantPaymentServices.RESERVATION_PAYMENT)
			{
				data +=
						"\"reservationNumber\":\"" +
								bene_data.getBeneficiary_account_number() +
								"\"," +
								"\"amount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() +
								"," +
								"\"currencyCode\":\"" +
								txn_data.getDestination_currency() +
								"\"," +
								"\"settlementCurrencyCode\":\"" +
								txn_data.getDestination_currency() + // TODO: Check what is the settlement currency with
																		// OP
								"\"," +
								"\"settlementAmount\":" +
								txn_data.getDestination_amount().setScale(2, BigDecimal.ROUND_HALF_EVEN)
										.stripTrailingZeros().toPlainString() + // TODO: Check
								// what is the
								// settlement
								// amount with
								// OP
								"," +
								"\"agentRefNo\":\"" +
								txn_data.getOut_going_transaction_reference() +
								"\"";
			}
		}
		else if (ws_call_type.equals(GET_REMITTANCE_DETAILS_METHOD_IND))
		{
			if (
				txn_data.getPartner_transaction_reference() != null &&
						txn_data.getPartner_transaction_reference().length() != 0
			)
			{
				target_payment_service = GovermantPaymentServices.SSS_CONTRIBUTION_SERACH_BY_PRN;
			}
			else if (
				bene_data.getPartner_beneficiary_id() != null && bene_data.getPartner_beneficiary_id().length() != 0
			)
			{
				target_payment_service = GovermantPaymentServices.SSS_CONTRIBUTION_SERACH_BY_MEMBER_ID;
			}
			else
			{
				return null;
			}

			if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_SERACH_BY_PRN)
			{
				data += "\"prn\":\"" + txn_data.getPartner_transaction_reference() + "\"";
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_SERACH_BY_MEMBER_ID)
			{
				data +=
						"\"memberId\":\"" +
								bene_data.getPartner_beneficiary_id() +
								"\"," +
								"\"birthDate\":\"" +
								new SimpleDateFormat("yyyy-MM-dd").format(bene_data.getDate_of_birth()) +
								"\"";
			}
		}
		else if (ws_call_type.equals(STATUS_INQ_METHOD_IND))
		{
			if (txn_data.getOut_going_transaction_reference() != null)
			{
				data += "\"agentRefNo\":\"" + txn_data.getOut_going_transaction_reference() + "\"";
			}
			else
			{
				data += "\"transactionNumber\":\"" + txn_data.getPartner_transaction_reference() + "\"";
			}
		}

		data += "}";
		api_request += data + "}";

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(api_request);
		String prettyJsonString = gson.toJson(je);

		// TODO
		System.out.println("--------------------------------- JSON Input -----------------------------------------");
		System.out.println(prettyJsonString); // Print it with specified indentation
		System.out.println("--------------------------------- JSON Input -----------------------------------------");

		return api_request;
	}

	public static GovermantPaymentServices get_goverment_payment_service(String remittance_mode, String delivery_mode)
	{
		if (remittance_mode.equals("15")) // PhilHealth
		{
			if (delivery_mode.equals("20")) // Year wise
			{
				return GovermantPaymentServices.PHIL_HEALTH_YEAR_WISE;
			}
			else if (delivery_mode.equals("21")) // Month wise
			{
				return GovermantPaymentServices.PHIL_HEALTH_MONTH_WISE;
			}
		}
		else if (remittance_mode.equals("16")) // Pag-IBIG Payment
		{
			if (delivery_mode.equals("22")) // Savings
			{
				return GovermantPaymentServices.PAG_IBIG_SAVING;
			}
			else if (delivery_mode.equals("23")) // MP2
			{
				return GovermantPaymentServices.PAG_IBIG_MP2;
			}
			else if (delivery_mode.equals("24")) // Short Term Loan
			{
				return GovermantPaymentServices.PAG_IBIG_SHORT_TERM_LOAN;
			}
			else if (delivery_mode.equals("25")) // Housing Loan
			{
				return GovermantPaymentServices.PAG_IBIG_HOUSE_LOAN;
			}
		}
		else if (remittance_mode.equals("17")) // SSS Payment
		{
			if (delivery_mode.equals("26")) // Contribution - New PRN
			{
				return GovermantPaymentServices.SSS_CONTRIBUTION_NEW_PRN;
			}
			else if (delivery_mode.equals("27")) // Contribution - Existing PRN
			{
				return GovermantPaymentServices.SSS_CONTRIBUTION_EXISTING_PRN;
			}
			else if (delivery_mode.equals("28")) // Short-term Loan
			{
				return GovermantPaymentServices.SSS_SHORT_TERM_LOAN;
			}
			else if (delivery_mode.equals("29")) // Real Estate Loan
			{
				return GovermantPaymentServices.SSS_REAL_ESTATE_LOAN;
			}
			else if (delivery_mode.equals("30")) // Miscellaneous Payments
			{
				return GovermantPaymentServices.SSS_MISCELLANOUS;
			}
		}
		else if (remittance_mode.equals("000")) // Bill Payments - TODO: Fill in the correct values
		{
			if (delivery_mode.equals("11"))
			{
				return GovermantPaymentServices.BILL_PAYMENTS;
			}
		}
		else if (remittance_mode.equals("147")) // Top-up Wallet - TODO: Fill in the correct values
		{
			if (delivery_mode.equals("11"))
			{
				return GovermantPaymentServices.TOP_UP_WALLET;
			}
		}
		else if (remittance_mode.equals("00")) // Reservation Payment - TODO: Fill in the correct values
		{
			if (delivery_mode.equals("11"))
			{
				return GovermantPaymentServices.RESERVATION_PAYMENT;
			}
		}

		return GovermantPaymentServices.UN_DEFINED;
	}

	public static String call_vintaja_api(String api_input, String signed_payload,
			ExOwsLoginCredentials owsLoginCredentialsObject, String ws_call_type) throws Exception
	{
		StringBuffer response_buffer = new StringBuffer();
		DataOutputStream out = null;
		try
		{
			// System.setProperty("javax.net.debug", "all");

			String url_string = null;

			if (ws_call_type.equals(STATUS_INQ_METHOD_IND))
				url_string = owsLoginCredentialsObject.getFlexiField2(); // inquiry
			else if (ws_call_type.equals(VALIDATE_SEND_TXN_INPUTS_METHOD_IND))
				url_string = owsLoginCredentialsObject.getFlexiField3(); // validate
			else
				url_string = owsLoginCredentialsObject.getFlexiField1(); // process

			URL uc = new URL(url_string);
			HttpURLConnection connection = (HttpURLConnection) uc.openConnection();

			System.out.println("SIGNATURE: " + signed_payload);

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("SIGNATURE", signed_payload); // Adding singed data to request header
			connection.setRequestProperty(javax.xml.soap.SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");

			// sending request
			OutputStream wr = connection.getOutputStream();
			wr.write(api_input.getBytes());
			wr.flush();

			// reading response
			BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;

			while ((line = rd.readLine()) != null)
			{
				response_buffer.append(line);
			}

			rd.close();

			return response_buffer.toString();
		}
		finally
		{
			if (out != null)
				out.close();
		}
	}

	public static ServiceProviderResponse parse_api_response(String api_response_string,
			ServiceProviderResponse response, String ws_call_type) throws Exception
	{
		final String SPLITTER_CHAR = ",", KEY_VALUE_SEPARATOR_CHAR = ":";
		final String[] parent_tags_to_remove = { "data" };

		Map<String, String> api_response_map =
				Common_API_Utils.getMapFromString(SPLITTER_CHAR,
						KEY_VALUE_SEPARATOR_CHAR,
						Common_API_Utils.clean_up_jason_string(api_response_string, parent_tags_to_remove));

		if (ws_call_type.equals(GET_REMITTANCE_DETAILS_METHOD_IND))
		{
			((Get_Rmittance_Details_Call_Response) response).getBeneficiaryDto().setPartner_beneficiary_id(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("memberId")));

			((Get_Rmittance_Details_Call_Response) response).getBeneficiaryDto().setPartner_beneficiary_type(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("memberType")));

			((Get_Rmittance_Details_Call_Response) response).getBeneficiaryDto()
					.setFirst_name(api_response_map.get(Common_API_Utils.remove_hidden_characters("fullName​")));

			((Get_Rmittance_Details_Call_Response) response).getBeneficiaryDto()
					.setDate_of_birth(Common_API_Utils.stringToDate(
							api_response_map.get(Common_API_Utils.remove_hidden_characters("birthDate")),
							DATE_FORMAT_IN_USE));

			((Get_Rmittance_Details_Call_Response) response).getTransactionDto().setDestination_amount(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("amount")) != null
							? new BigDecimal(api_response_map.get(Common_API_Utils.remove_hidden_characters("amount")))
							: null);

			((Get_Rmittance_Details_Call_Response) response).getTransactionDto()
					.setFlexi_field_1(api_response_map.get(Common_API_Utils.remove_hidden_characters("flexiFund")));

			// Other response tags
			((Get_Rmittance_Details_Call_Response) response).getTransactionDto().setPartner_transaction_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("transactionNumber"))); // Vintaja
			// reference
			((Get_Rmittance_Details_Call_Response) response).getTransactionDto().setBene_bank_remittance_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("referenceNumber")));

			((Get_Rmittance_Details_Call_Response) response).getTransactionDto().setAddtional_external_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("iprn")));

			if (
				((Get_Rmittance_Details_Call_Response) response).getTransactionDto()
						.getAddtional_external_reference() == null
			)
			{
				((Get_Rmittance_Details_Call_Response) response).getTransactionDto().setAddtional_external_reference(
						api_response_map.get(Common_API_Utils.remove_hidden_characters("prn")));
			}

			((Get_Rmittance_Details_Call_Response) response).getTransactionDto()
					.setCoverage_start_date(Common_API_Utils.stringToDate(
							api_response_map.get(Common_API_Utils.remove_hidden_characters("startYear")),
							api_response_map.get(Common_API_Utils.remove_hidden_characters("startMonth")),
							"01" /* First day in the month */));

			((Get_Rmittance_Details_Call_Response) response).getTransactionDto()
					.setCoverage_end_date(Common_API_Utils.stringToDate(
							api_response_map.get(Common_API_Utils.remove_hidden_characters("endYear")),
							api_response_map.get(Common_API_Utils.remove_hidden_characters("endMonth")),
							"01"/* First day in the month */));
		}

		else if (ws_call_type.equals(SEND_TXN_METHOD_IND))
		{
			// Other response tags
			((Remittance_Call_Response) response).setPartner_transaction_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("transactionNumber"))); // Vintaja
			// reference
			((Remittance_Call_Response) response).setBene_bank_remittance_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("referenceNumber")));

			((Remittance_Call_Response) response).setAddtional_external_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("iprn")));

			if (((Remittance_Call_Response) response).getAddtional_external_reference() == null)
			{
				((Remittance_Call_Response) response).setAddtional_external_reference(
						api_response_map.get(Common_API_Utils.remove_hidden_characters("prn")));
			}
		}
		else if (ws_call_type.equals(STATUS_INQ_METHOD_IND))
		// used in other calls as well
		{
			// Other response tags
			((Status_Call_Response) response).setPartner_transaction_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("transactionNumber"))); // Vintaja
			// reference
			((Status_Call_Response) response).setBene_bank_remittance_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("referenceNumber")));

			((Status_Call_Response) response).setAddtional_external_reference(
					api_response_map.get(Common_API_Utils.remove_hidden_characters("iprn")));

			if (((Status_Call_Response) response).getAddtional_external_reference() == null)
			{
				((Status_Call_Response) response).setAddtional_external_reference(
						api_response_map.get(Common_API_Utils.remove_hidden_characters("prn")));
			}
		}

		// Common tag
		response.setResponse_code(api_response_map.get(Common_API_Utils.remove_hidden_characters("responseCode")));
		response.setResponse_description(api_response_map.get(Common_API_Utils.remove_hidden_characters("remarks")));

		return response;
	}

	// This method need to be called before going on to call SSS contribution
	// transaction
	public static void special_validation_for_SSS_contribution_existing_prn(
			Get_Rmittance_Details_Call_Response get_txn_detalis_response, ServiceProviderResponse response,
			TransactionData txn_data, Customer customer_data, Benificiary bene_data) throws Exception
	{
		if (get_txn_detalis_response.getAction_ind().equals("I"))
		{
			// Partner amount mismatch with our local amount to be sent
			if (
				get_txn_detalis_response.getTransactionDto().getDestination_amount()
						.compareTo(txn_data.getDestination_amount()) != 0
			)
			{
				response.setAction_ind("R");
				response.setResponse_description("Amount (" +
						txn_data.getDestination_amount() +
						") given mismatch with amount (" +
						get_txn_detalis_response.getTransactionDto().getDestination_amount() +
						") need to be paid as per partner API. " +
						get_txn_detalis_response.getResponse_description());
				throw new Exception(response.getResponse_description());
			}

			// Partner Flexi amount mismatch with our local amount to be sent
			if (get_txn_detalis_response.getTransactionDto().getFlexi_field_1().equals(txn_data.getFlexi_field_1()))
			{
				response.setAction_ind("R");
				response.setResponse_description("Flexi Amount (" +
						txn_data.getFlexi_field_1() +
						") given mismatch with Flexi amount (" +
						get_txn_detalis_response.getTransactionDto().getFlexi_field_1() +
						") need to be paid as per partner API. " +
						get_txn_detalis_response.getResponse_description());

				throw new Exception(response.getResponse_description());
			}

			// PRN mismatch with the PRN to pass
			if (
				get_txn_detalis_response.getTransactionDto().getPartner_transaction_reference()
						.equals(txn_data.getPartner_transaction_reference())
			)
			{
				response.setAction_ind("R");
				response.setResponse_description("PRN (" +
						txn_data.getPartner_transaction_reference() +
						") given mismatch with partner PRN (" +
						get_txn_detalis_response.getTransactionDto().getPartner_transaction_reference() +
						") need to be used as per partner API. " +
						get_txn_detalis_response.getResponse_description());

				throw new Exception(response.getResponse_description());
			}
		}
		else
		{
			response.setAction_ind("T");
			response.setResponse_description("Can not validate given data with partner API. " +
					get_txn_detalis_response.getResponse_description());

			response.setRequest_XML(get_txn_detalis_response.getRequest_XML());
			response.setRequest_XML(get_txn_detalis_response.getResponse_XML());

			throw new Exception(response.getResponse_description());
		}
	}
}