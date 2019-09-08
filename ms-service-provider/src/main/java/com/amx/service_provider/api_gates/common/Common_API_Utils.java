package com.amx.service_provider.api_gates.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.transaction.Transactional;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang3.StringUtils;

import com.amx.jax.model.request.serviceprovider.Benificiary;
import com.amx.jax.model.request.serviceprovider.Customer;
import com.amx.jax.model.request.serviceprovider.TransactionData;
import com.amx.jax.model.response.serviceprovider.ServiceProviderResponse;
import com.amx.service_provider.api_gates.vintaja.GovermantPaymentServices;
import com.amx.service_provider.api_gates.vintaja.VintajaUtils;
import com.amx.service_provider.dbmodel.webservice.ExOwsLoginCredentials;
import com.amx.service_provider.dbmodel.webservice.OwsParamRespcode;
import com.amx.service_provider.dbmodel.webservice.OwsParamRespcodeKey;
import com.amx.service_provider.dbmodel.webservice.OwsTransferLog;
import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;
import com.amx.service_provider.repository.webservice.OwsTransferLogRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Common_API_Utils
{

	public static final String SEND_TXN_METHOD_IND = new String("2"), STATUS_INQ_METHOD_IND = new String("3"),
			COMMON_WS_CALL_TYPE = new String("99"), CREATOR_UPDATOR = new String("OWS_SCHEDULER");

	public static String getActionInd(String routting_bank_code, String response_code, String ws_call_type,
			OwsParamRespcodeRepository owsParamRespcodeRepository)
	{
		// Search with the given ws_call_type
		OwsParamRespcode owsParamRespcode =
				owsParamRespcodeRepository.findByOwsParamRespcodeKey(
						new OwsParamRespcodeKey(routting_bank_code, response_code, ws_call_type));

		// Search with the common ws_call_type
		if (owsParamRespcode == null)
			owsParamRespcode =
					owsParamRespcodeRepository.findByOwsParamRespcodeKey(
							new OwsParamRespcodeKey(routting_bank_code, response_code, COMMON_WS_CALL_TYPE));

		if (owsParamRespcode != null)
			return owsParamRespcode.getAction_ind();
		else
			return null;
	}

	public static String getResponseDescription(String routting_bank_code, String response_code, String ws_call_type,
			OwsParamRespcodeRepository owsParamRespcodeRepository)
	{
		// Search with the given ws_call_type
		OwsParamRespcode owsParamRespcode =
				owsParamRespcodeRepository.findByOwsParamRespcodeKey(
						new OwsParamRespcodeKey(routting_bank_code, response_code, ws_call_type));

		// Search with the common ws_call_type
		if (owsParamRespcode == null)
			owsParamRespcode =
					owsParamRespcodeRepository.findByOwsParamRespcodeKey(
							new OwsParamRespcodeKey(routting_bank_code, response_code, COMMON_WS_CALL_TYPE));

		if (owsParamRespcode != null)
			return owsParamRespcode.getResponse_des();
		else
			return null;
	}

	public static String getStackTrace(final Throwable throwable)
	{
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
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

	public static String getProperties(String key, String location) throws Exception
	{
		String value = null;

		ResourceBundle props = ResourceBundle.getBundle(location);
		value = props.getString(key);

		return value;
	}

	public static String getActualStringValue(String str)
	{
		if (str == null)
			return "";
		else
			return str.replaceAll("[^A-Za-z0-9 ]", ""); // Remove unknown chars from string
	}

	public static boolean isEmpty(String value)
	{
		if (value == null || value.length() == 0)
			return true;
		else
			return false;
	}

	public static String generate_random_value(int number_of_digits, boolean useLower, boolean useUpper,
			boolean useDigits, boolean usePunctuation)
	{
		RandomValueGenerator randomValueGenerator =
				new RandomValueGenerator.PasswordGeneratorBuilder().useLower(useLower).useUpper(useUpper)
						.useDigits(useDigits).usePunctuation(usePunctuation).build();

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

	public static Date stringToDate(String date_string, String format)
	{
		if (date_string != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat(format);

			Date date = null;
			try
			{
				date = sdf.parse(date_string);
			}
			catch (ParseException e)
			{

			}

			return date;
		}
		else
			return null;
	}

	public static Date stringToDate(String year, String month, String day)
	{
		String date_string = null;
		try
		{
			year = year.trim();
			month = month.trim();
			day = day.trim();

			if (month.length() == 1) // months from Jan to Sep
			{
				month = "0" + month;
			}

			if (day.length() == 1) // days from 1 to 9
			{
				day = "0" + day;
			}

			date_string = year + "-" + month + "-" + day;
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
		}

		return stringToDate(date_string, "yyyy-MM-dd");
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

	@Transactional
	public static void insert_api_call_log(TransactionData txn_data, ServiceProviderResponse response,
			OwsTransferLogRepository owsTransferLogRep) throws Exception
	{
		owsTransferLogRep.save(new OwsTransferLog(txn_data.getCompany_code(), txn_data.getDocument_code(),
				txn_data.getDocument_finance_year(), txn_data.getDocument_no(), txn_data.getRoutting_bank_code(),
				txn_data.getRoutting_bank_code(), response.getResponse_code(), response.getResponse_description(),
				SEND_TXN_METHOD_IND, response.getRequest_XML(), response.getResponse_XML(), CREATOR_UPDATOR,
				new Date()));
	}

	public static Map<String, String> getMapFromString(String splitter_char, String key_value_separator_char,
			String data)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] stubs = data.split(splitter_char);
		for (String string : stubs)
		{
			String[] stub = string.split(key_value_separator_char);
			String value = null;
			String key = null;

			if (stub.length > 1) // Take the value only if there is a value for the current key. Otherwise pass
									// null
			{
				key = remove_hidden_characters(stub[0]);
				value = remove_hidden_characters(stub[1]);

				map.put(key, value);

				// TODO: REMOVE AFTER TESTING
				//
				// System.out.println("********************************");
				// System.out.println(key + " -> " + key.hashCode());
				// System.out.println("********************************");
			}
		}

		return map;
	}

	public static String clean_up_jason_string(String api_response_string, String[] parent_tages)
	{
		if (api_response_string != null)
		{
			// removing all parent tags to end up with only key - values pairs
			if (parent_tages != null && parent_tages.length != 0)
			{
				for (int i = 0; i < parent_tages.length; i++)
				{
					api_response_string = api_response_string.replace("\"" + parent_tages[i] + "\":", "");
				}
			}

			api_response_string =
					api_response_string.replace("{", "").replace("}", "").replace("\"", "").replace("\\", "");

			api_response_string = remove_extra_spaces_within_string(api_response_string);

			Character last_splitter_char = null;
			for (int i = 0; i < api_response_string.length(); i++)
			{
				char current_char = api_response_string.charAt(i);
				if (current_char == ',' || current_char == ':')
				{
					if (last_splitter_char == null)
					{
						last_splitter_char = current_char;
					}
					else
					{
						if (current_char != last_splitter_char) // Normal char flow
						{
							last_splitter_char = current_char;
						}
						else
						// If equals then this is wrong char and we need to remove it from the String
						{
							api_response_string =
									api_response_string.substring(0, i) + " " + api_response_string.substring(i + 1);
						}
					}
				}
			}
		}
		return api_response_string;
	}

	private static PrivateKey get_private_key(String filename) throws Exception
	{
		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static String sign_and_encode_payload(String api_input, ExOwsLoginCredentials owsLoginCredentialsObject)
			throws Exception
	{
		// The key is generated from the keystore file with the format pkcs8. Note
		// un-check the pem option
		PrivateKey private_key = get_private_key(owsLoginCredentialsObject.getKeystore_path());

		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(private_key);
		sig.update(api_input.getBytes("UTF8"));
		byte[] signatureBytes = sig.sign();
		return Base64.encode(signatureBytes);
	}

	public static String sign_and_encode_payload(String api_input, String key_store_path) throws Exception
	{
		// The key is generated from the keystore file with the format pkcs8. Note
		// un-check the pem option
		PrivateKey private_key = get_private_key(key_store_path);

		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(private_key);
		sig.update(api_input.getBytes("UTF8"));
		byte[] signatureBytes = sig.sign();
		return Base64.encode(signatureBytes);
	}

	public static String get_formatted_jason_string(String jason_string) throws Exception
	{
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jason_string);
		return gson.toJson(je);
	}

	public static HashMap<String, String> validate_initial_inputs(String application_country_3_digit_ISO,
			String destination_country_3_digit_ISO, String routting_bank_code, String destination_currency,
			String remittance_mode, String delivery_mode)
	{
		HashMap<String, String> validation_result = new HashMap<String, String>();

		if (StringUtils.isEmpty(application_country_3_digit_ISO) == true)
			validation_result.put("application_country_3_digit_ISO", "Application country ISO is empty");

		if (StringUtils.isEmpty(destination_country_3_digit_ISO) == true)
			validation_result.put("destination_country_3_digit_ISO", "Destination country ISO is empty");

		if (StringUtils.isEmpty(routting_bank_code) == true)
			validation_result.put("routting_bank_code", "Routting bank code is empty");

		if (StringUtils.isEmpty(destination_currency) == true)
			validation_result.put("destination_currency", "Destination currency is empty");

		if (StringUtils.isEmpty(remittance_mode) == true)
			validation_result.put("remittance_mode", "remittance_mode is empty");

		if (StringUtils.isEmpty(delivery_mode) == true)
			validation_result.put("delivery_mode", "Delivery mode is empty");

		return validation_result;
	}

	public static HashMap<String, String> validate_send_remittance_inputs(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data)
	{
		HashMap<String, String> validation_result = new HashMap<String, String>();

		// Should not be called before validate_initial_inputs method

		if (txn_data.getRoutting_bank_code().equals("VINTJA"))
		{
			GovermantPaymentServices target_payment_service =
					VintajaUtils.get_goverment_payment_service(txn_data.getRemittance_mode(),
							txn_data.getDelivery_mode());

			if (target_payment_service == GovermantPaymentServices.PHIL_HEALTH_YEAR_WISE)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				if (bene_data.getDate_of_birth() == null)
					validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				 if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
				 validation_result.put("bene_data.getContact_no()", "beneficiary contact is	 empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.PHIL_HEALTH_MONTH_WISE)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				// if (bene_data.getDate_of_birth() == null)
				// validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of
				// birth is empty");

				 if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
				 validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_SAVING)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (txn_data.getCoverage_end_date() == null)
					validation_result.put("txn_data.getCoverage_end_date", "coverage end date is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_MP2)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (txn_data.getCoverage_end_date() == null)
					validation_result.put("txn_data.getCoverage_end_date", "coverage end date is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number (MP2) is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_SHORT_TERM_LOAN)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.PAG_IBIG_HOUSE_LOAN)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (txn_data.getCoverage_end_date() == null)
					validation_result.put("txn_data.getCoverage_end_date", "coverage end date is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number (Loan Number) is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_NEW_PRN)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (txn_data.getCoverage_start_date() == null)
					validation_result.put("txn_data.getCoverage_start_date", "coverage start date is empty");

				if (txn_data.getCoverage_end_date() == null)
					validation_result.put("txn_data.getCoverage_end_date", "coverage end date is empty");

				if (StringUtils.isEmpty(txn_data.getFlexi_field_1()) == true) // Flexifund
					validation_result.put("Flexifund", "Flexifund is empty, pass 0 if not applicable");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_CONTRIBUTION_EXISTING_PRN)
			{
				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");

				if (StringUtils.isEmpty(txn_data.getPartner_transaction_reference()) == true)
					validation_result.put("txn_data.getPartner_transaction_reference()", "PRN is empty");

			}
			else if (target_payment_service == GovermantPaymentServices.SSS_SHORT_TERM_LOAN)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_REAL_ESTATE_LOAN)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.SSS_MISCELLANOUS)
			{
				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_id()) == true)
					validation_result.put("partner_beneficiary_id", "partner beneficiary id is empty");

				if (StringUtils.isEmpty(bene_data.getLast_name()) == true)
					validation_result.put("bene_data.getLast_name", "beneficiary last name is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				//if (bene_data.getDate_of_birth() == null)
					//validation_result.put("bene_data.getDate_of_birth()", "beneficiary date of birth is empty");

				if (StringUtils.isEmpty(bene_data.getContact_no()) == true)
					validation_result.put("bene_data.getContact_no()", "beneficiary contact is empty");

				// if (StringUtils.isEmpty(bene_data.getEmail()) == true)
				// validation_result.put("bene_data.getEmail()", "beneficiary email is empty");

				if (StringUtils.isEmpty(bene_data.getPartner_beneficiary_type()) == true)
					validation_result.put("bene_data.getPartner_beneficiary_type()",
							"beneficiary member type is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(txn_data.getFlexi_field_2()) == true)
					validation_result.put("misalliance payment type", "misalliance payment type is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.BILL_PAYMENTS)
			{
				if (StringUtils.isEmpty(bene_data.getBeneficiary_bank_code()) == true)
					validation_result.put("bene_data.getBeneficiary_bank_code()",
							" beneficiary bank code (biller code) id is empty");

				if (StringUtils.isEmpty(bene_data.getFirst_name()) == true)
					validation_result.put("bene_data.getFirst_name()", "beneficiary first name is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.TOP_UP_WALLET)
			{
				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(txn_data.getDestination_currency()) == true)
					validation_result.put("txn_data.getDestination_currency()", "Destination currency is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number (PayRemit number) is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
			else if (target_payment_service == GovermantPaymentServices.RESERVATION_PAYMENT)
			{
				if (StringUtils.isEmpty(txn_data.getDestination_currency()) == true)
					validation_result.put("txn_data.getDestination_currency()", "Destination currency is empty");

				if (txn_data.getDestination_amount() == null)
					validation_result.put("txn_data.getDestination_amount", "amount is empty");

				if (StringUtils.isEmpty(bene_data.getBeneficiary_account_number()) == true)
					validation_result.put("bene_data.getBeneficiary_account_number",
							"beneficiary account number (Reservation number) is empty");

				if (StringUtils.isEmpty(txn_data.getOut_going_transaction_reference()) == true)
					validation_result.put("txn_data.getOut_going_transaction_reference",
							"outgoing reference number is empty");
			}
		}

		return validation_result;
	}

	public static HashMap<String, String> validate_get_qutation_inputs(TransactionData txn_data, Customer customer_data,
			Benificiary bene_data)
	{

		HashMap<String, String> validation_result = new HashMap<String, String>();
		// TODO Auto-generated method stub
		return validation_result;
	}

	public static HashMap<String, String> validate_get_remittance_details_inputs(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data)
	{

		HashMap<String, String> validation_result = new HashMap<String, String>();
		// TODO Auto-generated method stub
		return validation_result;
	}
	
	public static HashMap<String, String> validate_get_remittance_status_inputs(TransactionData txn_data,
			Customer customer_data, Benificiary bene_data)
	{
		HashMap<String, String> validation_result = new HashMap<String, String>();
		// TODO Auto-generated method stub
		return validation_result;
	}

	public static String remove_extra_spaces_within_string(String input)
	{
		try
		{
			input = input.trim();
			do
			{
				input = input.replace("  ", " ");
			} while (input.contains("  "));
		}
		catch (Exception e)
		{

		}

		return input;
	}

	public static String remove_hidden_characters(String input)
	{
		try
		{
			input =
					input.trim().replaceAll("\r\n", "\n").replaceAll("\r", "\n")
							.replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}&&[^\\s]]", "");
		}
		catch (Exception e)
		{

		}
		return input;
	}

	public static void main(String[] args)
	{
		String year = "1980";
		String month = "2";
		String day = "01";

		System.out.println(stringToDate(year, month, day));
	}



}