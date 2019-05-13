package com.amx.service_provider.homesend;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.axis.types.UnsignedInt;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.amg.vcHomeSend.ows.VendorSpecificField;
import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;

/**
 * This is a Utility class for the WService Class
 * 
 */
public class HomesendUtils
{
	/*
	 * -------------------------------------------------------------------------- -
	 * DATE HELPER FUNCTIONS
	 */
	public static final String DATE_FORMAT_MASK_DEFAULT = "dd-MM-yyyy";
	public static final String DATE_FORMAT_MASK_TIME = "dd-MM-yyyy hh:mm.ss";

	static final Logger logger = Logger.getLogger("WServiceUtils.class");
	static final String GET_QUOTATION_METHOD_IND = new String("6"), SEND_TXN_METHOD_IND = new String("2"),
			REMITTANCE_STATUS_INQ_IND = new String("3"), SEND_CANCEL_REQUEST_IND = new String("7"),
			CANCEL_STATUS_INQ_IND = new String("8");
	static final int SWIFT_CODE_IND = 0, BENEFICIARY_BANK_CODE_IND = 1, BENEFICIARY_BRANCH_CODE_IND = 2,
			BENEFICIARY_BRANCH_NAME_IND = 3;

	/**
	 * This method is built to get the stack trance form an Exception as a string.
	 * It will be helpfull for logging purposes
	 */
	public static String getStackTrace(final Throwable throwable)
	{
		try
		{
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			throwable.printStackTrace(pw);
			return sw.getBuffer().toString();
		}
		catch (Exception e)
		{
			return "Unable to get Stack Transce for the give Exception";
		}
	}

	public static String getLastWord(String input)
	{
		String wordSeparator = " ";
		boolean inputIsOnlyOneWord = !StringUtils.contains(input, wordSeparator);
		if (inputIsOnlyOneWord)
		{
			return input;
		}
		return StringUtils.substringAfterLast(input, wordSeparator);
	}

	public static int getWordCount(String input)
	{
		String trim = input.trim();
		if (trim.isEmpty())
			return 0;
		return trim.split("\\s+").length; // separate string around spaces
	}

	public static String getProperties(String key, String location)
	{
		String value = null;
		try
		{

			ResourceBundle props = ResourceBundle.getBundle(location);
			value = props.getString(key);
		}
		catch (Exception e)
		{
			logger.error(HomesendUtils.getStackTrace(e));
			logger.error("Error: getProperties:: error while getting propertiy from file");
		}

		return value;
	}

	public static String getActualStringValue(String str)
	{
		if (str == null)
			return "";
		else
			return str.replaceAll("[^A-Za-z0-9 ]", ""); // Remove unknown chars from string
	}

	// Methods for HomeSend
	public static String get_filed_value(VendorSpecificField[] vendorSpecificFields, UnsignedInt filed_id)
	{
		if (vendorSpecificFields != null)
			for (int i = 0; i < vendorSpecificFields.length; i++)
			{
				if (vendorSpecificFields[i].getFieldId().equals(filed_id))
				{
					return vendorSpecificFields[i].getValue();
				}
			}

		return null;
	}

	public static void printList(List<VendorSpecificField> vendorSpecificFields)
	{
		logger.info("\n-------------------------  VSF List  -------------------------\n");
		for (int i = 0; i < vendorSpecificFields.size(); i++)
		{
			logger.info("------ item " + (i + 1) + "--------");
			logger.info("Filed ID: " + vendorSpecificFields.get(i).getFieldId());
			logger.info("Value: " + vendorSpecificFields.get(i).getValue());
		}
		logger.info("\n----------------------------------------------------------\n");
	}

