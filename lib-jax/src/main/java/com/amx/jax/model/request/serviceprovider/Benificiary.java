package com.amx.jax.model.request.serviceprovider;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Benificiary
{
	// VW_EX_BENEFICIARY

	// Beneficiary Object Details
	String beneficiary_reference;

	boolean is_iban_number_holder;
	int bic_indicator;
	int beneficiary_bank_branch_indicator;
	String beneficiary_account_number;
	String beneficiary_account_type;

	String beneficiary_bank_code;
	String beneficiary_bank_branch_swift_code;
	String beneficiary_bank_name;
	String wallet_service_provider;

	String beneficiary_branch_code;
	String beneficiary_branch_name;

	String beneficiary_id_number;
	String beneficiary_id_type;
	String nationality_3_digit_ISO;
	Date date_of_birth;

	String first_name;
	String middle_name;
	String last_name;
	String full_addrerss;
	String address_zip;
	String street;
	String city;
	String state;
	String district;
	String contact_no;

	String profession;
	String beneficiary_type;
	String relation_to_beneficiary;

	public String validate_benificiary_input(String remittance_mode, String destination_country_2_digit_ISO)
	{
		// Validation is done on the filed that we think it is mandatory for any product
		// and any country. Any conditional
		// Fields that its existing in the API call depend on specific country or
		// specific product is not cover here because
		// it should be done on the country wise rules.

		ArrayList<String> validation_error_list = new ArrayList<String>();
		String validation_errors = "";

		if (beneficiary_reference == null)
			validation_error_list.add("Beneficiary sequence ID is empty");

		if (beneficiary_account_number == null)
			validation_error_list.add("Beneficiary account number is empty");

		if (beneficiary_type == null)
			validation_error_list.add("Beneficiary type is empty");

		if (first_name == null)
			validation_error_list.add("First name is empty");

		if (beneficiary_type != null && beneficiary_type.equalsIgnoreCase("I"))
		{
			if (last_name == null)
				validation_error_list.add("Last name is empty");
		}
		
		// TODO: If the country support IBAN number, then we need to validate if the give account number is IBAN or not
		// We can achieve that by following the same checks apply in online an app. There they have a master setup on country level (most likely) to tell if 
		// a given country support IBAN or not

		
		// TODO: Also there are some parameter checks in some parameter table need to be applied. Check with Chiranjiv on that and see what he did exactly
		
		if (destination_country_2_digit_ISO != null)
		{
			// Special checks apply at code level due. TODO: check if those checks can be applied on the screen
			if (destination_country_2_digit_ISO.equals("UK"))
			{
				// Validate that the street should contains at least two words

				if (street == null)
					validation_error_list.add("Beneficary street is empty");
				else if (get_words_count(street) < 2)
					validation_error_list.add("Beneficary street should be two words at least");
			}
			else if (destination_country_2_digit_ISO.equals("AU"))
			{
				// Validate if BSB code is in correct format (6 digits)
				if (beneficiary_bank_code == null || beneficiary_bank_code.trim().length()!=6)
					validation_error_list.add("BSB code should be 6 digit lenght for Australia");
			}
			else if (destination_country_2_digit_ISO.equals("BD"))
			{
				// Validate if Routing number is in correct format (9 digits)
				if (beneficiary_bank_code == null || beneficiary_bank_code.trim().length()!=9)
					validation_error_list.add("Routing number should be 9 digit lenght for Bangladesh");
			}
		}

		// if (full_addrerss == null)
		// validation_error_list.add("Full address is empty");

		if (remittance_mode != null && remittance_mode.equalsIgnoreCase("11")) // Mobile Wallet
		{
			if (contact_no == null && wallet_service_provider == null)
				validation_error_list.add(
						"Both Contact no and Wallet Service Provider Name are empty. One of them should not be empty for wallet product");
		}

		for (int i = 0; i < validation_error_list.size(); i++)
		{
			if (i != 0)
				validation_errors += "\n";
			else
				validation_errors = "------------- Beneficiary Validation Errors - Internal JAX Check -------------\n";

			validation_errors = validation_errors + (i + 1) + "- " + validation_error_list.get(i);
		}

		return validation_errors;
	}

	public String getBeneficiary_reference()
	{
		return beneficiary_reference;
	}

	public void setBeneficiary_reference(String beneficiary_reference)
	{
		this.beneficiary_reference = beneficiary_reference;
	}

	public boolean isIs_iban_number_holder()
	{
		return is_iban_number_holder;
	}

	public void setIs_iban_number_holder(boolean is_iban_number_holder)
	{
		this.is_iban_number_holder = is_iban_number_holder;
	}

	public int getBic_indicator()
	{
		return bic_indicator;
	}

	public void setBic_indicator(int bic_indicator)
	{
		this.bic_indicator = bic_indicator;
	}

	public int getBeneficiary_bank_branch_indicator()
	{
		return beneficiary_bank_branch_indicator;
	}

	public void setBeneficiary_bank_branch_indicator(int beneficiary_bank_branch_indicator)
	{
		this.beneficiary_bank_branch_indicator = beneficiary_bank_branch_indicator;
	}

	public String getBeneficiary_account_number()
	{
		return beneficiary_account_number;
	}

	public void setBeneficiary_account_number(String beneficiary_account_number)
	{
		this.beneficiary_account_number = beneficiary_account_number;
	}

	public String getBeneficiary_account_type()
	{
		return beneficiary_account_type;
	}

	public void setBeneficiary_account_type(String beneficiary_account_type)
	{
		this.beneficiary_account_type = beneficiary_account_type;
	}

	public String getBeneficiary_bank_code()
	{
		return beneficiary_bank_code;
	}

	public void setBeneficiary_bank_code(String beneficiary_bank_code)
	{
		this.beneficiary_bank_code = beneficiary_bank_code;
	}

	public String getBeneficiary_bank_branch_swift_code()
	{
		return beneficiary_bank_branch_swift_code;
	}

	public void setBeneficiary_bank_branch_swift_code(String beneficiary_bank_branch_swift_code)
	{
		this.beneficiary_bank_branch_swift_code = beneficiary_bank_branch_swift_code;
	}

	public String getBeneficiary_bank_name()
	{
		return beneficiary_bank_name;
	}

	public void setBeneficiary_bank_name(String beneficiary_bank_name)
	{
		this.beneficiary_bank_name = beneficiary_bank_name;
	}

	public String getWallet_service_provider()
	{
		return wallet_service_provider;
	}

	public void setWallet_service_provider(String wallet_service_provider)
	{
		this.wallet_service_provider = wallet_service_provider;
	}

	public String getBeneficiary_branch_code()
	{
		return beneficiary_branch_code;
	}

	public void setBeneficiary_branch_code(String beneficiary_branch_code)
	{
		this.beneficiary_branch_code = beneficiary_branch_code;
	}

	public String getBeneficiary_branch_name()
	{
		return beneficiary_branch_name;
	}

	public void setBeneficiary_branch_name(String beneficiary_branch_name)
	{
		this.beneficiary_branch_name = beneficiary_branch_name;
	}

	public String getBeneficiary_id_number()
	{
		return beneficiary_id_number;
	}

	public void setBeneficiary_id_number(String beneficiary_id_number)
	{
		this.beneficiary_id_number = beneficiary_id_number;
	}

	public String getBeneficiary_id_type()
	{
		return beneficiary_id_type;
	}

	public void setBeneficiary_id_type(String beneficiary_id_type)
	{
		this.beneficiary_id_type = beneficiary_id_type;
	}

	public String getNationality_3_digit_ISO()
	{
		return nationality_3_digit_ISO;
	}

	public void setNationality_3_digit_ISO(String nationality_3_digit_ISO)
	{
		this.nationality_3_digit_ISO = nationality_3_digit_ISO;
	}

	public Date getDate_of_birth()
	{
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth)
	{
		this.date_of_birth = date_of_birth;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getMiddle_name()
	{
		return middle_name;
	}

	public void setMiddle_name(String middle_name)
	{
		this.middle_name = middle_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public String getFull_addrerss()
	{
		return full_addrerss;
	}

	public void setFull_addrerss(String full_addrerss)
	{
		this.full_addrerss = full_addrerss;
	}

	public String getAddress_zip()
	{
		return address_zip;
	}

	public void setAddress_zip(String address_zip)
	{
		this.address_zip = address_zip;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	public String getContact_no()
	{
		return contact_no;
	}

	public void setContact_no(String contact_no)
	{
		this.contact_no = contact_no;
	}

	public String getProfession()
	{
		return profession;
	}

	public void setProfession(String profession)
	{
		this.profession = profession;
	}

	public String getBeneficiary_type()
	{
		return beneficiary_type;
	}

	public void setBeneficiary_type(String beneficiary_type)
	{
		this.beneficiary_type = beneficiary_type;
	}

	public String getRelation_to_beneficiary()
	{
		return relation_to_beneficiary;
	}

	public void setRelation_to_beneficiary(String relation_to_beneficiary)
	{
		this.relation_to_beneficiary = relation_to_beneficiary;
	}

	public String getFull_name()
	{
		return StringUtils.normalizeSpace(first_name + " " + (middle_name == null ? "" : middle_name) + " "
				+ (last_name == null ? "" : last_name));
	}

	private int get_words_count(String str)
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

	@Override
	public String toString()
	{
		return "Benificiary [beneficiary_reference=" + beneficiary_reference + ", is_iban_number_holder="
				+ is_iban_number_holder + ", bic_indicator=" + bic_indicator + ", beneficiary_bank_branch_indicator="
				+ beneficiary_bank_branch_indicator + ", beneficiary_account_number=" + beneficiary_account_number
				+ ", beneficiary_account_type=" + beneficiary_account_type + ", beneficiary_bank_code="
				+ beneficiary_bank_code + ", beneficiary_bank_branch_swift_code=" + beneficiary_bank_branch_swift_code
				+ ", beneficiary_bank_name=" + beneficiary_bank_name + ", wallet_service_provider="
				+ wallet_service_provider + ", beneficiary_branch_code=" + beneficiary_branch_code
				+ ", beneficiary_branch_name=" + beneficiary_branch_name + ", beneficiary_id_number="
				+ beneficiary_id_number + ", beneficiary_id_type=" + beneficiary_id_type + ", nationality_3_digit_ISO="
				+ nationality_3_digit_ISO + ", date_of_birth=" + date_of_birth + ", first_name=" + first_name
				+ ", middle_name=" + middle_name + ", last_name=" + last_name + ", full_addrerss=" + full_addrerss
				+ ", address_zip=" + address_zip + ", street=" + street + ", city=" + city + ", state=" + state
				+ ", district=" + district + ", contact_no=" + contact_no + ", profession=" + profession
				+ ", beneficiary_type=" + beneficiary_type + ", relation_to_beneficiary=" + relation_to_beneficiary
				+ "]";
	}
}