package com.amx.service_provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication 
 @Import({MsServiceProviderConfig.class })
 


//@SpringBootApplication
//@ComponentScan(basePackages = { "com.amx.jax" })
//@EnableAsync(proxyTargetClass = true)
//@EnableCaching
public class MsServiceProviderApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(MsServiceProviderApplication.class, args);
	}


}

//
// @Autowired
// static ServiceProviderManger ws;
//
// public static void main(String[] args)
// {
// // SpringApplication.run(MsServiceProviderApplication.class, args);
// //= new ServiceProviderGateWay();
//
// String request_sequence_id = HomesendUtils.generate_random_value(10/*
// number_of_digits */,
// false/* useLower */,
// false/* useUpper */,
// true/* useDigits */,
// false/* usePunctuation */);
//
// // ///////////////////////////////////////////////////////////////////////
// TransactionData txn_data = new TransactionData();
// txn_data.setDelivery_mode("05");
// txn_data.setDestination_amount(new BigDecimal("31660.00"));
// txn_data.setDestination_country_2_digit_ISO(null);
// txn_data.setDestination_country_3_digit_ISO("ESP");
// txn_data.setDestination_currency("EUR");
// txn_data.setOut_going_transaction_reference(null);
// txn_data.setFurther_instruction("INV NO 3089 2ND PAY");
// txn_data.setOrigin_country_3_digit_ISO("KWT");
// txn_data.setPurpose_of_remittance("TRADE PURPOSE");
// txn_data.setRemittance_mode("07");
// txn_data.setRequest_sequence_id(request_sequence_id);
// txn_data.setSettlement_currency("USD");
// txn_data.setSource_of_fund_desc("BUS");
// txn_data.setTxn_collocation_type("BANK");
// txn_data.setApplication_country_3_digit_ISO("KWT");
// txn_data.setRoutting_bank_code("HOME");
//
// Customer customer_data = new Customer();
// customer_data.setAddress_zip("13077");
// customer_data.setBuilding_no("BLOCK");
// customer_data.setCity("FARWANIYA");
// customer_data.setContact_no("66365210");
// customer_data.setCustomer_reference("878423");
// customer_data.setCustomer_type("C");
// customer_data.setDate_of_birth(new Date());
// customer_data.setDistrict("KUWAIT");
// customer_data.setEmail(null);
// customer_data.setEmployer_name(null);
// customer_data.setFirst_name("HASSAN AL NASER GENERAL TRADING CO W.L.L");
// customer_data.setFull_addrerss("LOCK,HOUSE001,FLAT05");
// customer_data.setGender("M");
// customer_data.setIdentity_expiry_date(null);
// customer_data.setIdentity_no(null);
// customer_data.setIdentity_type_desc(null);
// customer_data.setLast_name(null);
// customer_data.setMiddle_name(null);
// customer_data.setNationality_3_digit_ISO("KWT");
// customer_data.setProfession("Others");
// customer_data.setState("KUWAIT");
// customer_data.setStreet("HOUSE001");
//
// Benificiary bene_data = new Benificiary();
// bene_data.setAddress_zip(",");
// bene_data.setBeneficiary_account_number("ES0700810180760001894698");
// bene_data.setBeneficiary_account_type("SAVINGS ACCOUNT");
// bene_data.setBeneficiary_bank_branch_swift_code("BSABESBBXXX");
// bene_data.setBeneficiary_bank_code(null);
// bene_data.setBeneficiary_bank_name("BANCO DE SABADELL SABADELL");
// bene_data.setBeneficiary_branch_code(null);
// bene_data.setBeneficiary_branch_name("HEADOFFICE");
// bene_data.setBeneficiary_id_number(null);
// bene_data.setBeneficiary_id_type(null);
// bene_data.setBeneficiary_reference("10707022");
// bene_data.setBeneficiary_type("C");
// bene_data.setCity(null);
// bene_data.setContact_no("1");
// bene_data.setDate_of_birth(null);
// bene_data.setDistrict("OTHERS");
// bene_data.setFirst_name("INSOCO");
// bene_data.setFull_addrerss("OTHERS,OTHERS");
// bene_data.setIs_iban_number_holder(true);
// bene_data.setBic_indicator(0);
// bene_data.setLast_name("S L");
// bene_data.setMiddle_name(null);
// bene_data.setNationality_3_digit_ISO("ESP");
// bene_data.setProfession(null);
// bene_data.setRelation_to_beneficiary("OTHERS");
// bene_data.setState("OTHERS");
// bene_data.setStreet(null);
// bene_data.setWallet_service_provider(null);
//
// Quotation_Call_Response aoc_response = (Quotation_Call_Response) ws
// .getQutation(txn_data, customer_data, bene_data);
//
// System.out.println("******************************* Get Quotation Response
// *******************************");
// System.out.println(aoc_response.toString());
//
// if (aoc_response.getAction_ind().equalsIgnoreCase("I"))
// {
//
// txn_data.setPartner_transaction_reference((aoc_response.getPartner_transaction_reference()));
// txn_data.setSettlement_amount(aoc_response.getTotal_charged_amount_in_settlement_currency());
//
// System.out.println("******************************* Remmitance Response
// *******************************");
// System.out.println(
// ((Remittance_Call_Response) ws.sendRemittance(txn_data, customer_data,
// bene_data)).toString());
// }
// else
// {
// System.out.println("Fee Inquiry call faild, can not continue to Remmitance
// call");
// }
//
// }
//