	public static String validate_quotation_request_input(BigDecimal settlement_amount, String settlement_currency,
			BigDecimal destination_amount, String destination_currency, String destination_country_3_digit_ISO,
			String origin_country_3_digit_ISO, String customer_id, String customer_type, String beneficary_id,
			String beneficary_type, String beneficary_name, String beneficiary_account, boolean is_iban_number,
			String request_sequence_id, String remittance_mode, String delivery_mode)
	{
		ArrayList<String> validation_error_list = new ArrayList<String>();
		String validation_errors = "";

		// General Null check
		if (settlement_amount != null && destination_amount != null)
		{
			validation_error_list.add(
					"Pass either amount in destination currency or in settlement currency. Do not pass both values");
		}
		else if (settlement_amount == null && destination_amount == null)
		{
			validation_error_list.add(
					"Pass either amount in destination currency or in settlement currency. No data passed for any of them");
		}

		if (settlement_currency == null)
			validation_error_list.add("Settlement currency is empty");

		if (destination_currency == null)
			validation_error_list.add("Destination currency is empty");

		if (destination_country_3_digit_ISO == null)
			validation_error_list.add("Destination country ISO is empty");

		if (origin_country_3_digit_ISO == null)
			validation_error_list.add("Origin country ISO is empty");

		if (customer_id == null)
			validation_error_list.add("Customer ID is empty");

		if (customer_type == null)
			validation_error_list.add("Customer type (i.e. individual or corporate) is empty");

		if (beneficary_type == null)
			validation_error_list.add("Beneficary type (i.e. individual or corporate) is empty");

		if (request_sequence_id == null)
			validation_error_list.add("Request sequence ID is empty");

		if (remittance_mode == null || delivery_mode == null)
		{
			if (remittance_mode == null)
				validation_error_list.add("Remittance mode is empty");

			if (delivery_mode == null)
				validation_error_list.add("Delivery mode is empty");
		}
		else
		{
			// Specific check based on product

			if (remittance_mode.equalsIgnoreCase("07")) // Bank Account
			{
				if (beneficiary_account == null)
					validation_error_list.add("Beneficiary account is empty");

			}
			else if (remittance_mode.equalsIgnoreCase("05")) // Cash Payment
			{
				if (beneficary_id == null && beneficary_name == null)
					validation_error_list.add("Both beneficiary ID and name are empty. You need to pass one of them"
							+ " based on the destination country rules given by HomeSed");
			}
		}

		for (int i = 0; i < validation_error_list.size(); i++)
		{
			if (i != 0)
				validation_errors += "\n";

			validation_errors = validation_errors + (i + 1) + "- " + validation_error_list.get(i);
		}

		return validation_errors;
	}

	public static String form_routting_tag(String destination_country_3_digit_ISO, String remittance_mode,
			String service_Code)
	{
		String routting_tag = null;

		routting_tag = destination_country_3_digit_ISO + "-"; // First part of the routing tag is the country ISO code

		if (remittance_mode.equalsIgnoreCase("07")) // Bank Account.
		{
			routting_tag = routting_tag + "BK";
		}
		else if (remittance_mode.equalsIgnoreCase("11")) // Wallet Transactions (i.e. E-Wallet or Mobile Wallet)
		{
			routting_tag = routting_tag + "MW-" + service_Code;
		}
		else if (remittance_mode.equalsIgnoreCase("05")) // Cash Payment
		{
			routting_tag = routting_tag + "CA";
		}
		else if (remittance_mode.equalsIgnoreCase("CARD_PAN_TEST")) // CARD TODO
		{
			routting_tag = routting_tag + "BK";
		}

		return routting_tag;
	}

	public static String form_service_type(String customer_type, String beneficary_type)
	{
		String service_type = null;

		if (customer_type.equalsIgnoreCase("I")) // Individual //95
		// instead of the codes
		{
			service_type = "P2";
		}
		else if (customer_type.equalsIgnoreCase("C")) // Corporate // 94
		{
			service_type = "B2";
		}

		if (beneficary_type.equalsIgnoreCase("I"))
		{
			service_type = service_type + "P";
		}
		else if (beneficary_type.equalsIgnoreCase("C"))
		{
			service_type = service_type + "B";
		}

		return service_type;
	}

	public static String form_bank_account(String beneficiary_account, boolean is_iban_number, int bic_indicator,
			String beneficiary_bank_branch_swift_code, String beneficiary_bank_code, String beneficiary_branch_code,
			String wallet_service_provider, String beneficary_id, String remittance_mode)
	{
		if (remittance_mode.equalsIgnoreCase("07")) // Bank Account
		{
			// beneficiary_account = (is_iban_number == true ? "iban:" : "ban:") +
			// beneficiary_account
			// + (isEmpty(beneficiary_branch_code) == false ? ";bic=" +
			// beneficiary_branch_code : "");

			if (is_iban_number == true)
			{
				beneficiary_account = "iban:".concat(beneficiary_account);
			}
			else
			{
				beneficiary_account = "ban:".concat(beneficiary_account);

				if (bic_indicator == SWIFT_CODE_IND && isEmpty(beneficiary_bank_branch_swift_code) == false)
				{
					beneficiary_account = beneficiary_account.concat(";bic=" + beneficiary_bank_branch_swift_code);
				}
				else if (bic_indicator == BENEFICIARY_BANK_CODE_IND && isEmpty(beneficiary_bank_code) == false)
				{
					beneficiary_account = beneficiary_account.concat(";bic=" + beneficiary_bank_code);
				}
				else if (bic_indicator == BENEFICIARY_BRANCH_CODE_IND && isEmpty(beneficiary_branch_code) == false)
				{
					beneficiary_account = beneficiary_account.concat(";bic=" + beneficiary_branch_code);
				}
			}
		}
		else if (remittance_mode.equalsIgnoreCase("11")) // Wallet Transaction
		{
			if (wallet_service_provider != null && wallet_service_provider.length() != 0) // E-Wallet (i.e. like PayPal
																							// account)
				beneficiary_account = "ewallet:" + beneficiary_account + ";sp:" + wallet_service_provider;
			else
				beneficiary_account = "tel:+" + beneficiary_account; // M-Wallet i,e(Mobile Wallet, money will come to
																		// beneficiary mobile
			// account)

		}
		else if (remittance_mode.equalsIgnoreCase("05")) // Cash Payment
		{
			beneficiary_account = "ewallet:" + beneficary_id; // TODO: Check here some time beneficiary name should
		}
		else if (remittance_mode.equalsIgnoreCase("CARD_PAN_TEST"))
		{
			beneficiary_account = "pan:" + beneficiary_account;
		}

		return beneficiary_account;
	}

	public static String validate_remittance_request_input(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{
		String validation_errors = "";

		if (txn_data == null)
		{
			validation_errors = "------------- Transaction Validation Errors - Internal JAX Check -------------\n"
					+ "* - Non of transaction data is available\n";
		}
		else
		{
			validation_errors = txn_data.validate_transactionData_input();

			if (customer_data == null)
			{
				validation_errors += "\n------------- Cusotmer Validation Errors - Internal JAX Check -------------\n"
						+ "* - Non of customer data is available\n";
			}
			else
				validation_errors += (validation_errors != null && validation_errors.length() != 0 ? "\n" : "")
						+ customer_data.validate_customer_input();

			if (bene_data == null)
			{
				validation_errors += "\n------------- Beneficiary Validation Errors - Internal JAX Check -------------\n"
						+ "* - Non of beneficiary data is available\n";
			}
			else
				validation_errors += (validation_errors != null && validation_errors.length() != 0 ? "\n" : "")
						+ bene_data.validate_benificiary_input(txn_data.getRemittance_mode(), txn_data.getDestination_country_2_digit_ISO());
		}

		return validation_errors;
	}

	public static String validate_status_request_input(String request_sequence_id, String homeSend_transaction_id)
	{
		String validation_errors = "";

		if (request_sequence_id == null)
			validation_errors += "- Request sequence ID is empty";

		if (homeSend_transaction_id == null)
			validation_errors += "- HomeSend transaction ID is empty";

		return validation_errors;
	}

	public static String validate_cancellation_request_input(String request_sequence_id, String homeSend_transaction_id,
			String cancel_reason)
	{
		String validation_errors = "";

		if (request_sequence_id == null)
			validation_errors += "- Request sequence ID is empty";

		if (homeSend_transaction_id == null)
			validation_errors += "- HomeSend transaction ID is empty";

		if (cancel_reason == null)
			validation_errors += "- Cancel reason code is empty";

		return validation_errors;
	}

	public static boolean isEmpty(String value)
	{
		if (value == null || value.length() == 0)
			return true;
		else
			return false;
	}

	public static ArrayList<VendorSpecificField> form_account_of_charge_vsf_list(String origin_country_3_digit_ISO,
			String service_type, String destination_currency, String beneficiary_bank_branch_swift_code,
			String beneficiary_bank_code, String beneficiary_branch_code)
	{
		final String props_PATH = "com.amx.service_provider.config.Homesend_fileds_code_mapping";
		UnsignedInt VENDOR_ID;
		ResourceBundle props = ResourceBundle.getBundle(props_PATH);
		ArrayList<VendorSpecificField> vsf_list = new ArrayList<VendorSpecificField>();

		VENDOR_ID = new UnsignedInt(props.getString("VENDOR_ID"));

		// Account of Charge Transaction Data
		if (isEmpty(origin_country_3_digit_ISO) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_SP_COUNTRY")),
					origin_country_3_digit_ISO));

		if (isEmpty(service_type) == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SERVICE_TYPE")), service_type));

		if (isEmpty(destination_currency) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_CURRENCY")),
					destination_currency));

		return vsf_list;
	}

	public static ArrayList<VendorSpecificField> form_remittance_vsf_list(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data, String api_login)
	{
		final String props_PATH = "com.amx.service_provider.config.Homesend_fileds_code_mapping";
		UnsignedInt VENDOR_ID;
		ResourceBundle props = ResourceBundle.getBundle(props_PATH);
		ArrayList<VendorSpecificField> vsf_list = new ArrayList<VendorSpecificField>();

		VENDOR_ID = new UnsignedInt(props.getString("VENDOR_ID"));

		// Transaction Data
		if (isEmpty(txn_data.getOrigin_country_3_digit_ISO()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_SP_COUNTRY")),
					txn_data.getOrigin_country_3_digit_ISO()));
		if (isEmpty(api_login) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("SENDER_SERVICE_PROVIDER_NAME")), api_login));
		if (isEmpty(form_service_type(customer_data.getCustomer_type(), bene_data.getBeneficiary_type())) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SERVICE_TYPE")),
					form_service_type(customer_data.getCustomer_type(), bene_data.getBeneficiary_type())));
		if (isEmpty(txn_data.getOut_going_transaction_reference()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("EXCHANGE_HOUSE_ID")),
					txn_data.getOut_going_transaction_reference()));
		if (isEmpty(txn_data.getFurther_instruction()) == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("ADDITIONAL_INFORMATION_FIELD")),
							txn_data.getFurther_instruction()));
		// Note on Terminology:
		// HomeSend (SOURCE_OF_INCOME) --> AMIEC (Source of fund).. Values as per
		// HomeSend (SAL, BUS, LOAN, PIE)
		// HomeSend (SOURCE_OF_FUND) ----> AMIEC (Collection type). Values as per
		// HomeSend (CASH, BANK, CARD, WALLET and CHEQUE)
		if (isEmpty(txn_data.getSource_of_fund_desc()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SOURCE_OF_INCOME")),
					txn_data.getSource_of_fund_desc()));
		if (isEmpty(txn_data.getTxn_collocation_type()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SOURCE_OF_FUND")),
					txn_data.getTxn_collocation_type()));
		if (isEmpty(txn_data.getPurpose_of_remittance()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("PURPOSE_OF_REMITTANCE")),
					txn_data.getPurpose_of_remittance()));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("SPB_RETURNED_PRINCIPLE_DEST_SP_AMOUNT_SPB_CUR")), ""));

		// Customer Data
		if (customer_data.getCustomer_type().equalsIgnoreCase("C"))
		{
			if (isEmpty(customer_data.getFull_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_NAME")),
						customer_data.getFull_name()));
		}
		else
		{
			if (isEmpty(customer_data.getFirst_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_FIRST_NAME")),
						customer_data.getFirst_name()));
			if (isEmpty(customer_data.getMiddle_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_MIDDLE_NAME")),
						customer_data.getMiddle_name()));
			if (isEmpty(customer_data.getLast_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_LAST_NAME")),
						customer_data.getLast_name()));
		}

		if (isEmpty(customer_data.getNationality_3_digit_ISO()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_NATIONALITY")),
					customer_data.getNationality_3_digit_ISO()));
		if (isEmpty(dateToString(customer_data.getDate_of_birth(), "yyyy-MM-dd")) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_DATE_OF_BIRTH")),
					dateToString(customer_data.getDate_of_birth(), "yyyy-MM-dd")));
		// if (isEmpty(customer_data.getFull_addrerss()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("SENDER_ADDRESS")),
		// customer_data.getFull_addrerss()));
		if (isEmpty(customer_data.getStreet()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ADDRESS_STREET")),
					customer_data.getStreet()));
		if (isEmpty(customer_data.getCity()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ADDRESS_CITY")),
					customer_data.getCity()));
		if (isEmpty(customer_data.getAddress_zip()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ADDRESS_ZIP")),
					customer_data.getAddress_zip()));
		if (isEmpty(customer_data.getState()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ADDRESS_STATE")),
					customer_data.getState()));
		if (isEmpty(txn_data.getOrigin_country_3_digit_ISO()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ADDRESS_COUNTRY")),
					txn_data.getOrigin_country_3_digit_ISO()));
		if (isEmpty(customer_data.getCustomer_reference()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_REFERENCE")),
					customer_data.getCustomer_reference()));
		if (isEmpty(customer_data.getIdentity_type_desc()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_TYPE")),
					customer_data.getIdentity_type_desc()));
		if (isEmpty(customer_data.getIdentity_no()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_NUMBER")),
					customer_data.getIdentity_no()));
		if (isEmpty("") == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_ISSUED_BY")), ""));
		if (isEmpty("") == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_ISSUED_AT")), ""));
		if (isEmpty("") == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_ISSUE_DATE")), ""));
		if (isEmpty(dateToString(customer_data.getIdentity_expiry_date(), "yyyy-MM-dd")) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_ID_EXPIRY_DATE")),
					dateToString(customer_data.getIdentity_expiry_date(), "yyyy-MM-dd")));
		if (isEmpty("") == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_PLACE_OF_BIRTH")), ""));
		if (isEmpty(customer_data.getContact_no()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("SENDER_MOBILE_TELEPHONE_NUMBER")), customer_data.getContact_no()));
		if (isEmpty(customer_data.getEmail()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_EMAIL_ADDRESS")),
					customer_data.getEmail()));
		if (isEmpty(customer_data.getProfession()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("SENDER_OCCUPATION")),
					customer_data.getProfession()));

		// Beneficiary Data
		if (bene_data.getBeneficiary_type().equalsIgnoreCase("C"))
		{
			if (isEmpty(bene_data.getFirst_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("IN_BENEFICIARY_NAME")),
						bene_data.getFirst_name()
								+ (bene_data.getMiddle_name() != null ? bene_data.getMiddle_name() : "")
								+ bene_data.getLast_name()));
		}
		else
		{
			if (isEmpty(bene_data.getFirst_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID,
						new UnsignedInt(props.getString("BENEFICIARY_FIRST_NAME")), bene_data.getFirst_name()));
			if (isEmpty(bene_data.getMiddle_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID,
						new UnsignedInt(props.getString("BENEFICIARY_MIDDLE_NAME")), bene_data.getMiddle_name()));
			if (isEmpty(bene_data.getLast_name()) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID,
						new UnsignedInt(props.getString("BENEFICIARY_LAST_NAME")), bene_data.getLast_name()));
		}

		if (
			bene_data.getBeneficiary_bank_branch_indicator() == SWIFT_CODE_IND
					&& isEmpty(bene_data.getBeneficiary_bank_branch_swift_code()) == false)
			{
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BANK_CODE")),
					bene_data.getBeneficiary_bank_branch_swift_code()));
		}
		else if (
			bene_data.getBeneficiary_bank_branch_indicator() == BENEFICIARY_BANK_CODE_IND
					&& isEmpty(bene_data.getBeneficiary_bank_code()) == false)
			{
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BANK_CODE")),
					bene_data.getBeneficiary_bank_code()));
		}
		else if (
			bene_data.getBeneficiary_bank_branch_indicator() == BENEFICIARY_BRANCH_CODE_IND
					&& isEmpty(bene_data.getBeneficiary_branch_code()) == false)
			{
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BANK_CODE")),
					bene_data.getBeneficiary_branch_code()));
		}
		else if (bene_data.getBeneficiary_bank_branch_indicator() == BENEFICIARY_BRANCH_NAME_IND)
		{
			String bank_branch_name = null;

			if (isEmpty(bene_data.getBeneficiary_bank_name()) == false)
			{
				bank_branch_name = bene_data.getBeneficiary_bank_name();

				if (isEmpty(bene_data.getBeneficiary_branch_name()) == false)
				{
					bank_branch_name.concat(" - " + bene_data.getBeneficiary_branch_name());
				}
			}
			else if (isEmpty(bene_data.getBeneficiary_branch_name()) == false)
			{
				bank_branch_name = bene_data.getBeneficiary_branch_name();
			}

			if (isEmpty(bank_branch_name) == false)
				vsf_list.add(new VendorSpecificField(VENDOR_ID,
						new UnsignedInt(props.getString("BENEFICIARY_BANK_CODE")), bank_branch_name));
		}

		if (isEmpty(bene_data.getBeneficiary_bank_name()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BANK_NAME")),
					bene_data.getBeneficiary_bank_name()));
		if (isEmpty(bene_data.getBeneficiary_branch_name()) == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BANK_BRANCH_NAME")),
							bene_data.getBeneficiary_branch_name()));
		if (isEmpty(bene_data.getContact_no()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_TELEPHONE_NUMBER")), bene_data.getContact_no()));
		if (isEmpty(bene_data.getStreet()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_STREET_CHILD")), bene_data.getStreet()));
		if (isEmpty(bene_data.getCity()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_CITY_CHILD")), bene_data.getCity()));
		if (isEmpty(bene_data.getAddress_zip()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_ZIP_CHILD")), bene_data.getAddress_zip()));
		if (isEmpty(bene_data.getState()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_STATE_CHILD")), bene_data.getState()));
		if (isEmpty(bene_data.getNationality_3_digit_ISO()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_NATIONALITY_CHILD")),
					bene_data.getNationality_3_digit_ISO()));
		if (isEmpty(txn_data.getDestination_country_3_digit_ISO()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_COUNTRY_CHILD")),
					txn_data.getDestination_country_3_digit_ISO()));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_EMAIL")), ""));
		if (isEmpty(bene_data.getBeneficiary_id_type()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_ID_TYPE")),
					bene_data.getBeneficiary_id_type()));
		if (isEmpty(bene_data.getBeneficiary_id_number()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_ID_NUMBER")),
					bene_data.getBeneficiary_id_number()));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ID_ISSUED_BY")), ""));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ID_ISSUED_AT")), ""));
		if (isEmpty("") == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_ISSUE_DATE")), ""));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_EXPIRY_DATE")),
					""));
		if (isEmpty(dateToString(bene_data.getDate_of_birth(), "yyyy-MM-dd")) == false)
			vsf_list.add(
					new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_DATE_OF_BIRTH")),
							dateToString(bene_data.getDate_of_birth(), "yyyy-MM-dd")));
		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_PLACE_OF_BIRTH")), ""));
		if (isEmpty(bene_data.getProfession()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_OCCUPATION")),
					bene_data.getProfession()));
		if (isEmpty(bene_data.getFull_addrerss()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_CHILD")), bene_data.getFull_addrerss()));
		if (isEmpty(bene_data.getStreet()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ADDRESS_STREET_1_PARENT")), bene_data.getStreet()));
		// if (isEmpty(bene_data.getCity()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_CITY_PARENT")),
		// bene_data.getCity()));
		// if (isEmpty(bene_data.getAddress_zip()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_ZIP_PARENT")),
		// bene_data.getAddress_zip()));
		// if (isEmpty(bene_data.getState()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_STATE_PARENT")),
		// bene_data.getState()));
		// if (isEmpty(bene_data.getStreet()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_STREET_2_PARENT")),
		// bene_data.getStreet()));
		// if (isEmpty(txn_data.getDestination_country_3_digit_ISO()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_COUNTRY_PARENT")),
		// txn_data.getDestination_country_3_digit_ISO()));

		if (isEmpty("") == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_ROMANIZED_NAME")), ""));
		// if (isEmpty(bene_data.getFull_addrerss()) == false)
		// vsf_list.add(new VendorSpecificField(VENDOR_ID, new
		// UnsignedInt(props.getString("BENEFICIARY_ADDRESS_PARENT")),
		// bene_data.getFull_addrerss()));
		if (isEmpty(bene_data.getContact_no()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_MOBILE_NUMBER")), bene_data.getContact_no()));
		if (isEmpty(bene_data.getContact_no()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_LANDLINE_NUMBER")), bene_data.getContact_no()));
		if (isEmpty(dateToString(bene_data.getDate_of_birth(), "yyyy-MM-dd")) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID, new UnsignedInt(props.getString("BENEFICIARY_BORN_DATE")),
					dateToString(bene_data.getDate_of_birth(), "yyyy-MM-dd")));
		if (isEmpty(bene_data.getBeneficiary_account_type()) == false)
			vsf_list.add(new VendorSpecificField(VENDOR_ID,
					new UnsignedInt(props.getString("BENEFICIARY_BANK_ACCOUNT_TYPE")),
					bene_data.getBeneficiary_account_type()));

		return vsf_list;
	}

	public static boolean is_iban_number(String destination_country_2_digit_ISO, String beneficiary_account)
	{
		try
		{
			// IBAN number format has to start with two digit of destination 2 ISO code
			// It also has minimum value of 15 character
			return beneficiary_account.startsWith(destination_country_2_digit_ISO)
					&& beneficiary_account.length() >= 15;
		}
		catch (Exception e)
		{
			logger.info(getStackTrace(e));
			return false;
		}
	}

	public static Object log_quotation_input_details(BigDecimal settlement_amount, String settlement_currency,
			BigDecimal destination_amount, String destination_currency, String destination_country_2_digit_ISO,
			String destination_country_3_digit_ISO, String origin_country_3_digit_ISO, String customer_reference,
			String customer_type, String beneficary_reference, String beneficary_type, String beneficary_name,
			String beneficiary_account, boolean is_iban_number, int bic_indicator, String beneficiary_bank_code,
			String beneficiary_branch_code, String beneficiary_bank_branch_swift_code, String wallet_service_provider,
			String request_sequence_id, String remittance_mode, String delivery_mode)
	{

		return "Quotation Inputs [settlement_amount=" + settlement_amount + ", settlement_currency="
				+ settlement_currency + ", destination_amount=" + destination_amount + ", destination_currency="
				+ destination_currency + ", destination_country_2_digit_ISO=" + destination_country_2_digit_ISO
				+ ", destination_country_3_digit_ISO=" + destination_country_3_digit_ISO
				+ ", origin_country_3_digit_ISO=" + origin_country_3_digit_ISO + ", customer_reference="
				+ customer_reference + ", customer_type=" + customer_type + ", beneficary_reference="
				+ beneficary_reference + ", beneficary_type=" + beneficary_type + ", beneficary_name=" + beneficary_name
				+ ", beneficiary_account=" + beneficiary_account + ", is_iban_number=" + is_iban_number
				+ ", bic_indicator=" + bic_indicator + ", beneficiary_bank_code=" + beneficiary_bank_code
				+ ", beneficiary_branch_code=" + beneficiary_branch_code + ", beneficiary_bank_branch_swift_code="
				+ beneficiary_bank_branch_swift_code + ", wallet_service_provider=" + wallet_service_provider
				+ ", request_sequence_id=" + request_sequence_id + ", remittance_mode=" + remittance_mode
				+ ", delivery_mode=" + delivery_mode + "]";
	}

	public static Object log_send_remittance_input_details(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{
		return "Send Remittance Input: [\n" + (txn_data != null ? txn_data.toString() : "txn_data: null") + "\n"
				+ (customer_data != null ? customer_data.toString() : "customer_data: null") + "\n"
				+ (bene_data != null ? bene_data.toString() : "bene_data: null") + "\n";
	}

	public static String generate_random_value(int number_of_digits, boolean useLower, boolean useUpper,
			boolean useDigits, boolean usePunctuation)
	{
		RandomValueGenerator randomValueGenerator = new RandomValueGenerator.PasswordGeneratorBuilder()
				.useLower(useLower).useUpper(useUpper).useDigits(useDigits).usePunctuation(usePunctuation).build();

		return randomValueGenerator.generate(number_of_digits);
	}

	public static String dateToString(Date date, String format)
	{
		if (date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			String date_to_string = sdf.format(date);
			return date_to_string;
		}
		else
			return null;
	}

	public static int get_words_count(String str)
	{
		int count = 0;
		try
		{
			if (!(" ".equals(str.substring(0, 1))) || !(" ".equals(str.substring(str.length() - 1))))
			{
				for (int i = 0; i < str.length(); i++)
				{
					if (str.charAt(i) == ' ')
					{
						count++;
					}
				}
				count = count + 1;
			}
		}
		catch (Exception e)
		{
			// Do nothing
		}
		return count; // returns 0 if string starts or ends with space " ".
	}
}